package br.com.opensig.core.client.controlador.comando;

import java.io.Serializable;

/**
 * Enumerador que identifica o modo de solicitacao.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public enum EModo implements Serializable {

	/**
	 * Quando e uma acao para executar na listagem.
	 */
	LISTAGEM,
	/**
	 * Quando e uma acao para executar em um registro.
	 */
	REGISTRO
}
