package br.com.opensig.core.client.controlador.parametro;

import br.com.opensig.core.client.servico.CoreException;

/**
 * Classe que representa uma exceçao de parametro.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ParametroException extends CoreException {

	private static final long serialVersionUID = 1L;

	/**
	 * @see CoreException#CoreException(String)
	 */
	public ParametroException(String message) {
		super(message);
	}

	/**
	 * @see CoreException#CoreException(String, Throwable)
	 */
	public ParametroException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see CoreException#CoreException(Throwable)
	 */
	public ParametroException(Throwable cause) {
		super(cause);
	}

	/**
	 * @see CoreException#CoreException()
	 */
	public ParametroException() {
		super();
	}
}
