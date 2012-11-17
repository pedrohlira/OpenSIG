package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoAba;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Classe do comando novo, comando padronizado para criar um novo registro.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoNovo extends ComandoAba {

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				execute();
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
	
	@Override
	protected void execute() {
		LISTA.getPanel().getSelectionModel().clearSelections();
		FORM.limparDados();
		super.execute(contexto);
	}
}
