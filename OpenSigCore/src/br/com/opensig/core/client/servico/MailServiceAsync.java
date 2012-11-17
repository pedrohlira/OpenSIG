package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Anexo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que padroniza o envio de email.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface MailServiceAsync {

	/**
	 * @see MailService#enviarEmail(String, String, String, String)
	 * @param asyncCallback
	 *            retorno assincrono.
	 */
	public abstract void enviarEmail(String de, String para, String assunto, String mensagem, AsyncCallback asyncCallback);

	/**
	 * @see MailService#enviarEmail(String, String, String, String, Anexo[])
	 * @param asyncCallback
	 *            retorno assincrono.
	 */
	public abstract void enviarEmail(String de, String para, String assunto, String mensagem, Anexo[] anexos, AsyncCallback asyncCallback);

	/**
	 * @see MailService#enviarEmail(String, String, String, String, String, String, Anexo[])
	 * @param asyncCallback
	 *            retorno assincrono.
	 */
	public abstract void enviarEmail(String de, String para, String copia, String oculto, String assunto, String mensagem, Anexo[] anexos, AsyncCallback asyncCallback);
}
