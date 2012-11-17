package br.com.opensig.core.client.visao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.Lista;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.NodeTraversalCallback;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.tree.TreeFilter;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.TreeTraversalCallback;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

/**
 * Classe que representa as lista em arvores.
 * 
 * @param <E>
 *            o tipo generico de dados usado.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Arvore<E extends Dados> extends TreePanel {

	private E classe;
	private TreeFilter treeFiltro;
	private Toolbar toolFiltro;
	private ToolbarButton btnAdicionar;
	private ToolbarButton btnRemover;
	private ToolbarButton btnFiltro;
	private TextField txtFiltro;
	private TreeNode root;
	private Label lblValidacao;
	private String iconeNode;
	private boolean filtrar;
	private boolean editar;
	private boolean expandir;

	/**
	 * Construtor padrao.
	 */
	public Arvore() {
		this.root = new TreeNode();
	}

	/**
	 * Construtor informando a classe do tipo generico informado e o titudo da arvore.
	 * 
	 * @param classe
	 *            um objeto que será usado como referencia.
	 * @param titulo
	 *            o texto com o nome a ser mostrado.
	 */
	public Arvore(E classe, String titulo) {
		this.classe = classe;
		this.root = new TreeNode(titulo);
	}

	/**
	 * Metodo que limpa todos os nodes.
	 */
	public void limpar() {
		for (Node node : root.getChildNodes()) {
			root.removeChild(node);
		}
	}

	/**
	 * Metodo que carrega os nodes oriundos do banco de dados.
	 * 
	 * @param filtro
	 *            um filtro a ser usado para recuperar os dados.
	 * @param asyncCallback
	 *            a funcao de retorno a ser disparada apos recuperar os dados.
	 */
	public void carregar(final IFiltro filtro, final AsyncCallback<Lista<E>> asyncCallback) {
		CoreProxy<E> proxy = new CoreProxy<E>(classe);
		proxy.selecionar(0, 0, filtro, false, new AsyncCallback<Lista<E>>() {

			public void onFailure(final Throwable caught) {
				MessageBox.confirm(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtCarregar());
							carregar(filtro, asyncCallback);
						} else if (asyncCallback != null) {
							asyncCallback.onFailure(caught);
						}
					}
				});
			}

			public void onSuccess(Lista<E> result) {
				for (E objeto : result.getLista()) {
					root.appendChild(novoItem(objeto));
				}
				if (asyncCallback != null) {
					asyncCallback.onSuccess(result);
				}
			}
		});
	}

	/**
	 * Metodo que marca os nodes como checados, usando uma lista de string passada.
	 * 
	 * @param nomes
	 *            um array de string com os nomes a serem marcados.
	 */
	public void selecionar(final String[] nomes) {
		root.cascade(new NodeTraversalCallback() {
			public boolean execute(Node node) {
				((TreeNode) node).getUI().toggleCheck(false);
				if (nomes != null) {
					for (String nome : nomes) {
						String childText = ((TreeNode) node).getText();
						if (nome.trim().equalsIgnoreCase(childText.trim())) {
							((TreeNode) node).getUI().toggleCheck(true);
							break;
						}
					}
				}
				return true;
			}
		});
		
		root.sort(new Comparator<TreeNode>() {
			public int compare(TreeNode arg0, TreeNode arg1) {
				if (arg0.getUI().isChecked() && !arg1.getUI().isChecked()) {
					return -1;
				} else if (!arg0.getUI().isChecked() && arg1.getUI().isChecked()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	/**
	 * Metodo que valida se tem algum objeto selecionado e insere a colecao.
	 * 
	 * @param objs
	 *            a colecao de objetos que sera usada para inserir os node checados.
	 * @return verdadeiro se tiver pelo menos 1 item selecionado, false caso contrario.
	 */
	public boolean validar(final Collection<E> objs) {
		root.cascade(new NodeTraversalCallback() {
			public boolean execute(Node node) {
				TreeNode tNode = (TreeNode) node;
				E obj = (E) node.getUserObject();

				if (obj != null && (tNode.getUI().isChecked() || obj.getId().intValue() == 0)) {
					objs.add(obj);
				}
				return true;
			}
		});

		lblValidacao.setVisible(objs.isEmpty());
		return !objs.isEmpty();
	}

	/**
	 * Metodo que valida se tem alguma string selecionado e insere a colecao.
	 * 
	 * @param objs
	 *            a colecao de strings que sera usada para inserir os node checados.
	 * @return verdadeiro se tiver pelo menos 1 item selecionado, false caso contrario.
	 */
	public boolean validarCategoria(Collection<String[]> objs) {
		boolean valida = false;

		if (root.getChildNodes().length > 0) {
			TreeNode tNode;
			E obj;

			for (Node node : root.getChildNodes()) {
				tNode = (TreeNode) node;
				obj = (E) node.getUserObject();

				if (tNode.getUI().isChecked() || obj.getId().intValue() == 0) {
					String[] valores = new String[] { obj.getId().intValue() + "", obj.toString() };
					objs.add(valores);
					valida = true;
				}
			}
		}

		lblValidacao.setVisible(!valida);
		return valida;
	}

	/**
	 * Metodo que inicializa os objetos visuais.
	 */
	public void inicializar() {
		btnAdicionar = new ToolbarButton();
		btnAdicionar.setIconCls("icon-novo");
		btnAdicionar.setTooltip(OpenSigCore.i18n.txtAdicionar());
		btnAdicionar.setDisabled(true);
		btnAdicionar.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				adicionar();
			}
		});

		btnRemover = new ToolbarButton();
		btnRemover.setIconCls("icon-excluir");
		btnRemover.setTooltip(OpenSigCore.i18n.txtRemover());
		btnRemover.disable();
		btnRemover.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				remover();
			}
		});

		if (filtrar || expandir) {
			toolFiltro = new Toolbar();
			toolFiltro.setHeight(25);

			if (filtrar) {
				criarFiltro();
			}

			if (expandir) {
				criarExpansao();
			}

			if (editar) {
				criarBotoes();
			}
		}

		if (toolFiltro != null) {
			setTopToolbar(toolFiltro);
		}

		lblValidacao = new Label(OpenSigCore.i18n.errCategoria());
		lblValidacao.setCls("style-erro");
		lblValidacao.hide();
		add(lblValidacao);

		setAutoScroll(true);
		setContainerScroll(true);
		setRootVisible(false);
		setRootNode(root);
		treeFiltro = new TreeFilter(this);
	}

	private void criarBotoes() {
		toolFiltro.addSpacer();
		toolFiltro.addButton(btnAdicionar);
		toolFiltro.addButton(btnRemover);
	}

	private void criarExpansao() {
		ToolbarButton btnExpandir = new ToolbarButton();
		btnExpandir.setIconCls("icon-expandir");
		btnExpandir.setTooltip(OpenSigCore.i18n.txtExpandir());
		btnExpandir.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				expandAll();
			}
		});

		ToolbarButton btnRecolher = new ToolbarButton();
		btnRecolher.setIconCls("icon-recolher");
		btnRecolher.setTooltip(OpenSigCore.i18n.txtRecolher());
		btnRecolher.addListener(new ButtonListenerAdapter() {

			public void onClick(Button button, EventObject e) {
				collapseAll();
			}
		});

		toolFiltro.addButton(btnExpandir);
		toolFiltro.addButton(btnRecolher);
	}

	private void criarFiltro() {
		btnFiltro = new ToolbarButton();
		btnFiltro.setTooltip(OpenSigCore.i18n.txtFiltrar());
		btnFiltro.setIconCls("icon-filtroMais");
		btnFiltro.setEnableToggle(true);
		btnFiltro.addListener(new ButtonListenerAdapter() {

			public void onToggle(Button button, boolean pressed) {
				if (pressed) {
					button.setIconCls("icon-filtroMenos");
				} else {
					button.setIconCls("icon-filtroMais");
					treeFiltro.clear();
				}
				filtrarNodes(treeFiltro, pressed, txtFiltro.getValueAsString());
			}
		});

		txtFiltro = new TextField();
		txtFiltro.setWidth(100);
		txtFiltro.setMinLength(1);
		txtFiltro.setSelectOnFocus(true);
		txtFiltro.addListener(new TextFieldListenerAdapter() {
			public void onRender(Component component) {
				txtFiltro.getEl().addListener("keyup", new EventCallback() {
					public void execute(EventObject e) {
						new Timer() {
							public void run() {
								filtrarNodes(treeFiltro, false, txtFiltro.getValueAsString());
							}
						}.schedule(1000);
					}
				});
			}
		});
		txtFiltro.addKeyPressListener(new EventCallback() {
			public void execute(EventObject e) {
				btnAdicionar.setDisabled(txtFiltro.getValueAsString() == null);
			}
		});

		toolFiltro.addButton(btnFiltro);
		toolFiltro.addField(txtFiltro);
	}

	private void filtrarNodes(final TreeFilter treeFiltro, final boolean filtrado, final String filtro) {
		if (filtro == null || filtro.equals("")) {
			treeFiltro.clear();
			treeFiltro.filterBy(new TreeTraversalCallback() {
				public boolean execute(TreeNode node) {
					node.setText(Format.stripTags(node.getText()));
					return true;
				}
			});
		} else {
			treeFiltro.filterBy(new TreeTraversalCallback() {
				public boolean execute(TreeNode node) {
					String texto = Format.stripTags(node.getText());
					node.setText(texto);
					if (texto.toLowerCase().indexOf(filtro.toLowerCase()) != -1) {
						node.setText("<b>" + texto + "</b>");
						((TreeNode) node.getParentNode()).expand();
						return true;
					} else {
						final List childMatches = new ArrayList();
						node.cascade(new NodeTraversalCallback() {
							public boolean execute(com.gwtext.client.data.Node node) {
								String childText = ((TreeNode) node).getText();
								if (childText.toLowerCase().indexOf(filtro.toLowerCase()) != -1) {
									childMatches.add(new Object());
								}
								return true;
							}
						});
						return !filtrado || childMatches.size() != 0;
					}
				}
			});
		}
	}

	public TreeNode novoItem(E obj) {
		TreeNode node = new TreeNode(obj.toString());
		node.setIconCls(iconeNode);
		node.setChecked(false);
		node.setUserObject(obj);
		node.addListener(new TreeNodeListenerAdapter() {
			public void onClick(Node node, EventObject e) {
				E obj = (E) node.getUserObject();
				if (obj.getId().intValue() == 0) {
					btnRemover.enable();
				} else {
					btnRemover.disable();
				}
			}
		});

		return node;
	}

	/**
	 * Metodo que adiciona um novo item na arvore.
	 */
	public void adicionar() {
		final String novo = txtFiltro.getValueAsString();
		boolean existe = false;

		if (txtFiltro.isValid()) {
			TreeNode tNode;
			for (Node node : root.getChildNodes()) {
				tNode = (TreeNode) node;
				if (tNode.getText().equalsIgnoreCase("<B>" + novo + "</B>")) {
					existe = true;
					break;
				}
			}

			if (!existe) {
				E obj = (E) new Dados("", "", "") {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public String[] toArray() {
						return null;
					}

					public void setId(Number id) {
					}

					public Number getId() {
						return 0;
					}

					public String toString() {
						return novo.toUpperCase();
					}
				};
				tNode = novoItem(obj);

				if (root.getFirstChild() != null) {
					root.insertBefore(tNode, root.getFirstChild());
				} else {
					root.appendChild(tNode);
				}
				((TreeNode) root.getFirstChild()).getUI().toggleCheck(true);

				btnAdicionar.disable();
				txtFiltro.setValue("");
			} else {
				MessageBox.alert(getTitle(), OpenSigCore.i18n.errExiste());
			}
		}
	}

	/**
	 * Metodo que remove o item selecionado da arvore.
	 */
	public void remover() {
		TreeNode tNode;
		E obj;
		boolean existe = false;

		for (Node node : root.getChildNodes()) {
			tNode = (TreeNode) node;
			if (getSelectionModel().isSelected(tNode)) {
				obj = (E) tNode.getUserObject();
				if (obj.getId().intValue() == 0) {
					root.removeChild(tNode);
					btnRemover.disable();
					existe = true;
					break;
				}
			}
		}

		if (!existe) {
			MessageBox.alert(getTitle(), OpenSigCore.i18n.msgSelecionar());
		}
	}

	// Gets e Seteres

	public E getClasse() {
		return classe;
	}

	public void setClasse(E classe) {
		this.classe = classe;
	}

	public TreeFilter getTreeFiltro() {
		return treeFiltro;
	}

	public void setTreeFiltro(TreeFilter treeFiltro) {
		this.treeFiltro = treeFiltro;
	}

	public Toolbar getToolFiltro() {
		return toolFiltro;
	}

	public void setToolFiltro(Toolbar toolFiltro) {
		this.toolFiltro = toolFiltro;
	}

	public ToolbarButton getBtnAdicionar() {
		return btnAdicionar;
	}

	public void setBtnAdicionar(ToolbarButton btnAdicionar) {
		this.btnAdicionar = btnAdicionar;
	}

	public ToolbarButton getBtnRemover() {
		return btnRemover;
	}

	public void setBtnRemover(ToolbarButton btnRemover) {
		this.btnRemover = btnRemover;
	}

	public TextField getTxtFiltro() {
		return txtFiltro;
	}

	public void setTxtFiltro(TextField txtFiltro) {
		this.txtFiltro = txtFiltro;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public boolean isFiltrar() {
		return filtrar;
	}

	public void setFiltrar(boolean filtrar) {
		this.filtrar = filtrar;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public boolean isExpandir() {
		return expandir;
	}

	public void setExpandir(boolean expandir) {
		this.expandir = expandir;
	}

	public ToolbarButton getBtnFiltro() {
		return btnFiltro;
	}

	public void setBtnFiltro(ToolbarButton btnFiltro) {
		this.btnFiltro = btnFiltro;
	}

	public Label getLblValidacao() {
		return lblValidacao;
	}

	public void setLblValidacao(Label lblValidacao) {
		this.lblValidacao = lblValidacao;
	}

	public String getIconeNode() {
		return iconeNode;
	}

	public void setIconeNode(String iconeNode) {
		this.iconeNode = iconeNode;
	}

}
