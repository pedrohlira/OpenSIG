package br.com.opensig.core.client.visao.abstrato;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoAdicionar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoRemover;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * Classe que representa o modelo genérico das sub-listas "grids" do sistema.
 * 
 * @param <E>
 *            classe generica da listagem.
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AListagemEditor<E extends Dados> extends EditorGridPanel implements IListagemEditor<E> {

	/**
	 * Classe do tipo que a sub-listagem manipula.
	 */
	protected E classe;
	/**
	 * Mapa de contexto da funcao.
	 */
	protected Map contexto;
	/**
	 * Definicao de registros para os campos.
	 */
	protected RecordDef campos;
	/**
	 * Modelo de coluna dos campos.
	 */
	protected ColumnModel modelos;
	/**
	 * Filtro padrao na sub-listagem sem poder remover.
	 */
	protected IFiltro filtroPadrao;
	/**
	 * Proxy de dados, para preencher a sub-listagem.
	 */
	protected CoreProxy<E> proxy;
	/**
	 * Botao de adicionar registro.
	 */
	protected ToolbarButton btnAdicionar;
	/**
	 * Botao de remover registro.
	 */
	protected ToolbarButton btnRemover;
	/**
	 * Menu com botao direito.
	 */
	protected Menu menu;
	/**
	 * Informa se a lista tem barra de tarefas.
	 */
	protected boolean barraTarefa;
	/**
	 * Linha e coluna da celula clicada.
	 */
	protected int[] celula;
	/**
	 * Componente padrao de foco apos adicionar.
	 */
	protected Component focusPadrao;

	private IComando cmdAdicionar;
	private IComando cmdRemover;

	/**
	 * Construtor que recebe a classe de dados e se tera barra de tarefas,
	 * 
	 * @param classe
	 *            o objeto de classe do tipo informado.
	 * @param barraTarefa
	 *            verdadeiro para ter acao de adicionar e remover.
	 */
	public AListagemEditor(E classe, boolean barraTarefa) {
		this.classe = classe;
		this.barraTarefa = barraTarefa;
		this.menu = new Menu();

		contexto = new HashMap();
		contexto.put("lista", this);
	}

	@Override
	public void inicializar() {
		proxy = new CoreProxy<E>(classe, filtroPadrao);
		Store dados = new Store(proxy, new ArrayReader(campos), true);
		dados.setDefaultSort(classe.getCampoOrdem(), SortDir.getValue(classe.getOrdemDirecao().toString()));
		dados.setRemoteSort(false);
		dados.addStoreListener(new StoreListenerAdapter() {
			public boolean doBeforeLoad(Store store) {
				boolean retorno = true;
				// valida se pode atualizar
				try {
					for (Record rec : store.getRecords()) {
						retorno &= rec.getAsInteger(rec.getFields()[0]) > 0;
					}
				} catch (Exception e) {
					retorno = false;
				}

				return retorno;
			}
		});

		if (barraTarefa) {
			cmdAdicionar = new ComandoAdicionar<E>(this);
			cmdRemover = new ComandoRemover<E>(this);

			Toolbar tool = new Toolbar();
			tool.setHeight(25);
			tool.addButton(getAdicionar());
			tool.addButton(getRemover());
			tool.addSeparator();
			setTopToolbar(tool);
			focusPadrao = btnAdicionar;

			menu.addItem(getMenuAdicionar());
			menu.addItem(getMenuRemover());
			menu.addSeparator();
		}

		addGridRowListener(new GridRowListenerAdapter() {
			public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
				grid.getSelectionModel().selectRow(rowIndex);
				if (grid.getTopToolbar().isVisible()) {
					menu.showAt(e.getXY());
				}
			}
		});

		addGridCellListener(new GridCellListenerAdapter() {
			public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
				celula = new int[] { rowIndex, colindex };
			}
		});

		for (BaseColumnConfig col : modelos.getColumnConfigs()) {
			if (col instanceof ColumnConfig) {
				ColumnConfig coluna = (ColumnConfig) col;
				if (coluna.getEditor() != null) {
					Field campo = coluna.getEditor().getField();
					setEditorListener(campo);
				}
			}
		}

		setAutoScroll(true);
		setMargins(1);
		setLoadMask(true);
		setStore(dados);
		setColumnModel(modelos);
		setClicksToEdit(1);
		setTrackMouseOver(true);
		setSelectionModel(new RowSelectionModel(true));
	}

	@Override
	public void setEditorListener(Field campo) {
		campo.addListener(new FieldListenerAdapter() {
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					setCellFocus();
				}
			}
		});
	}

	protected void setCellFocus() {
		int col = celula[1] + 1;
		for (; col < modelos.getColumnCount(); col++) {
			if (modelos.isCellEditable(col, celula[0]) && !modelos.isHidden(col)) {
				startEditing(celula[0], col);
				break;
			}
		}

		if (col == modelos.getColumnCount() && focusPadrao != null) {
			focusPadrao.fireEvent("onkeypress");
		}
	}

	@Override
	public void startEditing(int rowIndex, int colIndex) {
		celula = new int[] { rowIndex, colIndex };
		super.startEditing(rowIndex, colIndex);
	}

	@Override
	public void stopEditing() {
		celula = new int[] { -1, -1 };
		super.stopEditing();
	}

	private void executarComando(IComando comando) {
		comando = AntesDoComando(comando);

		if (comando != null) {
			// comando para ativar o depois
			IComando depois = new AComando<E>() {
				public void execute(Map contexto) {
					DepoisDoComando(comando);
				}
			};
			UtilClient.comandoFinal(comando, depois);
			comando.execute(contexto);
		}
	}

	@Override
	public ToolbarButton getAdicionar() {
		btnAdicionar = new ToolbarButton(OpenSigCore.i18n.txtAdicionar());
		btnAdicionar.setIconCls("icon-novo");
		btnAdicionar.setTooltip(OpenSigCore.i18n.txtAdicionar() + "/" + OpenSigCore.i18n.txtRegistro());
		btnAdicionar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				executarComando(cmdAdicionar);
			}
		});
		btnAdicionar.addListener("onkeypress", new Function() {
			public void execute() {
				executarComando(cmdAdicionar);
			}
		});

		return btnAdicionar;
	}

	@Override
	public ToolbarButton getRemover() {
		btnRemover = new ToolbarButton(OpenSigCore.i18n.txtRemover());
		btnRemover.setIconCls("icon-excluir");
		btnRemover.setTooltip(OpenSigCore.i18n.txtRemover() + "/" + OpenSigCore.i18n.txtRegistro());
		btnRemover.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				executarComando(cmdRemover);
			}
		});

		btnRemover.addListener("onkeypress", new Function() {
			public void execute() {
				executarComando(cmdRemover);
			}
		});

		return btnRemover;
	}

	@Override
	public Item getMenuAdicionar() {
		Item btnMenu = new Item(OpenSigCore.i18n.txtAdicionar());
		btnMenu.setIconCls("icon-novo");
		btnMenu.setTitle(OpenSigCore.i18n.txtAdicionar() + "/" + OpenSigCore.i18n.txtRegistro());
		btnMenu.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				executarComando(cmdAdicionar);
			}
		});

		return btnMenu;
	}

	@Override
	public Item getMenuRemover() {
		Item btnMenu = new Item(OpenSigCore.i18n.txtRemover());
		btnMenu.setIconCls("icon-excluir");
		btnMenu.setTitle(OpenSigCore.i18n.txtRemover() + "/" + OpenSigCore.i18n.txtRegistro());
		btnMenu.addListener(new BaseItemListenerAdapter() {
			public void onClick(BaseItem item, EventObject e) {
				executarComando(cmdRemover);
			}
		});

		return btnMenu;
	}

	@Override
	public abstract boolean validar(List<E> lista);

	@Override
	public RecordDef getCampos() {
		return campos;
	}

	@Override
	public void setCampos(RecordDef campos) {
		this.campos = campos;
	}

	@Override
	public ColumnModel getModelos() {
		return modelos;
	}

	@Override
	public void setModelos(ColumnModel modelos) {
		this.modelos = modelos;
	}

	@Override
	public IFiltro getFiltroPadrao() {
		return filtroPadrao;
	}

	@Override
	public void setFiltroPadrao(IFiltro filtroPadrao) {
		this.filtroPadrao = filtroPadrao;
	}

	@Override
	public CoreProxy<E> getProxy() {
		return proxy;
	}

	@Override
	public void setProxy(CoreProxy<E> proxy) {
		this.proxy = proxy;
	}

	@Override
	public E getClasse() {
		return classe;
	}

	@Override
	public ToolbarButton getBtnAdicionar() {
		return btnAdicionar;
	}

	@Override
	public void setBtnAdicionar(ToolbarButton btnAdicionar) {
		this.btnAdicionar = btnAdicionar;
	}

	@Override
	public ToolbarButton getBtnRemover() {
		return btnRemover;
	}

	@Override
	public void setBtnRemover(ToolbarButton btnRemover) {
		this.btnRemover = btnRemover;
	}

	@Override
	public boolean isBarraTarefa() {
		return barraTarefa;
	}

	@Override
	public void setBarraTarefa(boolean barraTarefa) {
		this.barraTarefa = barraTarefa;
	}

	@Override
	public Menu getMenu() {
		return menu;
	}

	@Override
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	public Map getContexto() {
		return contexto;
	}

	@Override
	public void setContexto(Map contexto) {
		this.contexto = contexto;
	}

	@Override
	public int[] getCelula() {
		return celula;
	}

	@Override
	public void setCelula(int[] celula) {
		this.celula = celula;
	}

	@Override
	public void setClasse(E classe) {
		this.classe = classe;
	}

	@Override
	public Component getFocusPadrao() {
		return focusPadrao;
	}

	@Override
	public void setFocusPadrao(Component focusPadrao) {
		this.focusPadrao = focusPadrao;
	}

	@Override
	public EditorGridPanel getPanel() {
		return this;
	}

	@Override
	public IComando AntesDoComando(IComando comando) {
		return comando;
	}

	@Override
	public void DepoisDoComando(IComando comando) {
	}
}
