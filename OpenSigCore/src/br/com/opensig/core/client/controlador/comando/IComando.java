package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

/**
 * Interface que generaliza as ações do sistema em forma de comando.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IComando {

	/**
	 * Metodo que executa a acao.
	 * 
	 * @param contexto
	 *            o contexto da acao.
	 */

	public void execute(Map contexto);

	/**
	 * Metodo que recupera o proximo comando.
	 * 
	 * @return proximo comando.
	 */
	public IComando getProximo();

	/**
	 * Metodo que define o próximo comando.
	 * 
	 * @param comando
	 *            próximo comando.
	 */
	public void setProximo(IComando comando);

}
