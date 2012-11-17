package br.com.opensig.client.visao.layout;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.client.visao.NavegacaoFavorito;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Observable;
import br.com.opensig.core.client.padroes.Observer;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.Colecao;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.permissao.shared.modelo.SisFavorito;
import br.com.opensig.permissao.shared.modelo.SisFavoritoGrafico;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

/**
 * Classe do componente visual que compoen a esquerda da area de trabalho.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Favoritos extends Panel implements Observer {

	private static Favoritos favoritos;
	private NavegacaoFavorito navegacaoFav;
	private TreeNode meusFav;
	private TreeNode usuarioFav;
	private TreeNode grupoFav;

	/**
	 * Construtor padrao
	 */
	private Favoritos() {
		inicializar();
		Ponte.getInstancia().addObserver(this);
	}

	/**
	 * Metodo que devolve a referencia a unica instancia.
	 * 
	 * @return referencia a unica instancia do objeto.
	 */
	public static Favoritos getInstancia() {
		if (favoritos == null) {
			favoritos = new Favoritos();
		}
		return favoritos;
	}

	// inicializa os componentes visuais
	private void inicializar() {
		// favoritos proprios
		meusFav = new TreeNode(OpenSigCore.i18n.txtFavorito() + " - " + OpenSigCore.i18n.txtDono(), "icon-fav");
		meusFav.setExpandable(true);
		meusFav.setExpanded(true);

		// favoritos do seu usuario
		usuarioFav = new TreeNode(OpenSigCore.i18n.txtFavorito() + " - " + OpenSigCore.i18n.txtUsuario(), "icon-usuario");
		usuarioFav.setExpandable(true);
		usuarioFav.addListener(new TreeNodeListenerAdapter() {
			public void onExpand(Node node) {
				if (node.getChildNodes().length == 0) {
					addUsuarioFav();
				}
			}
		});

		// favoritos dos seus grupos
		grupoFav = new TreeNode(OpenSigCore.i18n.txtFavorito() + " - " + OpenSigCore.i18n.txtGrupo(), "icon-grupo");
		grupoFav.setExpandable(true);
		grupoFav.addListener(new TreeNodeListenerAdapter() {
			public void onExpand(Node node) {
				if (node.getChildNodes().length == 0) {
					addGrupoFav();
				}
			}
		});

		navegacaoFav = new NavegacaoFavorito();
		navegacaoFav.getTreeTarefa().getRoot().appendChild(meusFav);
		navegacaoFav.getTreeTarefa().getRoot().appendChild(usuarioFav);
		navegacaoFav.getTreeTarefa().getRoot().appendChild(grupoFav);

		setTitle(OpenSigCore.i18n.txtFavorito());
		setIconCls("icon-favorito");
		setLayout(new RowLayout());

		add(navegacaoFav.getTreeTarefa(), new RowLayoutData());
	}

	@Override
	public void update(Observable o, final Object arg) {
		if (arg instanceof IFavorito) {
			navegacaoFav.novoFavorito(meusFav, (IFavorito) arg);
		} else if (arg instanceof ILogin) {
			if (meusFav.getChildNodes().length == 0) {
				addMeusFav();
			}
		}
	}

	// Metodo que recupera e carrega os favoritos criados por voce
	private void addMeusFav() {
		Esquerda.getInstancia().getEl().mask(OpenSigCore.i18n.txtAguarde());
		FiltroObjeto fo = new FiltroObjeto("sisUsuario", ECompara.IGUAL, new SisUsuario(Ponte.getLogin().getId()));

		CoreProxy<SisFavorito> proxy = new CoreProxy<SisFavorito>(new SisFavorito());
		proxy.selecionar(fo, new AsyncCallback<Lista<SisFavorito>>() {

			public void onFailure(Throwable caught) {
				Esquerda.getInstancia().getEl().unmask();
				MessageBox.alert(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.errFavorito());
			}

			public void onSuccess(Lista<SisFavorito> result) {
				for (String[] favs : result.getDados()) {
					navegacaoFav.novoFavorito(meusFav, getFavorito(favs));
				}
				Esquerda.getInstancia().getEl().unmask();
			}
		});
	}

	// Metodo que recupera e carrega os favoritos dados ao seu usuario
	private void addUsuarioFav() {
		Esquerda.getInstancia().getEl().mask(OpenSigCore.i18n.txtAguarde());
		FiltroNumero fn = new FiltroNumero("sisUsuarioId", ECompara.IGUAL, Ponte.getLogin().getId());
		fn.setCampoPrefixo("t0.");

		SisFavorito fav = new SisFavorito();
		Colecao col = new Colecao("SisUsuario", "t.sisUsuarios", "JOIN", "t0");
		fav.setColecao(new Colecao[] { col });

		CoreProxy<SisFavorito> proxy = new CoreProxy<SisFavorito>(fav);
		proxy.selecionar(fn, new AsyncCallback<Lista<SisFavorito>>() {

			public void onFailure(Throwable caught) {
				Esquerda.getInstancia().getEl().unmask();
				MessageBox.alert(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.errFavorito());
			}

			public void onSuccess(Lista<SisFavorito> result) {
				for (String[] favs : result.getDados()) {
					navegacaoFav.novoFavorito(usuarioFav, getFavorito(favs));
				}
				Esquerda.getInstancia().getEl().unmask();
			}
		});
	}

	// Metodo que recupera e carrega os favoritos dados ao seus grupos
	private void addGrupoFav() {
		Esquerda.getInstancia().getEl().mask(OpenSigCore.i18n.txtAguarde());
		FiltroNumero fn = new FiltroNumero("sisUsuarioId", ECompara.IGUAL, Ponte.getLogin().getId());
		fn.setCampoPrefixo("t1.");

		CoreProxy<SisFavorito> proxy = new CoreProxy<SisFavorito>(new SisFavorito());
		proxy.selecionar(fn, new AsyncCallback<Lista<SisFavorito>>() {

			public void onFailure(Throwable caught) {
				Esquerda.getInstancia().getEl().unmask();
				MessageBox.alert(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.errFavorito());
			}

			public void onSuccess(Lista<SisFavorito> result) {
				for (String[] favs : result.getDados()) {
					navegacaoFav.novoFavorito(grupoFav, getFavorito(favs));
				}
				Esquerda.getInstancia().getEl().unmask();
			}
		});
	}

	// transforma um array de string no objeto de favorito
	private SisFavorito getFavorito(String[] favs) {
		SisFuncao funcao = new SisFuncao();
		funcao.setSisFuncaoClasse(favs[3]);

		List<SisFavoritoGrafico> graficos = new ArrayList<SisFavoritoGrafico>();
		if (favs[4].equalsIgnoreCase("true")) {
			graficos.add(new SisFavoritoGrafico());
		}

		SisFavorito fav = new SisFavorito();
		fav.setSisFavoritoId(Integer.valueOf(favs[0]));
		fav.setSisFavoritoNome(favs[1]);
		fav.setSisFavoritoDescricao(favs[2]);
		fav.setSisFavoritoGrafico(graficos);
		fav.setSisFuncao(funcao);
		return fav;
	}

	// Gets e Seteres
	
	public static Favoritos getFavoritos() {
		return favoritos;
	}

	public static void setFavoritos(Favoritos favoritos) {
		Favoritos.favoritos = favoritos;
	}

	public NavegacaoFavorito getNavegacaoFav() {
		return navegacaoFav;
	}

	public void setNavegacaoFav(NavegacaoFavorito navegacaoFav) {
		this.navegacaoFav = navegacaoFav;
	}

	public TreeNode getMeusFav() {
		return meusFav;
	}

	public void setMeusFav(TreeNode meusFav) {
		this.meusFav = meusFav;
	}

	public TreeNode getUsuarioFav() {
		return usuarioFav;
	}

	public void setUsuarioFav(TreeNode usuarioFav) {
		this.usuarioFav = usuarioFav;
	}

	public TreeNode getGrupoFav() {
		return grupoFav;
	}

	public void setGrupoFav(TreeNode grupoFav) {
		this.grupoFav = grupoFav;
	}
	
	
}
