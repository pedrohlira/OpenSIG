package br.com.opensig.core.server;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletException;

import br.com.opensig.core.client.servico.MailException;
import br.com.opensig.core.client.servico.MailService;
import br.com.opensig.core.shared.modelo.Anexo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Classe que gerencia os emails enviados do sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class MailServiceImpl extends RemoteServiceServlet implements MailService {

	private static final Properties conf = new Properties();

	@Override
	public void init() throws ServletException {
		Enumeration<String> param = getInitParameterNames();
		for (; param.hasMoreElements();) {
			String nome = param.nextElement();
			String valor = getInitParameter(nome);
			conf.put(nome, valor);
		}
	}

	/**
	 * @see MailService#enviarEmail(String, String, String, String, String, String, Anexo[])
	 */
	public synchronized static void enviar(String de, String para, String copia, String oculta, String assunto, String mensagem, Anexo[] anexos) throws MailException {
		// configurando o servidor de envio
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(conf.getProperty("mail.user"), conf.getProperty("mail.pwd"));
			}
		};
		Session mailSession = Session.getDefaultInstance(conf, auth);

		if (de == null) {
			de = conf.getProperty("mail.user");
		}

		// configurando e enviando a mensagem
		try {
			// componente de email
			MimeMultipart mpRoot = new MimeMultipart("mixed");
			MimeMultipart mpContent = new MimeMultipart("alternative");
			MimeBodyPart contentPartRoot = new MimeBodyPart();
			contentPartRoot.setContent(mpContent);
			mpRoot.addBodyPart(contentPartRoot);

			// texto html
			MimeBodyPart corpoParte = new MimeBodyPart();
			corpoParte.setContent(mensagem, "text/html");
			mpContent.addBodyPart(corpoParte);

			// anexos
			if (anexos != null) {
				for (Anexo anexo : anexos) {
					DataSource fds = new ByteArrayDataSource(anexo.getDados(), anexo.getTipo());
					MimeBodyPart attachament = new MimeBodyPart();
					attachament.setDisposition(Part.ATTACHMENT);
					attachament.setDataHandler(new DataHandler(fds));
					attachament.setFileName(anexo.getNome());
					mpRoot.addBodyPart(attachament);
				}
			}

			// mensagem
			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(de));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(para));
			if (copia != null) {
				message.setRecipient(Message.RecipientType.CC, new InternetAddress(copia));
			}
			if (oculta != null) {
				message.setRecipient(Message.RecipientType.BCC, new InternetAddress(oculta));
			}
			message.setSentDate(new Date());
			message.setSubject(assunto);
			message.setContent(mpRoot);
			message.saveChanges();
			
			Transport.send(message);
		} catch (Exception e) {
			throw new MailException(e.getMessage());
		}
	}

	@Override
	public void enviarEmail(String de, String para, String assunto, String mensagem) throws MailException {
		enviarEmail(de, para, assunto, mensagem, null);
	}

	@Override
	public void enviarEmail(String de, String para, String assunto, String mensagem, Anexo[] anexos) throws MailException {
		enviarEmail(de, para, null, null, assunto, mensagem, anexos);
	}

	@Override
	public void enviarEmail(String de, String para, String copia, String oculta, String assunto, String mensagem, Anexo[] anexos) throws MailException {
		enviar(de, para, copia, oculta, assunto, mensagem, anexos);
	}
}
