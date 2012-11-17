package br.com.opensig.financeiro.shared.modelo;

import java.io.Serializable;

public enum EOperacao implements Serializable {
	/**
	 * Acao de pagar uma entidade.
	 */
	PAGAR,
	/**
	 * Acao de receber de uma entidade.
	 */
	RECEBER

}
