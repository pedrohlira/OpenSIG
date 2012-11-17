package br.com.opensig.core.client.controlador.filtro;

import java.io.Serializable;

import br.com.opensig.core.client.controlador.parametro.AParametro;

/**
 * Classe que abstrai as implementações do filtro definindos todos os métodos
 * com funcionalidades padronizadas.
 * 
 * @param <E>
 *            usando genérico para tipar o modelo de filtro usado.
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class AFiltro<E extends Serializable> extends AParametro<E> implements IFiltro<E> {

	/**
	 * Construtor padrao.
	 */
	public AFiltro() {
	}

	/**
	 * Construtor que define o campo, a comparaçao e o valor.
	 * 
	 * @param campo
	 *            o nome do campo.
	 * @param compara
	 *            o tipo de comparaçao usada.
	 * @param valor
	 *            o valor do filtro.
	 */
	public AFiltro(String campo, ECompara compara, String valor){
		this.campo = campo;
		this.compara = compara;
		setValorString(valor);
	}

	/**
	 * Construtor que define o campo, a comparaçao e o valor.
	 * 
	 * @param campo
	 *            o nome do campo.
	 * @param compara
	 *            o tipo de comparaçao usada.
	 * @param valor
	 *            o valor do filtro.
	 */
	public AFiltro(String campo, ECompara compara, E valor) {
		this.campo = campo;
		this.compara = compara;
		this.valor = valor;
	}

	@Override
	public ECompara getCompara() {
		return compara;
	}

	@Override
	public void setCompara(ECompara compara) {
		this.compara = compara;
	}
}
