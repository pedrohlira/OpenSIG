package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Anexo;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Interface que padroniza o envio de email.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface MailService extends RemoteService {

	/**
	 * @see #enviarEmail(String, String, String, String, String, String,
	 *      Anexo[])
	 */
	public void enviarEmail(String de, String para, String assunto, String mensagem) throws MailException;

	/**
	 * @see #enviarEmail(String, String, String, String, String, String,
	 *      Anexo[])
	 */
	public void enviarEmail(String de, String para, String assunto, String mensagem, Anexo[] anexos) throws MailException;

	/**
	 * Metodo que envia um email.
	 * 
	 * @param de
	 *            o email de origem.
	 * @param para
	 *            o email de destino.
	 * @param copia
	 *            um email para copiar, caso nao queira passar null.
	 * @param oculta
	 *            um email com copia oculta, caso nao queira passar null.
	 * @param assunto
	 *            o assunto do email.
	 * @param mensagem
	 *            a mensagem do corpo do email.
	 * @param anexos
	 *            os arquivos anexados ao email, caso nao tenha passar null.
	 * 
	 * @throws MailException
	 *             dispara uma excecao caso nao consiga enviar o email.
	 */
	public void enviarEmail(String de, String para, String copia, String oculta, String assunto, String mensagem, Anexo[] anexos) throws MailException;
}
