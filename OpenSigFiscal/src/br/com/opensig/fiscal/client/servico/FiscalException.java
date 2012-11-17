package br.com.opensig.fiscal.client.servico;

import br.com.opensig.core.client.servico.CoreException;

/**
 * Classe que representa uma exceção de persistência do sistema.
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/10/2008
 *
 */
public class FiscalException extends CoreException {
    private static final long serialVersionUID = -5203345892627186453L;

    /**
     * @see CoreException#CoreException
     */
    public FiscalException() {
        super();
    }

    /**
     * @see CoreException#CoreException(String message)
     */
    public FiscalException(String message) {
        super(message);
    }

    /**
     * @see CoreException#CoreException(String message, Throwable cause)
     */
    public FiscalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see CoreException#CoreException(Throwable cause)
     */
    public FiscalException(Throwable cause) {
        super(cause);
    }
}
