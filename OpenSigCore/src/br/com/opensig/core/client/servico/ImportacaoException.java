package br.com.opensig.core.client.servico;

/**
 * Classe que representa uma exceçao de importacao do sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ImportacaoException extends CoreException {

	/**
	 * @see CoreException#CoreException
	 */
	public ImportacaoException() {
		super();
	}

	/**
	 * @see CoreException#CoreException(String message)
	 */
	public ImportacaoException(String message) {
		super(message);
	}

	/**
	 * @see CoreException#CoreException(String message, Throwable cause)
	 */
	public ImportacaoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see CoreException#CoreException(Throwable cause)
	 */
	public ImportacaoException(Throwable cause) {
		super(cause);
	}
}
