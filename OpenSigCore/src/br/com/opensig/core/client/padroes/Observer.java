package br.com.opensig.core.client.padroes;

/**
 * Interface que define um observador, para receber notificações.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface Observer {

	/**
	 * Metodo que é disparado quando o objeto observado é modificado.
	 * 
	 * @param o
	 *            objeto observado.
	 * @param arg
	 *            paramêtro opcional de passagem.
	 */
	void update(Observable o, Object arg);
}
