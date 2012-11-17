/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.Sql;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Classe do comando executar, que faz uma chamada direta ao banco com
 * instrucoes JQL.
 * 
 * @param <E>
 *            o tipo de dados do comando.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoExecutar<E extends Dados> extends AComando<E> {

	private Sql[] sqls;

	/**
	 * @see AComando#AComando()
	 */
	public ComandoExecutar() {
		super(null);
	}

	/**
	 * Construtor padrao.
	 * 
	 * @param sqls
	 *            um array de objetos Sql que serao traduzidos em instrucoes.
	 */
	public ComandoExecutar(Sql[] sqls) {
		this(null, sqls);
	}

	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoExecutar(IComando proximo) {
		super(proximo);
	}

	/**
	 * Construtor padrao.
	 * 
	 * @param proximo
	 *            o comando a ser executado apos termino das instrucoes.
	 * @param sqls
	 *            um array de objetos Sql que serao traduzidos em instrucoes.
	 */
	public ComandoExecutar(IComando proximo, Sql[] sqls) {
		super(proximo);
		this.sqls = sqls;
	}

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto);
		CoreProxy<E> proxy = new CoreProxy<E>(DADOS);
		proxy.executar(sqls, new AsyncCallback<Integer[]>() {

			public void onFailure(Throwable caught) {
				contexto.put("erro", caught);
				new ComandoSalvarFinal().execute(contexto);
			}

			public void onSuccess(Integer[] result) {
				if (comando != null) {
					contexto.put("resultado", result);
					comando.execute(contexto);
				}
			}
		});
	}

	// Gets e Seteres
	
	public Sql[] getSqls() {
		return sqls;
	}

	public void setSqls(Sql[] sqls) {
		this.sqls = sqls;
	}

}
