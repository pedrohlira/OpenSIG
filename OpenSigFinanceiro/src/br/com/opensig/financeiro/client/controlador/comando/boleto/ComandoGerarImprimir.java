package br.com.opensig.financeiro.client.controlador.comando.boleto;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

public class ComandoGerarImprimir extends ComandoGerarHtml {

	public void execute(final Map contexto) {
		setAsyncCallback(new AsyncCallback<String>() {

			public void onSuccess(String arg0) {
				MessageBox.hide();
				UtilClient.exportar("ExportacaoService?imp=true&id=" + arg0);

				if (comando != null) {
					comando.execute(contexto);
				}
			}

			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtImprimir(), OpenSigCore.i18n.errImprimir());
			}
		});

		super.execute(contexto);
	}
}
