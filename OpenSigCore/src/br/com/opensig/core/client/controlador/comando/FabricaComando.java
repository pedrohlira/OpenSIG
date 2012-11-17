package br.com.opensig.core.client.controlador.comando;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Classe que disponibiliza uma soluçao para a falta de Reflection e fornece as
 * classes de comando via seus nomes.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class FabricaComando {

	private static final FabricaComando fb = new FabricaComando();
	private Map<String, IComando> comandos = new HashMap<String, IComando>();

	private FabricaComando() {
	}

	/**
	 * Metodo que retorna a instancia da fábrica.
	 * 
	 * @return uma fábrica de comandos.
	 */
	public static FabricaComando getInstancia() {
		return fb;
	}

	/**
	 * Metodo que adiciona uma classe de comando refenciada por uma nome.
	 * 
	 * @param nome
	 *            da classe de comando.
	 * @param comando
	 *            o comando propriamente dito
	 */
	public void addComando(String nome, IComando comando) {
		comandos.put(nome, comando);
	}

	/**
	 * Metodo que retorna uma classe de comando informado o nome.
	 * 
	 * @param nome
	 *            da classe que representa o comando.
	 * @return o comando propriemente dito.
	 */
	public IComando getComando(String nome) {
		return comandos.get(nome);
	}

	/**
	 * Metodo que recupera o nome completo do comando passando o final dele.
	 * 
	 * @param sufixo
	 *            o nome final do comando somente a classe sem o package.
	 * @return o nome completo do comando package + classe.
	 */
	public String getComandoCompleto(String sufixo) {
		String nome = null;
		for (Entry<String, IComando> com : comandos.entrySet()) {
			if (com.getKey().endsWith(sufixo)) {
				nome = com.getKey();
				break;
			}
		}
		return nome;
	}
}
