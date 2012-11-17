package br.com.opensig.fiscal.server.acao;

import org.w3c.dom.Document;

import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.retconsrecinfe.TProtNFe.InfProt;
import br.com.opensig.retconsrecinfe.TRetConsReciNFe;

public class RetornarNfeSaida implements Runnable {

	private FiscalServiceImpl servico;
	private FisNotaSaida saida;
	private int espera;
	private Autenticacao auth;

	public RetornarNfeSaida(FiscalServiceImpl servico, FisNotaSaida saida, int espera, Autenticacao auth) throws OpenSigException {
		this.servico = servico;
		this.saida = saida;
		this.espera = espera * 1000;
		this.auth = auth;
	}

	@Override
	public void run() {
		String para = null;
		EmpEmpresa empresa = saida.getEmpEmpresa();
		
		try {
			// espera o tempo de processamento na sefaz
			Thread.sleep(espera);
			// envia para sefaz
			String proc = servico.receberNFe(saida.getFisNotaSaidaXml(), empresa.getEmpEmpresaId(), saida.getFisNotaSaidaRecibo());
			// analisa o retorno e seta os status
			TRetConsReciNFe ret = UtilServer.xmlToObj(proc, "br.com.opensig.retconsrecinfe");
			// verifica se sucesso
			if (ret.getProtNFe().isEmpty()) {
				saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
				saida.setFisNotaSaidaErro(ret.getXMotivo());
			} else {
				if (ret.getProtNFe().get(0).getInfProt().getCStat().equals("100")) {
					InfProt prot = ret.getProtNFe().get(0).getInfProt();
					saida.setFisNotaSaidaXml(montaProcNfe(saida.getFisNotaSaidaXml(), proc, auth.getConf().get("nfe.versao")));
					saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.AUTORIZADO));
					saida.setFisNotaSaidaProtocolo(prot.getNProt());

					Document doc = UtilServer.getXml(saida.getFisNotaSaidaXml());
					para = UtilServer.getValorTag(doc.getDocumentElement(), "email", false);
				} else {
					saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
					saida.setFisNotaSaidaErro(ret.getProtNFe().get(0).getInfProt().getXMotivo());
				}
			}
		} catch (Exception e) {
			saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
			saida.setFisNotaSaidaErro(e.getMessage());
		} finally {
			try {
				saida.setEmpEmpresa(empresa);
				servico.salvar(saida, false);
				if (para != null) {
					EnviarEmail email = new EnviarEmail(servico, saida, auth);
					Thread enviar = new Thread(email);
					enviar.start();
				}
			} catch (OpenSigException e) {
				UtilServer.LOG.error("Problemas ao salvar a saida apos retornar.", e);
			}
		}
	}

	public static String montaProcNfe(String nfe, String proc, String versao) throws OpenSigException {
		// transforma em doc
		Document doc1 = UtilServer.getXml(nfe);
		Document doc2 = UtilServer.getXml(proc);

		// pega as tags corretas
		nfe = UtilServer.getXml(doc1.getElementsByTagName("NFe").item(0));
		proc = UtilServer.getXml(doc2.getElementsByTagName("protNFe").item(0));

		// unifica
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<nfeProc versao=\"" + versao + "\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
		sb.append(nfe.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		sb.append(proc.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		sb.append("</nfeProc>");

		return sb.toString();
	}
}
