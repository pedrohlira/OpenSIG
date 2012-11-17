package br.com.opensig.core.client.padroes;

/**
 * Interface que generaliza os objetos que são visitados.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface Visitable {

	/**
	 * Metodo que aceita um visitador para seus objetos.
	 * 
	 * @param visitor
	 *            Um visitador que irá manipular os objetos de um forma
	 *            específica.
	 */
	public void accept(Visitor visitor);
}
