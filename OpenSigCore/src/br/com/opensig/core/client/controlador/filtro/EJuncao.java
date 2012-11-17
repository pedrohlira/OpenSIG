package br.com.opensig.core.client.controlador.filtro;

/**
 * Enumerador que define as junções dos filtros nas condições de filtragem das
 * listagens.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public enum EJuncao {

	/**
	 * Campo do tipo intercçao, onde as condições anterior e posteior devem ser
	 * satisfeitas.
	 */
	E,
	/**
	 * Campo do tipo uniao, onde uma das duas condições anterior ou posterior
	 * deve ser satisfeita.
	 */
	OU;

	/**
	 * Metodo que retorna no formato JQL a junçao do enumerador.
	 * 
	 * @return uma string no padrao de JQL.
	 */
	public String toString() {
		if (this == E) {
			return "AND";
		} else {
			return "OR";
		}
	}
}
