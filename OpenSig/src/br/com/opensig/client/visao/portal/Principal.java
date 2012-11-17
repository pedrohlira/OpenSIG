package br.com.opensig.client.visao.portal;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Observable;
import br.com.opensig.core.client.padroes.Observer;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.permissao.shared.modelo.SisFavorito;
import br.com.opensig.permissao.shared.modelo.SisFavoritoPortal;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragSource;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.portal.Portal;
import com.gwtext.client.widgets.portal.PortalColumn;
import com.gwtext.client.widgets.portal.PortalDropZone;

/**
 * Classe que representa o painel de controle que contem as mini-janelas
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Principal extends Portal implements Observer {

	private static Principal principal;
	private PortalColumn coluna1;
	private PortalColumn coluna2;

	/**
	 * Construtor padrao.
	 */
	private Principal() {
		super();
		inicializar();
		Ponte.getInstancia().addObserver(this);
	}

	/**
	 * Metodo que retorna a instacia da classe, pois so existe uma referencia
	 * para ela.
	 * 
	 * @return a instacia unica da classe.
	 */
	public static Principal getInstancia() {
		if (principal == null) {
			principal = new Principal();
		}
		return principal;
	}

	// inicializa os componentes visuais.
	private void inicializar() {
		setId(Principal.class.getName() + "_tab");
		setTitle(OpenSigCore.i18n.txtPrincipal(), "icon-principal");

		coluna1 = new PortalColumn();
		coluna1.setId("coluna1");
		coluna1.setPaddings(2, 2, 2, 0);
		coluna2 = new PortalColumn();
		coluna2.setId("coluna2");
		coluna2.setPaddings(2, 2, 2, 0);

		add(coluna1, new ColumnLayoutData(.5));
		add(coluna2, new ColumnLayoutData(.5));
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ILogin) {
			FiltroObjeto fo = new FiltroObjeto("sisUsuario", ECompara.IGUAL, new SisUsuario(Ponte.getLogin().getId()));
			SisFavoritoPortal portal = new SisFavoritoPortal();
			CoreProxy<SisFavoritoPortal> proxy = new CoreProxy<SisFavoritoPortal>(portal);

			proxy.selecionar(fo, new AsyncCallback<Lista<SisFavoritoPortal>>() {

				public void onFailure(Throwable caught) {
				}

				public void onSuccess(Lista<SisFavoritoPortal> result) {
					for (String[] favPortal : result.getDados()) {
						adicionar(new Portatil(favPortal), favPortal[1]);
					}
				}
			});
		}
	}

	/**
	 * Metodo que apos redenrizar executa as modificacoes visuais.
	 */
	protected void afterRender() {
		// permite que o usuario modifique e salve as posicoes.
		new PortalDropZone(principal, null) {

			public boolean notifyDrop(DragSource source, EventObject e, DragData data) {
				boolean retorno = super.notifyDrop(source, e, data);

				if (retorno) {
					Portatil portatil = (Portatil) findByID(source.getId());
					SisFavoritoPortal portal = new SisFavoritoPortal(portatil.getPortalId());
					PortalColumn coluna = (PortalColumn) findByID("coluna" + (getCol() + 1));

					portal.setSisFavoritoPortalColuna((byte) (getCol() + 1));
					portal.setSisFavoritoPortalOrdem((byte) (getPos() == -1 ? coluna.getItems().length + 1 : getPos() + 1));
					portal.setSisFavorito(new SisFavorito(portatil.getFavoritoId()));
					portal.setSisUsuario(new SisUsuario(Ponte.getLogin().getId()));

					CoreProxy<SisFavoritoPortal> proxy = new CoreProxy<SisFavoritoPortal>(portal);
					proxy.salvar(new AsyncCallback<SisFavoritoPortal>() {
						public void onFailure(Throwable caught) {
						}

						public void onSuccess(SisFavoritoPortal result) {
						}
					});
				}

				return retorno;
			}
		};
	}

	/**
	 * Remove uma mina janela do painel de controle.
	 */
	public void remove(String id) {
		coluna1.remove(id);
		coluna2.remove(id);
	}

	/**
	 * Adiciona uma mini janela ao painel de controle.
	 * 
	 * @param portatil
	 *            um tipo de mini janela que contem a funcao escolhida.
	 * @param colunaId
	 *            em qual coluna deve aparecer.
	 */
	public void adicionar(final Portatil portatil, String colunaId) {
		PortalColumn coluna = (PortalColumn) findByID("coluna" + colunaId);
		coluna.add(portatil);

		portatil.addListener(new PanelListenerAdapter() {
			public void onRender(Component component) {
				portatil.addDados();
			};
		});

		doLayout();
	}

	/**
	 * Metodo que retorna a linha e coluna melhor indicada para adicionar uma
	 * nova mini-janela.
	 * 
	 * @return um array de inteiros com a posicao.
	 */
	public int[] getPosicao() {
		int colunaId = 0;

		if (coluna1.getComponents().length <= coluna2.getComponents().length) {
			colunaId = 1;
		} else {
			colunaId = 2;
		}

		PortalColumn coluna = (PortalColumn) findByID("coluna" + colunaId);
		return new int[] { colunaId, coluna.getComponents().length };
	}

	// Gets e Seteres

	public static Principal getPrincipal() {
		return principal;
	}

	public static void setPrincipal(Principal principal) {
		Principal.principal = principal;
	}

	public PortalColumn getColuna1() {
		return coluna1;
	}

	public void setColuna1(PortalColumn coluna1) {
		this.coluna1 = coluna1;
	}

	public PortalColumn getColuna2() {
		return coluna2;
	}

	public void setColuna2(PortalColumn coluna2) {
		this.coluna2 = coluna2;
	}

}
