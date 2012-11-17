package br.com.opensig.permissao.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.permissao.client.servico.PermissaoProxy;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

public class ComandoSair extends AComando {

	public void execute(Map contexto) {
		MessageBox.confirm(OpenSigCore.i18n.txtSair(), OpenSigCore.i18n.msgSair(), new MessageBox.ConfirmCallback() {
			public void execute(String btnID) {
				if (btnID.equalsIgnoreCase("yes")) {
					PermissaoProxy login = new PermissaoProxy();
					login.sair(new AsyncCallback() {
						public void onFailure(final Throwable caught) {
						}
						public void onSuccess(Object result) {
							Window.Location.reload();
						}
					});
				}
			}
		});
	}
}
