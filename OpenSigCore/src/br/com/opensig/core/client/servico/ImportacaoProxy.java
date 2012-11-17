package br.com.opensig.core.client.servico;

import java.util.List;
import java.util.Map;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Classe que padroniza a importacao dos dados.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ImportacaoProxy<E extends Dados> extends CoreProxy<E> implements ImportacaoServiceAsync<E> {

	private static final ImportacaoServiceAsync async = (ImportacaoServiceAsync) GWT.create(ImportacaoService.class);
	private static final ServiceDefTarget sdf = (ServiceDefTarget) async;

	public ImportacaoProxy() {
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "ImportacaoService");
	}

	@Override
	public void importar(SisExpImp modo, List<String> arquivos, AsyncCallback<Map<String, List<E>>> asyncCallback) {
		async.importar(modo, arquivos, asyncCallback);
	}

}
