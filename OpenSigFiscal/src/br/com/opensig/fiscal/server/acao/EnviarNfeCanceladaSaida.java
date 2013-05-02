package br.com.opensig.fiscal.server.acao;

import java.util.Date;

import org.w3c.dom.Document;

import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.server.NFe;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.retenveventocancnfe.TRetEnvEvento;

public class EnviarNfeCanceladaSaida extends Chain {

	private FiscalServiceImpl servico;
	private FisNotaSaida saida;
	private Autenticacao auth;

	public EnviarNfeCanceladaSaida(Chain next, FiscalServiceImpl servico, FisNotaSaida saida, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.saida = saida;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		String para = null;
		try {
			// valida se ja esta assinada, senao assina
			long id = new Date().getTime();
			String xml = saida.getFisNotaSaidaXmlCancelado();

			// adicionando dados ao xml
			if (xml.indexOf("<envEvento") < 0) {
				int eveINI = xml.indexOf("<evento");
				int eveFIM = xml.indexOf("</evento>");
				xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envEvento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"" + auth.getConf().get("nfe.evento") + "\"><idLote>" + id
						+ "</idLote>" + xml.substring(eveINI, eveFIM) + "</evento></envEvento>";
			}
			// assina
			if (xml.indexOf("<Signature") > 0) {
				int posicao = xml.indexOf("<Signature");
				xml = xml.substring(0, posicao) +"</evento></envEvento>";
			}
			Document doc = UtilServer.getXml(xml);
			xml = NFe.assinarXML(doc, ENotaStatus.CANCELANDO, auth);
			// valida
			String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_cancelando"));
			NFe.validarXML(xml, xsd);
			// envia para sefaz
			String proc = servico.evento(xml);
			// analisa o retorno e seta os status
			TRetEnvEvento ret = UtilServer.xmlToObj(proc, "br.com.opensig.retenveventocancnfe");
			// verifica se sucesso
			if (ret.getCStat().equals("128")) {
				saida.setFisNotaSaidaEvento(1);
				saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.CANCELADO));
				saida.setFisNotaSaidaProtocoloCancelado(ret.getRetEvento().get(0).getInfEvento().getNProt());
				saida.setFisNotaSaidaXmlCancelado(montaProcCancNfe(saida.getFisNotaSaidaXmlCancelado(), proc, auth.getConf().get("nfe.evento")));
				saida.setFisNotaSaidaErro("");
				servico.salvar(saida, false);
				
				Document doc1 = UtilServer.getXml(saida.getFisNotaSaidaXml());
				para = UtilServer.getValorTag(doc1.getDocumentElement(), "email", false);
				if (para != null) {
					EnviarEmail email = new EnviarEmail(servico, saida, auth);
					Thread enviar = new Thread(email);
					enviar.start();
				}
			} else {
				throw new OpenSigException(ret.getXMotivo());
			}
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage());
		}

		if (next != null) {
			next.execute();
		}
	}

	public static String montaProcCancNfe(String canc, String proc, String versao) throws OpenSigException {
		// transforma em doc
		Document doc1 = UtilServer.getXml(canc);
		Document doc2 = UtilServer.getXml(proc);

		// pega as tags corretas
		canc = UtilServer.getXml(doc1.getElementsByTagName("evento").item(0));
		proc = UtilServer.getXml(doc2.getElementsByTagName("retEvento").item(0));

		// unifica
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<procEventoNFe versao=\"" + versao + "\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
		sb.append(canc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		sb.append(proc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		sb.append("</procEventoNFe>");

		return sb.toString();
	}

}
