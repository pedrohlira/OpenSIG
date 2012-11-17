package br.com.opensig.core.client.servico;

import java.util.List;
import java.util.Map;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que padroniza as importacoes dos dados de forma assincrona.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface ImportacaoServiceAsync<E extends Dados> extends CoreServiceAsync<E> {

	/**
	 * @see ImportacaoService#importar(SisExpImp, List)
	 * @param asyncCallback
	 *            um objeto assincrono com inteiro.
	 */
	public abstract void importar(SisExpImp modo, List<String> arquivos, AsyncCallback<Map<String, List<E>>> asyncCallback);

}
