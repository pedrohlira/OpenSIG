package br.com.opensig.client.visao.portal;

import java.util.HashMap;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.IGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.permissao.shared.modelo.SisFavorito;
import br.com.opensig.permissao.shared.modelo.SisFavoritoPortal;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.ToolHandler;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.portal.Portlet;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe que representa uma funcao mostrada como mini-janela no painel de
 * controle.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Portatil extends Portlet {

	private IListagem lista;
	private IGrafico grafico;
	private int portalId;
	private int favoritoId;
	private String titulo;

	/**
	 * Contrutor que recebe um conjunto de favoritos do tipo portal.
	 * 
	 * @param favPortal
	 *            um array de string com os dados dos favoritos.
	 */
	public Portatil(String[] favPortal) {
		this.portalId = Integer.valueOf(favPortal[0]);
		this.favoritoId = Integer.valueOf(favPortal[3]);
		this.titulo = favPortal[4];

		inicializar();
	}

	// inicializa os componentes visuais.
	private void inicializar() {
		// botao de re-atualizar
		Tool refresh = new Tool(Tool.REFRESH, new ToolHandler() {
			public void onClick(EventObject e, ExtElement toolEl, Panel panel) {
				if (grafico == null) {
					lista.getPanel().getStore().reload();
				} else {
					grafico.gerarGrafico();
				}
			}
		});

		// botao de maximar/minimizar a janela
		Tool maximize = new Tool(Tool.MAXIMIZE, new ToolHandler() {
			public void onClick(EventObject e, ExtElement toolEl, Panel panel) {
				final Window wnd = new Window();
				wnd.setTitle(getTitle());
				wnd.setIconCls(getIconCls());
				wnd.setLayout(new FitLayout());
				wnd.setMaximizable(true);
				wnd.setClosable(false);

				if (grafico == null) {
					lista.getPanel().getBottomToolbar().show();
					wnd.add(lista.getPanel());
				} else {
					wnd.add(grafico.getPanel());
				}

				wnd.show();
				wnd.maximize();

				wnd.addListener(new WindowListenerAdapter() {
					public void onRestore(Window source) {
						if (grafico == null) {
							lista.getPanel().getBottomToolbar().hide();
							add(lista.getPanel());
						} else {
							add(grafico.getPanel());
						}
						doLayout();
						wnd.close();
					}
				});
			}
		});

		// bota de fechar a janela
		Tool close = new Tool(Tool.CLOSE, new ToolHandler() {
			public void onClick(EventObject e, ExtElement toolEl, final Panel panel) {
				MessageBox.confirm(OpenSigCore.i18n.txtFavorito(), OpenSigCore.i18n.msgFavorito(), new MessageBox.ConfirmCallback() {

					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							Principal.getInstancia().remove(panel.getId());
							CoreProxy<SisFavoritoPortal> proxy = new CoreProxy<SisFavoritoPortal>(new SisFavoritoPortal(portalId));
							proxy.deletar(new AsyncCallback<SisFavoritoPortal>() {

								public void onFailure(Throwable caught) {
									MessageBox.alert(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.errExcluir());
								}

								public void onSuccess(SisFavoritoPortal result) {
									new ToastWindow(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluirOK()).show();
								}
							});
						}
					}
				});

			}
		});

		// layout padrao de uma mini-janela
		setHeight(310);
		setPaddings(0);
		setBodyBorder(false);
		setBorder(false);
		setId("portatil" + favoritoId);
		setTitle(titulo);
		setCollapsible(false);
		setLayout(new FitLayout());
		setTools(new Tool[] { refresh, maximize, close });
	}

	/**
	 * Metodo que adiciona os dados dentro de uma mini-janela
	 */
	public void addDados() {
		getEl().mask(OpenSigCore.i18n.txtAguarde());

		CoreProxy<SisFavorito> proxy = new CoreProxy<SisFavorito>(new SisFavorito());
		proxy.selecionar(favoritoId, new AsyncCallback<SisFavorito>() {

			public void onFailure(Throwable caught) {
				getEl().unmask();
				setHtml(OpenSigCore.i18n.errFavorito());
			}

			public void onSuccess(SisFavorito result) {
				IComando comando = FabricaComando.getInstancia().getComando(result.getSisFuncao().getSisFuncaoClasse());
				Map contexto = new HashMap();
				contexto.put("dados", result.getSisFuncao());
				contexto.put("favorito", result);
				contexto.put("portal", "sim");
				comando.execute(contexto);
				lista = (IListagem) contexto.get("lista");

				if (result.getSisFavoritoGrafico().isEmpty()) {
					lista.setFavorito(result);
					lista.getPanel().getTopToolbar().hide();
					lista.getPanel().getBottomToolbar().hide();
					lista.getPanel().purgeListeners();

					add(lista.getPanel());
					setIconCls("icon-padrao");
					doLayout();
				} else {
					grafico = (IGrafico) contexto.get("grafico");
					grafico.setFavorito(result.getSisFavoritoGrafico().get(0));
					grafico.getPanel().getTopToolbar().hide();
					grafico.getPanel().purgeListeners();

					add(grafico.getPanel());
					setIconCls("icon-grafico");
					doLayout();
					grafico.gerarGrafico();
				}

				getEl().unmask();
			}
		});
	}

	// Gets e Seteres

	public int getPortalId() {
		return portalId;
	}

	public void setPortalId(int portalId) {
		this.portalId = portalId;
	}

	public int getFavoritoId() {
		return favoritoId;
	}

	public void setFavoritoId(int favoritoId) {
		this.favoritoId = favoritoId;
	}

	public IListagem getLista() {
		return lista;
	}

	public void setLista(IListagem lista) {
		this.lista = lista;
	}

	public IGrafico getGrafico() {
		return grafico;
	}

	public void setGrafico(IGrafico grafico) {
		this.grafico = grafico;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
