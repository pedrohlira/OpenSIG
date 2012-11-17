package br.com.opensig.fiscal.server.acao;

import org.w3c.dom.Document;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.MailServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Anexo;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

public class EnviarEmail implements Runnable {

	private FiscalServiceImpl servico;
	private FisNotaSaida saida;
	private Autenticacao auth;

	public EnviarEmail(FiscalServiceImpl servico, FisNotaSaida saida, Autenticacao auth) throws OpenSigException {
		this.servico = servico;
		this.saida = saida;
		this.auth = auth;
	}

	@Override
	public void run() {
		try {
			// pega os dados completos da empresa
			FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, saida.getEmpEmpresa().getEmpEmpresaId());
			EmpEmpresa empresa = (EmpEmpresa) servico.selecionar(new EmpEmpresa(), fn, false);
			
			// de
			String de = null;
			for (EmpContato cont : empresa.getEmpEntidade().getEmpContatos()) {
				if (auth.getConf().get("nfe.tipocontemail").equals(cont.getEmpContatoTipo().getEmpContatoTipoId() + "")) {
					de = cont.getEmpContatoDescricao();
					break;
				}
			}
			// para
			Document doc = UtilServer.getXml(saida.getFisNotaSaidaXml());
			String para = UtilServer.getValorTag(doc.getDocumentElement(), "email", false);

			// assunto, mensagem e anexo
			String assunto = "";
			String msg = "";
			Anexo[] anexos = null;
			if (saida.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId()) {
				// assunto
				assunto = "NFe - " + saida.getFisNotaSaidaChave();
				// inserindo parametros
				msg = UtilServer.getTextoArquivo(UtilServer.getRealPath("/WEB-INF/modelos/nfe.html"));
				msg = msg.replace("#valor#", UtilServer.formataNumero(saida.getFisNotaSaidaValor(), 1, 2, true));
				// anexos
				String nome = saida.getFisNotaSaidaChave() + "-procNFe";
				// pdf
				Anexo xml = new Anexo(nome + ".xml", "text/xml", saida.getFisNotaSaidaXml().getBytes());
				Anexo pdf = new Anexo(nome + ".pdf", "application/pdf", servico.getDanfe(saida.getFisNotaSaidaXml()));
				anexos = new Anexo[] { pdf, xml };
			} else {
				// assunto
				assunto = "NFe Cancelada - " + saida.getFisNotaSaidaChave();
				// inserindo parametros
				Document canc = UtilServer.getXml(saida.getFisNotaSaidaXmlCancelado());
				msg = UtilServer.getTextoArquivo(UtilServer.getRealPath("/WEB-INF/modelos/cancelada.html"));
				msg = msg.replace("#motivo#", UtilServer.getValorTag(canc.getDocumentElement(), "xMotivo", false));
				// anexos
				Anexo xml = new Anexo(saida.getFisNotaSaidaChave() + "-procCancNFe.xml", "text/xml", saida.getFisNotaSaidaXmlCancelado().getBytes());
				anexos = new Anexo[] { xml };
			}
			// inserindo parametros
			msg = msg.replace("#numero#", saida.getFisNotaSaidaNumero() + "");
			msg = msg.replace("#serie#", UtilServer.getValorTag(doc.getDocumentElement(), "serie", false));
			msg = msg.replace("#emitente#", empresa.getEmpEntidade().getEmpEntidadeNome1());
			msg = msg.replace("#chave#", saida.getFisNotaSaidaChave());

			// enviando
			MailServiceImpl.enviar(de, para, null, null, assunto, msg, anexos);
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no envio do email com NFe - " + saida.getFisNotaSaidaChave(), e);
		}
	}
}
