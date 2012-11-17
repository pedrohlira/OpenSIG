package br.com.opensig.core.shared.modelo;

import java.io.Serializable;

/**
 * Enumerador que define os tipos de graficos.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public enum EGrafico implements Serializable {

	/**
	 * Campo que define como Pizza.
	 */
	PIZZA,
	/**
	 * Campo que define como Barra.
	 */
	BARRA,
	/**
	 * Campo que define como Linha.
	 */
	LINHA;
}