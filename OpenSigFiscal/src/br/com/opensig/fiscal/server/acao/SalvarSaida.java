package br.com.opensig.fiscal.server.acao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroObjeto;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.servico.FiscalException;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

public class SalvarSaida extends Chain {

	private Document doc;
	private String xml;
	private FisNotaStatus status;
	private EmpEmpresa empresa;
	private FisNotaSaida nota;
	private FiscalServiceImpl<FisNotaSaida> servico;
	private Autenticacao auth;

	public SalvarSaida(Chain next, String xml, FisNotaStatus status, Autenticacao auth) throws OpenSigException {
		super(next);
		this.xml = xml;
		this.status = status;
		this.auth = auth;
		this.empresa = new EmpEmpresa(Integer.valueOf(auth.getEmpresa()[0]));
		this.servico = new FiscalServiceImpl<FisNotaSaida>(auth);
	}

	@Override
	public void execute() throws OpenSigException {
		// valida o plano
		new ValidarPlano(null, servico, status, auth).execute();
		
		doc = UtilServer.getXml(xml);
		IFiltro filtro;

		// faz o filtro
		if (status.getFisNotaStatusId() == ENotaStatus.INUTILIZANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.INUTILIZADO.getId()) {
			GrupoFiltro gf = new GrupoFiltro();
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
			gf.add(fo, EJuncao.E);

			String numero = UtilServer.getValorTag(doc.getDocumentElement(), "nNFIni", true);
			FiltroNumero fn = new FiltroNumero("fisNotaSaidaNumero", ECompara.IGUAL, numero);
			gf.add(fn);
			filtro = gf;
		} else if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZANDO.getId()) {
			String chave = doc.getElementsByTagName("infNFe").item(0).getAttributes().item(0).getNodeValue().replace("NFe", "");
			filtro = new FiltroTexto("fisNotaSaidaChave", ECompara.IGUAL, chave);
		} else {
			String chave = UtilServer.getValorTag(doc.getDocumentElement(), "chNFe", true);
			filtro = new FiltroTexto("fisNotaSaidaChave", ECompara.IGUAL, chave);
		}

		// faz a busca
		nota = servico.selecionar(new FisNotaSaida(), filtro, false);

		// verifica se ja existe
		if (nota != null) {
			atualizar(filtro);
		} else if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId()) {
			salvarNota();
		} else if (status.getFisNotaStatusId() == ENotaStatus.CANCELANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.CANCELADO.getId()) {
			throw new FiscalException("Não foi encontrado no sistema a nota fiscal de saida correspondente ao cancelamento!");
		} else if (status.getFisNotaStatusId() == ENotaStatus.INUTILIZANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.INUTILIZADO.getId()) {
			salvarInut();
		}

		// enviando para sefaz
		if (status.getFisNotaStatusId() == ENotaStatus.CANCELANDO.getId()) {
			next = new EnviarNfeCanceladaSaida(next, servico, nota, auth);
		} else if (status.getFisNotaStatusId() == ENotaStatus.INUTILIZANDO.getId()) {
			next = new EnviarNfeInutilizadaSaida(next, servico, nota, auth);
		} else if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZANDO.getId()) {
			next = new EnviarNfeSaida(next, servico, nota, auth);
		}

		if (next != null) {
			next.execute();
		}
	}

	public FisNotaSaida getNota() {
		return nota;
	}

	private void salvarNota() throws FiscalException {
		try {
			// recupera a chave
			String chave = doc.getElementsByTagName("infNFe").item(0).getAttributes().item(0).getNodeValue().replace("NFe", "");
			if (chave == null) {
				UtilServer.LOG.debug("Nao achou a tag -> chNFe.");
				throw new FiscalException(auth.getConf().get("errInvalido") + " -> chNFe");
			}
			// recupera o protocolo
			String prot = UtilServer.getValorTag(doc.getDocumentElement(), "nProt", false);
			if (prot == null) {
				prot = "";
			}
			// recupera o numero
			String numero = UtilServer.getValorTag(doc.getDocumentElement(), "nNF", true);
			// recupera a data
			String data = UtilServer.getValorTag(doc.getDocumentElement(), "dEmi", true);
			Date dtData = null;
			try {
				dtData = new SimpleDateFormat("yyyy-MM-dd").parse(data);
			} catch (ParseException e) {
				UtilServer.LOG.debug("Data invalida.");
				throw new FiscalException(auth.getConf().get("errInvalido") + " -> dEmi");
			}
			// recupera os totais
			Element total = (Element) doc.getElementsByTagName("total").item(0);
			// recupera o valor
			String valor = UtilServer.getValorTag(total, "vNF", true);
			// recupera o icms
			String icms = UtilServer.getValorTag(total, "vICMS", false);
			if (icms == null) {
				icms = "0.00";
			}
			// recupera o ipi
			String ipi = UtilServer.getValorTag(total, "vIPI", false);
			if (ipi == null) {
				ipi = "0.00";
			}
			// recupera o pis
			String pis = UtilServer.getValorTag(total, "vPIS", false);
			if (pis == null) {
				pis = "0.00";
			}
			// recupera o cofins
			String cofins = UtilServer.getValorTag(total, "vCOFINS", false);
			if (cofins == null) {
				cofins = "0.00";
			}
			// valida a autorizada com protocolo
			if (prot.equals("") && status.getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId()) {
				UtilServer.LOG.debug("Nao achou o protocolo.");
				throw new FiscalException(auth.getConf().get("errInvalido") + " -> nProt");
			}
			// em caso de contigencia com FS-DA
			String tipoEmissao = UtilServer.getValorTag(doc.getDocumentElement(), "tpEmis", true);
			if (tipoEmissao.equals("5")) {
				status.setFisNotaStatusId(ENotaStatus.FS_DA.getId());
			}

			// cria a saida
			nota = new FisNotaSaida();
			nota.setFisNotaStatus(status);
			nota.setEmpEmpresa(empresa);
			nota.setFisNotaSaidaNumero(Integer.valueOf(numero));
			nota.setFisNotaSaidaCadastro(new Date());
			nota.setFisNotaSaidaData(dtData);
			nota.setFisNotaSaidaValor(Double.valueOf(valor));
			nota.setFisNotaSaidaChave(chave);
			nota.setFisNotaSaidaIcms(Double.valueOf(icms));
			nota.setFisNotaSaidaIpi(Double.valueOf(ipi));
			nota.setFisNotaSaidaPis(Double.valueOf(pis));
			nota.setFisNotaSaidaCofins(Double.valueOf(cofins));
			nota.setFisNotaSaidaProtocolo(prot);
			nota.setFisNotaSaidaXml(xml);
			nota.setFisNotaSaidaProtocoloCancelado("");
			nota.setFisNotaSaidaXmlCancelado("");
			nota.setFisNotaSaidaRecibo("");
			nota.setFisNotaSaidaErro("");

			// salva a saida
			try {
				nota = servico.salvar(nota, false);
			} catch (Exception e) {
				throw new FiscalException("Não foi possível salvar a NF de saída ou ela já existe!", e);
			}
		} catch (OpenSigException ope) {
			throw new FiscalException(ope.getMessage());
		}
	}

	private void salvarInut() throws FiscalException {
		try {
			// recupera o protocolo
			String prot = UtilServer.getValorTag(doc.getDocumentElement(), "nProt", false);
			if (prot == null) {
				prot = "";
			}
			if (prot.equals("") && status.getFisNotaStatusId() == ENotaStatus.INUTILIZADO.getId()) {
				UtilServer.LOG.debug("Nao achou o protocolo.");
				throw new FiscalException(auth.getConf().get("errInvalido") + " -> nProt");
			}

			// recupera o numero inicial
			String numeroIni = UtilServer.getValorTag(doc.getDocumentElement(), "nNFIni", true);
			// recupera o numero final
			String numeroFim = UtilServer.getValorTag(doc.getDocumentElement(), "nNFFin", true);

			// cria a saida
			nota = new FisNotaSaida();
			nota.setFisNotaStatus(status);
			nota.setEmpEmpresa(empresa);
			nota.setFisNotaSaidaNumero(Integer.valueOf(numeroIni));
			nota.setFisNotaSaidaCadastro(new Date());
			nota.setFisNotaSaidaData(new Date());
			nota.setFisNotaSaidaValor(0.00);
			nota.setFisNotaSaidaChave("Ini=" + numeroIni + " - Fim=" + numeroFim);
			nota.setFisNotaSaidaIcms(0.00);
			nota.setFisNotaSaidaIpi(0.00);
			nota.setFisNotaSaidaPis(0.00);
			nota.setFisNotaSaidaCofins(0.00);
			nota.setFisNotaSaidaProtocolo(prot);
			nota.setFisNotaSaidaXml(xml);
			nota.setFisNotaSaidaProtocoloCancelado("");
			nota.setFisNotaSaidaXmlCancelado("");
			nota.setFisNotaSaidaRecibo("");
			nota.setFisNotaSaidaErro("");

			// salva a saida
			try {
				nota = servico.salvar(nota, false);
			} catch (Exception e) {
				throw new FiscalException("Não foi possível salvar a Inutilização de nota ou ela já existe!", e);
			}
		} catch (OpenSigException ope) {
			throw new FiscalException(ope.getMessage());
		}
	}

	private void atualizar(IFiltro filtro) throws FiscalException {
		String prot;

		// recupera o protocolo
		try {
			NodeList nl = doc.getElementsByTagName("nProt");
			prot = nl.getLength() > 1 ? nl.item(1).getFirstChild().getNodeValue() : nl.item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			prot = "";
		}

		if (prot.equals("") && status.getFisNotaStatusId() != ENotaStatus.INUTILIZANDO.getId() && status.getFisNotaStatusId() != ENotaStatus.AUTORIZANDO.getId()) {
			UtilServer.LOG.debug("Nao achou o protocolo.");
			throw new FiscalException(auth.getConf().get("errInvalido") + " -> nProt");
		}

		// cria o sql
		ParametroTexto pt = null;
		ParametroTexto pt1 = null;
		if (status.getFisNotaStatusId() == ENotaStatus.CANCELANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.CANCELADO.getId()) {
			pt = new ParametroTexto("fisNotaSaidaProtocoloCancelado", status.getFisNotaStatusId() == ENotaStatus.CANCELADO.getId() ? prot : "");
			pt1 = new ParametroTexto("fisNotaSaidaXmlCancelado", xml);
			nota.setFisNotaSaidaXmlCancelado(xml);
		} else {
			pt = new ParametroTexto("fisNotaSaidaProtocolo", prot);
			pt1 = new ParametroTexto("fisNotaSaidaXml", xml);
			nota.setFisNotaSaidaXml(xml);
		}
		ParametroObjeto po = new ParametroObjeto("fisNotaStatus", status);
		GrupoParametro gp = new GrupoParametro(new IParametro[] { pt, pt1, po });

		// atualiza a saida
		try {
			Sql sql = new Sql(new FisNotaSaida(), EComando.ATUALIZAR, filtro, gp);
			servico.executar(new Sql[] { sql });
		} catch (Exception e) {
			throw new FiscalException("Não foi possível atualizar a NF de saída!", e);
		}
	}
}
