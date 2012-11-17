package br.com.opensig.permissao.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.permissao.client.servico.PermissaoProxy;
import br.com.opensig.permissao.client.visao.BloquearSessao;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ComandoBloquear extends AComando {

	public void execute(Map contexto) {
		PermissaoProxy login = new PermissaoProxy();
		login.bloquear(true, new AsyncCallback() {
			public void onSuccess(Object result) {
				new BloquearSessao();
			}

			public void onFailure(Throwable caught) {
				Window.Location.reload();
			}
		});
	}
}
