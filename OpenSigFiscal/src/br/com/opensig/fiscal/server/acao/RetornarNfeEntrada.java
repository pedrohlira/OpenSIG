package br.com.opensig.fiscal.server.acao;

import org.w3c.dom.Document;

import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.retconsrecinfe.TProtNFe.InfProt;
import br.com.opensig.retconsrecinfe.TRetConsReciNFe;

public class RetornarNfeEntrada implements Runnable {

	private FiscalServiceImpl servico;
	private FisNotaEntrada entrada;
	private int espera;
	private Autenticacao auth;

	public RetornarNfeEntrada(FiscalServiceImpl servico, FisNotaEntrada entrada, int espera, Autenticacao auth) throws OpenSigException {
		this.servico = servico;
		this.entrada = entrada;
		this.espera = espera * 1000;
		this.auth = auth;
	}

	@Override
	public void run() {
		EmpEmpresa empresa = entrada.getEmpEmpresa();
		
		try {
			// espera o tempo de processamento na sefaz
			Thread.sleep(espera);
			// envia para sefaz
			String proc = servico.receberNFe(entrada.getFisNotaEntradaXml(), empresa.getEmpEmpresaId(), entrada.getFisNotaEntradaRecibo());
			// analisa o retorno e seta os status
			TRetConsReciNFe ret = UtilServer.xmlToObj(proc, "br.com.opensig.retconsrecinfe");
			// verifica se sucesso
			if (ret.getProtNFe().isEmpty()) {
				entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
				entrada.setFisNotaEntradaErro(ret.getXMotivo());
			} else {
				if (ret.getProtNFe().get(0).getInfProt().getCStat().equals("100")) {
					InfProt prot = ret.getProtNFe().get(0).getInfProt();
					entrada.setFisNotaEntradaXml(montaProcNfe(entrada.getFisNotaEntradaXml(), proc, auth.getConf().get("nfe.versao")));
					entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.AUTORIZADO));
					entrada.setFisNotaEntradaProtocolo(prot.getNProt());
				} else {
					entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
					entrada.setFisNotaEntradaErro(ret.getProtNFe().get(0).getInfProt().getXMotivo());
				}
			}
		} catch (Exception e) {
			entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
			entrada.setFisNotaEntradaErro(e.getMessage());
		} finally {
			try {
				entrada.setEmpEmpresa(empresa);
				servico.salvar(entrada, false);
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
