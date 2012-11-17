package br.com.opensig.core.client.servico;

/**
 * Classe que representa uma exceção de persistência do sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * 
 */
public class CoreException extends OpenSigException {
	private static final long serialVersionUID = -5203345892627186453L;

	/**
	 * @see OpenSigException#OpenSigException
	 */
	public CoreException() {
		super();
	}

	/**
	 * @see OpenSigException#OpenSigException(String message)
	 */
	public CoreException(String message) {
		super(message);
	}

	/**
	 * @see OpenSigException#OpenSigException(String message, Throwable cause)
	 */
	public CoreException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see OpenSigException#OpenSigException(Throwable cause)
	 */
	public CoreException(Throwable cause) {
		super(cause);
	}
}
