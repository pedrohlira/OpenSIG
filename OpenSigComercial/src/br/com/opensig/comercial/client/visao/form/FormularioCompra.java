package br.com.opensig.comercial.client.visao.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.client.GerarPreco;
import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.client.visao.lista.ListagemCompraProdutos;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoAdicionar;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.produto.client.controlador.comando.ComandoPesquisa;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioCompra extends AFormulario<ComCompra> {

	private Hidden hdnCod;
	private Hidden hdnFornecedor;
	private Hidden hdnEmpresa;
	private NumberField txtSerie;
	private NumberField txtNumero;
	private DateField dtEmissao;
	private DateField dtRecebimento;
	private ComboBox cmbNatureza;
	private ComboBox cmbEstado;
	private ComboBox cmbFornecedor;
	private NumberField txtIcmsBase;
	private NumberField txtIcmsValor;
	private NumberField txtSubBase;
	private NumberField txtSubValor;
	private NumberField txtValorProduto;
	private NumberField txtFrete;
	private NumberField txtSeguro;
	private NumberField txtDesconto;
	private NumberField txtIpi;
	private NumberField txtOutros;
	private NumberField txtValorNota;
	private Label lblRegistros;
	private TextArea txtObservacao;
	private NumberField pCfop;
	private TextField pIcmsCst;
	private NumberField pIcms;
	private TextField pIpiCst;
	private NumberField pIpi;
	private TextField pPisCst;
	private NumberField pPis;
	private TextField pCofinsCst;
	private NumberField pCofins;
	private Button btnAtualizar;
	private ListagemCompraProdutos gridProdutos;
	private List<ComCompraProduto> produtos;

	private boolean autosave;
	private AsyncCallback asyncSalvar;
	private ComandoPesquisa cmdPesquisa;

	public FormularioCompra(SisFuncao funcao) {
		super(new ComCompra(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comCompraId", "0");
		add(hdnCod);
		hdnFornecedor = new Hidden("empFornecedor.empFornecedorId", "0");
		add(hdnFornecedor);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtSerie = new NumberField(OpenSigCore.i18n.txtSerie(), "comCompraSerie", 50);
		txtSerie.setAllowBlank(false);
		txtSerie.setAllowDecimals(false);
		txtSerie.setAllowNegative(false);

		txtNumero = new NumberField(OpenSigCore.i18n.txtNumero(), "comCompraNumero", 80);
		txtNumero.setAllowBlank(false);
		txtNumero.setAllowDecimals(false);
		txtNumero.setAllowNegative(false);

		dtEmissao = new DateField(OpenSigCore.i18n.txtEmissao(), "comCompraEmissao", 90);
		dtEmissao.setAllowBlank(false);

		dtRecebimento = new DateField(OpenSigCore.i18n.txtRecebimento(), "comCompraRecebimento", 90);
		dtRecebimento.setAllowBlank(false);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtSerie, 70);
		linha1.addToRow(txtNumero, 100);
		linha1.addToRow(getNatureza(), 120);
		linha1.addToRow(dtEmissao, 110);
		linha1.addToRow(dtRecebimento, 110);
		linha1.addToRow(getEstado(), 120);
		add(linha1);

		txtIcmsBase = new NumberField(OpenSigCore.i18n.txtIcmsBase(), "comCompraIcmsBase", 80);
		txtIcmsBase.setAllowBlank(false);
		txtIcmsBase.setAllowNegative(false);
		txtIcmsBase.setMaxLength(11);
		txtIcmsBase.setDecimalPrecision(2);

		txtIcmsValor = new NumberField(OpenSigCore.i18n.txtIcmsValor(), "comCompraIcmsValor", 80);
		txtIcmsValor.setAllowBlank(false);
		txtIcmsValor.setAllowNegative(false);
		txtIcmsValor.setMaxLength(11);
		txtIcmsValor.setDecimalPrecision(2);

		txtSubBase = new NumberField(OpenSigCore.i18n.txtSubBase(), "comCompraIcmssubBase", 80);
		txtSubBase.setAllowBlank(false);
		txtSubBase.setAllowNegative(false);
		txtSubBase.setMaxLength(11);
		txtSubBase.setDecimalPrecision(2);
		txtSubBase.setValue(0.00);

		txtSubValor = new NumberField(OpenSigCore.i18n.txtSubValor(), "comCompraIcmssubValor", 80);
		txtSubValor.setAllowBlank(false);
		txtSubValor.setAllowNegative(false);
		txtSubValor.setMaxLength(11);
		txtSubValor.setDecimalPrecision(2);
		txtSubValor.setValue(0);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(getFornecedor(), 320);
		linha2.addToRow(txtIcmsBase, 100);
		linha2.addToRow(txtIcmsValor, 100);
		linha2.addToRow(txtSubBase, 100);
		linha2.addToRow(txtSubValor, 100);
		add(linha2);

		txtFrete = new NumberField(OpenSigCore.i18n.txtFrete(), "comCompraValorFrete", 80);
		txtFrete.setAllowBlank(false);
		txtFrete.setAllowNegative(false);
		txtFrete.setMaxLength(11);
		txtFrete.setDecimalPrecision(2);
		txtFrete.setValue(0);

		txtSeguro = new NumberField(OpenSigCore.i18n.txtSeguro(), "comCompraValorSeguro", 80);
		txtSeguro.setAllowBlank(false);
		txtSeguro.setAllowNegative(false);
		txtSeguro.setMaxLength(11);
		txtSeguro.setDecimalPrecision(2);
		txtSeguro.setValue(0);

		txtIpi = new NumberField(OpenSigCore.i18n.txtIpi(), "comCompraValorIpi", 80);
		txtIpi.setAllowBlank(false);
		txtIpi.setAllowNegative(false);
		txtIpi.setMaxLength(11);
		txtIpi.setDecimalPrecision(2);

		txtValorProduto = new NumberField(OpenSigCore.i18n.txtValorProduto(), "comCompraValorProduto", 80);
		txtValorProduto.setMaxLength(11);
		txtValorProduto.setDecimalPrecision(2);
		txtValorProduto.setReadOnly(true);

		txtDesconto = new NumberField(OpenSigCore.i18n.txtDesconto(), "comCompraValorDesconto", 80);
		txtDesconto.setAllowBlank(false);
		txtDesconto.setAllowNegative(false);
		txtDesconto.setMaxLength(11);
		txtDesconto.setDecimalPrecision(2);
		txtDesconto.setValue(0);

		txtOutros = new NumberField(OpenSigCore.i18n.txtOutro(), "comCompraValorOutros", 80);
		txtOutros.setAllowBlank(false);
		txtOutros.setAllowNegative(false);
		txtOutros.setMaxLength(11);
		txtOutros.setDecimalPrecision(2);
		txtOutros.setValue(0);

		txtValorNota = new NumberField(OpenSigCore.i18n.txtValorNota(), "comCompraValorNota", 120);
		txtValorNota.setAllowBlank(false);
		txtValorNota.setAllowNegative(false);
		txtValorNota.setMaxLength(11);
		txtValorNota.setDecimalPrecision(2);
		txtValorNota.setValue(0);

		MultiFieldPanel linha3 = new MultiFieldPanel();
		linha3.setBorder(false);
		linha3.addToRow(txtFrete, 100);
		linha3.addToRow(txtSeguro, 100);
		linha3.addToRow(txtIpi, 100);
		linha3.addToRow(txtValorProduto, 100);
		linha3.addToRow(txtDesconto, 100);
		linha3.addToRow(txtOutros, 100);
		linha3.addToRow(txtValorNota, 140);
		add(linha3);
		lblRegistros = new Label();
		
		pCfop = new NumberField(OpenSigCore.i18n.txtCfop(), "pCfop", 40);
		pCfop.setAllowDecimals(false);
		pCfop.setAllowNegative(false);
		pCfop.setMinLength(4);
		pCfop.setMaxLength(4);
		pCfop.setMinValue(1000);
		pCfop.setMaxValue(3999);
		
		pIcmsCst = new TextField(OpenSigCore.i18n.txtIcms() + " - " + OpenSigCore.i18n.txtCst(), "pIcmsCst", 50);
		pIcmsCst.setRegex("^(\\d{2}|\\d{3})$");
		
		pIcms = new NumberField(OpenSigCore.i18n.txtIcms() + " %", "pIcms", 40, 0);
		pIcms.setAllowBlank(false);
		pIcms.setAllowNegative(false);
		pIcms.setDecimalPrecision(2);
		pIcms.setMaxLength(5);
		
		pIpiCst = new TextField(OpenSigCore.i18n.txtIpi() + " - " + OpenSigCore.i18n.txtCst(), "pIpiCst", 50);
		pIpiCst.setRegex("^(\\d{2})$");

		pIpi = new NumberField(OpenSigCore.i18n.txtIpi() + " %", "pIpi", 40, 0);
		pIpi.setAllowBlank(false);
		pIpi.setAllowNegative(false);
		pIpi.setDecimalPrecision(2);
		pIpi.setMaxLength(5);

		pPisCst = new TextField(OpenSigCore.i18n.txtPis() + " - " + OpenSigCore.i18n.txtCst(), "pPisCst", 50);
		pPisCst.setRegex("^(\\d{2})$");

		pPis = new NumberField(OpenSigCore.i18n.txtPis() + " %", "pPis", 40, 0);
		pPis.setAllowNegative(false);
		pPis.setAllowBlank(false);
		pPis.setDecimalPrecision(2);
		pPis.setMaxLength(5);

		pCofinsCst = new TextField(OpenSigCore.i18n.txtCofins() + " - " + OpenSigCore.i18n.txtCst(), "pCofinsCst", 50);
		pCofinsCst.setRegex("^(\\d{2})$");

		pCofins = new NumberField(OpenSigCore.i18n.txtCofins() + " %", "pCofins", 40, 0);
		pCofins.setAllowNegative(false);
		pCofins.setAllowBlank(false);
		pCofins.setDecimalPrecision(2);
		pCofins.setMaxLength(5);
		
		btnAtualizar = new Button(OpenSigCore.i18n.txtAtualizar());
		btnAtualizar.setIconCls("icon-selecionar");
		btnAtualizar.addListener(new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				for(Record rec : gridProdutos.getStore().getRecords()){
					rec.set("comVendaProdutoCfop", pCfop.getValue() == null ? 0 : pCfop.getValue().intValue());
					rec.set("comVendaProdutoIcmsCst", pIcmsCst.getValueAsString());
					rec.set("comVendaProdutoIcms", pIcms.getValueAsString());
					rec.set("comVendaProdutoIpiCst", pIpiCst.getValueAsString());
					rec.set("comVendaProdutoIpi", pIpi.getValueAsString());
					rec.set("comVendaProdutoPisCst", pPisCst.getValueAsString());
					rec.set("comVendaProdutoPis", pPis.getValueAsString());
					rec.set("comVendaProdutoCofinsCst", pCofinsCst.getValueAsString());
					rec.set("comVendaProdutoCofins", pCofins.getValueAsString());
				}
			}
		});
		
		MultiFieldPanel linha4 = new MultiFieldPanel();
		linha4.setBorder(false);
		linha4.addToRow(pCfop, 60);
		linha4.addToRow(pIcmsCst, 70);
		linha4.addToRow(pIcms, 60);
		linha4.addToRow(pIpiCst, 70);
		linha4.addToRow(pIpi, 60);
		linha4.addToRow(pPisCst, 70);
		linha4.addToRow(pPis, 60);
		linha4.addToRow(pCofinsCst, 100);
		linha4.addToRow(pCofins, 80);
		linha4.addToRow(btnAtualizar, 100);
		
		FieldSet padrao = new FieldSet("Dados Padrões Para os Produtos");
		padrao.add(linha4);
		add(padrao);

		final AsyncCallback<Record> asyncPesquisa = new AsyncCallback<Record>() {

			public void onFailure(Throwable arg0) {
			}

			public void onSuccess(Record result) {
				gridProdutos.stopEditing();
				int pos;
				
				for (pos = 0; pos < gridProdutos.getStore().getCount(); pos++) {
					if (gridProdutos.getStore().getAt(pos).getAsInteger("prodProdutoId") == result.getAsInteger("prodProdutoId")
							&& gridProdutos.getStore().getAt(pos).getAsString("comVendaProdutoBarra").equals(result.getAsString("prodProdutoBarra"))) {
						break;
					}
				}

				if (pos == gridProdutos.getStore().getCount()) {
					Record rec = gridProdutos.getCampos().createRecord(new Object[gridProdutos.getCampos().getFields().length]);
					rec.set("comCompraProdutoId", 0);
					rec.set("prodProdutoId", result.getAsInteger("prodProdutoId"));
					rec.set("comCompraProdutoBarra", result.getAsString("prodProdutoBarra"));
					rec.set("prodProduto.prodProdutoDescricao", result.getAsString("prodProdutoDescricao"));
					rec.set("prodProduto.prodProdutoReferencia", result.getAsString("prodProdutoReferencia"));
					rec.set("comCompraProdutoQuantidade", 1);
					rec.set("prodEmbalagem.prodEmbalagemId", result.getAsInteger("prodEmbalagem.prodEmbalagemId"));
					rec.set("prodEmbalagem.prodEmbalagemNome", result.getAsString("prodEmbalagem.prodEmbalagemNome"));
					rec.set("comCompraProdutoValor", result.getAsDouble("prodProdutoCusto"));
					rec.set("comCompraProdutoTotal", 0);
					rec.set("comCompraProdutoCfop", pCfop.getValue() == null ? 0 : pCfop.getValue().intValue());
					rec.set("comCompraProdutoIcmsCst", pIcmsCst.getValueAsString());
					rec.set("comCompraProdutoIcms", pIcms.getValueAsString());
					rec.set("comCompraProdutoIpiCst", pIpiCst.getValueAsString());
					rec.set("comCompraProdutoIpi", pIpi.getValueAsString());
					rec.set("comCompraProdutoPisCst", pPisCst.getValueAsString());
					rec.set("comCompraProdutoPis", pPis.getValueAsString());
					rec.set("comCompraProdutoCofinsCst", pCofinsCst.getValueAsString());
					rec.set("comCompraProdutoCofins", pCofins.getValueAsString());
					rec.set("comCompraProdutoPreco", result.getAsDouble("prodProdutoPreco"));
					gridProdutos.getStore().add(rec);
				} else {
					new ToastWindow(getTitle(), OpenSigCore.i18n.errExiste()).show();
				}

				for (int col = 0; col < gridProdutos.getModelos().getColumnCount(); col++) {
					if (gridProdutos.getModelos().getDataIndex(col).equals("comCompraProdutoQuantidade")) {
						gridProdutos.startEditing(pos, col);
						break;
					}
				}
				txtValorProduto.setValue(totalizar());
			}
		};
		cmdPesquisa = new ComandoPesquisa(asyncPesquisa);

		gridProdutos = new ListagemCompraProdutos(true) {
			public IComando AntesDoComando(IComando comando) {
				if (comando instanceof ComandoAdicionar) {
					contexto = new HashMap();
					contexto.put("dados", funcao);

					int registros = gridProdutos.getStore().getRecords().length;
					int minimo = UtilClient.CONF.get("listagem.autosave") != null ? Integer.valueOf(UtilClient.CONF.get("listagem.autosave")) : 0;

					if (registros > 0 && minimo > 0 && registros % minimo == 0 && getForm().isValid() && setDados()) {
						autoSave();
						return null;
					} else {
						return cmdPesquisa;
					}
				} else {
					return comando;
				}
			};
		};

		gridProdutos.addEditorGridListener(new EditorGridListenerAdapter() {
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				int qtd = record.getAsInteger("comCompraProdutoQuantidade");
				double valor = record.getAsDouble("comCompraProdutoValor");
				record.set("comCompraProdutoTotal", qtd * valor);
				txtValorProduto.setValue(totalizar());
			}
		});

		gridProdutos.getTopToolbar().addText(OpenSigCore.i18n.txtRegistro() + ": ");
		gridProdutos.getTopToolbar().addElement(lblRegistros.getElement());
		gridProdutos.getTopToolbar().addSpacer();
		add(gridProdutos);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), "comCompraObservacao");
		txtObservacao.setMaxLength(4000);
		txtObservacao.setWidth("95%");
		add(txtObservacao);

		gridProdutos.getStore().addStoreListener(new StoreListenerAdapter() {

			public void onRemove(Store store, Record record, int index) {
				txtValorProduto.setValue(totalizar());
			}

			public void onLoad(Store store, Record[] records) {
				txtValorProduto.setValue(totalizar());

				if (hdnCod.getValueAsString().equals("0")) {
					for (Record rec : records) {
						rec.set(rec.getFields()[0], 0);
					}
				}
			}
		});

		asyncSalvar = new AsyncCallback<ComCompra>() {
			public void onFailure(Throwable caught) {
				contexto.put("erro", caught);
				ComandoSalvarFinal fim = new ComandoSalvarFinal();
				if (autosave) {
					fim.setProximo(cmdPesquisa);
				}
				fim.execute(contexto);
				autosave = false;
			};

			public void onSuccess(ComCompra result) {
				contexto.put("resultado", result);
				hdnCod.setValue(result.getComCompraId() + "");
				ComandoSalvarFinal fim = new ComandoSalvarFinal();
				if (autosave) {
					fim.setProximo(cmdPesquisa);
				}
				fim.execute(contexto);
				autosave = false;
			};
		};
	}

	private void autoSave() {
		autosave = true;
		IComando comando = AntesDaAcao(new ComandoSalvar());
		comando.execute(contexto);
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
			// salavando
			IComando salvar = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					ComercialProxy proxy = new ComercialProxy();
					proxy.salvarCompra(classe, asyncSalvar);
				}
			};
			// calcula os precos zerados
			comando = new AComando(salvar) {
				public void execute(final Map contexto) {
					super.execute(contexto);
					GerarPreco gp = new GerarPreco(classe);
					gp.gerar(new AsyncCallback() {
						public void onFailure(Throwable caught) {
							onSuccess(null);
						};

						public void onSuccess(Object result) {
							comando.execute(contexto);
						};
					});
				}
			};
		}

		return comando;
	}

	public boolean setDados() {
		boolean retorno = true;
		produtos = new ArrayList<ComCompraProduto>();

		if (!gridProdutos.validar(produtos)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		classe.setComCompraProdutos(produtos);
		classe.setComCompraId(Integer.valueOf(hdnCod.getValueAsString()));
		if (txtSerie.getValue() != null) {
			classe.setComCompraSerie(txtSerie.getValue().intValue());
		}
		if (txtNumero.getValue() != null) {
			classe.setComCompraNumero(txtNumero.getValue().intValue());
		}
		if (cmbNatureza.getValue() != null) {
			ComNatureza natureza = new ComNatureza(Integer.valueOf(cmbNatureza.getValue()));
			classe.setComNatureza(natureza);
		}
		classe.setComCompraEmissao(dtEmissao.getValue());
		classe.setComCompraRecebimento(dtRecebimento.getValue());
		if (txtIcmsBase.getValue() != null) {
			classe.setComCompraIcmsBase(txtIcmsBase.getValue().doubleValue());
		}
		if (txtIcmsValor.getValue() != null) {
			classe.setComCompraIcmsValor(txtIcmsValor.getValue().doubleValue());
		}
		if (txtSubBase.getValue() != null) {
			classe.setComCompraIcmssubBase(txtSubBase.getValue().doubleValue());
		}
		if (txtSubValor.getValue() != null) {
			classe.setComCompraIcmssubValor(txtSubValor.getValue().doubleValue());
		}
		if (txtValorProduto.getValue() != null) {
			classe.setComCompraValorProduto(txtValorProduto.getValue().doubleValue());
		}
		if (txtFrete.getValue() != null) {
			classe.setComCompraValorFrete(txtFrete.getValue().doubleValue());
		}
		if (txtSeguro.getValue() != null) {
			classe.setComCompraValorSeguro(txtSeguro.getValue().doubleValue());
		}
		if (txtDesconto.getValue() != null) {
			classe.setComCompraValorDesconto(txtDesconto.getValue().doubleValue());
		}
		if (txtIpi.getValue() != null) {
			classe.setComCompraValorIpi(txtIpi.getValue().doubleValue());
		}
		if (txtOutros.getValue() != null) {
			classe.setComCompraValorOutros(txtOutros.getValue().doubleValue());
		}
		if (txtValorNota.getValue() != null) {
			classe.setComCompraValorNota(txtValorNota.getValue().doubleValue());
		}
		classe.setComCompraObservacao(txtObservacao.getValueAsString());

		if (!hdnFornecedor.getValueAsString().equals("0")) {
			EmpFornecedor fornecedor = new EmpFornecedor(Integer.valueOf(hdnFornecedor.getValueAsString()));
			classe.setEmpFornecedor(fornecedor);
		}

		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}

		if (cmbEstado.getValue() != null) {
			EmpEstado estado = new EmpEstado(Integer.valueOf(cmbEstado.getValue()));
			classe.setEmpEstado(estado);
		}

		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		FiltroNumero fn = new FiltroNumero("comCompraProdutoId", ECompara.IGUAL, 0);
		gridProdutos.getProxy().setFiltroPadrao(fn);
		gridProdutos.getStore().removeAll();
		lblRegistros.setText("0");
	}

	public void mostrarDados() {
		if (cmbNatureza.getStore().getRecords().length == 0) {
			cmbNatureza.getStore().load();
		} else {
			mostrar();
		}
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();

		if (rec != null) {
			getForm().loadRecord(rec);
			classe.setComCompraId(Integer.valueOf(hdnCod.getValueAsString()));
			FiltroObjeto fo = new FiltroObjeto("comCompra", ECompara.IGUAL, classe);
			gridProdutos.getProxy().setFiltroPadrao(fo);
			gridProdutos.getStore().reload();
		} else {
			for (Record rec2 : cmbNatureza.getStore().getRecords()) {
				if (rec2.getAsString("comNaturezaNome").equalsIgnoreCase("Compra")) {
					cmbNatureza.setValue(rec2.getAsString("comNaturezaId"));
					break;
				}
			}
		}
		txtSerie.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			classe.setComCompraFechada(false);
			classe.setComCompraPaga(false);
			classe.setComCompraNfe(false);
			duplicar = false;
		}
	}

	private ComboBox getNatureza() {
		FieldDef[] fdNatureza = new FieldDef[] { new IntegerFieldDef("comNaturezaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("comNaturezaNome") };
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		CoreProxy<ComNatureza> proxy = new CoreProxy<ComNatureza>(new ComNatureza(), fo);
		final Store storeNatureza = new Store(proxy, new ArrayReader(new RecordDef(fdNatureza)), true);
		storeNatureza.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				cmbEstado.getStore().load();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtNatureza(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeNatureza.load();
						}
					}
				});
			}
		});

		cmbNatureza = new ComboBox(OpenSigCore.i18n.txtNatureza(), "comNatureza.comNaturezaId", 100);
		cmbNatureza.setListWidth(200);
		cmbNatureza.setAllowBlank(false);
		cmbNatureza.setStore(storeNatureza);
		cmbNatureza.setTriggerAction(ComboBox.ALL);
		cmbNatureza.setMode(ComboBox.LOCAL);
		cmbNatureza.setDisplayField("comNaturezaNome");
		cmbNatureza.setValueField("comNaturezaId");
		cmbNatureza.setForceSelection(true);
		cmbNatureza.setEditable(false);

		return cmbNatureza;
	}

	private ComboBox getFornecedor() {
		cmbFornecedor = UtilClient.getComboEntidade(new ComboEntidade(new EmpFornecedor()));
		cmbFornecedor.setName("empFornecedor.empEntidade.empEntidadeNome1");
		cmbFornecedor.setLabel(OpenSigCore.i18n.txtFornecedor());
		cmbFornecedor.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnFornecedor.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbFornecedor.getRawValue().equals("")) {
					hdnFornecedor.setValue("0");
				}
			}
		});

		return cmbFornecedor;
	}

	private ComboBox getEstado() {
		FieldDef[] fdEstado = new FieldDef[] { new IntegerFieldDef("empEstadoId"), new IntegerFieldDef("empEstadoIbge"), new StringFieldDef("empEstadoDescricao") };
		CoreProxy<EmpEstado> proxy = new CoreProxy<EmpEstado>(new EmpEstado());
		final Store storeEstado = new Store(proxy, new ArrayReader(new RecordDef(fdEstado)), false);
		storeEstado.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				mostrar();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtEstado(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeEstado.load();
						}
					}
				});
			}
		});

		cmbEstado = new ComboBox(OpenSigCore.i18n.txtEstado(), "empEstado.empEstadoId", 100);
		cmbEstado.setAllowBlank(false);
		cmbEstado.setEditable(false);
		cmbEstado.setStore(storeEstado);
		cmbEstado.setTriggerAction(ComboBox.ALL);
		cmbEstado.setMode(ComboBox.LOCAL);
		cmbEstado.setDisplayField("empEstadoDescricao");
		cmbEstado.setValueField("empEstadoId");
		cmbEstado.setForceSelection(true);
		cmbEstado.setListWidth(200);

		return cmbEstado;
	}

	public void gerarListas() {
		// produtos
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridProdutos.getModelos().getColumnCount(); i++) {
			if (gridProdutos.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridProdutos.getModelos().getColumnHeader(i), gridProdutos.getModelos().getColumnWidth(i), null);
				if (gridProdutos.getModelos().getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) gridProdutos.getModelos().getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados.add(meta);
			}
		}

		// trocando campos visiveis
		metadados.set(14, metadados.get(13));
		metadados.set(13, null);

		SortState ordem = gridProdutos.getStore().getSortState();
		ComCompraProduto comProd = new ComCompraProduto();
		comProd.setCampoOrdem(ordem.getField());
		comProd.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("comCompra", ECompara.IGUAL, new ComCompra(id));

		ExpListagem<ComCompraProduto> produtos = new ExpListagem<ComCompraProduto>();
		produtos.setClasse(comProd);
		produtos.setMetadados(metadados);
		produtos.setNome(gridProdutos.getTitle());
		produtos.setFiltro(filtro);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(produtos);
	}

	// totaliza os produtos
	private double totalizar() {
		double totalProdutos = 0.00;

		lblRegistros.setText(gridProdutos.getStore().getTotalCount() + "");
		for (Record rec : gridProdutos.getStore().getRecords()) {
			totalProdutos += rec.getAsDouble("comCompraProdutoTotal");
		}

		return totalProdutos;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnFornecedor() {
		return hdnFornecedor;
	}

	public void setHdnFornecedor(Hidden hdnFornecedor) {
		this.hdnFornecedor = hdnFornecedor;
	}

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
	}

	public NumberField getTxtSerie() {
		return txtSerie;
	}

	public void setTxtSerie(NumberField txtSerie) {
		this.txtSerie = txtSerie;
	}

	public NumberField getTxtNumero() {
		return txtNumero;
	}

	public void setTxtNumero(NumberField txtNumero) {
		this.txtNumero = txtNumero;
	}

	public ComboBox getCmbNatureza() {
		return cmbNatureza;
	}

	public void setCmbNatureza(ComboBox cmbNatureza) {
		this.cmbNatureza = cmbNatureza;
	}

	public DateField getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(DateField dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public DateField getDtRecebimento() {
		return dtRecebimento;
	}

	public void setDtRecebimento(DateField dtRecebimento) {
		this.dtRecebimento = dtRecebimento;
	}

	public ComboBox getCmbFornecedor() {
		return cmbFornecedor;
	}

	public void setCmbFornecedor(ComboBox cmbFornecedor) {
		this.cmbFornecedor = cmbFornecedor;
	}

	public NumberField getTxtIcmsBase() {
		return txtIcmsBase;
	}

	public void setTxtIcmsBase(NumberField txtIcmsBase) {
		this.txtIcmsBase = txtIcmsBase;
	}

	public NumberField getTxtIcmsValor() {
		return txtIcmsValor;
	}

	public void setTxtIcmsValor(NumberField txtIcmsValor) {
		this.txtIcmsValor = txtIcmsValor;
	}

	public NumberField getTxtSubBase() {
		return txtSubBase;
	}

	public void setTxtSubBase(NumberField txtSubBase) {
		this.txtSubBase = txtSubBase;
	}

	public NumberField getTxtSubValor() {
		return txtSubValor;
	}

	public void setTxtSubValor(NumberField txtSubValor) {
		this.txtSubValor = txtSubValor;
	}

	public NumberField getTxtValorProduto() {
		return txtValorProduto;
	}

	public void setTxtValorProduto(NumberField txtValorProduto) {
		this.txtValorProduto = txtValorProduto;
	}

	public NumberField getTxtFrete() {
		return txtFrete;
	}

	public void setTxtFrete(NumberField txtFrete) {
		this.txtFrete = txtFrete;
	}

	public NumberField getTxtSeguro() {
		return txtSeguro;
	}

	public void setTxtSeguro(NumberField txtSeguro) {
		this.txtSeguro = txtSeguro;
	}

	public NumberField getTxtDesconto() {
		return txtDesconto;
	}

	public void setTxtDesconto(NumberField txtDesconto) {
		this.txtDesconto = txtDesconto;
	}

	public NumberField getTxtIpi() {
		return txtIpi;
	}

	public void setTxtIpi(NumberField txtIpi) {
		this.txtIpi = txtIpi;
	}

	public NumberField getTxtOutros() {
		return txtOutros;
	}

	public void setTxtOutros(NumberField txtOutros) {
		this.txtOutros = txtOutros;
	}

	public NumberField getTxtValorNota() {
		return txtValorNota;
	}

	public void setTxtValorNota(NumberField txtValorNota) {
		this.txtValorNota = txtValorNota;
	}

	public TextArea getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(TextArea txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public ListagemCompraProdutos getGridProdutos() {
		return gridProdutos;
	}

	public void setGridProdutos(ListagemCompraProdutos gridProdutos) {
		this.gridProdutos = gridProdutos;
	}

	public List<ComCompraProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ComCompraProduto> produtos) {
		this.produtos = produtos;
	}

	public ComboBox getCmbEstado() {
		return cmbEstado;
	}

	public void setCmbEstado(ComboBox cmbEstado) {
		this.cmbEstado = cmbEstado;
	}

	public Label getLblRegistros() {
		return lblRegistros;
	}

	public void setLblRegistros(Label lblRegistros) {
		this.lblRegistros = lblRegistros;
	}

}