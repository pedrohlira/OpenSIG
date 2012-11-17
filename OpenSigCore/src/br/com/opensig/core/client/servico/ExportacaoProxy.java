package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Classe que padroniza a exportacao dos dados.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ExportacaoProxy<E extends Dados> extends CoreProxy<E> implements ExportacaoServiceAsync<E> {

	private static final ExportacaoServiceAsync async = (ExportacaoServiceAsync) GWT.create(ExportacaoService.class);
	private static final ServiceDefTarget sdf = (ServiceDefTarget) async;

	public ExportacaoProxy() {
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "ExportacaoService");
	}

	@Override
	public void exportar(SisExpImp modo, ExpListagem<E> expLista, AsyncCallback<String> asyncCallback) {
		async.exportar(modo, expLista, asyncCallback);
	}

	@Override
	public void exportar(SisExpImp modo, ExpRegistro<E> expRegistro, AsyncCallback<String> asyncCallback) {
		async.exportar(modo, expRegistro, asyncCallback);
	}

	@Override
	public void exportar(String arquivo, String nome, AsyncCallback<String> asyncCallback) {
		async.exportar(arquivo, nome, asyncCallback);
	}
}
