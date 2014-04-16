package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.poker.client.servico.PokerProxy;
import br.com.opensig.poker.shared.modelo.PokerCash;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoCashFechar extends ComandoAcao {

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				final Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec != null && !rec.getAsBoolean("pokerCashFechado")) {
					MessageBox.confirm(Ponte.getCentro().getActiveTab().getTitle(), OpenSigCore.i18n.msgConfirma(), new MessageBox.ConfirmCallback() {

						public void execute(String btnID) {
							if (btnID.equalsIgnoreCase("yes")) {
								MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtFechar());
								PokerCash cash = new PokerCash(rec.getAsInteger("pokerCashId"));

								PokerProxy proxy = new PokerProxy();
								proxy.fecharCash(cash, new AsyncCallback() {
									public void onSuccess(Object result) {
										MessageBox.hide();
										LISTA.getPanel().getStore().reload();
										new ToastWindow(OpenSigCore.i18n.txtFechar(), OpenSigCore.i18n.msgSalvarOK()).show();
									};
									
									public void onFailure(Throwable caught) {
										MessageBox.hide();
										new ToastWindow(OpenSigCore.i18n.txtFechar(), caught.getMessage()).show();
									};
								});
							}
						}
					});
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtFechar(), OpenSigCore.i18n.txtAcessoNegado());
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
