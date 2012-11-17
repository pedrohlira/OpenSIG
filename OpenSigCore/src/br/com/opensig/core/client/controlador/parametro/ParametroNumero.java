package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;

/**
 * Classe que define um parametro do tipo número.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ParametroNumero extends AParametro<Number> {

	private static final long serialVersionUID = 1L;

	/**
	 * @see AParametro#AParametro()
	 */
	public ParametroNumero() {
		super();
	}

	/**
	 * @see AParametro#AParametro(String, String)
	 */
	public ParametroNumero(String campo, String valor) {
		super(campo, valor);
	}

	/**
	 * @see AParametro#AParametro(String, Serializable)
	 */
	public ParametroNumero(String campo, Number valor) {
		super(campo, valor);
	}

	@Override
	public void setValorString(String valor) {
		if (valor == null) {
			super.setValor(0);
		} else if (valor.indexOf(".") > 0) {
			super.setValor(Float.valueOf(valor));
		} else {
			super.setValor(Integer.valueOf(valor));
		}
	}
}
