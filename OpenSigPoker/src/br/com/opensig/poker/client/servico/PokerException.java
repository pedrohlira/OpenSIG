package br.com.opensig.poker.client.servico;

import br.com.opensig.core.client.servico.CoreException;

/**
 * Classe que representa uma exceção da classe SisFavorito do sistema.
 * @author Pedro H. Lira
 */
public class PokerException extends CoreException {

    /**
     * @see CoreException#CoreException()
     */
    public PokerException() {
        super();
    }

    /**
     * @see CoreException#CoreException(String)
     */
    public PokerException(String message) {
        super(message);
    }

    /**
     * @see CoreException#CoreException(String, Throwable)
     */
    public PokerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see CoreException#CoreException(Throwable)
     */
    public PokerException(Throwable cause) {
        super(cause);
    }
}
