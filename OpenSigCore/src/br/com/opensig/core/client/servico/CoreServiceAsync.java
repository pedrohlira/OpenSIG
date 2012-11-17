package br.com.opensig.core.client.servico;

import java.util.Collection;

import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que padroniza a persistencia dos dadosde forma assincrona.
 * 
 * @param <E>
 *            a tipagem do objeto a ser persistido um POJO.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface CoreServiceAsync<E extends Dados> {

	/**
	 * @see CoreService#selecionar(Dados, int, int, IFiltro, boolean)
	 * 
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void selecionar(Dados classe, int inicio, int limite, IFiltro filtro, boolean dependencia, AsyncCallback<Lista<E>> asyncCallback);

	/**
	 * @see CoreService#selecionar(Dados, IFiltro, boolean)
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void selecionar(Dados classe, IFiltro filtro, boolean dependencia, AsyncCallback<E> asyncCallback);

	/**
	 * @see CoreService#buscar(Dados, String, EBusca, IFiltro)
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void buscar(Dados classe, String campo, EBusca busca, IFiltro filtro, AsyncCallback<Number> asyncCallback);

	/**
	 * @see CoreService#buscar(Dados, String, String, String, String, EBusca, EDirecao, IFiltro)
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void buscar(Dados classe, String campoX, String campoSubX, String grupoX, String campoY, EBusca busca, EDirecao direcao, IFiltro filtro,
			AsyncCallback<Collection<String[]>> asyncCallback);

	/**
	 * @see CoreService#salvar(Collection)
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void salvar(Collection<E> unidades, AsyncCallback<Collection<E>> asyncCallback);

	/**
	 * @see CoreService#salvar(Dados)
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void salvar(E unidade, AsyncCallback<E> asyncCallback);

	/**
	 * @see CoreService#deletar(Collection)
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void deletar(Collection<E> unidades, AsyncCallback<Collection<E>> asyncCallback);

	/**
	 * @see CoreService#deletar(Dados)
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo do POJO.
	 */
	public abstract void deletar(E unidade, AsyncCallback<E> asyncCallback);

	/**
	 * @see CoreService#executar(Sql[])
	 * @param asyncCallback
	 *            um objeto assincrono com inteiros.
	 */
	public abstract void executar(Sql[] sqls, AsyncCallback<Integer[]> asyncCallback);

	/**
	 * @see CoreService#executar(String)
	 * @param asyncCallback
	 *            um objeto assincrono com inteiros.
	 */
	public abstract void executar(String sql, AsyncCallback<Integer> asyncCallback);
	
	/**
	 * @see CoreService#getAuth()
	 * @param asyncCallback
	 *            um objeto assincrono da autenticacao.
	 */
	public abstract void getAuth(AsyncCallback<Autenticacao> asyncCallback);
}
