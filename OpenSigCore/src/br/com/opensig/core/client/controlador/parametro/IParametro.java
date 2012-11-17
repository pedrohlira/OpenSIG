package br.com.opensig.core.client.controlador.parametro;

import java.io.Serializable;
import java.util.Collection;

/**
 * Interface que define os parametros utilizados nos comandos.
 * 
 * @param <E>
 *            recebe como genérico uma classe serializável.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface IParametro<E extends Serializable> extends Serializable {

	/**
	 * Metodo que retorna o nome do campo único para uso na interaçao de valores.
	 * 
	 * @return o nome do campo único.
	 */
	public String getCampoId();

	/**
	 * Metodo que retorna o prefixo do campo na tabela usado pelo filtro.
	 * 
	 * @return o prefixo do campo.
	 */
	public String getCampoPrefixo();

	/**
	 * Metodo que define o prefixo do campo na tabela usado pelo filtro.
	 * 
	 * @param prefixo
	 *            o prefixo dele.
	 */
	public void setCampoPrefixo(String prefixo);

	/**
	 * Metodo que retorna o nome do campo na tabela usado pelo filtro.
	 * 
	 * @return o nome do campo.
	 */
	public String getCampo();

	/**
	 * Metodo que define o nome do campo na tabela usado pelo filtro.
	 * 
	 * @param campo
	 *            o nome dele.
	 */
	public void setCampo(String campo);

	/**
	 * Metodo que retorna o valor do tipo genérico informado usado pelo filtro.
	 * 
	 * @return o valor do filtro.
	 */
	public E getValor();

	/**
	 * Metodo que define o valor do tipo genérico informado usado pelo filtro.
	 * 
	 * @param valor
	 *            o conteudo dele
	 */
	public void setValor(E valor);

	/**
	 * Metodo que define o valor do tipo genérico informado usado pelo filtro.
	 * 
	 * @param valor
	 *            o conteudo dele no tipo String.
	 */
	public void setValorString(String valor);
	
	/**
	 * Metodo que retorna os parâmetros inbutidos de forma recursiva.
	 * 
	 * @return uma coleçao de parâmetros tipados e definidos.
	 */
	public Collection<IParametro<E>> getParametro();

	/**
	 * Metodo que retorna os parêmetros inbutidos de forma recursiva.
	 * 
	 * @param parametro
	 *            o objeto a ser usado para pesquisa dos parâmetros.
	 * @return uma coleçao de parâmetros tipados e definidos.
	 */
	public Collection<IParametro<E>> getParametro(IParametro<E> parametro);
	
	/**
	 * Metodo que retorna a instruçao SQL em formato JPA.
	 * 
	 * @return a instruçao no formato JQL.
	 * @throws ParametroException
	 *             dispara uma exceçao caso a formataçao esteja errada.
	 */
	public String getSql() throws ParametroException;
}
