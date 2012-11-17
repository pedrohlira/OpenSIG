package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que define um parametro do tipo objeto.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ParametroObjeto extends AParametro<Dados> {

	private static final long serialVersionUID = 1L;

	/**
	 * @see AParametro#AParametro()
	 */
	public ParametroObjeto() {
		super();
	}

	/**
	 * @see AParametro#AParametro(String, Serializable)
	 */
	public ParametroObjeto(String campo, Dados valor) {
		super(campo, valor);
	}

	@Override
	public void setValorString(String valor) {
		throw new NullPointerException(OpenSigCore.i18n.errFiltro());
	}
}
