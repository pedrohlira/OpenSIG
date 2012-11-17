package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;

/**
 * Classe que define um parametro do tipo número que faz uso do proprio valor na
 * atualização.
 * 
 * @author Pedro H. Lira
 * @since 31/05/2010
 * @version 1.0
 */
public class ParametroFormula extends ParametroNumero {

	private static final long serialVersionUID = 1L;
	private String campo2;
	private String oper;

	/**
	 * @see AParametro#AParametro()
	 */
	public ParametroFormula() {
		super();
	}

	/**
	 * @see AParametro#AParametro(String, Serializable)
	 */
	public ParametroFormula(String campo, Number valor) {
		this(campo, valor.toString());
	}

	public ParametroFormula(String campo, String campo2, Number valor) {
		this(campo, campo2, valor.toString());
	}
	
	/**
	 * @see AParametro#AParametro(String, String)
	 */
	public ParametroFormula(String campo, String valor) {
		this(campo, campo, valor);
	}

	public ParametroFormula(String campo, String campo2, String valor) {
		this.campo = campo;
		this.campo2 = campo2;
		setValorString(valor);
	}

	@Override
	public String getSql() throws ParametroException {
		String c1 = prefixo + campo;
		String c2 = prefixo + campo2;
		return c1 + " = " + c2 + oper + getCampoId();
	}

	public void setValorString(String valor) {
		String op = valor.substring(0, 1);
		if(!(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/"))){
			op = "+";
		}
		valor = valor.replace(op, "");
		oper = op + ":";
		super.setValorString(valor);
	}

	public String getCampo2() {
		return campo2;
	}

	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}

}
