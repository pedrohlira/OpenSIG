package br.com.opensig.core.client.controlador.comando.form;

import java.util.Collection;
import java.util.Map;

import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Classe do comando salvar, usada em formularios.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoSalvar<E extends Dados> extends ComandoAcao<E> {

	private Collection<E> registros;

	/**
	 * @see AComando#AComando()
	 */
	public ComandoSalvar() {
		this(new ComandoSalvarFinal());
	}

	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoSalvar(IComando proximo) {
		super(proximo);

		if (proximo == null) {
			throw new IllegalArgumentException("Deve passar o próximo comando!");
		}
	}

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto);
		if (registros != null) {
			CoreProxy<E> proxy = new CoreProxy<E>(DADOS);
			proxy.salvar(registros, new AsyncCallback<Collection<E>>() {

				public void onFailure(Throwable caught) {
					contexto.put("erro", caught);
					comando.execute(contexto);
				}

				public void onSuccess(Collection<E> result) {
					contexto.put("resultado", result);
					comando.execute(contexto);
				}
			});
		} else {
			CoreProxy<E> proxy = new CoreProxy<E>(DADOS);
			proxy.salvar(ASYNC);
		}
	}

	// Gets e Seteres
	
	public Collection<E> getRegistros() {
		return registros;
	}

	public void setRegistros(Collection<E> registros) {
		this.registros = registros;
	}
}
