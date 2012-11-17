package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;

import br.com.opensig.core.client.controlador.parametro.IParametro;

/**
 * Interface que define os filtros utilizados nas listagens.
 * 
 * @param <E>
 *            recebe como genérico uma classe serializável.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IFiltro<E extends Serializable> extends IParametro<E> {

	/**
	 * Metodo que retorna o tipo de comparação usada no filtro.
	 * 
	 * @return a comparação usada.
	 */
	public ECompara getCompara();

	/**
	 * Metodo que define o tipo de comparação usada no filtro.
	 * 
	 * @param compara
	 *            o tipo de comparação.
	 */
	public void setCompara(ECompara compara);
}
