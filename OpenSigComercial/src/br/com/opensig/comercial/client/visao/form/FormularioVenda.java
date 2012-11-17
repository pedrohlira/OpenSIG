package br.com.opensig.comercial.client.visao.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.client.visao.lista.ListagemVendaProdutos;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoAdicionar;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.PermitirSistema;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.produto.client.controlador.comando.ComandoPesquisa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioVenda extends AFormulario<ComVenda> {

	private Hidden hdnCod;
	private Hidden hdnCliente;
	private Hidden hdnUsuario;
	private Hidden hdnEmpresa;
	private ComboBox cmbCliente;
	private ComboBox cmbNatureza;
	private TextField txtUsuario;
	private NumberField txtPadraoDesc;
	private TextArea txtObservacao;
	private Label lblRegistros;
	private ListagemVendaProdutos gridProdutos;
	private List<ComVendaProduto> produtos;

	private double bruto;
	private double liquido;
	private boolean autorizado;
	private boolean autosave;
	private double max;
	private AsyncCallback asyncSalvar;
	private AsyncCallback<ILogin> asyncLogin;
	private ComandoPesquisa cmdPesquisa;

	public FormularioVenda(SisFuncao funcao) {
		super(new ComVenda(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comVendaId", "0");
		add(hdnCod);
		hdnUsuario = new Hidden("sisUsuario.sisUsuarioId", "0");
		add(hdnUsuario);
		hdnCliente = new Hidden("empCliente.empClienteId", "0");
		add(hdnCliente);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtUsuario = new TextField(OpenSigCore.i18n.txtUsuario(), "sisUsuario.sisUsuarioLogin", 150);
		txtUsuario.setReadOnly(true);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getCliente(), 330);
		linha1.addToRow(txtUsuario, 160);
		linha1.addToRow(getNatureza(), 170);
		add(linha1);
		lblRegistros = new Label();

		txtPadraoDesc = new NumberField("", "padraoCfop", 50, 0);
		txtPadraoDesc.setAllowNegative(false);
		txtPadraoDesc.addListener(new TextFieldListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (newVal == null || newVal.toString().equals("")) {
					field.setValue("0");
				} else if (Double.parseDouble(newVal.toString()) > 100.00) {
					field.setValue("100");
				}
			}
		});

		final AsyncCallback<Record> asyncPesquisa = new AsyncCallback<Record>() {

			public void onFailure(Throwable arg0) {
				// nada
			}

			public void onSuccess(Record result) {
				gridProdutos.stopEditing();
				int pos;

				for (pos = 0; pos < gridProdutos.getStore().getCount(); pos++) {
					if (gridProdutos.getStore().getAt(pos).getAsInteger("prodProdutoId") == result.getAsInteger("prodProdutoId")) {
						break;
					}
				}

				if (pos == gridProdutos.getStore().getCount()) {
					double bruto = result.getAsDouble("prodProdutoPreco");
					double desc = txtPadraoDesc.getValue().doubleValue();
					double liquido = bruto - (bruto * desc / 100);
					String strValor = NumberFormat.getFormat("0.##").format(liquido);
					liquido = Double.valueOf(strValor.replace(",", "."));

					Record reg = gridProdutos.getCampos().createRecord(new Object[gridProdutos.getCampos().getFields().length]);
					reg.set("comVendaProdutoId", 0);
					reg.set("prodProdutoId", result.getAsInteger("prodProdutoId"));
					reg.set("prodProduto.prodProdutoBarra", result.getAsString("prodProdutoBarra"));
					reg.set("prodProduto.prodProdutoDescricao", result.getAsString("prodProdutoDescricao"));
					reg.set("prodProduto.prodProdutoReferencia", result.getAsString("prodProdutoReferencia"));
					reg.set("comVendaProdutoQuantidade", 0);
					reg.set("prodEmbalagem.prodEmbalagemId", result.getAsInteger("prodEmbalagem.prodEmbalagemId"));
					reg.set("prodEmbalagem.prodEmbalagemNome", result.getAsString("prodEmbalagem.prodEmbalagemNome"));
					reg.set("comVendaProdutoBruto", bruto);
					reg.set("comVendaProdutoDesconto", desc);
					reg.set("comVendaProdutoLiquido", liquido);
					reg.set("comVendaProdutoTotalBruto", 0);
					reg.set("comVendaProdutoTotalLiquido", 0);
					reg.set("comVendaProdutoIcms", 0);
					reg.set("comVendaProdutoIpi", result.getAsDouble("prodIpi.prodIpiAliquota"));
					reg.set("comVendaProdutoEstoque", result.getAsInteger("t1.prodEstoqueQuantidade"));

					if (reg.getAsInteger("comVendaProdutoEstoque") > 0 || UtilClient.CONF.get("estoque.ativo").equalsIgnoreCase("nao")) {
						gridProdutos.getStore().add(reg);
					} else {
						new ToastWindow(OpenSigCore.i18n.txtEstoque(), reg.getAsString("prodProduto.prodProdutoDescricao") + " = 0").show();
						return;
					}
				} else {
					new ToastWindow(getTitle(), OpenSigCore.i18n.errExiste()).show();
				}

				for (int col = 0; col < gridProdutos.getModelos().getColumnCount(); col++) {
					if (gridProdutos.getModelos().getDataIndex(col).equals("comVendaProdutoQuantidade")) {
						gridProdutos.startEditing(pos, col);
						break;
					}
				}

				totalizar("");
			}
		};
		cmdPesquisa = new ComandoPesquisa(asyncPesquisa);

		gridProdutos = new ListagemVendaProdutos(true) {
			public IComando AntesDoComando(IComando comando) {
				if (comando instanceof ComandoAdicionar) {
					contexto = new HashMap();
					contexto.put("dados", funcao);
					cmdPesquisa.execute(contexto);

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
		gridProdutos.getTopToolbar().addText(OpenSigCore.i18n.txtPadrao() + "-" + OpenSigCore.i18n.txtDesconto());
		gridProdutos.getTopToolbar().addField(txtPadraoDesc);
		gridProdutos.getTopToolbar().addSpacer();

		gridProdutos.getTopToolbar().addSeparator();
		gridProdutos.getTopToolbar().addText(OpenSigCore.i18n.txtRegistro() + ": ");
		gridProdutos.getTopToolbar().addElement(lblRegistros.getElement());
		gridProdutos.getTopToolbar().addSpacer();
		add(gridProdutos);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), "comVendaObservacao");
		txtObservacao.setMaxLength(255);
		txtObservacao.setWidth("95%");
		add(txtObservacao);

		gridProdutos.addEditorGridListener(new EditorGridListenerAdapter() {
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				totalizar("");
			}
		});
		gridProdutos.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onRemove(Store store, Record record, int index) {
				totalizar("");
			}

			public void onLoad(Store store, Record[] records) {
				totalizar("");

				if (hdnCod.getValueAsString().equals("0")) {
					for (Record rec : records) {
						rec.set(rec.getFields()[0], 0);
					}
				}
			}
		});

		asyncSalvar = new AsyncCallback<ComVenda>() {
			public void onFailure(Throwable caught) {
				contexto.put("erro", caught);
				ComandoSalvarFinal fim = new ComandoSalvarFinal();
				if (autosave) {
					fim.setProximo(cmdPesquisa);
				}
				fim.execute(contexto);
				autosave = false;
			};

			public void onSuccess(ComVenda result) {
				contexto.put("resultado", result);
				hdnCod.setValue(result.getComVendaId() + "");
				ComandoSalvarFinal fim = new ComandoSalvarFinal();
				if (autosave) {
					fim.setProximo(cmdPesquisa);
				}
				fim.execute(contexto);
				autosave = false;
			};
		};

		asyncLogin = new AsyncCallback<ILogin>() {
			public void onSuccess(ILogin result) {
				if (result != null && result.getDesconto() > max) {
					autorizado = true;
					ComercialProxy proxy = new ComercialProxy();
					proxy.salvarVenda(classe, asyncSalvar);
				} else {
					autorizado = false;
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				}
			}

			public void onFailure(Throwable caught) {
				autorizado = false;
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errSalvar());
			}
		};
	}

	private void autoSave() {
		autosave = true;
		AntesDaAcao(new ComandoSalvar());
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
			max = 0.00;
			for (Record rec : gridProdutos.getStore().getRecords()) {
				max = rec.getAsDouble("comVendaProdutoDesconto") > max ? rec.getAsDouble("comVendaProdutoDesconto") : max;
			}

			if (max <= Ponte.getLogin().getDesconto() || autorizado) {
				ComercialProxy proxy = new ComercialProxy();
				proxy.salvarVenda(classe, asyncSalvar);
			} else {
				PermitirSistema permitir = (PermitirSistema) GWT.create(PermitirSistema.class);
				permitir.setInfo(OpenSigCore.i18n.msgPermissaoDesconto(Ponte.getLogin().getDesconto() + ""));
				permitir.executar(asyncLogin);
			}
			comando = null;
		}

		return comando;
	}

	/*
	 * @see br.com.sig.core.client.visao.lista.IFormulario#setDados()
	 */
	public boolean setDados() {
		boolean retorno = true;
		produtos = new ArrayList<ComVendaProduto>();

		if (!gridProdutos.validar(produtos)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		classe.setComVendaProdutos(produtos);
		classe.setComVendaId(Integer.valueOf(hdnCod.getValueAsString()));
		if (!hdnCliente.getValueAsString().equals("0")) {
			EmpCliente cliente = new EmpCliente(Integer.valueOf(hdnCliente.getValueAsString()));
			classe.setEmpCliente(cliente);
		}

		if (cmbNatureza.getValue() != null) {
			ComNatureza natureza = new ComNatureza(Integer.valueOf(cmbNatureza.getValue()));
			classe.setComNatureza(natureza);
		}

		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}

		if (hdnUsuario.getValueAsString().equals("0")) {
			classe.setSisUsuario(new SisUsuario(Ponte.getLogin().getId()));
		} else {
			classe.setSisUsuario(new SisUsuario(Integer.valueOf(hdnUsuario.getValueAsString())));
		}

		classe.setComVendaData(new Date());
		classe.setComVendaValorBruto(bruto);
		classe.setComVendaValorLiquido(liquido);
		classe.setComVendaObservacao(txtObservacao.getValueAsString());

		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		FiltroNumero fn = new FiltroNumero("comVendaProdutoId", ECompara.IGUAL, 0);
		gridProdutos.getProxy().setFiltroPadrao(fn);
		gridProdutos.getStore().removeAll();
		lblRegistros.setText("0");
		autorizado = false;
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
			classe.setComVendaId(Integer.valueOf(hdnCod.getValueAsString()));
			FiltroObjeto fo = new FiltroObjeto("comVenda", ECompara.IGUAL, classe);
			gridProdutos.getProxy().setFiltroPadrao(fo);
			gridProdutos.getStore().reload();
		} else {
			txtUsuario.setValue(Ponte.getLogin().getUsuario());
			for (Record rec2 : cmbNatureza.getStore().getRecords()) {
				if (rec2.getAsString("comNaturezaNome").equalsIgnoreCase("Venda")) {
					cmbNatureza.setValue(rec2.getAsString("comNaturezaId"));
					break;
				}
			}
		}
		cmbCliente.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			hdnUsuario.setValue("0");
			txtUsuario.setValue(Ponte.getLogin().getUsuario());
			classe.setComVendaFechada(false);
			classe.setComVendaRecebida(false);
			classe.setComVendaNfe(false);
			classe.setComVendaCancelada(false);
			duplicar = false;
		}
	}

	private ComboBox getCliente() {
		cmbCliente = UtilClient.getComboEntidade(new ComboEntidade(new EmpCliente()));
		cmbCliente.setName("empCliente.empEntidade.empEntidadeNome1");
		cmbCliente.setLabel(OpenSigCore.i18n.txtCliente());
		cmbCliente.setWidth(300);
		cmbCliente.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnCliente.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbCliente.getRawValue().equals("")) {
					hdnCliente.setValue("0");
				}
			}
		});

		return cmbCliente;
	}

	private ComboBox getNatureza() {
		FieldDef[] fdNatureza = new FieldDef[] { new IntegerFieldDef("comNaturezaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("comNaturezaNome"), new StringFieldDef("comNaturezaDescricao"), new IntegerFieldDef("comNaturezaCfopDentro"), new IntegerFieldDef("comNaturezaCfopFora") };
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		CoreProxy<ComNatureza> proxy = new CoreProxy<ComNatureza>(new ComNatureza(), fo);
		final Store storeNatureza = new Store(proxy, new ArrayReader(new RecordDef(fdNatureza)), true);
		storeNatureza.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				mostrar();
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

		cmbNatureza = new ComboBox(OpenSigCore.i18n.txtNatureza(), "comNatureza.comNaturezaId", 150);
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

		SortState ordem = gridProdutos.getStore().getSortState();
		ComVendaProduto venProd = new ComVendaProduto();
		venProd.setCampoOrdem(ordem.getField());
		venProd.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("comVenda", ECompara.IGUAL, new ComVenda(id));
		
		ExpListagem<ComVendaProduto> produtos = new ExpListagem<ComVendaProduto>();
		produtos.setClasse(venProd);
		produtos.setMetadados(metadados);
		produtos.setNome(gridProdutos.getTitle());
		produtos.setFiltro(filtro);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(produtos);
	}

	private void totalizar(String field) {
		double bruto = 0.00;
		double liquido = 0.00;

		lblRegistros.setText(gridProdutos.getStore().getRecords().length + "");
		for (Record rec : gridProdutos.getStore().getRecords()) {
			bruto += rec.getAsDouble("comVendaProdutoTotalBruto");
			liquido += rec.getAsDouble("comVendaProdutoTotalLiquido");
		}

		this.bruto = bruto;
		this.liquido = liquido;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnCliente() {
		return hdnCliente;
	}

	public void setHdnCliente(Hidden hdnCliente) {
		this.hdnCliente = hdnCliente;
	}

	public Hidden getHdnUsuario() {
		return hdnUsuario;
	}

	public void setHdnUsuario(Hidden hdnUsuario) {
		this.hdnUsuario = hdnUsuario;
	}

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
	}

	public ComboBox getCmbCliente() {
		return cmbCliente;
	}

	public void setCmbCliente(ComboBox cmbCliente) {
		this.cmbCliente = cmbCliente;
	}

	public TextField getTxtUsuario() {
		return txtUsuario;
	}

	public void setTxtUsuario(TextField txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public TextArea getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(TextArea txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public Label getLblRegistros() {
		return lblRegistros;
	}

	public void setLblRegistros(Label lblRegistros) {
		this.lblRegistros = lblRegistros;
	}

	public ListagemVendaProdutos getGridProdutos() {
		return gridProdutos;
	}

	public void setGridProdutos(ListagemVendaProdutos gridProdutos) {
		this.gridProdutos = gridProdutos;
	}

	public List<ComVendaProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ComVendaProduto> produtos) {
		this.produtos = produtos;
	}

	public ComboBox getCmbNatureza() {
		return cmbNatureza;
	}

	public void setCmbNatureza(ComboBox cmbNatureza) {
		this.cmbNatureza = cmbNatureza;
	}

	public NumberField getTxtPadraoDesc() {
		return txtPadraoDesc;
	}

	public void setTxtPadraoDesc(NumberField txtPadraoDesc) {
		this.txtPadraoDesc = txtPadraoDesc;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}
}
