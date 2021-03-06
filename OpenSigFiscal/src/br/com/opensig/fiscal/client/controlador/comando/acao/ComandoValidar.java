package br.com.opensig.fiscal.client.controlador.comando.acao;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.fiscal.client.servico.FiscalProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoValidar extends ComandoAcao {

	private IFiltro filtro;

	public ComandoValidar() {
	}

	public void execute(Map contexto) {
		super.execute(contexto);
		EModo modo = contexto.get("acao") != null ? (EModo) contexto.get("acao") : EModo.LISTAGEM;

		if (modo == EModo.LISTAGEM) {
			filtro = LISTA.getProxy().getFiltroAtual();
		} else {
			Record rec = LISTA.getPanel().getSelectionModel().getSelected();
			if (rec.getAsString("fisNotaEntradaRecibo") != null && rec.getAsString("fisNotaEntradaRecibo").equals("OK")) {
				new ToastWindow(OpenSigCore.i18n.txtValidar(), "OK").show();
				return;
			} else {
				String chave = rec.getAsString("fisNotaEntradaChave");
				filtro = new FiltroTexto("fisNotaEntradaChave", ECompara.IGUAL, chave);
			}
		}

		MessageBox.confirm(OpenSigCore.i18n.txtValidar(), "Deseja validar automaticamente?", new ConfirmCallback() {
			public void execute(String btnID) {
				if (btnID.equalsIgnoreCase("yes")) {
					validar(true);
				} else if (btnID.equalsIgnoreCase("no")) {
					validar(false);
				}
			}
		});
	}

	private void validar(boolean auto) {
		int amb = Integer.valueOf(UtilClient.CONF.get("nfe.tipoamb"));
		FiscalProxy proxy = new FiscalProxy();

		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtValidar());
		proxy.validar(amb, filtro, auto, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtValidar(), caught.getMessage());
			}

			public void onSuccess(String result) {
				MessageBox.hide();
				LISTA.getPanel().getStore().reload();
				new ToastWindow(OpenSigCore.i18n.txtValidar(), result).show();
			}
		});
	}
}
