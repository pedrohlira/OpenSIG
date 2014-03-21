package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Anexo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que padroniza o envio de email.
 * 
 * @author Pedro H. Lira
 */
public interface MailServiceAsync {

	/**
	 * @see MailService#enviarEmail(String, String, String, Anexo[])
	 * @param asyncCallback
	 *            retorno assincrono.
	 */
	public abstract void enviarEmail(String para, String assunto, String mensagem, Anexo[] anexos, AsyncCallback asyncCallback);
}
