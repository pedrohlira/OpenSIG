package br.com.opensig.empresa.client.servico;

import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class EmpresaProxy<E extends Dados> extends CoreProxy<E> implements EmpresaServiceAsync<E> {

	private EmpresaServiceAsync async = (EmpresaServiceAsync) GWT.create(EmpresaService.class);
	private ServiceDefTarget sdf = (ServiceDefTarget) async;

	public EmpresaProxy() {
		this(null);
	}
	
	public EmpresaProxy(E classe) {
		super.classe = classe;
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "EmpresaService");
	}

	@Override
	public void salvar(E dados, AsyncCallback<E> asyncCallback) {
		async.salvar(dados, asyncCallback);

	}
}
