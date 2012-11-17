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
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

public class FormularioPesquisa extends AFormulario<ProdProduto> {
	private ListagemProduto gridProdutos;
	private ListagemPreco gridPrecos;
	private TextField txtBusca;
	private Checkbox chkCod;
	private Checkbox chkBarra;
	private Checkbox chkDescricao;
	private Checkbox chkRef;
	private Checkbox chkFornecedor;
	private ToolbarButton btnPesquisar;
	private AsyncCallback<Record> asyncResultado;
	private Window wnd;

	public FormularioPesquisa(ProdProduto classe, SisFuncao funcao) {
		super(classe, funcao);
		inicializar();
	}

	public void inicializar() {
		// montando o grid de produtos
		gridProdutos = new ListagemProduto(this);
		gridProdutos.purgeListeners();
		gridProdutos.getTopToolbar().hide();
		gridProdutos.setTitle(OpenSigCore.i18n.txtProduto());
		gridProdutos.setIconCls("icon-produto");
		gridProdutos.setFiltroPadrao(new FiltroNumero("prodProdutoId", ECompara.IGUAL, 0));
		gridProdutos.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (records.length > 0) {
					gridProdutos.getSelectionModel().selectFirstRow();
					pesquisaSucesso();
				} else {
					pesquisaFalha();
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
				if (records.length == 0 && gridProdutos.getStore().getRecords().length == 1) {
					selecionar();
				}
			}
		});

		// setando do form
		setLayout(new ColumnLayout());
		add(gridProdutos, new ColumnLayoutData(.80));
		add(gridPrecos, new ColumnLayoutData(.20));

		super.inicializar();
		setHeader(false);
		enable();

		// montando os filtros de busca
		txtBusca = new TextField("", "txtBusca", 300);
		txtBusca.setAllowBlank(false);
		txtBusca.setTitle(OpenSigCore.i18n.msgPesquisar());
		txtBusca.setMaxLength(100);
		txtBusca.setSelectOnFocus(true);
		txtBusca.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				pesquisar();
			}
		});

		chkCod = new Checkbox(OpenSigCore.i18n.txtCod());
		chkCod.setChecked(true);
		chkBarra = new Checkbox(OpenSigCore.i18n.txtBarra());
		chkBarra.setChecked(true);
		chkDescricao = new Checkbox(OpenSigCore.i18n.txtDescricao());
		chkDescricao.setChecked(true);
		chkRef = new Checkbox(OpenSigCore.i18n.txtRef());
		chkFornecedor = new Checkbox(OpenSigCore.i18n.txtFornecedor());

		btnPesquisar = new ToolbarButton(OpenSigCore.i18n.txtPesquisar());
		btnPesquisar.setIconCls("icon-pesquisa");
		btnPesquisar.setTitle(OpenSigCore.i18n.msgPesquisar());
		btnPesquisar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				pesquisar();
			}
		});

		// muda a barra de tarefas
		Toolbar tool = new Toolbar();

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
		tool.addButton(btnSelecionar);

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
		tool.addButton(btnCancelar);

		tool.addSeparator();
		tool.addText(OpenSigCore.i18n.txtPesquisar() + ":");
		tool.addField(txtBusca);
		tool.addSeparator();
		tool.addText(OpenSigCore.i18n.txtFiltros() + ": ");
		tool.addField(chkCod);
		tool.addSpacer();
		tool.addSpacer();
		tool.addField(chkBarra);
		tool.addSpacer();
		tool.addSpacer();
		tool.addField(chkDescricao);
		tool.addSpacer();
		tool.addSpacer();
		tool.addField(chkRef);
		tool.addSpacer();
		tool.addSpacer();
		tool.addField(chkFornecedor);
		tool.addSeparator();
		tool.addButton(btnPesquisar);
		setTlbAcao(tool);
	}

	public boolean setDados() {
		Record rec = gridProdutos.getSelectionModel().getSelected();
		carregaPrecos(new ProdProduto(rec.getAsInteger("prodProdutoId")));
		return true;
	}

	public void limparDados() {
		try {
			gridProdutos.getStore().removeAll();
			gridPrecos.getStore().removeAll();
			txtBusca.setValue("");
		} catch (Exception e) {
			// nada
		}
	}

	public void mostrarDados() {
		gridProdutos.setHeight(Ext.getBody().getHeight() - 100);
		gridPrecos.setHeight(Ext.getBody().getHeight() - 100);
	}

	private void carregaPrecos(ProdProduto prod) {
		FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, prod);
		gridPrecos.getProxy().setFiltroPadrao(fo);
		gridPrecos.getStore().reload();
	}

	private void selecionar() {
		if (asyncResultado != null && getForm().isValid()) {
			Record pro = gridProdutos.getSelectionModel().getSelected();
			Record pre = gridPrecos.getSelectionModel().getSelected();

			if (pro != null) {
				if (pre != null) {
					pro.set("prodEmbalagem.prodEmbalagemId", pre.getAsString("prodEmbalagem.prodEmbalagemId"));
					pro.set("prodEmbalagem.prodEmbalagemNome", pre.getAsString("prodEmbalagem.prodEmbalagemNome"));
					pro.set("prodProdutoPreco", pre.getAsString("prodPrecoValor"));
				}
				asyncResultado.onSuccess(pro);
			} else {
				asyncResultado.onFailure(new NullPointerException());
			}

			asyncResultado = null;
			wnd.hide();
		}
	}

	private void pesquisar() {
		if (!txtBusca.getValueAsString().isEmpty() && (chkCod.getValue() || chkBarra.getValue() || chkDescricao.getValue() || chkRef.getValue() || chkFornecedor.getValue())) {
			GrupoFiltro gf = new GrupoFiltro();

			if (chkCod.getValue()) {
				// codigo
				try {
					FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.IGUAL, txtBusca.getValueAsString());
					gf.add(fn, EJuncao.OU);
				} catch (Exception e) {
					FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.IGUAL, 0);
					gf.add(fn, EJuncao.OU);
				}
			}

			if (chkBarra.getValue()) {
				try {
					// barra
					FiltroTexto ft = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, txtBusca.getValueAsString());
					gf.add(ft, EJuncao.OU);
					// barra do preco
					FiltroTexto ft1 = new FiltroTexto("prodPrecoBarra", ECompara.IGUAL, txtBusca.getValueAsString());
					ft1.setCampoPrefixo("t2.");
					gf.add(ft1, EJuncao.OU);
				} catch (Exception e) {
					FiltroTexto ft = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, "");
					gf.add(ft, EJuncao.OU);
				}
			}

			if (chkDescricao.getValue()) {
				// descricao
				FiltroTexto ft = new FiltroTexto("prodProdutoDescricao", ECompara.CONTEM, txtBusca.getValueAsString());
				gf.add(ft, EJuncao.OU);
			}

			if (chkRef.getValue()) {
				// referencia
				FiltroTexto ft3 = new FiltroTexto("prodProdutoReferencia", ECompara.CONTEM, txtBusca.getValueAsString());
				gf.add(ft3, EJuncao.OU);
			}

			if (chkFornecedor.getValue()) {
				// fornecedor
				FiltroTexto ft2 = new FiltroTexto("empFornecedor.empEntidade.empEntidadeNome1", ECompara.CONTEM, txtBusca.getValueAsString());
				gf.add(ft2, EJuncao.OU);
			}

			FiltroBinario fb = new FiltroBinario("prodProdutoAtivo", ECompara.IGUAL, 1);
			GrupoFiltro padrao = new GrupoFiltro(EJuncao.E, new IFiltro[] { fb, gf });

			gridProdutos.getProxy().setFiltroPadrao(padrao);
			gridProdutos.getStore().load(0, gridProdutos.getPaginador().getPageSize());
		}
	}

	private void pesquisaFalha() {
		limparDados();
		MessageBox.alert(OpenSigCore.i18n.txtPesquisar(), OpenSigCore.i18n.errRegistro(), new AlertCallback() {
			public void execute() {
				txtBusca.focus(true, 10);
			}
		});
	}

	private void pesquisaSucesso() {
		setDados();
		gridProdutos.focus(true, 10);
	}

	public SisFuncao getFuncao() {
		return funcao;
	}

	public void setFuncao(SisFuncao funcao) {
		this.funcao = funcao;
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

	public Checkbox getChkCod() {
		return chkCod;
	}

	public void setChkCod(Checkbox chkCod) {
		this.chkCod = chkCod;
	}

	public Checkbox getChkBarra() {
		return chkBarra;
	}

	public void setChkBarra(Checkbox chkBarra) {
		this.chkBarra = chkBarra;
	}

	public Checkbox getChkDescricao() {
		return chkDescricao;
	}

	public void setChkDescricao(Checkbox chkDescricao) {
		this.chkDescricao = chkDescricao;
	}

	public Checkbox getChkRef() {
		return chkRef;
	}

	public void setChkRef(Checkbox chkRef) {
		this.chkRef = chkRef;
	}

	public Checkbox getChkFornecedor() {
		return chkFornecedor;
	}

	public void setChkFornecedor(Checkbox chkFornecedor) {
		this.chkFornecedor = chkFornecedor;
	}

	public void setTxtBusca(TextField txtBusca) {
		this.txtBusca = txtBusca;
	}

	public ToolbarButton getBtnPesquisar() {
		return btnPesquisar;
	}

	public void setBtnPesquisar(ToolbarButton btnPesquisar) {
		this.btnPesquisar = btnPesquisar;
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

	public void gerarListas() {
	}
}
