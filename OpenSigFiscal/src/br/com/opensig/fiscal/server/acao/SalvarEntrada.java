package br.com.opensig.fiscal.server.acao;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroObjeto;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.servico.FiscalException;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.retconssitnfe.TRetConsSitNFe;

public class SalvarEntrada extends Chain {

	private Document doc;
	private String xml;
	private FisNotaStatus status;
	private EmpEmpresa empresa;
	private FisNotaEntrada nota;
	private FiscalServiceImpl<FisNotaEntrada> servico;
	private Autenticacao auth;

	public SalvarEntrada(Chain next, String xml, FisNotaStatus status, Autenticacao auth) throws OpenSigException {
		super(next);
		this.xml = xml;
		this.status = status;
		this.auth = auth;
		this.empresa = new EmpEmpresa(Integer.valueOf(auth.getEmpresa()[0]));
		this.servico = new FiscalServiceImpl<FisNotaEntrada>(auth);
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
			FiltroNumero fn = new FiltroNumero("fisNotaEntradaNumero", ECompara.IGUAL, numero);
			gf.add(fn);
			filtro = gf;
		} else if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZANDO.getId()) {
			String chave = doc.getElementsByTagName("infNFe").item(0).getAttributes().item(0).getNodeValue().replace("NFe", "");
			filtro = new FiltroTexto("fisNotaEntradaChave", ECompara.IGUAL, chave);
		} else {
			String chave = UtilServer.getValorTag(doc.getDocumentElement(), "chNFe", true);
			filtro = new FiltroTexto("fisNotaEntradaChave", ECompara.IGUAL, chave);
		}

		// faz a busca
		nota = servico.selecionar(new FisNotaEntrada(), filtro, false);

		// verifica se ja existe
		if (nota != null) {
			atualizar(filtro);
		} else if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId()) {
			salvarNota();
		} else if (status.getFisNotaStatusId() == ENotaStatus.CANCELANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.CANCELADO.getId()) {
			throw new FiscalException("Não foi encontrado no sistema a nota fiscal de entrada correspondente ao cancelamento!");
		} else if (status.getFisNotaStatusId() == ENotaStatus.INUTILIZANDO.getId() || status.getFisNotaStatusId() == ENotaStatus.INUTILIZADO.getId()) {
			salvarInut();
		}

		// enviando para sefaz
		if (status.getFisNotaStatusId() == ENotaStatus.CANCELANDO.getId()) {
			next = new EnviarNfeCanceladaEntrada(next, servico, nota, auth);
		} else if (status.getFisNotaStatusId() == ENotaStatus.INUTILIZANDO.getId()) {
			next = new EnviarNfeInutilizadaEntrada(next, servico, nota, auth);
		} else if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZANDO.getId()) {
			next = new EnviarNfeEntrada(next, servico, nota, auth);
		}

		if (next != null) {
			next.execute();
		}
	}

	public FisNotaEntrada getNota() {
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
			// recupera o cnpj
			Element emit = (Element) doc.getElementsByTagName("emit").item(0);
			String cnpj = UtilServer.getValorTag(emit, "CNPJ", true);
			try {
				cnpj = UtilServer.formataTexto(cnpj, "##.###.###/####-##");
			} catch (ParseException e) {
				UtilServer.LOG.debug("Cnpj invalido.");
				throw new FiscalException(auth.getConf().get("errInvalido") + " -> CNPJ");
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

			nota = new FisNotaEntrada();
			nota.setFisNotaStatus(status);
			nota.setEmpEmpresa(empresa);
			nota.setFisNotaEntradaNumero(Integer.valueOf(numero));
			nota.setFisNotaEntradaCadastro(new Date());
			nota.setFisNotaEntradaData(dtData);
			nota.setFisNotaEntradaValor(Double.valueOf(valor));
			nota.setFisNotaEntradaChave(chave);
			nota.setFisNotaEntradaIcms(Double.valueOf(icms));
			nota.setFisNotaEntradaIpi(Double.valueOf(ipi));
			nota.setFisNotaEntradaPis(Double.valueOf(pis));
			nota.setFisNotaEntradaCofins(Double.valueOf(cofins));
			nota.setFisNotaEntradaProtocolo(prot);
			nota.setFisNotaEntradaXml(xml);
			nota.setFisNotaEntradaProtocoloCancelado("");
			nota.setFisNotaEntradaXmlCancelado("");
			nota.setFisNotaEntradaRecibo("");
			nota.setFisNotaEntradaErro("");

			try {
				if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId()) {
					// verifica se tem o certificado pra validar
					String pasta = auth.getEmpresa()[5].replaceAll("\\D", "");
					String pfx = UtilServer.PATH_EMPRESA + pasta + "/certificado.pfx";
					File arquivo = new File(pfx);

					if (arquivo.exists()) {
						// valida na sefaz
						int amb = Integer.valueOf(auth.getConf().get("nfe.tipoamb"));
						String resp = servico.situacao(amb, chave, empresa.getEmpEmpresaId());
						TRetConsSitNFe situacao = UtilServer.xmlToObj(resp, "br.com.opensig.retconssitnfe");

						// verifica se o status na sefaz é igual ao informado
						if (situacao.getCStat().equals("100")) {
							if (status.getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId() && situacao.getProtNFe() != null) {
								// valida se a data da nota ainda pode ser cancelada
								int dias = Integer.valueOf(auth.getConf().get("nfe.tempo_cancela"));
								Calendar cal = Calendar.getInstance();
								cal.setTime(nota.getFisNotaEntradaData());
								cal.add(Calendar.DATE, dias);

								Date hoje = new Date();
								if (hoje.compareTo(cal.getTime()) > 0) {
									nota.setFisNotaEntradaRecibo("OK");
								} else {
									nota.setFisNotaEntradaRecibo("PROVISORIO");
								}
							} else if (status.getFisNotaStatusId() == ENotaStatus.CANCELADO.getId() && situacao.getRetCancNFe() != null) {
								nota.setFisNotaEntradaRecibo("OK");
							} else {
								nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
								nota.setFisNotaEntradaErro("A nota de entrada");
							}
						} else {
							nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
							nota.setFisNotaEntradaErro("Nao achou a nota na sefaz ou problemas na hora do acesso ao servidor.");
						}
					} else {
						nota.setFisNotaEntradaRecibo("OK");
					}
				}
			} catch (Exception e) {
				nota.setFisNotaEntradaRecibo("OK");
			} finally {
				// salva a entrada
				nota = servico.salvar(nota, false);
			}

			// integracao com OpenSIG
			try {
				// achando a compra referente a NFe
				FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
				FiltroTexto ft = new FiltroTexto("empFornecedor.empEntidade.empEntidadeDocumento1", ECompara.IGUAL, cnpj);
				FiltroNumero fn = new FiltroNumero("comCompraNumero", ECompara.IGUAL, numero);
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft, fn });
				CoreServiceImpl<ComCompra> service = new CoreServiceImpl<ComCompra>();
				ComCompra compra = service.selecionar(new ComCompra(), gf, false);

				if (compra != null) {
					// atualiza a compra
					ParametroObjeto po = new ParametroObjeto("fisNotaEntrada", nota);
					ParametroBinario pb = new ParametroBinario("comCompraNfe", 1);
					GrupoParametro gp = new GrupoParametro(new IParametro[] { po, pb });
					Sql sql = new Sql(compra, EComando.ATUALIZAR, gf, gp);
					service.executar(new Sql[] { sql });
					// atualiza a data de recebimento
					nota.setFisNotaEntradaCadastro(compra.getComCompraRecebimento());
					nota = servico.salvar(nota, false);
				}
			} catch (Exception e) {
				UtilServer.LOG.info("Modulo Fiscal fora do OpenSIG.");
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
			nota = new FisNotaEntrada();
			nota.setFisNotaStatus(status);
			nota.setEmpEmpresa(empresa);
			nota.setFisNotaEntradaNumero(Integer.valueOf(numeroIni));
			nota.setFisNotaEntradaCadastro(new Date());
			nota.setFisNotaEntradaData(new Date());
			nota.setFisNotaEntradaValor(0.00);
			nota.setFisNotaEntradaChave("Ini=" + numeroIni + " - Fim=" + numeroFim);
			nota.setFisNotaEntradaIcms(0.00);
			nota.setFisNotaEntradaIpi(0.00);
			nota.setFisNotaEntradaPis(0.00);
			nota.setFisNotaEntradaCofins(0.00);
			nota.setFisNotaEntradaProtocolo(prot);
			nota.setFisNotaEntradaXml(xml);
			nota.setFisNotaEntradaProtocoloCancelado("");
			nota.setFisNotaEntradaXmlCancelado("");
			nota.setFisNotaEntradaErro("");

			// salva a entrada
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
			pt = new ParametroTexto("fisNotaEntradaProtocoloCancelado", status.getFisNotaStatusId() == ENotaStatus.CANCELADO.getId() ? prot : "");
			pt1 = new ParametroTexto("fisNotaEntradaXmlCancelado", xml);
			nota.setFisNotaEntradaXmlCancelado(xml);
		} else {
			pt = new ParametroTexto("fisNotaEntradaProtocolo", prot);
			pt1 = new ParametroTexto("fisNotaEntradaXml", xml);
			nota.setFisNotaEntradaXml(xml);
		}
		ParametroObjeto po = new ParametroObjeto("fisNotaStatus", status);
		GrupoParametro gp = new GrupoParametro(new IParametro[] { pt, pt1, po });

		// atualiza a entrada
		try {
			Sql sql = new Sql(new FisNotaEntrada(), EComando.ATUALIZAR, filtro, gp);
			servico.executar(new Sql[] { sql });
		} catch (Exception e) {
			throw new FiscalException("Não foi possível atualizar a NF de saída!", e);
		}
	}
}
