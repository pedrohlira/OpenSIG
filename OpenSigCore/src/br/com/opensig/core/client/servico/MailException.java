package br.com.opensig.core.client.servico;

/**
 * Classe que representa uma exceção de email do sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * 
 */
public class MailException extends CoreException {
	private static final long serialVersionUID = -5203345892627186453L;

	/**
	 * @see CoreException#CoreException
	 */
	public MailException() {
		super();
	}

	/**
	 * @see CoreException#CoreException(String message)
	 */
	public MailException(String message) {
		super(message);
	}

	/**
	 * @see CoreException#CoreException(String message, Throwable cause)
	 */
	public MailException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see CoreException#CoreException(Throwable cause)
	 */
	public MailException(Throwable cause) {
		super(cause);
	}
}
