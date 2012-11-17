package br.com.opensig.fiscal.shared.modelo;

import java.io.Serializable;

public enum ENotaOriem implements Serializable {

	/**
	 * Quando o CNPJ do emitente é igual ao da empresa
	 */
	EMISSOR,
	/**
	 * Quando o CNPJ do destinatário é igual ao da empresa
	 */
	DESTINATARIO,
	/**
	 * Quando não é identificado o CNPJ com o da empresa
	 */
	NENHUM;
}
