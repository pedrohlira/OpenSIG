package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;

/**
 * Classe que define um parametro do tipo texto.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ParametroTexto extends AParametro<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * @see AParametro#AParametro()
	 */
	public ParametroTexto() {
		super();
	}

	/**
	 * @see AParametro#AParametro(String, Serializable)
	 */
	public ParametroTexto(String campo, String valor) {
		super.campo = campo;
		super.valor = valor;
	}

	@Override
	public void setValorString(String valor) {
		super.valor = valor;
	}
}
