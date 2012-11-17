package br.com.opensig.core.shared.modelo;

import java.io.Serializable;
import java.util.List;

import br.com.opensig.core.client.controlador.filtro.IFiltro;

/**
 * Classe que representa a exportacao do registro.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ExpRegistro<E extends Dados> implements Serializable {

	private String nome;
	private E classe;
	private IFiltro filtro;
	private List<ExpMeta> metadados;
	private List<ExpListagem> expLista;

	/**
	 * Construtor padrao.
	 */
	public ExpRegistro() {
	}

	/**
	 * Construtor com os campos inicializados.
	 * 
	 * @param nome
	 *            o nome do tipo de dados da exportacao.
	 * @param classe
	 *            o tipo de dados para exportacao.
	 * @param metadados
	 *            a lista de metadados dos campos.
	 * @param filtro
	 *            o filtro que seleciona o registro.
	 * @param expLista
	 *            as sub listagens caso exista.
	 */
	public ExpRegistro(String nome, E classe, List<ExpMeta> metadados, IFiltro filtro, List<ExpListagem> expLista) {
		this.nome = nome;
		this.classe = classe;
		this.metadados = metadados;
		this.filtro = filtro;
		this.expLista = expLista;
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

	public List<ExpListagem> getExpLista() {
		return expLista;
	}

	public void setExpLista(List<ExpListagem> expLista) {
		this.expLista = expLista;
	}

}
