package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.parametro.ParametroException;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

/**
 * Classe que define um filtro do tipo data.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FiltroData extends AFiltro<Date> {

	/**
	 * @see AFiltro#AFiltro()
	 */
	public FiltroData() {
		super();
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, String)
	 */
	public FiltroData(String campo, ECompara compara, String valor) {
		super(campo, compara, valor);
	}

	/**
	 * @see AFiltro#AFiltro(String, ECompara, Serializable)
	 */
	public FiltroData(String campo, ECompara compara, Date valor) {
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
		if (valor != null) {
			super.setValor(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM).parse(valor));
		}
	}
}
