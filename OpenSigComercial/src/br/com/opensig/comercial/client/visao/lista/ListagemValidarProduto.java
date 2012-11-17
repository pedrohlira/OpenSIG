package br.com.opensig.comercial.client.visao.lista;

import java.util.HashMap;
import java.util.Map;

import br.com.opensig.comercial.client.GerarPreco;
import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.FormularioPagar;
import br.com.opensig.produto.client.controlador.comando.ComandoPesquisa;
import br.com.opensig.produto.shared.modelo.ProdCategoria;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemValidarProduto {

	private Window wnd;
	private TabPanel tab;
	private ListagemCompraProdutos gridProdutos;
	private FormularioPagar formPagar;
	private ComCompra compra;
	private DateField dtRecebimento;
	private ComboBox cmbNatureza;
	private ComboBox cmbCategoria;
	private NumberField txtFrete;
	private Checkbox chkFechar;
	private Checkbox chkPagar;
	private Map contexto;

	public ListagemValidarProduto(ComCompra compra, Dados dados) {
		this.compra = compra;
		contexto = new HashMap();
		contexto.put("dados", dados);

		// tab
		tab = new TabPanel();
		tab.setClosable(true);
		tab.setTabPosition(Position.BOTTOM);
		tab.add(getProdutos());
		tab.add(getPagamento().getPanel());
		tab.setActiveItem(1);

		// janela
		wnd = new Window();
		wnd.setTitle(OpenSigCore.i18n.txtImportar() + " -> " + OpenSigCore.i18n.txtNfe(), "icon-nfe");
		wnd.setLayout(new FitLayout());
		wnd.setModal(true);
		wnd.setWidth(1000);
		wnd.setHeight(700);
		wnd.setClosable(false);
		wnd.setButtonAlign(Position.CENTER);
		wnd.addButton(getCancelar());
		wnd.addButton(getSalvar());
		wnd.setTopToolbar(getTopo());
		wnd.add(tab);
		wnd.show();
	}

	private ListagemCompraProdutos getProdutos() {
		final AsyncCallback<Record> asyncPesquisa = new AsyncCallback<Record>() {

			public void onFailure(Throwable arg0) {
				// nada
			}

			public void onSuccess(Record result) {
				Record rec = gridProdutos.getSelectionModel().getSelected();
				rec.set("prodProdutoId", result.getAsInteger("prodProdutoId"));
				rec.set("prodProduto.prodProdutoBarra", "".equals(result.getAsString("prodProdutoBarra")) ? null : result.getAsString("prodProdutoBarra"));
				rec.set("prodProduto.prodProdutoDescricao", result.getAsString("prodProdutoDescricao"));
				rec.set("prodProduto.prodProdutoReferencia", result.getAsString("prodProdutoReferencia"));
				rec.set("prodProduto.prodProdutoIncentivo", result.getAsBoolean("prodProdutoIncentivo"));
				rec.set("prodEmbalagem.prodEmbalagemId", result.getAsInteger("prodEmbalagem.prodEmbalagemId"));
				rec.set("comCompraProdutoPreco", result.getAsDouble("prodProdutoPreco"));
			}
		};

		gridProdutos = new ListagemCompraProdutos(false);
		gridProdutos.purgeListeners();
		gridProdutos.setTitle(OpenSigCore.i18n.txtProduto(), "icon-produtocompra");
		gridProdutos.setStore(getDadosProdutos());
		gridProdutos.getStoreEmbalagem().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				gridProdutos.getStore().load();
			};
		});
		gridProdutos.addEditorGridListener(new EditorGridListenerAdapter() {
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex, int colIndex) {
				return record.getAsInteger("prodProdutoId") == 0 || field.equals("prodEmbalagem.prodEmbalagemId") || field.equals("comCompraProdutoPreco");
			}
		});
		gridProdutos.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
				gridProdutos.getSelectionModel().selectRow(rowIndex);
				ComandoPesquisa cmdPesquisa = new ComandoPesquisa(asyncPesquisa);
				cmdPesquisa.execute(contexto);
			}
		});

		ToolTip tip = new ToolTip(OpenSigCore.i18n.msgCompraProduto());
		tip.applyTo(gridProdutos);

		return gridProdutos;
	}

	private FormularioPagar getPagamento() {
		formPagar = new FormularioPagar((SisFuncao) contexto.get("dados"));
		formPagar.setTitle(OpenSigCore.i18n.txtPagar(), "icon-pagar");
		formPagar.enable();

		formPagar.addListener(new FormPanelListenerAdapter() {
			public void onRender(Component component) {
				// setando a pagar
				formPagar.getTopToolbar().hide();
				formPagar.getHdnEmpresa().setValue(compra.getEmpEmpresa().getEmpEmpresaId() + "");
				formPagar.getHdnEntidade().setValue(compra.getEmpFornecedor().getEmpEntidade().getEmpEntidadeId() + "");
				formPagar.getCmbEntidade().setValue(compra.getEmpFornecedor().getEmpEntidade().getEmpEntidadeNome1());
				formPagar.getCmbEntidade().disable();
				formPagar.getTxtNfe().setValue(compra.getComCompraNumero());
				formPagar.getTxtNfe().disable();
				formPagar.getTxtValor().setValue(compra.getComCompraValorNota());
				formPagar.getTxtValor().disable();
				String data = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(compra.getComCompraRecebimento());
				formPagar.getDtCadastro().setValue(data);
				formPagar.getDtCadastro().disable();
				formPagar.mostrarDados();
				formPagar.getGridFormas().setHeight(260);
				formPagar.getGridFormas().setStore(getDadosPagamentos());
				formPagar.getCmbConta().getStore().addStoreListener(new StoreListenerAdapter() {
					public void onLoad(Store store, Record[] records) {
						formPagar.getCmbConta().setValue(UtilClient.CONF.get("conta.padrao"));
					}
				});

				formPagar.getGridFormas().getStoreForma().addStoreListener(new StoreListenerAdapter() {
					public void onLoad(Store store, Record[] records) {
						formPagar.getGridFormas().getStore().load();
					}
				});

				Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
					public boolean execute() {
						if (formPagar.getTreeCategoria().getRoot().getChildNodes().length > 0) {
							formPagar.getTreeCategoria().selecionar(new String[] { UtilClient.CONF.get("categoria.compra") });
							formPagar.getTreeCategoria().disable();
							return false;
						} else {
							return true;
						}
					}
				}, 1000);
				tab.setActiveItem(0);
			}
		});

		return formPagar;
	}

	private Toolbar getTopo() {
		// barra de tarefas
		dtRecebimento = new DateField(OpenSigCore.i18n.txtRecebimento(), "comCompraRecebimento", 90);
		dtRecebimento.setAllowBlank(false);

		txtFrete = new NumberField(OpenSigCore.i18n.txtFrete(), "comCompraValorFrete", 80);
		txtFrete.setAllowBlank(false);
		txtFrete.setAllowNegative(false);
		txtFrete.setMaxLength(13);

		chkPagar = new Checkbox(OpenSigCore.i18n.txtPagar(), "comCompraPaga");
		chkPagar.setChecked(compra.getComCompraPaga());

		chkFechar = new Checkbox(OpenSigCore.i18n.txtFechar(), "comCompraFechada");
		chkFechar.setChecked(true);
		chkFechar.addListener(new CheckboxListenerAdapter() {
			public void onCheck(Checkbox field, boolean checked) {
				if (checked == false) {
					chkPagar.setChecked(false);
					chkPagar.disable();
				} else {
					chkPagar.enable();
				}
			}
		});

		Toolbar tool = new Toolbar();
		tool.addText(OpenSigCore.i18n.txtRecebimento() + ": ");
		tool.addField(dtRecebimento);
		tool.addSpacer();
		tool.addText(OpenSigCore.i18n.txtNatureza() + ": ");
		tool.addField(getNatureza());
		tool.addSpacer();
		tool.addText(OpenSigCore.i18n.txtFrete() + ": ");
		tool.addField(txtFrete);
		tool.addSpacer();
		tool.addField(chkFechar);
		tool.addSpacer();
		tool.addField(chkPagar);
		tool.addFill();
		tool.addText(OpenSigCore.i18n.txtCategoria() + ": ");
		tool.addField(getCategoria());

		return tool;
	}

	private Button getSalvar() {
		Button btnSalvar = new Button();
		btnSalvar.setText(OpenSigCore.i18n.txtSalvar());
		btnSalvar.setIconCls("icon-salvar");
		btnSalvar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				salvar();
			}
		});
		return btnSalvar;
	}

	private Button getCancelar() {
		Button btnCancelar = new Button();
		btnCancelar.setText(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				wnd.close();
			}
		});
		return btnCancelar;
	}

	private void salvar() {
		// validando a compra
		boolean validado = true;
		int row = 0;
		for (Record rec : gridProdutos.getStore().getRecords()) {
			atualizaItem(rec, row++);
		}

		if (dtRecebimento.getValue() == null) {
			validado = false;
			new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errInvalido() + " -> " + OpenSigCore.i18n.txtRecebimento()).show();
		}

		if (txtFrete.getValue() == null) {
			validado = false;
			new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errInvalido() + " -> " + OpenSigCore.i18n.txtFrete()).show();
		}

		if (chkPagar.getValue()) {
			if (formPagar.setDados()) {
				compra.setFinPagar(formPagar.getClasse());
			} else {
				validado = false;
				tab.setActiveItem(1);
			}
		}

		for (ComCompraProduto cProd : compra.getComCompraProdutos()) {
			ProdProduto prod = cProd.getProdProduto();
			if (prod.getProdProdutoId() == 0) {
				prod.setProdProdutoCategoria(cmbCategoria.getValue());
			}
			if ("".equals(prod.getProdProdutoBarra())) {
				prod.setProdProdutoBarra(null);
			}
		}

		if (validado) {
			compra.setComCompraRecebimento(dtRecebimento.getValue());
			compra.setComCompraValorFrete(txtFrete.getValue().doubleValue());
			compra.setComNatureza(new ComNatureza(Integer.valueOf(cmbNatureza.getValue())));
			compra.setComCompraFechada(chkFechar.getValue());
			compra.setComCompraPaga(chkPagar.getValue());
			wnd.close();
			gerarPreco();
		}
	}

	private void atualizaItem(Record rec, int row) {
		// recupera
		ComCompraProduto comPro = compra.getComCompraProdutos().get(row);
		ProdProduto prod = comPro.getProdProduto();
		// altera
		prod.setProdProdutoId(rec.getAsInteger("prodProdutoId"));
		prod.setProdProdutoBarra("".equals(rec.getAsString("prodProduto.prodProdutoBarra")) ? null : rec.getAsString("prodProduto.prodProdutoBarra"));
		prod.setProdProdutoDescricao(rec.getAsString("prodProduto.prodProdutoDescricao"));
		prod.setProdProdutoReferencia(rec.getAsString("prodProduto.prodProdutoReferencia"));
		prod.setProdProdutoIncentivo(rec.getAsBoolean("prodProduto.prodProdutoIncentivo"));
		prod.setProdEmbalagem(new ProdEmbalagem(rec.getAsInteger("prodEmbalagem.prodEmbalagemId")));

		// seta
		comPro.setProdProduto(prod);
		comPro.setComCompraProdutoPreco(rec.getAsDouble("comCompraProdutoPreco"));
		comPro.setProdEmbalagem(new ProdEmbalagem(rec.getAsInteger("prodEmbalagem.prodEmbalagemId")));
		compra.getComCompraProdutos().set(row, comPro);
	}

	private void gerarPreco() {
		GerarPreco gp = new GerarPreco(compra);
		gp.gerar(new AsyncCallback() {
			public void onFailure(Throwable caught) {
				onSuccess(null);
			};

			public void onSuccess(Object result) {
				enviar();
			};
		});
	}

	private void enviar() {
		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
		ComercialProxy proxy = new ComercialProxy();
		proxy.salvarCompra(compra, new AsyncCallback<ComCompra>() {
			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errSalvar());
				new ToastWindow(OpenSigCore.i18n.txtSalvar(), caught.getMessage()).show();
			};

			public void onSuccess(ComCompra result) {
				MessageBox.hide();
				TabPanel tab = (TabPanel) Ponte.getCentro().getActiveTab();
				((IListagem) tab.getActiveTab()).getPanel().getStore().reload();
				new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.msgSalvarOK()).show();
			};
		});
	}

	private Store getDadosProdutos() {
		String[][] dados = new String[compra.getComCompraProdutos().size()][];
		for (int i = 0; i < compra.getComCompraProdutos().size(); i++) {
			dados[i] = compra.getComCompraProdutos().get(i).toArray();
		}

		for (BaseColumnConfig base : gridProdutos.getModelos().getColumnConfigs()) {
			ColumnConfig conf = null;
			if (base instanceof SummaryColumnConfig) {
				SummaryColumnConfig sumario = (SummaryColumnConfig) base;
				conf = (ColumnConfig) sumario.getColumn();
			} else {
				conf = (ColumnConfig) base;
			}

			conf.setSortable(false);
			gridProdutos.getModelos().setEditable(conf.getId(), false);

			// barra
			if (conf.getDataIndex().equals("prodProduto.prodProdutoBarra")) {
				TextField txtBarra = new TextField();
				txtBarra.setMinLength(8);
				txtBarra.setMaxLength(14);
				txtBarra.setRegex("^(\\d{8}|\\d{12}|\\d{13}|\\d{14})$");

				gridProdutos.setEditorListener(txtBarra);
				gridProdutos.getModelos().setEditable(conf.getId(), true);
				conf.setEditor(new GridEditor(txtBarra));
			}
			// descricao
			if (conf.getDataIndex().equals("prodProduto.prodProdutoDescricao")) {
				TextField txtDescricao = new TextField();
				txtDescricao.setAllowBlank(false);
				txtDescricao.setMaxLength(100);

				gridProdutos.setEditorListener(txtDescricao);
				gridProdutos.getModelos().setEditable(conf.getId(), true);
				conf.setEditor(new GridEditor(txtDescricao));
			}
			// ref
			if (conf.getDataIndex().equals("prodProduto.prodProdutoReferencia")) {
				TextField txtRef = new TextField();
				txtRef.setRegex("^\\w*$");
				txtRef.setMaxLength(20);

				gridProdutos.setEditorListener(txtRef);
				gridProdutos.getModelos().setEditable(conf.getId(), true);
				conf.setEditor(new GridEditor(txtRef));
			}
			// embalagem
			if (conf.getDataIndex().startsWith("prodEmbalagem.prodEmbalagemId")) {
				gridProdutos.getModelos().setEditable(conf.getId(), true);
			}
			// preco
			if (conf.getDataIndex().startsWith("comCompraProdutoPreco")) {
				gridProdutos.getModelos().setEditable(conf.getId(), true);
			}
			// incentivo
			if (conf.getDataIndex().equals("prodProduto.prodProdutoIncentivo")) {
				gridProdutos.getModelos().setEditable(conf.getId(), true);
				conf.setEditor(new GridEditor(new Checkbox()));
			}

		}

		MemoryProxy proxy = new MemoryProxy(dados);
		ArrayReader reader = new ArrayReader(gridProdutos.getCampos());
		Store store = new Store(proxy, reader);
		store.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				for (int i = 0; i < records.length; i++) {
					if (records[i].getAsInteger("prodProdutoId") == 0) {
						gridProdutos.getView().getRow(i).getStyle().setColor("blue");
					}
				}
			}
		});

		return store;
	}

	private Store getDadosPagamentos() {
		String[][] dados = new String[compra.getFinPagar().getFinPagamentos().size()][];
		for (int i = 0; i < compra.getFinPagar().getFinPagamentos().size(); i++) {
			dados[i] = compra.getFinPagar().getFinPagamentos().get(i).toArray();
		}

		MemoryProxy proxy = new MemoryProxy(dados);
		ArrayReader reader = new ArrayReader(formPagar.getGridFormas().getCampos());
		Store store = new Store(proxy, reader);

		return store;
	}

	private ComboBox getNatureza() {
		FieldDef[] fdNatureza = new FieldDef[] { new IntegerFieldDef("comNaturezaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("comNaturezaNome") };
		CoreProxy<ComNatureza> proxy = new CoreProxy<ComNatureza>(new ComNatureza());
		final Store storeNatureza = new Store(proxy, new ArrayReader(new RecordDef(fdNatureza)), true);
		storeNatureza.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				for (Record rec : records) {
					if (rec.getAsString("comNaturezaNome").equalsIgnoreCase(OpenSigCore.i18n.txtCompra())) {
						cmbNatureza.setValue(rec.getAsString("comNaturezaId"));
						break;
					}
				}
				txtFrete.setValue(compra.getComCompraValorFrete());
				String data = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(compra.getComCompraRecebimento());
				dtRecebimento.setValue(data);
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
		storeNatureza.load();

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

	private ComboBox getCategoria() {
		FieldDef[] fdCategoria = new FieldDef[] { new IntegerFieldDef("prodCategoriaId"), new StringFieldDef("prodCategoriaDescricao") };
		CoreProxy<ProdCategoria> proxy = new CoreProxy<ProdCategoria>(new ProdCategoria());
		final Store storeCategoria = new Store(proxy, new ArrayReader(new RecordDef(fdCategoria)), true);
		storeCategoria.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				cmbCategoria.setValue(UtilClient.CONF.get("categoria.padrao"));
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtNatureza(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeCategoria.load();
						}
					}
				});
			}
		});
		storeCategoria.load();

		cmbCategoria = new ComboBox(OpenSigCore.i18n.txtNatureza(), "prodCategoria.prodCategoriaId", 200);
		cmbCategoria.setListWidth(200);
		cmbCategoria.setAllowBlank(false);
		cmbCategoria.setStore(storeCategoria);
		cmbCategoria.setTriggerAction(ComboBox.ALL);
		cmbCategoria.setMode(ComboBox.LOCAL);
		cmbCategoria.setDisplayField("prodCategoriaDescricao");
		cmbCategoria.setValueField("prodCategoriaDescricao");
		cmbCategoria.setForceSelection(true);
		cmbCategoria.setEditable(false);

		return cmbCategoria;
	}
}
