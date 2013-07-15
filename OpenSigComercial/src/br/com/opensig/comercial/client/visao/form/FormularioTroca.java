package br.com.opensig.comercial.client.visao.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.client.visao.lista.ListagemTrocaProdutos;
import br.com.opensig.comercial.shared.modelo.ComTroca;
import br.com.opensig.comercial.shared.modelo.ComTrocaProduto;
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
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.js.OpenSigEmpresaJS;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.produto.client.controlador.comando.ComandoPesquisa;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioTroca extends AFormulario<ComTroca> {

	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private Hidden hdnCompra;
	private TextField txtCliente;
	private NumberField txtValor;
	private NumberField txtEcf;
	private NumberField txtCoo;
	private Label lblRegistros;
	private ListagemTrocaProdutos gridProdutos;
	private List<ComTrocaProduto> produtos;
	private ComandoPesquisa cmdPesquisa;

	public FormularioTroca(SisFuncao funcao) {
		super(new ComTroca(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comTrocaId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtCliente = new TextField(OpenSigCore.i18n.txtEntidadeDoc1(), "comTrocaCliente", 200);
		txtCliente.setAllowBlank(false);
		txtCliente.setMinLength(11);
		txtCliente.setMaxLength(18);
		txtCliente.setSelectOnFocus(true);
		txtCliente.setInvalidText(OpenSigCore.i18n.msgCampoInvalido());
		txtCliente.setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				boolean cnpj = OpenSigEmpresaJS.validarCNPJ(txtCliente.getId(), value);
				boolean cpf = OpenSigEmpresaJS.validarCPF(txtCliente.getId(), value);
				return cnpj || cpf;
			}
		});

		txtValor = new NumberField(OpenSigCore.i18n.txtValor(), "comTrocaValor", 100);
		txtValor.setValue(0.00);
		txtValor.setReadOnly(true);

		txtEcf = new NumberField(OpenSigCore.i18n.txtEcf(), "comTrocaEcf", 50);
		txtEcf.setAllowBlank(false);
		txtEcf.setAllowDecimals(false);
		txtEcf.setAllowNegative(false);

		txtCoo = new NumberField(OpenSigCore.i18n.txtCoo(), "comTrocaCoo", 80);
		txtCoo.setAllowBlank(false);
		txtCoo.setAllowDecimals(false);
		txtCoo.setAllowNegative(false);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtCliente, 220);
		linha1.addToRow(txtValor, 120);
		linha1.addToRow(txtEcf, 70);
		linha1.addToRow(txtCoo, 100);
		add(linha1);
		lblRegistros = new Label();

		final AsyncCallback<Record> asyncPesquisa = new AsyncCallback<Record>() {

			public void onFailure(Throwable arg0) {
			}

			public void onSuccess(Record result) {
				gridProdutos.stopEditing();
				Record reg = gridProdutos.getCampos().createRecord(new Object[gridProdutos.getCampos().getFields().length]);
				reg.set("comTrocaProdutoId", 0);
				reg.set("prodProdutoId", result.getAsInteger("prodProdutoId"));
				reg.set("comTrocaProdutoBarra", result.getAsString("prodProdutoBarra"));
				reg.set("prodProduto.prodProdutoDescricao", result.getAsString("prodProdutoDescricao"));
				reg.set("prodProduto.prodProdutoReferencia", result.getAsString("prodProdutoReferencia"));
				reg.set("comTrocaProdutoQuantidade", 0);
				reg.set("prodEmbalagem.prodEmbalagemId", result.getAsInteger("prodEmbalagem.prodEmbalagemId"));
				reg.set("prodEmbalagem.prodEmbalagemNome", result.getAsString("prodEmbalagem.prodEmbalagemNome"));
				reg.set("comTrocaProdutoValor", result.getAsDouble("prodProdutoPreco"));
				reg.set("comTrocaProdutoTotal", 0);
				gridProdutos.getStore().add(reg);

				for (int col = 0; col < gridProdutos.getModelos().getColumnCount(); col++) {
					if (gridProdutos.getModelos().getDataIndex(col).equals("comTrocaProdutoQuantidade")) {
						gridProdutos.startEditing(gridProdutos.getStore().getCount() - 1, col);
						break;
					}
				}
				txtValor.setValue(totalizar());
			}
		};
		cmdPesquisa = new ComandoPesquisa(asyncPesquisa);

		gridProdutos = new ListagemTrocaProdutos(true) {
			public IComando AntesDoComando(IComando comando) {
				if (comando instanceof ComandoAdicionar) {
					contexto = new HashMap();
					contexto.put("dados", funcao);
					return cmdPesquisa;
				} else {
					return comando;
				}
			};
		};

		gridProdutos.addEditorGridListener(new EditorGridListenerAdapter() {
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				int qtd = record.getAsInteger("comTrocaProdutoQuantidade");
				double valor = record.getAsDouble("comTrocaProdutoValor");
				record.set("comTrocaProdutoTotal", qtd * valor);
				txtValor.setValue(totalizar());
			}
		});

		gridProdutos.getTopToolbar().addText(OpenSigCore.i18n.txtRegistro() + ": ");
		gridProdutos.getTopToolbar().addElement(lblRegistros.getElement());
		gridProdutos.getTopToolbar().addSpacer();
		add(gridProdutos);

		gridProdutos.getStore().addStoreListener(new StoreListenerAdapter() {

			public void onRemove(Store store, Record record, int index) {
				txtValor.setValue(totalizar());
			}

			public void onLoad(Store store, Record[] records) {
				txtValor.setValue(totalizar());

				if (hdnCod.getValueAsString().equals("0")) {
					for (Record rec : records) {
						rec.set(rec.getFields()[0], 0);
					}
				}
			}
		});

	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		if (comando instanceof ComandoSalvar) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					ComercialProxy proxy = new ComercialProxy();
					proxy.salvarTroca(classe, ASYNC);
				}
			};
		}
		return comando;
	}

	public boolean setDados() {
		boolean retorno = true;
		produtos = new ArrayList<ComTrocaProduto>();

		if (!gridProdutos.validar(produtos)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		classe.setComTrocaProdutos(produtos);
		classe.setComTrocaId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setComTrocaCliente(txtCliente.getValueAsString());
		classe.setComTrocaData(new Date());
		classe.setComTrocaValor(txtValor.getValue().doubleValue());
		if (txtEcf.getValue() != null) {
			classe.setComTrocaEcf(txtEcf.getValue().intValue());
		}
		if (txtCoo.getValue() != null) {
			classe.setComTrocaCoo(txtCoo.getValue().intValue());
		}
		classe.setComTrocaAtivo(true);
		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}

		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		FiltroNumero fn = new FiltroNumero("comTrocaProdutoId", ECompara.IGUAL, 0);
		gridProdutos.getProxy().setFiltroPadrao(fn);
		gridProdutos.getStore().removeAll();
		lblRegistros.setText("0");
	}

	public void mostrarDados() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();

		if (rec != null) {
			getForm().loadRecord(rec);
			classe.setComTrocaId(Integer.valueOf(hdnCod.getValueAsString()));
			FiltroObjeto fo = new FiltroObjeto("comTroca", ECompara.IGUAL, classe);
			gridProdutos.getProxy().setFiltroPadrao(fo);
			gridProdutos.getStore().reload();
		}
		txtCliente.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			duplicar = false;
		}
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
		metadados.set(12, metadados.get(11));
		metadados.set(11, null);

		SortState ordem = gridProdutos.getStore().getSortState();
		ComTrocaProduto comProd = new ComTrocaProduto();
		comProd.setCampoOrdem(ordem.getField());
		comProd.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("comTroca", ECompara.IGUAL, new ComTroca(id));

		ExpListagem<ComTrocaProduto> produtos = new ExpListagem<ComTrocaProduto>();
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
			totalProdutos += rec.getAsDouble("comTrocaProdutoTotal");
		}

		return totalProdutos;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
	}

	public Hidden getHdnCompra() {
		return hdnCompra;
	}

	public void setHdnCompra(Hidden hdnCompra) {
		this.hdnCompra = hdnCompra;
	}

	public TextField getTxtCliente() {
		return txtCliente;
	}

	public void setTxtCliente(TextField txtCliente) {
		this.txtCliente = txtCliente;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public Label getLblRegistros() {
		return lblRegistros;
	}

	public void setLblRegistros(Label lblRegistros) {
		this.lblRegistros = lblRegistros;
	}

	public ListagemTrocaProdutos getGridProdutos() {
		return gridProdutos;
	}

	public void setGridProdutos(ListagemTrocaProdutos gridProdutos) {
		this.gridProdutos = gridProdutos;
	}

	public List<ComTrocaProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ComTrocaProduto> produtos) {
		this.produtos = produtos;
	}

	public ComandoPesquisa getCmdPesquisa() {
		return cmdPesquisa;
	}

	public void setCmdPesquisa(ComandoPesquisa cmdPesquisa) {
		this.cmdPesquisa = cmdPesquisa;
	}

	public NumberField getTxtEcf() {
		return txtEcf;
	}

	public void setTxtEcf(NumberField txtEcf) {
		this.txtEcf = txtEcf;
	}

	public NumberField getTxtCoo() {
		return txtCoo;
	}

	public void setTxtCoo(NumberField txtCoo) {
		this.txtCoo = txtCoo;
	}

}