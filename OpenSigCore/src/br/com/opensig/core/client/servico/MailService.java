package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Anexo;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Interface que padroniza o envio de email.
 * 
 * @author Pedro H. Lira
 */
public interface MailService extends RemoteService {

	/**
	 * Metodo que envia um email.
	 * 
	 * @param para
	 *            o email de destino.
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
	public void enviarEmail(String para, String assunto, String mensagem, Anexo[] anexos) throws MailException;
}
