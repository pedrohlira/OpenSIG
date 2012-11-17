package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

/**
 * Classe que implementa o comando acao do tipo aba.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class ComandoAba extends ComandoAcao {

	/**
	 * Metodo de execucao para as abas apos validar comando.
	 */
	protected abstract void execute();

	@Override
	public void execute(Map contexto) {
		super.execute(contexto);
		LISTA.getPanel().disable();
		FORM.getPanel().enable();
		TAB.activate(1);
		FORM.mostrarDados();

		if (comando != null) {
			comando.execute(contexto);
		}
	}
}
