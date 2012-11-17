package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoCompraProduto;
import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluirFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.servico.ImportacaoProxy;
import br.com.opensig.core.client.visao.JanelaUpload;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.controlador.comando.ComandoFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.controlador.comando.ComandoPagar;

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
import com.gwtextux.client.widgets.upload.UploadDialog;
import com.gwtextux.client.widgets.upload.UploadDialogListenerAdapter;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemCompra extends AListagem<ComCompra> {

	protected IComando cmdExcluir;

	public ListagemCompra(IFormulario<ComCompra> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comCompraId"), new IntegerFieldDef("comCompraSerie"), new IntegerFieldDef("comCompraNumero"),
				new IntegerFieldDef("comNatureza.comNaturezaId"), new StringFieldDef("comNatureza.comNaturezaNome"), new DateFieldDef("comCompraEmissao"), new DateFieldDef("comCompraRecebimento"),
				new IntegerFieldDef("empFornecedor.empFornecedorId"), new IntegerFieldDef("empFornecedor.empEntidade.empEntidadeId"), new StringFieldDef("empFornecedor.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"), new IntegerFieldDef("empEstado.empEstadoId"),
				new StringFieldDef("empEstado.empEstadoDescricao"), new FloatFieldDef("comCompraIcmsBase"), new FloatFieldDef("comCompraIcmsValor"), new FloatFieldDef("comCompraIcmssubBase"),
				new FloatFieldDef("comCompraIcmssubValor"), new FloatFieldDef("comCompraValorProduto"), new FloatFieldDef("comCompraValorFrete"), new FloatFieldDef("comCompraValorSeguro"),
				new FloatFieldDef("comCompraValorDesconto"), new FloatFieldDef("comCompraValorIpi"), new FloatFieldDef("comCompraValorOutros"), new FloatFieldDef("comCompraValorNota"),
				new BooleanFieldDef("comCompraFechada"), new IntegerFieldDef("finPagar.finConta.finContaId"), new IntegerFieldDef("finPagar.finPagarId"), new BooleanFieldDef("comCompraPaga"),
				new IntegerFieldDef("fisNotaEntrada.fisNotaEntradaId"), new BooleanFieldDef("comCompraNfe"), new StringFieldDef("comCompraObservacao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comCompraId", 75, true);
		ColumnConfig ccSerie = new ColumnConfig(OpenSigCore.i18n.txtSerie(), "comCompraSerie", 50, true);
		ccSerie.setHidden(true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "comCompraNumero", 75, true);
		ColumnConfig ccNaturezaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtNatureza(), "comNatureza.comNaturezaId", 100, true);
		ccNaturezaId.setHidden(true);
		ColumnConfig ccNatureza = new ColumnConfig(OpenSigCore.i18n.txtNatureza(), "comNatureza.comNaturezaNome", 100, true);
		ColumnConfig ccEmissao = new ColumnConfig(OpenSigCore.i18n.txtEmissao(), "comCompraEmissao", 75, true, DATA);
		ccEmissao.setHidden(true);
		ColumnConfig ccRecebimento = new ColumnConfig(OpenSigCore.i18n.txtRecebimento(), "comCompraRecebimento", 75, true, DATA);
		ColumnConfig ccFonecedorId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtFornecedor(), "empFornecedor.empFornecedorId", 100, true);
		ccFonecedorId.setHidden(true);
		ColumnConfig ccEntidadeId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEntidade(), "empFornecedor.empEntidade.empEntidadeId", 100, true);
		ccEntidadeId.setHidden(true);
		ColumnConfig ccFornecedor = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccEstadoId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEstado(), "empEstado.empEstadoId", 100, true);
		ccEstadoId.setHidden(true);
		ColumnConfig ccEstado = new ColumnConfig(OpenSigCore.i18n.txtEstado(), "empEstado.empEstadoDescricao", 200, true);
		ccEstado.setHidden(true);
		ColumnConfig ccIcmsBase = new ColumnConfig(OpenSigCore.i18n.txtIcmsBase(), "comCompraIcmsBase", 75, true, DINHEIRO);
		ccIcmsBase.setHidden(true);
		ColumnConfig ccIcmsValor = new ColumnConfig(OpenSigCore.i18n.txtIcmsValor(), "comCompraIcmsValor", 75, true, DINHEIRO);
		ColumnConfig ccSubBase = new ColumnConfig(OpenSigCore.i18n.txtSubBase(), "comCompraIcmssubBase", 75, true, DINHEIRO);
		ccSubBase.setHidden(true);
		ColumnConfig ccSubValor = new ColumnConfig(OpenSigCore.i18n.txtSubValor(), "comCompraIcmssubValor", 75, true, DINHEIRO);
		ColumnConfig ccValorProduto = new ColumnConfig(OpenSigCore.i18n.txtValorProduto(), "comCompraValorProduto", 75, true, DINHEIRO);
		ColumnConfig ccFrete = new ColumnConfig(OpenSigCore.i18n.txtFrete(), "comCompraValorFrete", 75, true, DINHEIRO);
		ccFrete.setHidden(true);
		ColumnConfig ccSeguro = new ColumnConfig(OpenSigCore.i18n.txtSeguro(), "comCompraValorSeguro", 75, true, DINHEIRO);
		ccSeguro.setHidden(true);
		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto(), "comCompraValorDesconto", 75, true, DINHEIRO);
		ccDesconto.setHidden(true);
		ColumnConfig ccIpi = new ColumnConfig(OpenSigCore.i18n.txtValor() + " " + OpenSigCore.i18n.txtIpi(), "comCompraValorIpi", 75, true, DINHEIRO);
		ColumnConfig ccOutros = new ColumnConfig(OpenSigCore.i18n.txtOutro(), "comCompraValorOutros", 75, true, DINHEIRO);
		ccOutros.setHidden(true);
		ColumnConfig ccValorNota = new ColumnConfig(OpenSigCore.i18n.txtValorNota(), "comCompraValorNota", 75, true, DINHEIRO);
		ColumnConfig ccFechar = new ColumnConfig(OpenSigCore.i18n.txtFechada(), "comCompraFechada", 75, true, BOLEANO);
		ColumnConfig ccContaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtConta(), "finPagar.finConta.finContaId", 100, true);
		ccContaId.setHidden(true);
		ColumnConfig ccPagarId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtPagar(), "finPagar.finPagarId", 100, true);
		ccPagarId.setHidden(true);
		ColumnConfig ccPagar = new ColumnConfig(OpenSigCore.i18n.txtPaga(), "comCompraPaga", 75, true, BOLEANO);
		ColumnConfig ccNfeId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtNfe(), "fisNotaEntrada.fisNotaEntradaId", 100, true);
		ccNfeId.setHidden(true);
		ColumnConfig ccNfe = new ColumnConfig(OpenSigCore.i18n.txtNfe(), "comCompraNfe", 75, true, BOLEANO);
		ColumnConfig ccObs = new ColumnConfig(OpenSigCore.i18n.txtObservacao(), "comCompraObservacao", 200, true);

		// sumarios
		SummaryColumnConfig sumIcms = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccIcmsValor, DINHEIRO);
		SummaryColumnConfig sumSub = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccSubValor, DINHEIRO);
		SummaryColumnConfig sumFrete = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccFrete, DINHEIRO);
		SummaryColumnConfig sumSeguro = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccSeguro, DINHEIRO);
		SummaryColumnConfig sumDesconto = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccDesconto, DINHEIRO);
		SummaryColumnConfig sumIpi = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccIpi, DINHEIRO);
		SummaryColumnConfig sumOutros = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccOutros, DINHEIRO);
		SummaryColumnConfig sumNota = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccValorNota, DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccSerie, ccNumero, ccNaturezaId, ccNatureza, ccEmissao, ccRecebimento, ccFonecedorId, ccEntidadeId, ccFornecedor, ccEmpresaId,
				ccEmpresa, ccEstadoId, ccEstado, ccIcmsBase, sumIcms, ccSubBase, sumSub, ccValorProduto, sumFrete, sumSeguro, sumDesconto, sumIpi, sumOutros, sumNota, ccFechar, ccContaId, ccPagarId,
				ccPagar, ccNfeId, ccNfe, ccObs };
		modelos = new ColumnModel(bcc);

		// deletando
		cmdExcluir = new AComando(new ComandoExcluirFinal()) {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				ComercialProxy proxy = new ComercialProxy();
				proxy.excluirCompra(classe, ASYNC);
			}
		};

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode editar
		if (comando instanceof ComandoEditar) {
			if (rec != null && rec.getAsBoolean("comCompraFechada")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}// valida se pode excluir
		else if (comando instanceof ComandoExcluir) {
			comando = null;
			if (rec != null) {
				MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
							cmdExcluir.execute(contexto);
						}
					}
				});
			}
		}

		return comando;
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comCompraRecebimento")) {
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
		filtros.get("comCompraRecebimento").setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void setImportacao(final SisExpImp modo) {
		final JanelaUpload<ComCompra> janela = new JanelaUpload<ComCompra>();
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
		ImportacaoProxy<ComCompra> proxy = new ImportacaoProxy<ComCompra>();
		proxy.importar(modo, arquivos, new AsyncCallback<Map<String, List<ComCompra>>>() {

			public void onSuccess(Map<String, List<ComCompra>> result) {
				getEl().unmask();
				new ListagemValidarProduto(result.get("ok").get(0), funcao);
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

		// fornecedor
		SisFuncao fornecedor = UtilClient.getFuncaoPermitida(ComandoFornecedor.class);
		MenuItem itemFornecedor = gerarFuncao(fornecedor, "empFornecedorId", "empFornecedor.empFornecedorId");
		if (itemFornecedor != null) {
			mnuContexto.addItem(itemFornecedor);
		}

		// produtos compra
		SisFuncao produto = UtilClient.getFuncaoPermitida(ComandoCompraProduto.class);
		MenuItem itemProduto = gerarFuncao(produto, "comCompra.comCompraId", "comCompraId");
		if (itemProduto != null) {
			mnuContexto.addItem(itemProduto);
		}

		// pagar
		SisFuncao pagar = UtilClient.getFuncaoPermitida(ComandoPagar.class);
		MenuItem itemPagar = gerarFuncao(pagar, "finPagarId", "finPagar.finPagarId");
		if (itemPagar != null) {
			mnuContexto.addItem(itemPagar);
		}

		// nota entrada
		String strEntrada = FabricaComando.getInstancia().getComandoCompleto("ComandoEntrada");
		SisFuncao entrada = UtilClient.getFuncaoPermitida(strEntrada);
		MenuItem itemEntrada = gerarFuncao(entrada, "fisNotaEntradaId", "fisNotaEntrada.fisNotaEntradaId");
		if (itemEntrada != null) {
			mnuContexto.addItem(itemEntrada);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
