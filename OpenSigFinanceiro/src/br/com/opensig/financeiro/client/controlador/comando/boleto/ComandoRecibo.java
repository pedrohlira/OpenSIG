package br.com.opensig.financeiro.client.controlador.comando.boleto;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;

public class ComandoRecibo extends ComandoGerar {

	public ComandoRecibo() {
		this("html");
	}

	public ComandoRecibo(String tipo) {
		super(tipo, true);
	}

	public void execute(Map contexto) {
		super.execute(contexto);
		final Record rec = LISTA.getPanel().getSelectionModel().getSelected();

		if (!rec.getAsString("finRecebimentoStatus").equalsIgnoreCase(OpenSigCore.i18n.txtAberto())) {
			getGerar().execute(contexto);
		} else {
			MessageBox.alert(OpenSigCore.i18n.txtBoleto(), OpenSigCore.i18n.errRecibo());
		}
	}
}
