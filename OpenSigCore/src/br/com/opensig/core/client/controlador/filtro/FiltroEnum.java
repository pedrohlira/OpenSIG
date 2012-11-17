package br.com.opensig.core.client.controlador.filtro;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.parametro.ParametroException;

/**
 * Classe que define um filtro do tipo enum.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FiltroEnum extends AFiltro<Enum> {

	/**
	 * @see AFiltro#AFiltro()
	 */
	public FiltroEnum() {
		super();
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, java.io.Serializable)
	 */
	public FiltroEnum(String campo, ECompara compara, Enum valor) {
		super(campo, compara, valor);
	}

	@Override
	public String getSql() throws ParametroException {
		if (compara == ECompara.IGUAL || compara == ECompara.DIFERENTE) {
			return super.getSql();
		} else {
			throw new ParametroException(OpenSigCore.i18n.errFiltro());
		}
	}

	@Override
	public void setValorString(String valor) {
		throw new NullPointerException(OpenSigCore.i18n.errFiltro());
	}
}
