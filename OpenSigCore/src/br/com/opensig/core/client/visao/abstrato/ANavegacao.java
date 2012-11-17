package br.com.opensig.core.client.visao.abstrato;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.padroes.Visitor;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;

/**
 * Classe que abstrai a construçao de um comando usando a soluçao via simulaçao
 * de Reflection.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public abstract class ANavegacao implements Visitor {

	/**
	 * Campo que define em formato de tabela em memoria os módulos lidos do xml.
	 */
	public static Store MODULOS;
	/**
	 * Campo que define em formato de tabela em memoria as funções lidas do xml.
	 */
	public static Store FUNCOES;
	/**
	 * Campo que define em formato de tabela em memoria as ações lidas do xml.
	 */
	public static Store ACOES;

	/**
	 * Metodo que filtra os dados de acordo com sua classe e retorna o registro.
	 * 
	 * @param store
	 *            massa de dados a ser filtrada.
	 * @param classe
	 *            a classe que representa o objeto.
	 * @return o registro do filtrado.
	 */
	public static Record getRegistro(Store store, String classe) {
		if (store == null || classe == null || classe.equals("")) {
			return null;
		} else {
			return UtilClient.getRegistro(store, "classe", classe);
		}
	}
}