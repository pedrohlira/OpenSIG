package br.com.opensig.fiscal.server.acao;

import org.w3c.dom.Document;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.server.NFe;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.retinutnfe.TRetInutNFe;

public class EnviarNfeInutilizadaSaida extends Chain {

	private FiscalServiceImpl servico;
	private FisNotaSaida saida;
	private Autenticacao auth;

	public EnviarNfeInutilizadaSaida(Chain next, FiscalServiceImpl servico, FisNotaSaida saida, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.saida = saida;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		try {
			// valida se ja esta assinada, senao assina
			String xml = saida.getFisNotaSaidaXml();
			Document doc = UtilServer.getXml(xml);
			if (doc.getElementsByTagName("Signature").item(0) == null) {
				// assina
				xml = NFe.assinarXML(doc, ENotaStatus.INUTILIZANDO, auth);
			}
			// valida
			String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_inutilizando"));
			NFe.validarXML(xml, xsd);
			validarNumero(doc);
			// envia para sefaz
			String inut = servico.inutilizar(xml);
			// analisa o retorno e seta os status
			TRetInutNFe ret = UtilServer.xmlToObj(inut, "br.com.opensig.retinutnfe");
			// verifica se sucesso
			if (ret.getInfInut().getCStat().equals("102")) {
				saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.INUTILIZADO));
				saida.setFisNotaSaidaProtocolo(ret.getInfInut().getNProt());
				saida.setFisNotaSaidaXml(montaProcInutNfe(saida.getFisNotaSaidaXml(), inut, auth.getConf().get("nfe.versao")));
				servico.salvar(saida, false);
			} else {
				throw new OpenSigException(ret.getInfInut().getXMotivo());
			}
		} catch (Exception e) {
			throw new OpenSigException(e.getMessage());
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

	private void validarNumero(Document doc) throws OpenSigException {
		int ini = Integer.valueOf(doc.getElementsByTagName("nNFIni").item(0).getTextContent());
		int fim = Integer.valueOf(doc.getElementsByTagName("nNFFin").item(0).getTextContent());

		if (ini <= fim) {
			FiltroNumero fn = new FiltroNumero("empEmpresa.empEmpresaId", ECompara.IGUAL, auth.getEmpresa()[0]);
			Number nfeNumero = servico.buscar(new FisNotaSaida(), "t.fisNotaSaidaNumero", EBusca.MAXIMO, fn);
			int ultimo = nfeNumero != null ? nfeNumero.intValue() : 0;
			if (fim > ultimo) {
				throw new OpenSigException("A NFe final nao pode ser maior que a ultima NFe emitida.");
			}
		} else {
			throw new OpenSigException("A NFe de inicio nao pode ser maior que a NFe final.");
		}
	}

}
