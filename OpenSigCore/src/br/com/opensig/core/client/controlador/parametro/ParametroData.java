package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

/**
 * Classe que define um parametro do tipo data.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ParametroData extends AParametro<Date> {

    private static final long serialVersionUID = 1L;

    /**
     * @see AParametro#AParametro()
     */
    public ParametroData() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, String)
     */
    public ParametroData(String campo, String valor) {
        super(campo, valor);
    }

    /**
     * @see AParametro#AParametro(String, Serializable)
     */
    public ParametroData(String campo, Date valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
        if (valor != null) {
            super.setValor(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM).parse(valor));
        }
    }
}
