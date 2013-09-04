package br.com.opensig.produto.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.lista.ListagemGrades;
import br.com.opensig.produto.client.visao.lista.ListagemPreco;
import br.com.opensig.produto.client.visao.lista.ListagemProduto;
import br.com.opensig.produto.shared.modelo.ProdProduto;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Function;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;

public class FormularioPesquisa extends AFormulario<ProdProduto> {
	private ListagemProduto gridProdutos;
	private ListagemPreco gridPrecos;
	private ListagemGrades gridGrades;
	private TextField txtBusca;
	private AsyncCallback<Record> asyncResultado;
	private ToolbarButton btnPesquisar;
	private Window wnd;

	public FormularioPesquisa(ProdProduto classe, SisFuncao funcao) {
		super(classe, funcao);
		inicializar();
	}

	public void inicializar() {
		// montando o grid de produtos
		gridProdutos = new ListagemProduto(this);
		gridProdutos.purgeListeners();
		gridProdutos.setTitle(OpenSigCore.i18n.txtProduto());
		gridProdutos.setIconCls("icon-produto");
		gridProdutos.setFiltroPadrao(new FiltroNumero("prodProdutoId", ECompara.IGUAL, 0));
		gridProdutos.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (records.length > 0) {
					gridProdutos.getSelectionModel().selectFirstRow();
					setDados();
					gridProdutos.focus(true, 10);
				} else {
					limparDados();
					MessageBox.alert(OpenSigCore.i18n.txtPesquisar(), OpenSigCore.i18n.errRegistro(), new AlertCallback() {
						public void execute() {
							txtBusca.focus(true, 10);
						}
					});
				}
			}
		});
		gridProdutos.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				setDados();
			}

			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				selecionar();
			}
		});
		gridProdutos.addGridListener(new GridListenerAdapter() {
			public void onKeyPress(EventObject e) {
				if (e.getKey() == EventObject.DOWN || e.getKey() == EventObject.PAGEDOWN || e.getKey() == EventObject.UP || e.getKey() == EventObject.PAGEUP) {
					setDados();
				} else if (e.getKey() == EventObject.ENTER) {
					selecionar();
				}
			}
		});

		// montando o grid de preco
		gridPrecos = new ListagemPreco(false);
		for (int i = 0; i < gridPrecos.getModelos().getColumnCount(); i++) {
			gridPrecos.getModelos().setEditable(i, false);
		}
		gridPrecos.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				selecionar();
			}
		});
		gridPrecos.addGridListener(new GridListenerAdapter() {
			public void onKeyPress(EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					selecionar();
				}
			}
		});
		gridPrecos.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (records.length == 0) {
					gridGrades.getStore().reload();
				} else {
					gridGrades.getStore().removeAll();
				}
			}
		});

		// montando o grid de grade
		gridGrades = new ListagemGrades(false);
		for (int i = 0; i < gridGrades.getModelos().getColumnCount(); i++) {
			gridGrades.getModelos().setEditable(i, false);
		}
		gridGrades.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				selecionar();
			}
		});
		gridGrades.addGridListener(new GridListenerAdapter() {
			public void onKeyPress(EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					selecionar();
				}
			}
		});
		gridGrades.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (records.length > 0) {
					gridGrades.getSelectionModel().selectFirstRow();
				} else if (gridProdutos.getStore().getRecords().length == 1) {
					selecionar();
				}
			}
		});

		// setando as sublistas
		Panel coluna = new Panel();
		coluna.setLayout(new ColumnLayout());
		coluna.setBorder(false);
		coluna.add(gridPrecos, new ColumnLayoutData(.48));
		coluna.add(gridGrades, new ColumnLayoutData(.52));

		// setando do form
		setLayout(new RowLayout());
		add(gridProdutos);
		add(coluna);
		super.inicializar();
		setHeader(false);
		enable();

		// montando os filtros de busca
		txtBusca = new TextField("", "txtBusca");
		txtBusca.setWidth(Ext.getBody().getWidth() - 400);
		txtBusca.setAllowBlank(false);
		txtBusca.setTitle(OpenSigCore.i18n.msgPesquisar());
		txtBusca.setMaxLength(100);
		txtBusca.setSelectOnFocus(true);
		txtBusca.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				pesquisar();
			}
		});

		btnPesquisar = new ToolbarButton(OpenSigCore.i18n.txtPesquisar());
		btnPesquisar.setIconCls("icon-pesquisa");
		btnPesquisar.setTitle(OpenSigCore.i18n.msgPesquisar());
		btnPesquisar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				pesquisar();
			}
		});
		btnPesquisar.addListener("onkeypress", new Function() {
			public void execute() {
				pesquisar();
			}
		});

		// botao de selecionar
		ToolbarButton btnSelecionar = new ToolbarButton(OpenSigCore.i18n.txtSelecionar());
		btnSelecionar.setTooltip(OpenSigCore.i18n.msgSelecionar());
		btnSelecionar.setIconCls("icon-selecionar");
		btnSelecionar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				selecionar();
			}
		});
		btnSelecionar.addListener("onkeypress", new Function() {
			public void execute() {
				selecionar();
			}
		});

		// botao de cancelar
		ToolbarButton btnCancelar = new ToolbarButton(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setTooltip(OpenSigCore.i18n.msgCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				limparDados();
				asyncResultado = null;
				wnd.hide();
			}
		});
		btnSelecionar.addListener("onkeypress", new Function() {
			public void execute() {
				limparDados();
				asyncResultado = null;
				wnd.hide();
			}
		});

		// muda a barra de tarefas
		Toolbar tool = new Toolbar();
		tool.addButton(btnSelecionar);
		tool.addButton(btnCancelar);
		tool.addSeparator();
		tool.addText(OpenSigCore.i18n.txtPesquisar() + ":");
		tool.addField(txtBusca);
		tool.addSeparator();
		tool.addButton(btnPesquisar);
		setTlbAcao(tool);
	}

	public boolean setDados() {
		Record rec = gridProdutos.getSelectionModel().getSelected();
		ProdProduto prod = new ProdProduto(rec.getAsInteger("prodProdutoId"));

		FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
		gridPrecos.getProxy().setFiltroPadrao(fo);
		gridPrecos.getStore().reload();

		gridGrades.getProxy().setFiltroPadrao(fo);
		return true;
	}

	public void limparDados() {
		try {
			gridProdutos.getStore().removeAll();
			gridPrecos.getStore().removeAll();
			gridGrades.getStore().removeAll();
			txtBusca.setValue("");
		} catch (Exception e) {
			// nada
		}
	}

	public void mostrarDados() {
		gridProdutos.setHeight(Ext.getBody().getHeight() - 250);
		gridPrecos.setHeight(150);
		gridGrades.setHeight(150);
	}

	private void selecionar() {
		if (asyncResultado != null && getForm().isValid()) {
			Record pro = gridProdutos.getSelectionModel().getSelected();
			Record pre = gridPrecos.getSelectionModel().getSelected();
			Record gra = gridGrades.getSelectionModel().getSelected();

			if (pro != null) {
				if (pre != null) {
					pro.set("prodEmbalagem.prodEmbalagemId", pre.getAsString("prodEmbalagem.prodEmbalagemId"));
					pro.set("prodEmbalagem.prodEmbalagemNome", pre.getAsString("prodEmbalagem.prodEmbalagemNome"));
					pro.set("prodProdutoPreco", pre.getAsDouble("prodPrecoValor"));
				} else if (gra != null) {
					pro.set("t1.prodEstoqueQuantidade", gra.getAsDouble("prodGradeEstoque"));
					pro.set("prodProdutoBarra", gra.getAsString("prodGradeBarra"));
				}
				asyncResultado.onSuccess(pro);
			} else {
				asyncResultado.onFailure(null);
			}

			asyncResultado = null;
			wnd.hide();
		}
	}

	private void pesquisar() {
		if (!txtBusca.getValueAsString().isEmpty()) {
			String texto = txtBusca.getValueAsString();
			GrupoFiltro filtro = new GrupoFiltro();
			FiltroBinario fb = new FiltroBinario("prodProdutoAtivo", ECompara.IGUAL, 1);
			filtro.add(fb, EJuncao.E);
			ECompara compara;

			// verifica se e longo
			try {
				long valor = Long.valueOf(texto);
				// codigo
				FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.IGUAL, valor);
				// barra
				FiltroTexto ft = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, texto);
				// barra do preco
				FiltroTexto ft1 = new FiltroTexto("prodPrecoBarra", ECompara.IGUAL, texto);
				ft1.setCampoPrefixo("t2.");
				// barra da grade
				FiltroTexto ft2 = new FiltroTexto("prodGradeBarra", ECompara.IGUAL, texto);
				ft2.setCampoPrefixo("t3.");
				// referencia
				FiltroTexto ft3 = new FiltroTexto("prodProdutoReferencia", ECompara.CONTEM, texto);
				filtro.add(new GrupoFiltro(EJuncao.OU, new IFiltro[] { fn, ft, ft1, ft2, ft3 }));
			} catch (NumberFormatException ex) {
				// verifica se e decimal
				try {
					double valor = Double.valueOf(texto.replace(",", "."));
					// preco
					FiltroNumero fn2 = new FiltroNumero("prodProdutoPreco", ECompara.IGUAL, valor);
					// estoque
					FiltroNumero fn3 = new FiltroNumero("prodEstoqueQuantidade", ECompara.IGUAL, valor);
					fn3.setCampoPrefixo("t1.");
					filtro.add(new GrupoFiltro(EJuncao.OU, new IFiltro[] { fn2, fn3 }));
				} catch (NumberFormatException ex1) {
					if (texto.startsWith("%")) {
						compara = ECompara.CONTEM_FIM;
					} else if (texto.endsWith("%")) {
						compara = ECompara.CONTEM_INICIO;
					} else {
						compara = ECompara.CONTEM;
					}
					// descricao
					FiltroTexto ft1 = new FiltroTexto("prodProdutoDescricao", compara, texto);
					// referencia
					FiltroTexto ft2 = new FiltroTexto("prodProdutoReferencia", compara, texto);
					// fornecedor
					FiltroTexto ft3 = new FiltroTexto("empFornecedor.empEntidade.empEntidadeNome1", ECompara.CONTEM, texto);
					// barra do preco
					FiltroTexto ft4 = new FiltroTexto("prodPrecoBarra", ECompara.IGUAL, texto);
					ft4.setCampoPrefixo("t2.");
					// barra da grade
					FiltroTexto ft5 = new FiltroTexto("prodGradeBarra", ECompara.IGUAL, texto);
					ft5.setCampoPrefixo("t3.");
					filtro.add(new GrupoFiltro(EJuncao.OU, new IFiltro[] { ft1, ft2, ft3, ft4, ft5 }));
				}
			}

			gridProdutos.getProxy().setFiltroPadrao(filtro);
			gridProdutos.getStore().load(0, gridProdutos.getPaginador().getPageSize());
		}
	}

	public ListagemProduto getGridProdutos() {
		return gridProdutos;
	}

	public void setGridProdutos(ListagemProduto gridProdutos) {
		this.gridProdutos = gridProdutos;
	}

	public ListagemPreco getGridPrecos() {
		return gridPrecos;
	}

	public void setGridPrecos(ListagemPreco gridPrecos) {
		this.gridPrecos = gridPrecos;
	}

	public TextField getTxtBusca() {
		return txtBusca;
	}

	public void setTxtBusca(TextField txtBusca) {
		this.txtBusca = txtBusca;
	}

	public AsyncCallback<Record> getAsyncResultado() {
		return asyncResultado;
	}

	public void setAsyncResultado(AsyncCallback<Record> asyncResultado) {
		this.asyncResultado = asyncResultado;
	}

	public Window getWnd() {
		return wnd;
	}

	public void setWnd(Window wnd) {
		this.wnd = wnd;
	}

	public ListagemGrades getGridGrades() {
		return gridGrades;
	}

	public void setGridGrades(ListagemGrades gridGrades) {
		this.gridGrades = gridGrades;
	}

	public void gerarListas() {
	}
}
