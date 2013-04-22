package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoVendaProduto;
import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
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
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
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
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridBooleanFilter;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.upload.UploadDialog;
import com.gwtextux.client.widgets.upload.UploadDialogListenerAdapter;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemVenda extends AListagem<ComVenda> {

	protected IComando cmdCancelar;
	protected IComando cmdExcluir;

	public ListagemVenda(IFormulario<ComVenda> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comVendaId"), new IntegerFieldDef("empCliente.empClienteId"), new IntegerFieldDef("empCliente.empEntidade.empEntidadeId"),
				new StringFieldDef("empCliente.empEntidade.empEntidadeNome1"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("sisUsuario.sisUsuarioId"), new StringFieldDef("sisUsuario.sisUsuarioLogin"), new IntegerFieldDef("sisVendedor.sisUsuarioId"),
				new StringFieldDef("sisVendedor.sisUsuarioLogin"), new DateFieldDef("comVendaData"), new FloatFieldDef("comVendaValorBruto"), new FloatFieldDef("comVendaValorLiquido"),
				new IntegerFieldDef("comNatureza.comNaturezaId"), new StringFieldDef("comNatureza.comNaturezaNome"), new BooleanFieldDef("comVendaFechada"),
				new IntegerFieldDef("finReceber.finReceberId"), new BooleanFieldDef("comVendaRecebida"), new IntegerFieldDef("fisNotaSaida.fisNotaSaidaId"), new BooleanFieldDef("comVendaNfe"),
				new BooleanFieldDef("comVendaCancelada"), new StringFieldDef("comVendaObservacao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comVendaId", 75, true);
		ColumnConfig ccClienteId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtCliente(), "empCliente.empClienteId", 100, true);
		ccClienteId.setHidden(true);
		ColumnConfig ccEntidadeId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEntidade(), "empCliente.empEntidade.empEntidadeId", 100, true);
		ccEntidadeId.setHidden(true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtCliente(), "empCliente.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, false);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 200, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccUsuarioId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtUsuario(), "sisUsuario.sisUsuarioId", 100, true);
		ccUsuarioId.setHidden(true);
		ColumnConfig ccLogin = new ColumnConfig(OpenSigCore.i18n.txtUsuario(), "sisUsuario.sisUsuarioLogin", 100, true);
		ColumnConfig ccVendedorId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtVendedor(), "sisVendedor.sisUsuarioId", 100, true);
		ccVendedorId.setHidden(true);
		ColumnConfig ccVendedor = new ColumnConfig(OpenSigCore.i18n.txtVendedor(), "sisVendedor.sisUsuarioLogin", 100, true);
		ColumnConfig ccNatureza = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtNatureza(), "comNatureza.comNaturezaId", 100, true);
		ccNatureza.setHidden(true);
		ColumnConfig ccNaturezaNome = new ColumnConfig(OpenSigCore.i18n.txtNatureza(), "comNatureza.comNaturezaNome", 100, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comVendaData", 120, true, DATAHORA);
		ColumnConfig ccFechada = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "comVendaFechada", 75, true, BOLEANO);
		ColumnConfig ccReceberId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtReceber(), "finReceber.finReceberId", 100, true);
		ccReceberId.setHidden(true);
		ColumnConfig ccRecebida = new ColumnConfig(OpenSigCore.i18n.txtRecebida(), "comVendaRecebida", 75, true, BOLEANO);
		ColumnConfig ccNfeId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtNfe(), "fisNotaSaida.fisNotaSaidaId", 100, true);
		ccNfeId.setHidden(true);
		ColumnConfig ccNfe = new ColumnConfig(OpenSigCore.i18n.txtNfe(), "comVendaNfe", 75, true, BOLEANO);
		ColumnConfig ccCancelada = new ColumnConfig(OpenSigCore.i18n.txtCancelada(), "comVendaCancelada", 75, true, BOLEANO);
		ColumnConfig ccObs = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), "comVendaObservacao", 200, true);

		// sumarios
		ColumnConfig ccBruto = new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comVendaValorBruto", 75, true, DINHEIRO);
		ccBruto.setHidden(true);
		SummaryColumnConfig sumBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccBruto, DINHEIRO);
		SummaryColumnConfig sumLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtLiquido(), "comVendaValorLiquido", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccClienteId, ccEntidadeId, ccNome, ccEmpresaId, ccEmpresa, ccUsuarioId, ccLogin, ccVendedorId, ccVendedor, ccData, sumBruto,
				sumLiquido, ccNatureza, ccNaturezaNome, ccFechada, ccReceberId, ccRecebida, ccNfeId, ccNfe, ccCancelada, ccObs };
		modelos = new ColumnModel(bcc);

		// cancelando
		cmdCancelar = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				ComercialProxy proxy = new ComercialProxy();
				proxy.cancelarVenda(classe, new AsyncCallback() {
					public void onFailure(Throwable caught) {
						getPanel().getEl().unmask();
						MessageBox.alert(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.errExcluir());
					};

					public void onSuccess(Object result) {
						getPanel().getEl().unmask();
						Record rec = getSelectionModel().getSelected();
						rec.set("comVendaCancelada", true);
						rec.set("comVendaObservacao", classe.getComVendaObservacao());
						new ToastWindow(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluirOK()).show();
					};
				});
			}
		};

		// excluindo
		cmdExcluir = new AComando(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				ComercialProxy proxy = new ComercialProxy();
				proxy.excluirVenda(classe, ASYNC);
			}
		};

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
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
			if (rec != null && rec.getAsBoolean("comVendaFechada")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}
		// valida se pode excluir ou cancelar
		else if (comando instanceof ComandoExcluir) {
			comando = null;
			if (rec != null && !rec.getAsBoolean("comVendaFechada")) {
				MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
							cmdExcluir.execute(contexto);
						}
					}
				});
			} else if (rec != null && rec.getAsBoolean("comVendaCancelada")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
			} else if (rec != null && rec.getAsBoolean("comVendaFechada") && !rec.getAsBoolean("comVendaCancelada")) {
				MessageBox.prompt(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.msgConfirma(), new PromptCallback() {
					public void execute(String btnID, String text) {
						if (btnID.equalsIgnoreCase("ok")) {
							if (text == null || text.trim().length() < 15 || text.trim().length() > 255) {
								new ToastWindow(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.errInvalido()).show();
							} else {
								getPanel().getEl().mask(OpenSigCore.i18n.txtAguarde());
								classe.setFisNotaSaida(new FisNotaSaida(rec.getAsInteger("fisNotaSaida.fisNotaSaidaId")));
								classe.setComVendaObservacao(text.trim());
								cmdCancelar.execute(contexto);
							}
						}
					}
				}, true);
			}
		}

		return comando;
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comVendaData")) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);

			} else if (entry.getKey().equals("comVendaCancelada")) {
				((GridBooleanFilter) entry.getValue()).setValue(false);
				entry.getValue().setActive(true, true);
			} else if (entry.getKey().equals("comNatureza.comNaturezaNome")) {
				// natureza
				FieldDef[] fdNatureza = new FieldDef[] { new IntegerFieldDef("comNaturezaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"),
						new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"), new StringFieldDef("comNaturezaNome") };
				CoreProxy<ComNatureza> proxy = new CoreProxy<ComNatureza>(new ComNatureza());
				Store storeNatureza = new Store(proxy, new ArrayReader(new RecordDef(fdNatureza)), true);

				GridListFilter fNatureza = new GridListFilter("comNatureza.comNaturezaNome", storeNatureza);
				fNatureza.setLabelField("comNaturezaNome");
				fNatureza.setLabelValue("comNaturezaNome");
				fNatureza.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fNatureza);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comVendaData").setActive(false, true);
		filtros.get("comVendaCancelada").setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void setImportacao(final SisExpImp modo) {
		final JanelaUpload<ComVenda> janela = new JanelaUpload<ComVenda>();
		janela.setTipos(modo.getSisExpImpExtensoes() != null ? modo.getSisExpImpExtensoes().split(" ") : null);
		janela.setAssincrono(new AsyncCallback() {
			public void onSuccess(Object result) {
				analisarNfe(modo, janela.getOks());
				janela.getUplArquivo().close();
			}

			public void onFailure(Throwable caught) {
				janela.resultado();
			}
		});
		janela.inicializar();
		janela.getUplArquivo().addListener(new UploadDialogListenerAdapter() {
			public boolean onBeforeAdd(UploadDialog source, String filename) {
				return source.getQueuedCount() == 0;
			}

			public void onFileAdd(UploadDialog source, String filename) {
				source.startUpload();
			}
		});
	}

	private void analisarNfe(SisExpImp modo, List<String> arquivos) {
		getEl().mask(OpenSigCore.i18n.txtAguarde());
		ImportacaoProxy<ComVenda> proxy = new ImportacaoProxy<ComVenda>();
		proxy.importar(modo, arquivos, new AsyncCallback<Map<String, List<ComVenda>>>() {

			public void onSuccess(Map<String, List<ComVenda>> result) {
				getEl().unmask();
				new ListagemValidarVenda(result.get("ok").get(0), funcao);
			}

			public void onFailure(Throwable caught) {
				getEl().unmask();
				MessageBox.alert(OpenSigCore.i18n.txtImportar(), OpenSigCore.i18n.errImportar());
				new ToastWindow(OpenSigCore.i18n.txtImportar(), caught.getMessage()).show();
			}
		});
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// cliente
		SisFuncao cliente = UtilClient.getFuncaoPermitida(ComandoCliente.class);
		MenuItem itemCliente = gerarFuncao(cliente, "empClienteId", "empCliente.empClienteId");
		if (itemCliente != null) {
			mnuContexto.addItem(itemCliente);
		}

		// produtos venda
		SisFuncao produto = UtilClient.getFuncaoPermitida(ComandoVendaProduto.class);
		MenuItem itemProduto = gerarFuncao(produto, "comVenda.comVendaId", "comVendaId");
		if (itemProduto != null) {
			mnuContexto.addItem(itemProduto);
		}

		// receber
		SisFuncao receber = UtilClient.getFuncaoPermitida(ComandoReceber.class);
		MenuItem itemReceber = gerarFuncao(receber, "finReceberId", "finReceber.finReceberId");
		if (itemReceber != null) {
			mnuContexto.addItem(itemReceber);
		}

		// nota saida
		String strSaida = FabricaComando.getInstancia().getComandoCompleto("ComandoSaida");
		SisFuncao saida = UtilClient.getFuncaoPermitida(strSaida);
		MenuItem itemSaida = gerarFuncao(saida, "fisNotaSaidaId", "fisNotaSaida.fisNotaSaidaId");
		if (itemSaida != null) {
			mnuContexto.addItem(itemSaida);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
