package br.com.opensig.core.client.servico;

/**
 * Classe que representa uma exceçao geral do sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class OpenSigException extends Exception {

	private static final long serialVersionUID = 2427145912383590227L;

	/**
	 * Contrutor padrao com mensagem e falha NULL.
	 */
	public OpenSigException() {
		super();
	}

	/**
	 * Contrutor que recebe o evento da falha original.
	 * 
	 * @param cause
	 *            Falha original.
	 */
	public OpenSigException(Throwable cause) {
		super(cause);
	}

	/**
	 * Contrutor que recebe uma mensagem e o evento da falha original.
	 * 
	 * @param message
	 *            String com texto adicional.
	 * @param cause
	 *            Falha original.
	 */
	public OpenSigException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Contrutor que recebe uma mensagem adicional.
	 * 
	 * @param message
	 *            String com texto adicional.
	 */
	public OpenSigException(String message) {
		super(message);
	}
}
