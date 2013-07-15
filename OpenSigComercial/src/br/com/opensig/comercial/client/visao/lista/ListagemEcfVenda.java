package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoEcfVendaProduto;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfZ;
import br.com.opensig.comercial.client.controlador.comando.ComandoTroca;
import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.Cat52;
import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.servico.ImportacaoProxy;
import br.com.opensig.core.client.visao.JanelaUpload;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.controlador.comando.ComandoCliente;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.controlador.comando.ComandoReceber;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemEcfVenda extends AListagem<ComEcfVenda> {

	protected IComando cmdCancelar;
	protected JanelaUpload<ComEcfVenda> janela;

	public ListagemEcfVenda(IFormulario<ComEcfVenda> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comEcfVendaId"), new IntegerFieldDef("comEcfZ.comEcfZId"), new IntegerFieldDef("comEcf.empEmpresa.empEmpresaId"),
				new StringFieldDef("comEcf.empEmpresa.empEntidade.empEntidadeNome1"), new IntegerFieldDef("sisUsuario.sisUsuarioId"), new StringFieldDef("sisUsuario.sisUsuarioLogin"),
				new IntegerFieldDef("sisVendedor.sisUsuarioId"), new StringFieldDef("sisVendedor.sisUsuarioLogin"), new IntegerFieldDef("sisGerente.sisUsuarioId"),
				new StringFieldDef("sisGerente.sisUsuarioLogin"), new IntegerFieldDef("empCliente.empClienteId"), new StringFieldDef("empCliente.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("comEcf.comEcfId"), new StringFieldDef("comEcf.comEcfSerie"), new IntegerFieldDef("comEcfVendaCcf"), new IntegerFieldDef("comEcfVendaCoo"),
				new DateFieldDef("comEcfVendaData"), new FloatFieldDef("comEcfVendaBruto"), new FloatFieldDef("comEcfVendaDesconto"), new FloatFieldDef("comEcfVendaAcrescimo"),
				new FloatFieldDef("comEcfVendaLiquido"), new BooleanFieldDef("comEcfVendaFechada"), new IntegerFieldDef("finReceber.finReceberId"), new IntegerFieldDef("comTroca.comTrocaId"),
				new BooleanFieldDef("comEcfVendaCancelada"), new StringFieldDef("comEcfVendaObservacao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comEcfVendaId", 75, true);
		ColumnConfig ccZId = new ColumnConfig("", "comEcfZ.comEcfZId", 10, false);
		ccZId.setHidden(true);
		ccZId.setFixed(true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "comEcf.empEmpresa.empEmpresaId", 100, false);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "comEcf.empEmpresa.empEntidade.empEntidadeNome1", 200, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccUsuarioId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtUsuario(), "sisUsuario.sisUsuarioId", 100, true);
		ccUsuarioId.setHidden(true);
		ColumnConfig ccLogin = new ColumnConfig(OpenSigCore.i18n.txtUsuario(), "sisUsuario.sisUsuarioLogin", 100, true);
		ColumnConfig ccVendedorId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtVendedor(), "sisVendedor.sisUsuarioId", 100, true);
		ccVendedorId.setHidden(true);
		ColumnConfig ccVendedor = new ColumnConfig(OpenSigCore.i18n.txtVendedor(), "sisVendedor.sisUsuarioLogin", 100, true);
		ColumnConfig ccGerenteId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtGerente(), "sisGerente.sisUsuarioId", 100, true);
		ccGerenteId.setHidden(true);
		ColumnConfig ccGerente = new ColumnConfig(OpenSigCore.i18n.txtGerente(), "sisGerente.sisUsuarioLogin", 100, true);
		ColumnConfig ccClienteId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtCliente(), "empCliente.empClienteId", 100, false);
		ccClienteId.setHidden(true);
		ColumnConfig ccCliente = new ColumnConfig(OpenSigCore.i18n.txtCliente(), "empCliente.empEntidade.empEntidadeNome1", 100, true);
		ColumnConfig ccEcfId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEcf(), "comEcf.comEcfId", 100, false);
		ccEcfId.setHidden(true);
		ColumnConfig ccEcf = new ColumnConfig(OpenSigCore.i18n.txtEcf(), "comEcf.comEcfSerie", 200, true);
		ColumnConfig ccCcf = new ColumnConfig(OpenSigCore.i18n.txtCcf(), "comEcfVendaCcf", 75, true);
		ColumnConfig ccCoo = new ColumnConfig(OpenSigCore.i18n.txtCoo(), "comEcfVendaCoo", 75, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comEcfVendaData", 75, true, DATA);
		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto(), "comEcfVendaDesconto", 75, true, PORCENTAGEM);
		ColumnConfig ccAcrescimo = new ColumnConfig(OpenSigCore.i18n.txtAcrescimo(), "comEcfVendaAcrescimo", 75, true, PORCENTAGEM);
		ColumnConfig ccFechada = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "comEcfVendaFechada", 75, true, BOLEANO);
		ColumnConfig ccReceberId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtReceber(), "finReceber.finReceberId", 100, true);
		ccReceberId.setHidden(true);
		ColumnConfig ccTrocaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtTroca(), "comTroca.comTrocaId", 100, true);
		ccTrocaId.setHidden(true);
		ColumnConfig ccCancelada = new ColumnConfig(OpenSigCore.i18n.txtCancelada(), "comEcfVendaCancelada", 75, true, BOLEANO);
		ColumnConfig ccObs = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), "comEcfVendaObservacao", 200, true);

		// sumarios
		SummaryColumnConfig sumBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comEcfVendaBruto", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtLiquido(), "comEcfVendaLiquido", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccZId, ccEmpresaId, ccEmpresa, ccUsuarioId, ccLogin, ccVendedorId, ccVendedor, ccGerenteId, ccGerente, ccClienteId, ccCliente, ccEcfId,
				ccEcf, ccCcf, ccCoo, ccData, sumBruto, ccDesconto, ccAcrescimo, sumLiquido, ccFechada, ccReceberId, ccTrocaId, ccCancelada, ccObs };
		modelos = new ColumnModel(bcc);

		// cancelando
		cmdCancelar = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				ComercialProxy proxy = new ComercialProxy();
				proxy.cancelarEcfVenda(classe, new AsyncCallback() {
					public void onFailure(Throwable caught) {
						getPanel().getEl().unmask();
						MessageBox.alert(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.errExcluir());
					};

					public void onSuccess(Object result) {
						getPanel().getEl().unmask();
						Record rec = getSelectionModel().getSelected();
						rec.set("comEcfVendaCancelada", true);
						new ToastWindow(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluirOK()).show();
					};
				});
			}
		};

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("comEcf.empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
			gf.add(fo, EJuncao.E);
		}

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("sisUsuario", ECompara.IGUAL, new SisUsuario(Ponte.getLogin().getId()));
			gf.add(fo);
		}

		filtroPadrao = gf.size() > 0 ? gf : null;
		super.inicializar();
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		final Record rec = getSelectionModel().getSelected();

		// valida se pode editar
		if (comando instanceof ComandoEditar) {
			if (rec != null && rec.getAsBoolean("comEcfVendaFechada")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		} else if (comando instanceof ComandoExcluir) {
			if (rec != null && !rec.getAsBoolean("comEcfVendaFechada") && !rec.getAsBoolean("comEcfVendaCancelada")) {
				comando = cmdCancelar;
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}

		return comando;
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comEcfVendaData")) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("comEcf.empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("comEcf.empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("comEcf.empEmpresa.empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("comEcf.empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);
			} else if (entry.getKey().equals("comEcf.comEcfSerie")) {
				// ecf
				FieldDef[] fdEcf = new FieldDef[] { new IntegerFieldDef("comEcfId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
						new StringFieldDef("comEcfCodigo"), new StringFieldDef("comEcfMfAdicional"), new StringFieldDef("comEcfIdentificacao"), new StringFieldDef("comEcfTipo"),
						new StringFieldDef("comEcfMarca"), new StringFieldDef("comEcfModelo"), new StringFieldDef("comEcfSerie"), new IntegerFieldDef("comEcfCaixa") };
				CoreProxy<ComEcf> proxy = new CoreProxy<ComEcf>(new ComEcf());
				Store storeEcf = new Store(proxy, new ArrayReader(new RecordDef(fdEcf)), true);

				GridListFilter fEcf = new GridListFilter("comEcf.comEcfSerie", storeEcf);
				fEcf.setLabelField("comEcfSerie");
				fEcf.setLabelValue("comEcfSerie");
				fEcf.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEcf);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comEcfVendaData").setActive(false, true);
		filtros.get("comEcf.empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void setImportacao(final SisExpImp modo) {
		janela = new JanelaUpload<ComEcfVenda>();
		janela.setTipos(modo.getSisExpImpExtensoes() != null ? modo.getSisExpImpExtensoes().split(" ") : null);
		janela.setAssincrono(new AsyncCallback() {
			public void onSuccess(Object result) {
				analisarCat52(modo, janela.getOks());
			}

			public void onFailure(Throwable caught) {
				analisarCat52(modo, janela.getOks());
			}
		});
		janela.inicializar();
	}

	private void analisarCat52(SisExpImp modo, List<String> arquivos) {
		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtEcf());
		ImportacaoProxy<Cat52> proxy = new ImportacaoProxy<Cat52>();
		proxy.importar(modo, arquivos, new AsyncCallback<Map<String, List<Cat52>>>() {

			public void onSuccess(Map<String, List<Cat52>> result) {
				janela.getOks().clear();
				janela.getErros().clear();
				MessageBox.hide();

				// oks
				List<Cat52> oks = result.get("ok");
				if (oks.size() > 0) {
					int vendas = 0;
					int naoFechadas = 0;
					int naoAchados = 0;
					for (Cat52 cat52 : oks) {
						vendas += cat52.getVendas();
						naoFechadas += cat52.getVendaNfechadas();
						naoAchados += cat52.getProdNachados();

						String ok = cat52.getArquivo() + "\n";
						ok += "\t-- Vendas = " + cat52.getVendas() + "\n";
						ok += "\t-- Vendas Nao Fechadas = " + cat52.getVendaNfechadas() + "\n";
						ok += "\t-- Produtos Nao Achados = " + cat52.getProdNachados();
						janela.getOks().add(ok);
					}

					String texto = "Total Arquivos = " + oks.size() + "\n";
					texto += "Total Vendas = " + vendas + "\n";
					texto += "Total de Vendas Nao Fechadas = " + naoFechadas + "\n";
					texto += "Total de Produtos Nao Achados = " + naoAchados;
					janela.getOks().add(texto);
				}

				// erros
				List<Cat52> erros = result.get("erro");
				for (Cat52 cat52 : erros) {
					String err = cat52.getArquivo() + "\n";
					err += "\t-- Erro :: " + cat52.getErro() + "\n";
					janela.getErros().add(err);
				}

				janela.resultado();
			}

			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtEcf(), caught.toString());
				new ToastWindow(OpenSigCore.i18n.txtImportar(), OpenSigCore.i18n.errImportar()).show();
			}
		});
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// reducao Z
		SisFuncao ecfZ = UtilClient.getFuncaoPermitida(ComandoEcfZ.class);
		MenuItem itemZ = gerarFuncao(ecfZ, "comEcfZId", "comEcfZ.comEcfZId");
		if (itemZ != null) {
			mnuContexto.addItem(itemZ);
		}

		// cliente
		SisFuncao cliente = UtilClient.getFuncaoPermitida(ComandoCliente.class);
		MenuItem itemCliente = gerarFuncao(cliente, "empClienteId", "empCliente.empClienteId");
		if (itemCliente != null) {
			mnuContexto.addItem(itemCliente);
		}

		// produtos venda
		SisFuncao produto = UtilClient.getFuncaoPermitida(ComandoEcfVendaProduto.class);
		MenuItem itemProduto = gerarFuncao(produto, "comEcfVenda.comEcfVendaId", "comEcfVendaId");
		if (itemProduto != null) {
			mnuContexto.addItem(itemProduto);
		}

		// receber
		SisFuncao receber = UtilClient.getFuncaoPermitida(ComandoReceber.class);
		MenuItem itemReceber = gerarFuncao(receber, "finReceberId", "finReceber.finReceberId");
		if (itemReceber != null) {
			mnuContexto.addItem(itemReceber);
		}
		
		// troca
		SisFuncao troca = UtilClient.getFuncaoPermitida(ComandoTroca.class);
		MenuItem itemTroca = gerarFuncao(troca, "comTrocaId", "comTroca.comTrocaId");
		if (itemTroca != null) {
			mnuContexto.addItem(itemTroca);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
