package br.com.opensig.core.shared.modelo;

import java.io.Serializable;

import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.IParametro;

/**
 * Classe de abstrae os dados das classes POJOs que representam os dados das tabelas.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */

public class Sql implements Serializable {

	private Dados classe;
	private EComando comando;
	private IParametro parametro;
	private IFiltro filtro;

	/**
	 * Construtor padrão.
	 */
	public Sql() {
		this(null, null);
	}

	/**
	 * Construtor que define as valores padrões de cada classe POJO.
	 * 
	 * @param classe
	 *            a classe de dados.
	 * @param comando
	 *            o comando a ser executado.
	 */
	public Sql(Dados classe, EComando comando) {
		this(classe, comando, null, null);
	}

	/**
	 * Construtor que define as valores padrões de cada classe POJO.
	 * 
	 * @param classe
	 *            a classe de dados.
	 * @param comando
	 *            o comando a ser executado.
	 * @param filtro
	 *            o filtro utilizado pelo comando para agir somente nos registros especificos.
	 */
	public Sql(Dados classe, EComando comando, IFiltro filtro) {
		this(classe, comando, filtro, null);
	}
	
	/**
	 * Construtor que define as valores padrões de cada classe POJO.
	 * 
	 * @param classe
	 *            a classe de dados.
	 * @param comando
	 *            o comando a ser executado.
	 * @param filtro
	 *            o filtro utilizado pelo comando para agir somente nos registros especificos.
	 * @param parametro
	 *            o parametro utilizado para atualização dos dados.
	 */
	public Sql(Dados classe, EComando comando, IFiltro filtro, IParametro parametro) {
		this.classe = classe;
		this.comando = comando;
		this.filtro = filtro;
		this.parametro = parametro;
	}

	/**
	 * Metodo que define o dado de persistência.
	 * 
	 * @param classe
	 *            o dado de persistência.
	 */
	public void setClasse(Dados classe) {
		this.classe = classe;
	}

	/**
	 * Metodo que retorna o dado de persistência.
	 * 
	 * @return a classe.
	 */
	public Dados getClasse() {
		return classe;
	}

	/**
	 * Metodo que retorna o comando executado.
	 * 
	 * @return o comando.
	 */
	public EComando getComando() {
		return comando;
	}

	/**
	 * Metodo que define o comando a ser executado.
	 * 
	 * @param comando
	 *            o comando.
	 */
	public void setComando(EComando comando) {
		this.comando = comando;
	}

	/**
	 * Metodo que retorna o filtro utilizado.
	 * 
	 * @return o filtro.
	 */
	public IFiltro getFiltro() {
		return filtro;
	}

	/**
	 * Metodo que define o filtro a ser utilizado.
	 * 
	 * @param filtro
	 *            o filtro.
	 */
	public void setFiltro(IFiltro filtro) {
		this.filtro = filtro;
	}

	/**
	 * Metodo que retorna o parametro utilizado.
	 * 
	 * @return o parametro.
	 */
	public IParametro getParametro() {
		return parametro;
	}

	/**
	 * Metodo que define o parametro a ser utilizado.
	 * 
	 * @param parametro
	 *            o parametro.
	 */
	public void setParametro(IParametro parametro) {
		this.parametro = parametro;
	}

}
