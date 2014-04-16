package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.js.OpenSigCoreJS;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;

public class ComandoTorneioPlay extends ComandoAcao {

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec != null && rec.getAsBoolean("pokerTorneioAtivo")) {
					int id = rec.getAsInteger("pokerTorneioId");
					String codigo = rec.getAsString("pokerTorneioCodigo");
					OpenSigCoreJS.abrirJanela("/player.jsp?tID=" + id, codigo, "height=710,width=1024,titlebar=no,toolbar=no,status=no,scrollbars=no,menubar=no,location=no,resizable=yes");
				} else {
					MessageBox.alert("Player", OpenSigCore.i18n.txtAcessoNegado());
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
