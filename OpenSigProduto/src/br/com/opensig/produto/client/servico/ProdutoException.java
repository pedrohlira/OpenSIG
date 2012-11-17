package br.com.opensig.produto.client.servico;

import br.com.opensig.core.client.servico.CoreException;

/**
 * Classe que representa uma exceçao do modulo comercial no sistema.
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/10/2008
 *
 */
public class ProdutoException extends CoreException {

    /**
     * @see CoreException#CoreException
     */
    public ProdutoException() {
        super();
    }

    /**
     * @see CoreException#CoreException(String message)
     */
    public ProdutoException(String message) {
        super(message);
    }

    /**
     * @see CoreException#CoreException(String message, Throwable cause)
     */
    public ProdutoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see CoreException#CoreException(Throwable cause)
     */
    public ProdutoException(Throwable cause) {
        super(cause);
    }
}
