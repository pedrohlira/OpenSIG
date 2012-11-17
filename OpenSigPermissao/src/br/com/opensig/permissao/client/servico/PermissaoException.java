package br.com.opensig.permissao.client.servico;

import br.com.opensig.core.client.servico.CoreException;

/**
 * Classe que representa uma exceção da classe SisFavorito do sistema.
 * @author Pedro H. Lira
 * @version 1.0
 * @since 27/08/2008
 */
public class PermissaoException extends CoreException {

    private static final long serialVersionUID = -83928617215989099L;

    /**
     * @see CoreException#CoreException()
     */
    public PermissaoException() {
        super();
    }

    /**
     * @see CoreException#CoreException(String)
     */
    public PermissaoException(String message) {
        super(message);
    }

    /**
     * @see CoreException#CoreException(String, Throwable)
     */
    public PermissaoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see CoreException#CoreException(Throwable)
     */
    public PermissaoException(Throwable cause) {
        super(cause);
    }
}
