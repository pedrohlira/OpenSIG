package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Anexo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Classe que padroniza o envio de email de forma assincrona.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class MailProxy implements MailServiceAsync {

	private MailServiceAsync async = (MailServiceAsync) GWT.create(MailService.class);
	private ServiceDefTarget sdf = (ServiceDefTarget) async;

	public MailProxy() {
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "MailService");
	}

	@Override
	public void enviarEmail(String de, String para, String assunto, String mensagem, AsyncCallback asyncCallback) {
		async.enviarEmail(de, para, assunto, mensagem, asyncCallback);
	}

	@Override
	public void enviarEmail(String de, String para, String assunto, String mensagem, Anexo[] anexos, AsyncCallback asyncCallback) {
		async.enviarEmail(de, para, assunto, mensagem, anexos, asyncCallback);
	}

	@Override
	public void enviarEmail(String de, String para, String copia, String oculto, String assunto, String mensagem, Anexo[] anexos, AsyncCallback asyncCallback) {
		async.enviarEmail(de, para, copia, oculto, assunto, mensagem, anexos, asyncCallback);
	}

}
