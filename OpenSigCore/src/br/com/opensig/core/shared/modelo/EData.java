package br.com.opensig.core.shared.modelo;

import java.io.Serializable;

/**
 * Enumerador que define os meios agrupamento de datas.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public enum EData implements Serializable {

	/**
	 * Campo que define como DIA o grupo.
	 */
	DIA,
	/**
	 * Campo que define como MES o grupo.
	 */
	MES,
	/**
	 * Campo que define como ANO o grupo.
	 */
	ANO;
}