package br.com.opensig.core.client.visao;

import br.com.opensig.core.shared.modelo.ILogin;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que habilita a permissao a uma funcao ou acao por outro usuario,
 * PermitirSistemaImpl implementa esta funcionalidade.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface PermitirSistema {

	/**
	 * Metodo que ativa a execucao.
	 * 
	 * @param async
	 *            uma funcao retorno com tipo de Login.
	 */
	public void executar(AsyncCallback<ILogin> async);

	/**
	 * Metodo que define a mensagem a ser mostrada ao usuario.
	 * 
	 * @param info
	 *            texto a ser mostrado.
	 */
	public void setInfo(String info);
}
