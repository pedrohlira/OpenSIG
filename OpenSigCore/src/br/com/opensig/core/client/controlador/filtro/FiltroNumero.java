package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo número.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FiltroNumero extends AFiltro<Number> {

	/**
	 * @see AFiltro#AFiltro()
	 */
	public FiltroNumero() {
		super();
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, String)
	 */
	public FiltroNumero(String campo, ECompara compara, String valor) {
		super(campo, compara, valor);
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, Serializable)
	 */
	public FiltroNumero(String campo, ECompara compara, Number valor) {
		super(campo, compara, valor);
	}

	@Override
	public String getSql() throws ParametroException {
		if (compara != ECompara.CONTEM && compara != ECompara.CONTEM_FIM && compara != ECompara.CONTEM_INICIO) {
			return super.getSql();
		} else {
			throw new ParametroException(OpenSigCore.i18n.errFiltro());
		}
	}

	@Override
	public void setValorString(String valor) {
		if (valor == null) {
			super.setValor(0);
		} else if (valor.indexOf(".") > 0) {
			super.setValor(Double.valueOf(valor));
		} else {
			super.setValor(Long.valueOf(valor));
		}
	}
}
