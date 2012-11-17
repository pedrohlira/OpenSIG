package br.com.opensig.core.server;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Classe que representa a conexão com o banco de dados.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Conexao extends HttpServlet {

	private static Map<String, String> dados = new HashMap<String, String>();

	@Override
	public void init() throws ServletException {
		Enumeration<String> param = getInitParameterNames();
		for (; param.hasMoreElements();) {
			String nome = param.nextElement();
			String valor = getInitParameter(nome);
			dados.put(nome, valor);
		}
	}
	
	/**
	 * Metodo que retorna uma instancia do manipulador de entidades.
	 * 
	 * @param pu
	 *            o nome da unidade de persistência que deseja utilizar.
	 * @return um objeto que manipula as entidades no banco de dados.
	 * @throws NullPointerException
	 *             pode ocorrer esta exceção caso o nome passado não exista.
	 */
	public static EntityManagerFactory getInstancia(String pu) throws NullPointerException {
		return getInstancia(pu, dados);
	}

	/**
	 * Metodo que retorna uma instancia do manipulador de entidades.
	 * 
	 * @param pu
	 *            o nome da unidade de persistência que deseja utilizar.
	 * @param dados
	 *            conjunto de informacoes para acesso ao banco.
	 * @return um objeto que manipula as entidades no banco de dados.
	 * @throws NullPointerException
	 *             pode ocorrer esta exceção caso o nome passado não exista.
	 */
	public static EntityManagerFactory getInstancia(String pu, Map<String, String> dados) {
		return Persistence.createEntityManagerFactory(pu, dados);
	}
}
