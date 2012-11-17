package br.com.opensig.core.shared.modelo;

import java.io.Serializable;
import java.util.List;

import br.com.opensig.core.client.controlador.filtro.IFiltro;

/**
 * Classe que representa a exportacao da listagem.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ExpListagem<E extends Dados> implements Serializable {

	private String nome;
	private E classe;
	private IFiltro filtro;
	private List<ExpMeta> metadados;

	/**
	 * Construtor padrao.
	 */
	public ExpListagem() {
	}

	/**
	 * Construtor com os campos inicializados.
	 * 
	 * @param nome
	 *            o texto que apresenta no topo da exportacao.
	 * @param classe
	 *            o tipo de dados para exportacao.
	 * @param filtro
	 *            o filtro que seleciona o registro.
	 * @param metadados
	 *            a lista de metadados dos campos.
	 */
	public ExpListagem(String nome, E classe, List<ExpMeta> metadados, IFiltro filtro) {
		this.nome = nome;
		this.classe = classe;
		this.metadados = metadados;
		this.filtro = filtro;
	}

	// Gets e Seteres

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public E getClasse() {
		return classe;
	}

	public void setClasse(E classe) {
		this.classe = classe;
	}

	public List<ExpMeta> getMetadados() {
		return metadados;
	}

	public void setMetadados(List<ExpMeta> metadados) {
		this.metadados = metadados;
	}

	public IFiltro getFiltro() {
		return filtro;
	}

	public void setFiltro(IFiltro filtro) {
		this.filtro = filtro;
	}

}
