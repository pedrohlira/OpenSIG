package br.com.opensig.core.client.controlador.filtro;

/**
 * Enumerador que define os tipos de comparações usados pelos filtros nas
 * listagens.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public enum ECompara {

	/**
	 * Informa que o valor do campo deve ser <code>=</code> ao valor informado.
	 */
	IGUAL,
	/**
	 * Informa que o valor do campo deve ser <code>!=</code> ao valor informado.
	 */
	DIFERENTE,
	/**
	 * Informa que o valor do campo deve ser <code>></code> ao valor informado.
	 */
	MAIOR,
	/**
	 * Informa que o valor do campo deve ser <code><</code> ao valor informado.
	 */
	MENOR,
	/**
	 * Informa que o valor do campo deve ser <code>>=</code> ao valor informado.
	 */
	MAIOR_IGUAL,
	/**
	 * Informa que o valor do campo deve ser <code><=</code> ao valor informado.
	 */
	MENOR_IGUAL,
	/**
	 * Informa que o valor do campo deve ser <code>LIKE</code> ao valor
	 * informado.
	 */
	CONTEM,
	/**
	 * Informa que o valor do campo deve ser <code>LIKE %</code> ao valor
	 * informado.
	 */
	CONTEM_INICIO,
	/**
	 * Informa que o valor do campo deve ser <code>% LIKE</code> ao valor
	 * informado.
	 */
	CONTEM_FIM,
	/**
	 * Informa que o valor do campo deve ser <code>IS NULL</code>.
	 */
	NULO,
	/**
	 * Informa que o valor do campo deve ser <code>IS EMPTY</code>.
	 */
	VAZIO;

	/**
	 * Metodo que retorna no formato JQL a comparação do enumerador.
	 * 
	 * @return uma string no padrão de JQL.
	 */
	public String toString() {
		switch (this) {
		case IGUAL:
			return "=";
		case DIFERENTE:
			return "<>";
		case MAIOR:
			return ">";
		case MENOR:
			return "<";
		case MAIOR_IGUAL:
			return ">=";
		case MENOR_IGUAL:
			return "<=";
		case NULO:
			return "IS NULL";
		case VAZIO:
			return "IS EMPTY";
		default:
			return "LIKE";
		}
	}

	/**
	 * Metodo que retorna o tipo de comparação baseado no modelo html.
	 * 
	 * @param valor
	 *            formato de html para o tipo de comparação.
	 * @return um tipo de ECompara.
	 */
	public static ECompara toCompara(String valor) {
		if (valor.toLowerCase().equals("lt")) {
			return MENOR;
		} else if (valor.toLowerCase().equals("gt")) {
			return MAIOR;
		} else {
			return IGUAL;
		}
	}
}