package br.com.opensig.permissao.client.servico;

import br.com.opensig.core.client.servico.CoreServiceAsync;
import br.com.opensig.core.shared.modelo.Autenticacao;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que define o login no sistema de forma assincrona.
 * 
 * @author Pedro H. Lira
 * @since 14/04/2009
 * @version 1.0
 */
public interface PermissaoServiceAsync extends CoreServiceAsync {

	/**
	 * @param asyncCallback
	 *            um objeto assincrono do mesmo tipo SisUsuario.
	 * @see PermissaoService#entrar(String, String, String, int, boolean)
	 */
	public abstract void entrar(String usuario, String senha, String captcha, int empresa, boolean permissao, AsyncCallback<Autenticacao> asyncCallback);

	/**
	 * @param asyncCallback
	 *            um objeto assincrono nulo.
	 * @see PermissaoService#sair()
	 */
	public abstract void sair(AsyncCallback asyncCallback);

	/**
	 * @param asyncCallback
	 *            um objeto assincrono boolean.
	 * @see PermissaoService#isLogado()
	 */
	public abstract void isLogado(AsyncCallback<Boolean> asyncCallback);

	/**
	 * @param asyncCallback
	 *            um objeto assincrono nulo.
	 * @see PermissaoService#bloquear(boolean)
	 */
	public abstract void bloquear(boolean bloqueio, AsyncCallback asyncCallback);

	/**
	 * @param asyncCallback
	 *            um objeto assincrono nulo.
	 * @see PermissaoService#recuparSenha(String)
	 */
	public abstract void recuperarSenha(String email, AsyncCallback asyncCallback);
}
