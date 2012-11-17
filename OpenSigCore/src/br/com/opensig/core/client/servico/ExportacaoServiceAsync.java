package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que padroniza as exportacoes dos dados de forma assincrona.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface ExportacaoServiceAsync<E extends Dados> extends CoreServiceAsync<E> {

	/**
	 * @see ExportacaoService#exportar(SisExpImp, ExpListagem)
	 * @param asyncCallback
	 *            um objeto assincrono com strings.
	 */
	public abstract void exportar(SisExpImp modo, ExpListagem<E> expLista, AsyncCallback<String> asyncCallback);

	/**
	 * @see ExportacaoService#exportar(SisExpImp, ExpRegistro)
	 * @param asyncCallback
	 *            um objeto assincrono com strings.
	 */
	public abstract void exportar(SisExpImp modo, ExpRegistro<E> expRegistro, AsyncCallback<String> asyncCallback);

	/**
	 * @see ExportacaoService#exportar(String, String)
	 * @param asyncCallback
	 *            um objeto assincrono com strings.
	 */
	public abstract void exportar(String arquivo, String nome, AsyncCallback<String> asyncCallback);
}
