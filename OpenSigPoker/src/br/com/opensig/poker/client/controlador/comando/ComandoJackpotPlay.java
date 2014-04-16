package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.js.OpenSigCoreJS;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ComandoJackpotPlay extends ComandoAcao {

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				OpenSigCoreJS.abrirJanela("/jackpot.jsp", "Jackpot", "height=710,width=1024,titlebar=no,toolbar=no,status=no,scrollbars=no,menubar=no,location=no,resizable=yes");
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
