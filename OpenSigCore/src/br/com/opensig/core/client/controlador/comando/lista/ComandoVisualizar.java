package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAba;

import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe do comando padrao, para toda comando que nao foi implementado.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */

public class ComandoVisualizar extends ComandoAba {

	@Override
	public void execute(Map contexto) {
		super.execute(contexto);
		execute();
	}

	@Override
	protected void execute() {
		FORM.getBtnSalvar().disable();
		new ToastWindow(OpenSigCore.i18n.txtFormulario(), OpenSigCore.i18n.txtVisualizar()).show();
	}
}
