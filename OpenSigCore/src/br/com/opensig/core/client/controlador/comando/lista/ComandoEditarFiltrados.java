package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.visao.EditarFiltrados;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Classe do comando editar filtrados, executa a edicao em lote.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoEditarFiltrados extends ComandoAcao {

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				new EditarFiltrados(LISTA);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
