package br.com.opensig.financeiro.client.servico;

import br.com.opensig.core.client.servico.CoreException;

/**
 * Classe que representa uma exceção de persistência do sistema.
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/10/2008
 *
 */
public class FinanceiroException extends CoreException {

    /**
     * @see CoreException#CoreException
     */
    public FinanceiroException() {
        super();
    }

    /**
     * @see CoreException#CoreException(String message)
     */
    public FinanceiroException(String message) {
        super(message);
    }

    /**
     * @see CoreException#CoreException(String message, Throwable cause)
     */
    public FinanceiroException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see CoreException#CoreException(Throwable cause)
     */
    public FinanceiroException(Throwable cause) {
        super(cause);
    }
}
