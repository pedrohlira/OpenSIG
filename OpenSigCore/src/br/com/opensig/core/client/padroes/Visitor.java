package br.com.opensig.core.client.padroes;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Interface que generaliza as classes visitadoras de outros objetos.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface Visitor {

	/**
	 * Mátodo que recebe um Array de objetos para manipular de acordo com a
	 * classe concreta.
	 * 
	 * @param dados
	 *            uma coleção de dados.
	 */
	public void visit(Dados[] dados);
}
