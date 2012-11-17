package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;

/**
 * Classe que define um parametro do tipo boleano.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ParametroBinario extends AParametro<Integer> {

	private static final long serialVersionUID = 1L;

	/**
	 * @see AParametro#AParametro()
	 */
	public ParametroBinario() {
		super();
	}

	/**
	 * @see AParametro#AParametro(String, String)
	 */
	public ParametroBinario(String campo, String valor) {
		super(campo, valor);
	}

	/**
	 * @see AParametro#AParametro(String, Serializable)
	 */
	public ParametroBinario(String campo, int valor) {
		super(campo, valor);
	}

	@Override
	public void setValorString(String valor) {
		if (valor == null || valor.equals("0")) {
			super.setValor(0);
		} else {
			super.setValor(1);
		}
	}
}
