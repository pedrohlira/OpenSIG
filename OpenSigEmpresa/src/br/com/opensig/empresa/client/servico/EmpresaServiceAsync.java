package br.com.opensig.empresa.client.servico;

import br.com.opensig.core.client.servico.CoreServiceAsync;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EmpresaServiceAsync<E extends Dados> extends CoreServiceAsync<E> {

	public abstract void salvar(E dados, AsyncCallback<E> asyncCallback);

}
