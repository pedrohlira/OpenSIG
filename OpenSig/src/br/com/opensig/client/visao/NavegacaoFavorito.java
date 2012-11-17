package br.com.opensig.client.visao;

import java.util.HashMap;
import java.util.Map;

import br.com.opensig.client.visao.portal.Portatil;
import br.com.opensig.client.visao.portal.Principal;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.permissao.shared.modelo.SisFavorito;
import br.com.opensig.permissao.shared.modelo.SisFavoritoPortal;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe que representa a navegacao lateral dos favoritos salvos pelos
 * usuarios.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class NavegacaoFavorito {

	private Menu menu;
	private Arvore treeTarefa;
	private SisFavorito favorito;

	/**
	 * Construtor padrao.
	 */
	public NavegacaoFavorito() {
		treeTarefa = new Arvore(null, OpenSigCore.i18n.txtFavorito());
		treeTarefa.setWidth(180);
		treeTarefa.setFiltrar(true);
		treeTarefa.addListener(new TreePanelListenerAdapter() {

			public void onContextMenu(final TreeNode node, EventObject e) {
				if (node.getUserObject() != null) {
					treeTarefa.getSelectionModel().select(node);
					favorito = (SisFavorito) node.getUserObject();
					showMenu(e.getXY());
				}
			}
		});
		treeTarefa.inicializar();
	}

	// abre o menu suspenso com as opcoes.
	private void showMenu(int[] xy) {
		if (menu == null) {
			menu = new Menu();

			Item coluna = new Item(OpenSigCore.i18n.txtAdicionar());
			coluna.setIconCls("icon-coluna");
			coluna.setTitle(OpenSigCore.i18n.txtPrincipal());
			coluna.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					adicionarFav();
				}
			});
			menu.addItem(coluna);

			Item remover = new Item(OpenSigCore.i18n.txtRemover());
			remover.setIconCls("icon-excluir");
			remover.setTitle(OpenSigCore.i18n.txtExcluir());
			remover.addListener(new BaseItemListenerAdapter() {
				public void onClick(BaseItem item, EventObject e) {
					removerFav();
				}
			});
			menu.addItem(remover);
		}

		menu.showAt(xy);
	}

	/**
	 * Metodo que cria um novo favorito na sistema.
	 * 
	 * @param pai
	 *            informar qual o grupo pai do novo favorito.
	 * @param sisFavorito
	 *            o objeto entidade que contem os dados favoritos.
	 */
	public void novoFavorito(final Node pai, final IFavorito sisFavorito) {
		final IComando comando = FabricaComando.getInstancia().getComando(sisFavorito.getSisFuncao().getSisFuncaoClasse());
		final Map contexto = new HashMap();
		final CoreProxy<SisFavorito> persiste = new CoreProxy<SisFavorito>(new SisFavorito());

		TreeNode node = new TreeNode(sisFavorito.getSisFavoritoNome(), sisFavorito.getSisFavoritoGrafico().isEmpty() ? "icon-padrao" : "icon-grafico");
		node.setUserObject(sisFavorito);
		node.addListener(new TreeNodeListenerAdapter() {

			public void onDblClick(Node node, EventObject e) {
				if (contexto.get("favorito") != null) {
					comando.execute(contexto);
				} else {
					favorito = (SisFavorito) node.getUserObject();
					if (favorito.getSisFuncao().getSisAcoes() != null) {
						contexto.put("dados", favorito.getSisFuncao());
						contexto.put("favorito", favorito);
						comando.execute(contexto);
					} else {
						persiste.selecionar(favorito.getSisFavoritoId(), new AsyncCallback<SisFavorito>() {

							public void onFailure(Throwable caught) {
								MessageBox.alert(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.errFavorito());
							}

							public void onSuccess(SisFavorito result) {
								SisFuncao funcao = result.getSisFuncao();
								funcao.setSisAcoes(Ponte.getLogin().getAcoes(funcao.getSisModulo().getSisModuloId(), funcao.getSisFuncaoId()));
								contexto.put("dados", funcao);
								contexto.put("favorito", result);
								comando.execute(contexto);
							}
						});
					}
				}
			}
		});
		pai.appendChild(node);
	}

	// adiciona o favorito criado a lista da esquerda.
	private void adicionarFav() {
		final int[] dados = Principal.getInstancia().getPosicao();
		SisFavoritoPortal portal = new SisFavoritoPortal();
		portal.setSisFavoritoPortalColuna((byte) dados[0]);
		portal.setSisFavoritoPortalOrdem((byte) dados[1]);
		portal.setSisFavorito(favorito);
		portal.setSisUsuario(new SisUsuario(Ponte.getLogin().getId()));

		Ponte.getCentro().scrollToTab(Principal.getInstancia(), true);
		Ponte.getCentro().activate(0);
		CoreProxy<SisFavoritoPortal> proxy = new CoreProxy<SisFavoritoPortal>(portal);
		proxy.salvar(new AsyncCallback<SisFavoritoPortal>() {
			public void onFailure(Throwable caught) {
				new ToastWindow(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.errExiste()).show();
			}

			public void onSuccess(SisFavoritoPortal result) {
				result.setSisFavorito(favorito);
				Portatil portatil = new Portatil(result.toArray());
				Principal.getInstancia().adicionar(portatil, dados[0] + "");

				new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.msgSalvarOK()).show();
			}
		});
	}

	// remove um favorito da lista e do sistema
	private void removerFav() {
		boolean existe = false;

		for (Node node : treeTarefa.getRoot().getFirstChild().getChildNodes()) {
			final TreeNode tNode = (TreeNode) node;
			if (treeTarefa.getSelectionModel().isSelected(tNode)) {
				MessageBox.confirm(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.msgFavorito(), new ConfirmCallback() {

					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {

							treeTarefa.getEl().mask(OpenSigCore.i18n.txtAguarde());
							favorito = (SisFavorito) tNode.getUserObject();
							CoreProxy<SisFavorito> proxy = new CoreProxy<SisFavorito>(favorito);
							proxy.deletar(new AsyncCallback<SisFavorito>() {

								public void onFailure(Throwable caught) {
									treeTarefa.getEl().unmask();
									MessageBox.alert(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.errExcluir());
								}

								public void onSuccess(SisFavorito result) {
									treeTarefa.getEl().unmask();
									treeTarefa.getRoot().getFirstChild().removeChild(tNode);
									new ToastWindow(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluirOK()).show();
								}
							});
						}
					}
				});

				existe = true;
				break;
			}
		}

		if (!existe) {
			MessageBox.alert(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.msgFavoritoInvalido());
		}
	}

	// Gets e Seteres

	public Arvore getTreeTarefa() {
		return treeTarefa;
	}

	public void setTreeTarefa(Arvore treeTarefa) {
		this.treeTarefa = treeTarefa;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public SisFavorito getFavorito() {
		return favorito;
	}

	public void setFavorito(SisFavorito favorito) {
		this.favorito = favorito;
	}

}
