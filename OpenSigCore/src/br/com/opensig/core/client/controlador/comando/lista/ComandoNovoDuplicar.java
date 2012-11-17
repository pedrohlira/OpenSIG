package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.ComandoAba;
import br.com.opensig.core.client.visao.abstrato.IFormulario;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * Classe do comando novo, comando padronizado para criar um novo registro baseado em um atual.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoNovoDuplicar extends ComandoAba {

	@Override
	public void execute(final Map contexto) {
		int id = UtilClient.getSelecionado((GridPanel) contexto.get("lista"));

		if (id > 0) {
			contexto.put("selecionado", id);
			super.execute(contexto, new AsyncCallback() {
				public void onSuccess(Object result) {
					execute();
				}

				public void onFailure(Throwable caught) {
				}
			});
		}
	}

	@Override
	protected void execute() {
		FORM = (IFormulario) contexto.get("form");
		FORM.setDuplicar(true);
		super.execute(contexto);
	}
}
