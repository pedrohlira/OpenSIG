package br.com.opensig.core.shared.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Classe que serve como modelo de dados para conter a coleçao de registros e a
 * quantidade original sem paginaçao.
 * 
 * @param <E>
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Lista<E extends Dados> implements Serializable {

	private List<E> lista;
	private String[][] dados;
	private int total;

	/**
	 * Metodo que retorna o total de registros sem paginaçao.
	 * 
	 * @return um inteiro.
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Metodo que define o total de registros sem paginaçao.
	 * 
	 * @param total
	 *            um inteiro.
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Metodo que retorna a coleçao dos objetos
	 * 
	 * @return uma coleçao de objetos do tipo definido.
	 */
	public List<E> getLista() {
		return lista;
	}

	/**
	 * Metodo que define a coleçao de objetos.
	 * 
	 * @param lista
	 *            uma coleçao de objetos do tipo definido.
	 */
	public void setLista(List<E> lista) {
		this.lista = lista;
	}

	/**
	 * Metodo que retorna os dados em forma de matriz.
	 * 
	 * @return uma matriz de Strings.
	 */
	public String[][] getDados() {
		return dados;
	}

	/**
	 * Metodo que define a matriz de dados.
	 * 
	 * @param dados
	 *            uma matriz de Strings.
	 */
	public void setDados(String[][] dados) {
		this.dados = dados;
	}

}
