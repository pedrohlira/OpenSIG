package br.com.opensig.core.shared.modelo;

import java.io.Serializable;

/**
 * Enumerador que define os comando de sql.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public enum EComando implements Serializable {

	/**
	 * Campo que define atualizaçao.
	 */
	ATUALIZAR,
	/**
	 * Campo que define exclusao.
	 */
	EXCLUIR;
}