package br.com.opensig.permissao.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.IGrafico;
import br.com.opensig.permissao.client.visao.form.FormularioFavorito;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * A classe com código executor está no SigPermissao, classe ComandoFavoritoImpl
 * 
 * @author Pedro H. Lira
 * @since 13/04/2009
 * @version 1.0
 */

public class ComandoFavoritoImpl extends ComandoAcao {

	private static Window wndFavorito;
	private static FormularioFavorito formFavorito;

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				if (wndFavorito == null) {
					formFavorito = new FormularioFavorito(LISTA.getFuncao());

					wndFavorito = new Window(OpenSigCore.i18n.txtFavorito(), 500, 310, true, false);
					wndFavorito.setId("wndFavorito");
					wndFavorito.setButtonAlign(Position.CENTER);
					wndFavorito.setClosable(false);
					wndFavorito.setLayout(new FitLayout());
					wndFavorito.setIconCls("icon-favorito");
					wndFavorito.add(formFavorito);
					wndFavorito.doLayout();
				}

				formFavorito.setLista(LISTA);
				TabPanel tab = (TabPanel) Ponte.getCentro().getActiveTab();
				if (tab.getActiveTab() instanceof IGrafico) {
					formFavorito.setGrafico(GRAFICO);
				}
				wndFavorito.show();

				if (comando != null) {
					comando.execute(contexto);
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
