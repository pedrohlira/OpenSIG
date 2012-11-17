package br.com.opensig.core.client.controlador.parametro;

import br.com.opensig.core.client.OpenSigCore;

/**
 * Classe que define um parametro do tipo enum.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ParametroEnum extends AParametro<Enum> {

    private static final long serialVersionUID = 1L;

    /**
     * @see AParametro#AParametro()
     */
    public ParametroEnum() {
        super();
    }

    /**
     * @see AParametro#AParametro(String, java.io.Serializable)
     */
    public ParametroEnum(String campo, Enum valor) {
        super(campo, valor);
    }

    @Override
    public void setValorString(String valor) {
    	throw new NullPointerException(OpenSigCore.i18n.errFiltro());
    }
}
