package br.com.opensig.core.client.controlador.comando.form;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.IComando;

/**
 * Classe do comando cancelar, usada em formularios.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoCancelar extends ComandoAcao {

	/**
	 * @see AComando#AComando()
	 */
	public ComandoCancelar(){
		this(null);
	}
	
	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoCancelar(IComando proximo){
		this.comando = proximo;
	}

	@Override
	public void execute(Map contexto) {
		super.execute(contexto);

		if (FORM != null) {
			try {
				FORM.getBtnSalvar().enable();
				FORM.limparDados();
				FORM.getPanel().disable();
			} catch (Exception e) {
				// caso nao tenha nao executa
			}
		}
		
		if (LISTA != null) {
			try {
				LISTA.getPanel().enable();
			} catch (Exception e) {
				// caso nao tenha nao executa
			}
		}
		
		if (TAB != null) {
			try {
				TAB.activate(0);
			} catch (Exception e) {
				// caso nao tenha nao executa
			}
		}
		
		if (comando != null) {
			comando.execute(contexto);
		}
	}
}
