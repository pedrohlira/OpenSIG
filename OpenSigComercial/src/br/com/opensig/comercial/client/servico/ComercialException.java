package br.com.opensig.comercial.client.servico;

import br.com.opensig.core.client.servico.CoreException;

/**
 * Classe que representa uma exceçao do modulo comercial no sistema.
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/10/2008
 *
 */
public class ComercialException extends CoreException {
    private static final long serialVersionUID = -5203345892627186453L;

    /**
     * @see CoreException#CoreException
     */
    public ComercialException() {
        super();
    }

    /**
     * @see CoreException#CoreException(String message)
     */
    public ComercialException(String message) {
        super(message);
    }

    /**
     * @see CoreException#CoreException(String message, Throwable cause)
     */
    public ComercialException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see CoreException#CoreException(Throwable cause)
     */
    public ComercialException(Throwable cause) {
        super(cause);
    }
}
