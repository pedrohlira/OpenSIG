package br.com.opensig.comercial.client.visao.lista;

import java.util.HashMap;
import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.FormularioReceber;
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
import com.gwtext.client.core.Ext;
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
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemValidarVenda {

	private Window wnd;
	private TabPanel tab;
	private ListagemVendaProdutos gridProdutos;
	private FormularioReceber formReceber;
	private ComVenda venda;
	private ComboBox cmbNatureza;
	private ComboBox cmbCategoria;
	private Checkbox chkFechar;
	private Checkbox chkReceber;
	private Map contexto;

	public ListagemValidarVenda(ComVenda venda, Dados dados) {
		this.venda = venda;
		contexto = new HashMap();
		contexto.put("dados", dados);

		// tab
		tab = new TabPanel();
		tab.setClosable(true);
		tab.setTabPosition(Position.BOTTOM);
		tab.add(getProdutos());
		tab.add(getRecebimento().getPanel());
		tab.setActiveItem(1);

		// janela
		wnd = new Window();
		wnd.setTitle(OpenSigCore.i18n.txtImportar() + " -> " + OpenSigCore.i18n.txtNfe(), "icon-nfe");
		wnd.setLayout(new FitLayout());
		wnd.setModal(true);
		wnd.setWidth(Ext.getBody().getWidth() - 20);
		wnd.setHeight(Ext.getBody().getHeight() - 20);
		wnd.setClosable(false);
		wnd.setButtonAlign(Position.CENTER);
		wnd.addButton(getCancelar());
		wnd.addButton(getSalvar());
		wnd.setTopToolbar(getTopo());
		wnd.add(tab);
		wnd.show();
	}

	private ListagemVendaProdutos getProdutos() {
		final AsyncCallback<Record> asyncPesquisa = new AsyncCallback<Record>() {

			public void onFailure(Throwable arg0) {
			}

			public void onSuccess(Record result) {
				Record rec = gridProdutos.getSelectionModel().getSelected();
				rec.set("prodProdutoId", result.getAsInteger("prodProdutoId"));
				rec.set("comVendaProdutoBarra", "".equals(result.getAsString("prodProdutoBarra")) ? null : result.getAsString("prodProdutoBarra"));
				rec.set("prodProduto.prodProdutoDescricao", result.getAsString("prodProdutoDescricao"));
				rec.set("prodProduto.prodProdutoReferencia", result.getAsString("prodProdutoReferencia"));
				rec.set("prodEmbalagem.prodEmbalagemId", result.getAsInteger("prodEmbalagem.prodEmbalagemId"));
			}
		};

		gridProdutos = new ListagemVendaProdutos(false);
		gridProdutos.purgeListeners();
		gridProdutos.setTitle(OpenSigCore.i18n.txtProduto(), "icon-produtovenda");
		gridProdutos.setStore(getDadosProdutos());
		gridProdutos.getStore().load();
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

	private FormularioReceber getRecebimento() {
		formReceber = new FormularioReceber((SisFuncao) contexto.get("dados"));
		formReceber.setTitle(OpenSigCore.i18n.txtReceber(), "icon-receber");
		formReceber.enable();

		formReceber.addListener(new FormPanelListenerAdapter() {
			public void onRender(Component component) {
				// setando a receber
				formReceber.getTopToolbar().hide();
				formReceber.getHdnEmpresa().setValue(venda.getEmpEmpresa().getEmpEmpresaId() + "");
				formReceber.getHdnEntidade().setValue(venda.getEmpCliente().getEmpEntidade().getEmpEntidadeId() + "");
				formReceber.getCmbEntidade().setValue(venda.getEmpCliente().getEmpEntidade().getEmpEntidadeNome1());
				formReceber.getCmbEntidade().disable();
				formReceber.getTxtNfe().setValue(venda.getFisNotaSaida().getFisNotaSaidaNumero());
				formReceber.getTxtNfe().disable();
				formReceber.getTxtValor().setValue(venda.getComVendaValorLiquido());
				formReceber.getTxtValor().disable();
				String data = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(venda.getComVendaData());
				formReceber.getDtCadastro().setValue(data);
				formReceber.getDtCadastro().disable();
				formReceber.mostrarDados();
				formReceber.getGridFormas().setHeight(260);
				formReceber.getGridFormas().setStore(getDadosRecebimentos());
				formReceber.getGridFormas().getStoreForma().addStoreListener(new StoreListenerAdapter() {
					public void onLoad(Store store, Record[] records) {
						formReceber.getGridFormas().getStore().load();
					}
				});

				Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
					public boolean execute() {
						if (formReceber.getTreeCategoria().getRoot().getChildNodes().length > 0) {
							formReceber.getTreeCategoria().selecionar(new String[] { UtilClient.CONF.get("categoria.venda") });
							formReceber.getTreeCategoria().disable();
							return false;
						} else {
							return true;
						}
					}
				}, 1000);
				tab.setActiveItem(0);
			}
		});

		return formReceber;
	}

	private Toolbar getTopo() {
		chkReceber = new Checkbox(OpenSigCore.i18n.txtReceber(), "comVendaRecebida");
		chkReceber.setChecked(venda.getComVendaRecebida());

		chkFechar = new Checkbox(OpenSigCore.i18n.txtFechar(), "comVendaFechada");
		chkFechar.setChecked(true);
		chkFechar.addListener(new CheckboxListenerAdapter() {
			public void onCheck(Checkbox field, boolean checked) {
				if (checked == false) {
					chkReceber.setChecked(false);
					chkReceber.disable();
				} else {
					chkReceber.enable();
				}
			}
		});

		Toolbar tool = new Toolbar();
		tool.addText(OpenSigCore.i18n.txtNatureza() + ": ");
		tool.addField(getNatureza());
		tool.addSpacer();
		tool.addField(chkFechar);
		tool.addSpacer();
		tool.addField(chkReceber);
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
		// validando a venda
		boolean validado = true;
		int row = 0;
		for (Record rec : gridProdutos.getStore().getRecords()) {
			atualizaItem(rec, row++);
		}

		if (chkReceber.getValue()) {
			if (formReceber.setDados()) {
				venda.setFinReceber(formReceber.getClasse());
			} else {
				validado = false;
				tab.setActiveItem(1);
			}
		}

		for (ComVendaProduto vp : venda.getComVendaProdutos()) {
			ProdProduto prod = vp.getProdProduto();
			if (prod.getProdProdutoId() == 0) {
				prod.setProdProdutoCategoria(cmbCategoria.getValue());
			}
			if ("".equals(prod.getProdProdutoBarra())) {
				prod.setProdProdutoBarra(null);
			}
		}

		if (validado) {
			venda.setComNatureza(new ComNatureza(Integer.valueOf(cmbNatureza.getValue())));
			venda.setComVendaFechada(chkFechar.getValue());
			venda.setComVendaRecebida(chkReceber.getValue());
			wnd.close();
			enviar();
		}
	}

	private void atualizaItem(Record rec, int row) {
		// recupera
		ComVendaProduto vp = venda.getComVendaProdutos().get(row);
		ProdProduto prod = vp.getProdProduto();
		// altera
		prod.setProdProdutoId(rec.getAsInteger("prodProdutoId"));
		prod.setProdProdutoBarra("".equals(rec.getAsString("comVendaProdutoBarra")) ? null : rec.getAsString("comVendaProdutoBarra"));
		prod.setProdProdutoDescricao(rec.getAsString("prodProduto.prodProdutoDescricao"));
		prod.setProdProdutoReferencia(rec.getAsString("prodProduto.prodProdutoReferencia") == null ? "" : rec.getAsString("prodProduto.prodProdutoReferencia"));
		prod.setProdEmbalagem(new ProdEmbalagem(rec.getAsInteger("prodEmbalagem.prodEmbalagemId")));

		// seta
		vp.setProdProduto(prod);
		vp.setProdEmbalagem(new ProdEmbalagem(rec.getAsInteger("prodEmbalagem.prodEmbalagemId")));
		venda.getComVendaProdutos().set(row, vp);
	}

	private void enviar() {
		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
		ComercialProxy proxy = new ComercialProxy();
		proxy.salvarVenda(venda, new AsyncCallback<ComVenda>() {
			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errSalvar());
				new ToastWindow(OpenSigCore.i18n.txtSalvar(), caught.getMessage()).show();
			};

			public void onSuccess(ComVenda result) {
				MessageBox.hide();
				TabPanel tab = (TabPanel) Ponte.getCentro().getActiveTab();
				((IListagem) tab.getActiveTab()).getPanel().getStore().reload();
				new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.msgSalvarOK()).show();
			};
		});
	}

	private Store getDadosProdutos() {
		String[][] dados = new String[venda.getComVendaProdutos().size()][];
		for (int i = 0; i < venda.getComVendaProdutos().size(); i++) {
			dados[i] = venda.getComVendaProdutos().get(i).toArray();
		}

		Renderer novo = new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (record.getAsInteger("prodProdutoId") == 0) {
					cellMetadata.setCssClass("azul");
				}
				return value != null ? value.toString() : "";
			}
		};

		for (BaseColumnConfig base : gridProdutos.getModelos().getColumnConfigs()) {
			ColumnConfig conf = null;
			if (base instanceof SummaryColumnConfig) {
				SummaryColumnConfig sumario = (SummaryColumnConfig) base;
				conf = (ColumnConfig) sumario.getColumn();
			} else {
				conf = (ColumnConfig) base;
			}
			conf.setRenderer(novo);
			conf.setSortable(false);
			gridProdutos.getModelos().setEditable(conf.getId(), false);

			// barra
			if (conf.getDataIndex().equals("comVendaProdutoBarra")) {
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
		}

		MemoryProxy proxy = new MemoryProxy(dados);
		ArrayReader reader = new ArrayReader(gridProdutos.getCampos());
		Store store = new Store(proxy, reader);

		return store;
	}

	private Store getDadosRecebimentos() {
		String[][] dados = new String[venda.getFinReceber().getFinRecebimentos().size()][];
		for (int i = 0; i < venda.getFinReceber().getFinRecebimentos().size(); i++) {
			dados[i] = venda.getFinReceber().getFinRecebimentos().get(i).toArray();
		}

		MemoryProxy proxy = new MemoryProxy(dados);
		ArrayReader reader = new ArrayReader(formReceber.getGridFormas().getCampos());
		Store store = new Store(proxy, reader);

		return store;
	}

	private ComboBox getNatureza() {
		FieldDef[] fdNatureza = new FieldDef[] { new IntegerFieldDef("comNaturezaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("comNaturezaNome") };
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, venda.getEmpEmpresa());
		CoreProxy<ComNatureza> proxy = new CoreProxy<ComNatureza>(new ComNatureza(), fo);
		final Store storeNatureza = new Store(proxy, new ArrayReader(new RecordDef(fdNatureza)), true);
		storeNatureza.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				for (Record rec : records) {
					if (rec.getAsString("comNaturezaNome").equalsIgnoreCase(OpenSigCore.i18n.txtVenda())) {
						cmbNatureza.setValue(rec.getAsString("comNaturezaId"));
						break;
					}
				}
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
