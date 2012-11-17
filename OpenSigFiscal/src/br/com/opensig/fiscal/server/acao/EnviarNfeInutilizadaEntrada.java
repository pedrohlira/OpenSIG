package br.com.opensig.fiscal.server.acao;

import org.w3c.dom.Document;

import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.server.NFe;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.retinutnfe.TRetInutNFe;

public class EnviarNfeInutilizadaEntrada extends Chain {

	private FiscalServiceImpl servico;
	private FisNotaEntrada entrada;
	private Autenticacao auth;

	public EnviarNfeInutilizadaEntrada(Chain next, FiscalServiceImpl servico, FisNotaEntrada entrada, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.entrada = entrada;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		try {
			// valida se ja esta assinada, senao assina
			String xml = entrada.getFisNotaEntradaXml();
			Document doc = UtilServer.getXml(xml);
			if (doc.getElementsByTagName("Signature").item(0) == null) {
				// assina
				xml = NFe.assinarXML(doc, ENotaStatus.INUTILIZANDO, auth);
			}
			// valida
			String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_inutilizando"));
			NFe.validarXML(xml, xsd);
			// envia para sefaz
			String inut = servico.inutilizar(xml, entrada.getEmpEmpresa().getEmpEmpresaId());
			// analisa o retorno e seta os status
			TRetInutNFe ret = UtilServer.xmlToObj(inut, "br.com.opensig.retinutnfe");
			// verifica se sucesso
			if (ret.getInfInut().getCStat().equals("102")) {
				entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.INUTILIZADO));
				entrada.setFisNotaEntradaProtocolo(ret.getInfInut().getNProt());
				entrada.setFisNotaEntradaXml(montaProcInutNfe(entrada.getFisNotaEntradaXml(), inut, auth.getConf().get("nfe.versao")));
			} else {
				entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
				entrada.setFisNotaEntradaErro(ret.getInfInut().getXMotivo());
			}
		} catch (Exception e) {
			entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
			entrada.setFisNotaEntradaErro(e.getMessage());
		} finally {
			servico.salvar(entrada, false);
		}

		if (next != null) {
			next.execute();
		}
	}

	public static String montaProcInutNfe(String inut, String proc, String versao) throws OpenSigException {
		// transforma em doc
		Document doc1 = UtilServer.getXml(inut);
		Document doc2 = UtilServer.getXml(proc);

		// pega as tags corretas
		inut = UtilServer.getXml(doc1.getElementsByTagName("inutNFe").item(0));
		proc = UtilServer.getXml(doc2.getElementsByTagName("retInutNFe").item(0));

		// unifica
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ProcInutNFe versao=\"" + versao + "\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
		sb.append(inut.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		sb.append(proc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		sb.append("</ProcInutNFe>");

		return sb.toString();
	}

}
