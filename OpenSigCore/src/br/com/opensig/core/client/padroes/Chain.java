package br.com.opensig.core.client.padroes;

import br.com.opensig.core.client.servico.OpenSigException;

/**
 * Classe que utiliza o padrao de projeto Chain Of Responsability
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class Chain {

	/**
	 * Proximo executor da fila.
	 */
	protected Chain next;

	/**
	 * Construtor que define o proximo executor da fila.
	 * 
	 * @param next
	 *            o objeto do proximo executir da fila, informa null se for o
	 *            ultimo.
	 * @throws OpenSigException
	 *             caso ocorra alguma excecao.
	 */
	public Chain(Chain next) throws OpenSigException {
		this.next = next;
	}

	/**
	 * Metodo que ativa a execucao do executor.
	 * 
	 * @throws OpenSigException
	 *             caso ocorra alguma excecao.
	 */
	public abstract void execute() throws OpenSigException;

	// Gets e Seteres

	public Chain getNext() {
		return next;
	}

	public void setNext(Chain next) throws OpenSigException {
		this.next = next;
	}

}
