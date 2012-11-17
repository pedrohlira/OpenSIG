package br.com.opensig.fiscal.server;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.xml.crypto.dsig.XMLSignatureFactory;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.httpclient.protocol.Protocol;
import org.jasypt.util.text.BasicTextEncryptor;
import org.w3c.dom.Document;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.fiscal.client.servico.FiscalException;
import br.com.opensig.fiscal.shared.modelo.FisCertificado;
import br.inf.portalfiscal.www.nfe.wsdl.cadconsultacadastro2.CadConsultaCadastro2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsulta2.NfeConsulta2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao2.NfeInutilizacao2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nferecepcao2.NfeRecepcao2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nferetrecepcao2.NfeRetRecepcao2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;

/**
 * Classe que realiza a comunicação com o WS da Sefaz
 * 
 * @author Pedro H. Lira
 * @since 01/08/2011
 * @version 1.0
 */
public class Sefaz {

	// conf de homologacao contendo as urls do WS
	private static Properties HOMOLOGACAO = new Properties();
	// conf de producao contendo as urls do WS
	private static Properties PRODUCAO = new Properties();
	// colecao de estados do ambiente virual nacional
	private static Collection<String> SVAN = new ArrayList<String>();
	// colecao de estados do ambiente virual do RS
	private static Collection<String> SVRS = new ArrayList<String>();
	// Autenticacao do usuario
	private Autenticacao auth;
	
	// setando os configs da sefaz
	static {
		try {
			// carrega a homologacao
			FileInputStream homo = new FileInputStream(UtilServer.getRealPath("/WEB-INF/conf/sefazH.conf"));
			HOMOLOGACAO.load(homo);
			homo.close();
			// carrega a producao
			FileInputStream prod = new FileInputStream(UtilServer.getRealPath("/WEB-INF/conf/sefazP.conf"));
			PRODUCAO.load(prod);
			prod.close();
			// seta os estados do ambiente virtual nacional
			for (String an : PRODUCAO.getProperty("SVAN_Estados").split(",")) {
				SVAN.add(an);
			}
			// seta os estados do ambiente virtual RS
			for (String rs : PRODUCAO.getProperty("SVRS_Estados").split(",")) {
				SVRS.add(rs);
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao ler config da sefaz", ex);
		}
	}

	/**
	 * Contrutor da classe.
	 */
	private Sefaz(Autenticacao auth) throws Exception {
		this.auth = auth;
	}

	/**
	 * Metodo que retorna um objeto Sefaz com dados da empresa passada.
	 * 
	 * @param empresa
	 *            o id da empresa logada.
	 * @return um objeto Sefaz desta empresa.
	 * @throws FiscalException
	 *             caso ocorra uma excecao.
	 */
	public static Sefaz getInstancia(Autenticacao auth) throws FiscalException {
		try {
			// faz a busca pela senha
			FiltroNumero fn = new FiltroNumero("empEmpresa.empEmpresaId", ECompara.IGUAL, auth.getEmpresa()[0]);
			FiscalServiceImpl<FisCertificado> service = new FiscalServiceImpl<FisCertificado>(auth);
			FisCertificado cert = new FisCertificado();
			cert = service.selecionar(cert, fn, false);
			
			// monta o arquivo
			String cnpj = UtilServer.normaliza(cert.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1());
			cnpj = cnpj.replaceAll("\\D", "");
			String pfx = UtilServer.PATH_EMPRESA + cnpj + "/certificado.pfx";
			
			// descriptografa a senha
			BasicTextEncryptor seguranca = new BasicTextEncryptor();
			seguranca.setPassword(UtilServer.CHAVE);
			String senha = seguranca.decrypt(cert.getFisCertificadoSenha());
			
			// conecta na sefaz
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
			Object[] chaves = NFe.lerCertificado(pfx, senha, fac);
			PrivateKey pk = (PrivateKey) chaves[0];
			X509Certificate x509 = (X509Certificate) chaves[2];
			String cacerts = UtilServer.PATH_EMPRESA + "NFeCacerts";
			SocketFactoryDinamico sfd = new SocketFactoryDinamico(x509, pk, cacerts);
			
			// ativando o protocolo
			Protocol protocol = new Protocol("https", sfd, 443);
			Protocol.registerProtocol("https", protocol);

			return new Sefaz(auth);
		} catch (Exception e) {
			throw new FiscalException("Problemas com o certificado.");
		}
	}

	/**
	 * Metodo que identifica os tipos do xml.
	 * 
	 * @param uf
	 *            o estado a ser usado.
	 * @param tipo
	 *            o tipo de servidor.
	 * @param servico
	 *            qual o serviço que será usado na sefaz.
	 * @param scan
	 *            caso seja usado o servidor nacional.
	 * @return Uma string contendo a URL do WS da Sefaz.
	 * @throws OpenSigException
	 *             dispara uma excecao caso ocorra algum erro.
	 */
	private String identificaXml(String uf, String tipo, String servico, boolean scan) throws FiscalException {
		try {
			// identifica se o estado é virtual ou scan
			String chave;
			if (scan) {
				chave = "SCAN_" + servico;
			} else if (SVAN.contains(uf)) {
				chave = "SVAN_" + servico;
			} else if (SVRS.contains(uf)) {
				chave = "SVRS_" + servico;
			} else {
				chave = uf + "_" + servico;
			}

			// identifica a url
			String url = tipo.equals("1") ? PRODUCAO.getProperty(chave) : HOMOLOGACAO.getProperty(chave);
			if (url == null) {
				throw new FiscalException("Este servico [" + servico + "] nao esta disponivel para o Estado = " + uf);
			} else {
				return url;
			}
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	/**
	 * Metodo que faz a pesquisa de status do servico.
	 * 
	 * @param consStatServ
	 *            o objeto de status.
	 * @return uma string com o XML.
	 * @throws FiscalException
	 *             dispara uma excecao caso ocorra algum erro.
	 */
	public String status(String xml) throws FiscalException {
		try {
			Document doc = UtilServer.getXml(xml);
			String uf = UtilServer.getValorTag(doc.getDocumentElement(), "cUF", false);
			String ambiente = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);
			String url = identificaXml(uf, ambiente, "NfeStatusServico", false);

			OMElement ome = AXIOMUtil.stringToOM(xml);
			NfeStatusServico2Stub.NfeDadosMsg dadosMsg = new NfeStatusServico2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			NfeStatusServico2Stub.NfeCabecMsg nfeCabecMsg = new NfeStatusServico2Stub.NfeCabecMsg();
			nfeCabecMsg.setCUF(uf);
			nfeCabecMsg.setVersaoDados(auth.getConf().get("nfe.versao"));

			NfeStatusServico2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeStatusServico2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

			NfeStatusServico2Stub stub = new NfeStatusServico2Stub(url);
			NfeStatusServico2Stub.NfeStatusServicoNF2Result result = stub.nfeStatusServicoNF2(dadosMsg, nfeCabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao solicitar o servico na sefaz :: ", e);
			throw new FiscalException(e.getMessage());
		}
	}

	/**
	 * Metodo que faz a pesquisa de status do servico.
	 * 
	 * @param consSitNFe
	 *            o objeto de situacao.
	 * @return uma string com o XML.
	 * @throws FiscalException
	 *             dispara uma excecao caso ocorra algum erro.
	 */
	public String situacao(String xml) throws FiscalException {
		try {
			Document doc = UtilServer.getXml(xml);
			String uf = UtilServer.getValorTag(doc.getDocumentElement(), "chNFe", false).substring(0, 2);
			String ambiente = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);
			String url = identificaXml(uf, ambiente, "NfeConsultaProtocolo", false);

			OMElement ome = AXIOMUtil.stringToOM(xml);
			NfeConsulta2Stub.NfeDadosMsg dadosMsg = new NfeConsulta2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			NfeConsulta2Stub.NfeCabecMsg nfeCabecMsg = new NfeConsulta2Stub.NfeCabecMsg();
			nfeCabecMsg.setCUF(uf);
			nfeCabecMsg.setVersaoDados(auth.getConf().get("nfe.versao"));

			NfeConsulta2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeConsulta2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

			NfeConsulta2Stub stub = new NfeConsulta2Stub(url);
			NfeConsulta2Stub.NfeConsultaNF2Result result = stub.nfeConsultaNF2(dadosMsg, nfeCabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao consultar a nfe na sefaz :: ", e);
			throw new FiscalException(e.getMessage());
		}
	}

	/**
	 * Metodo que faz a pesquisa de status do servico.
	 * 
	 * @param consCad
	 *            o objeto de situacao.
	 * @param ibge
	 *            o codigo da uf.
	 * @param ambiente
	 *            em qual ambiente deve buscar.
	 * @return uma string com o XML.
	 * @throws FiscalException
	 *             dispara uma excecao caso ocorra algum erro.
	 */
	public String cadastro(String xml, int ibge, int ambiente) throws FiscalException {
		try {
			String url = identificaXml(ibge + "", ambiente + "", "NfeConsultaCadastro", false);

			OMElement ome = AXIOMUtil.stringToOM(xml);
			CadConsultaCadastro2Stub.NfeDadosMsg dadosMsg = new CadConsultaCadastro2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			CadConsultaCadastro2Stub.NfeCabecMsg cabecMsg = new CadConsultaCadastro2Stub.NfeCabecMsg();
			cabecMsg.setCUF(ibge + "");
			cabecMsg.setVersaoDados(auth.getConf().get("nfe.versao"));

			CadConsultaCadastro2Stub.NfeCabecMsgE cabecMsgE = new CadConsultaCadastro2Stub.NfeCabecMsgE();
			cabecMsgE.setNfeCabecMsg(cabecMsg);

			CadConsultaCadastro2Stub stub = new CadConsultaCadastro2Stub(url);
			CadConsultaCadastro2Stub.ConsultaCadastro2Result result = stub.consultaCadastro2(dadosMsg, cabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao consultar a empresa na sefaz :: ", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public String enviarNFe(String xml) throws FiscalException {
		try {
			Document doc = UtilServer.getXml(xml);
			String uf = UtilServer.getValorTag(doc.getDocumentElement(), "cUF", false);
			String serie = UtilServer.getValorTag(doc.getDocumentElement(), "serie", false);
			String ambiente = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);
			int iserie = Integer.valueOf(serie);
			String url = identificaXml(uf, ambiente, "NfeRecepcao", iserie >= 900);

			OMElement ome = AXIOMUtil.stringToOM(xml);
			NfeRecepcao2Stub.NfeDadosMsg dadosMsg = new NfeRecepcao2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			NfeRecepcao2Stub.NfeCabecMsg nfeCabecMsg = new NfeRecepcao2Stub.NfeCabecMsg();
			nfeCabecMsg.setCUF(uf);
			nfeCabecMsg.setVersaoDados(auth.getConf().get("nfe.versao"));

			NfeRecepcao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeRecepcao2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

			NfeRecepcao2Stub stub = new NfeRecepcao2Stub(url);
			NfeRecepcao2Stub.NfeRecepcaoLote2Result result = stub.nfeRecepcaoLote2(dadosMsg, nfeCabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao enviar o xml para sefaz :: ", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public String retornoNFe(String xml, String uf, String ambiente, String serie) throws FiscalException {
		try {
			int iserie = Integer.valueOf(serie);
			String url = identificaXml(uf, ambiente, "NfeRetRecepcao", iserie >= 900);

			OMElement ome = AXIOMUtil.stringToOM(xml);
			NfeRetRecepcao2Stub.NfeDadosMsg dadosMsg = new NfeRetRecepcao2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			NfeRetRecepcao2Stub.NfeCabecMsg nfeCabecMsg = new NfeRetRecepcao2Stub.NfeCabecMsg();
			nfeCabecMsg.setCUF(uf);
			nfeCabecMsg.setVersaoDados(auth.getConf().get("nfe.versao"));

			NfeRetRecepcao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeRetRecepcao2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

			NfeRetRecepcao2Stub stub = new NfeRetRecepcao2Stub(url);
			NfeRetRecepcao2Stub.NfeRetRecepcao2Result result = stub.nfeRetRecepcao2(dadosMsg, nfeCabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao consultar o recibo do xml na sefaz :: ", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public String cancelar(String xml) throws FiscalException {
		try {
			Document doc = UtilServer.getXml(xml);
			String uf = UtilServer.getValorTag(doc.getDocumentElement(), "chNFe", false).substring(0, 2);
			String ambiente = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);
			String url = identificaXml(uf, ambiente, "NfeCancelamento", false);

			OMElement ome = AXIOMUtil.stringToOM(xml);
			NfeCancelamento2Stub.NfeDadosMsg dadosMsg = new NfeCancelamento2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			NfeCancelamento2Stub.NfeCabecMsg nfeCabecMsg = new NfeCancelamento2Stub.NfeCabecMsg();
			nfeCabecMsg.setCUF(uf);
			nfeCabecMsg.setVersaoDados(auth.getConf().get("nfe.versao"));

			NfeCancelamento2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeCancelamento2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

			NfeCancelamento2Stub stub = new NfeCancelamento2Stub(url);
			NfeCancelamento2Stub.NfeCancelamentoNF2Result result = stub.nfeCancelamentoNF2(dadosMsg, nfeCabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao cancelar uma nfe na sefaz :: ", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public String inutilizar(String xml) throws FiscalException {
		try {
			Document doc = UtilServer.getXml(xml);
			String uf = UtilServer.getValorTag(doc.getDocumentElement(), "cUF", false);
			String ambiente = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);
			String url = identificaXml(uf, ambiente, "NfeInutilizacao", false);

			OMElement ome = AXIOMUtil.stringToOM(xml);
			NfeInutilizacao2Stub.NfeDadosMsg dadosMsg = new NfeInutilizacao2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			NfeInutilizacao2Stub.NfeCabecMsg nfeCabecMsg = new NfeInutilizacao2Stub.NfeCabecMsg();
			nfeCabecMsg.setCUF(uf);
			nfeCabecMsg.setVersaoDados(auth.getConf().get("nfe.versao"));

			NfeInutilizacao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeInutilizacao2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

			NfeInutilizacao2Stub stub = new NfeInutilizacao2Stub(url);
			NfeInutilizacao2Stub.NfeInutilizacaoNF2Result result = stub.nfeInutilizacaoNF2(dadosMsg, nfeCabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao cancelar uma nfe na sefaz :: ", e);
			throw new FiscalException(e.getMessage());
		}
	}
}
