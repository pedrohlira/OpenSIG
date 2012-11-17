package br.com.opensig.financeiro.shared.modelo;

import java.io.Serializable;

public enum EAcao implements Serializable {
	/**
	 * Acao de quitar um pagamento ou recebimeto.
	 */
	QUITAR,
	/**
	 * Acao de estornar um pagamento ou recebimeto.
	 */
	ESTORNAR

}
