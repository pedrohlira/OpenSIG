package br.com.opensig.core.server;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import br.com.opensig.core.shared.modelo.Autenticacao;

/**
 * Classe que controla as sessoes logadas no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class SessionManager implements HttpSessionListener {

	/**
	 * Variavel a nivel de aplicacao que armazena os usuarios logadas.
	 */
	public static final Map<HttpSession, Autenticacao> LOGIN = new HashMap<HttpSession, Autenticacao>();

	/**
	 * Ao cria a sessao adiciona ao controle.
	 */
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		if (!LOGIN.containsKey(arg0.getSession())) {
			LOGIN.put(arg0.getSession(), null);
		}
	}

	/**
	 * Ao destruir a sessao remove do controle.
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		LOGIN.remove(arg0.getSession());
	}

	/**
	 * Limpa a sessao e remove o login.
	 * 
	 * @param sessao
	 *            a sessao atual usada.
	 */
	public static final void destruir(HttpSession sessao) {
		sessao.invalidate();
		LOGIN.remove(sessao);
	}
}
