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
import br.com.opensig.retenvinfe.TRetEnviNFe;

public class EnviarNfeSaida extends Chain {

	private FiscalServiceImpl servico;
	private FisNotaSaida saida;
	private Autenticacao auth;

	public EnviarNfeSaida(Chain next, FiscalServiceImpl servico, FisNotaSaida saida, Autenticacao auth) throws OpenSigException {
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
			long id = new Date().getTime();

			// adicionando dados ao xml
			if (xml.indexOf("<enviNFe") < 0) {
				int nfeINI = xml.indexOf("<NFe");
				int nfeFIM = xml.indexOf("</NFe>");
				xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><enviNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"" + auth.getConf().get("nfe.versao") + "\"><idLote>" + id + "</idLote>"
						+ xml.substring(nfeINI, nfeFIM) + "</NFe></enviNFe>";
			}
			// assina
			if (xml.indexOf("<Signature") > 0) {
				int posicao = xml.indexOf("<Signature");
				xml = xml.substring(0, posicao) + "</NFe></enviNFe>";
			}
			Document doc = UtilServer.getXml(xml);
			xml = NFe.assinarXML(doc, ENotaStatus.AUTORIZANDO, auth);
			// valida
			String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_enviando"));
			NFe.validarXML(xml, xsd);
			// envia para sefaz
			String recibo = servico.enviarNFe(xml, saida.getEmpEmpresa().getEmpEmpresaId());
			// analisa o retorno e seta os status
			TRetEnviNFe ret = UtilServer.xmlToObj(recibo, "br.com.opensig.retenvinfe");
			// verifica se sucesso
			if (ret.getCStat().equals("103")) {
				saida.setFisNotaSaidaXml(xml);
				saida.setFisNotaSaidaRecibo(ret.getInfRec().getNRec());
			} else {
				saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
				saida.setFisNotaSaidaErro(ret.getXMotivo());
			}
			// solicita o retorno
			int espera = Integer.valueOf(auth.getConf().get("nfe.tempo_retorno"));
			RetornarNfeSaida retorno = new RetornarNfeSaida(servico, saida, espera, auth);
			Thread retornar = new Thread(retorno);
			retornar.start();
		} catch (Exception e) {
			saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
			saida.setFisNotaSaidaErro(e.getMessage() == null ? "Erro no certificado, path dos arquivos ou conexão" : e.getMessage());
		} finally {
			servico.salvar(saida, false);
		}
	}

}
