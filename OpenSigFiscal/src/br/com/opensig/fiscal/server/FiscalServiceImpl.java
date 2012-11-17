package br.com.opensig.fiscal.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import nl.captcha.Captcha;

import org.jasypt.util.text.BasicTextEncryptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.misc.BASE64Decoder;
import br.com.opensig.conscad.TConsCad;
import br.com.opensig.conscad.TConsCad.InfCons;
import br.com.opensig.conscad.TUfCons;
import br.com.opensig.consrecinfe.TConsReciNFe;
import br.com.opensig.conssitnfe.TConsSitNFe;
import br.com.opensig.consstatserv.TConsStatServ;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.client.servico.ExportacaoException;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.SessionManager;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.servico.FiscalException;
import br.com.opensig.fiscal.client.servico.FiscalService;
import br.com.opensig.fiscal.server.acao.EnviarEmail;
import br.com.opensig.fiscal.server.acao.EnviarNfeCanceladaEntrada;
import br.com.opensig.fiscal.server.acao.EnviarNfeCanceladaSaida;
import br.com.opensig.fiscal.server.acao.GerarNfeCanceladaEntrada;
import br.com.opensig.fiscal.server.acao.GerarNfeCanceladaSaida;
import br.com.opensig.fiscal.server.acao.GerarNfeInutilizadaEntrada;
import br.com.opensig.fiscal.server.acao.GerarNfeInutilizadaSaida;
import br.com.opensig.fiscal.server.acao.RetornarNfeEntrada;
import br.com.opensig.fiscal.server.acao.RetornarNfeSaida;
import br.com.opensig.fiscal.server.acao.SalvarEntrada;
import br.com.opensig.fiscal.server.acao.SalvarSaida;
import br.com.opensig.fiscal.server.sped.EFD;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisCertificado;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisSpedFiscal;
import br.com.opensig.retconssitnfe.TProtNFe.InfProt;
import br.com.opensig.retconssitnfe.TRetCancNFe.InfCanc;
import br.com.opensig.retconssitnfe.TRetConsSitNFe;

@SuppressWarnings("restriction")
public class FiscalServiceImpl<E extends Dados> extends CoreServiceImpl<E> implements FiscalService<E> {

	public FiscalServiceImpl() {
	}

	public FiscalServiceImpl(Autenticacao auth) {
		super(auth);
	}

	public String exportar(String arquivo, String nome, String tipo) throws ExportacaoException {
		// valida sessao
		String retorno = "";
		HttpSession sessao = getThreadLocalRequest().getSession();
		byte[] obj = null;

		try {
			BASE64Decoder B64 = new BASE64Decoder();
			if (tipo.equalsIgnoreCase("pdf")) {
				String xml = new String(B64.decodeBuffer(arquivo));
				obj = getDanfe(xml);
			} else if (tipo.equalsIgnoreCase("zip") || tipo.equalsIgnoreCase("rec")) {
				Autenticacao auth = SessionManager.LOGIN.get(sessao);
				String cnpj = auth.getEmpresa()[5].replaceAll("\\D", "");
				String path = UtilServer.PATH_EMPRESA + cnpj + "/sped/" + arquivo;
				File arq = new File(path);

				if (arq.exists()) {
					InputStream is = new FileInputStream(arq);
					obj = new byte[is.available()];
					is.read(obj);
					is.close();
				} else {
					throw new ExportacaoException("Arquivo nao encontrado!");
				}
			} else {
				obj = B64.decodeBuffer(arquivo);
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao exportar", ex);
			throw new ExportacaoException("Erro ao exportar -> " + ex.getMessage());
		}

		retorno = sessao.getId() + new Date().getTime();
		sessao.setAttribute(retorno, obj);
		sessao.setAttribute(retorno + "arquivo", nome + "." + tipo);
		return retorno;
	}

	public byte[] getDanfe(String xml) {
		byte[] pdf = null;

		try {
			// gera um objeto DOM do xml
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
			// local do relatorio jasper
			String tipoImp = UtilServer.getValorTag(doc.getDocumentElement(), "tpImp", true);
			String jasper = UtilServer.PATH_EMPRESA + "danfe/danfe" + tipoImp + ".jasper";
			// fonte de dados
			String xpath = UtilServer.getValorTag(doc.getDocumentElement(), "nProt", false);
			xpath = xpath == null ? "/nfe/infNFe/det" : "/nfeProc/NFe/infNFe/det";
			JRXmlDataSource ds = new JRXmlDataSource(doc, xpath);
			// parametros
			Map<String, Object> param = new HashMap<String, Object>();
			Element ele = (Element) doc.getElementsByTagName("infNFe").item(0);
			String chave = ele.getAttribute("Id");
			param.put("Logo", "../" + chave.substring(9, 23) + "/logo.png");
			param.put("REPORT_LOCALE", UtilServer.LOCAL);
			// gerando o relatorio
			JasperPrint print = JasperFillManager.fillReport(jasper, param, ds);
			// exportando em pdf
			pdf = JasperExportManager.exportReportToPdf(print);
		} catch (Exception e) {
			UtilServer.LOG.error("Nao gerou o Danfe", e);
			pdf = null;
		}

		return pdf;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String chave = req.getParameter("chave");
		String captcha = req.getParameter("captcha");
		String opcao = req.getParameter("opcao");
		boolean validaCaptcha = false;
		byte[] obj = null;
		String nome = "";

		// valida o captcha
		if (captcha != null && !captcha.isEmpty()) {
			Captcha cap = (Captcha) req.getSession().getAttribute(Captcha.NAME);
			validaCaptcha = cap.isCorrect(captcha);
		}

		if (validaCaptcha) {
			try {
				int notaId = 0;
				String notaXml = "";
				String notaXmlCan = "";

				// busca na saida
				FiltroTexto ft = new FiltroTexto("fisNotaSaidaChave", ECompara.IGUAL, chave);
				FisNotaSaida saida = (FisNotaSaida) selecionar(new FisNotaSaida(), ft, false);
				if (saida != null) {
					notaId = saida.getFisNotaStatus().getFisNotaStatusId();
					notaXml = saida.getFisNotaSaidaXml();
					notaXmlCan = saida.getFisNotaSaidaXmlCancelado();
				} else {
					// busca na entrada
					ft = new FiltroTexto("fisNotaEntradaChave", ECompara.IGUAL, chave);
					FisNotaEntrada entrada = (FisNotaEntrada) selecionar(new FisNotaEntrada(), ft, false);
					if (entrada != null) {
						notaId = entrada.getFisNotaStatus().getFisNotaStatusId();
						notaXml = entrada.getFisNotaEntradaXml();
						notaXmlCan = entrada.getFisNotaEntradaXmlCancelado();
					} else {
						throw new Exception("Nao achou nenhuma NFe.");
					}
				}

				if (notaId == ENotaStatus.AUTORIZADO.getId() || notaId == ENotaStatus.CANCELADO.getId()) {
					if (opcao.equals("2")) {
						obj = notaXml.getBytes();
						nome = chave + "-procNFe.xml";
					} else if (opcao.equals("4")) {
						if (!notaXmlCan.equals("")) {
							obj = notaXmlCan.getBytes();
							nome = chave + "-procCanNFe.xml";
						} else {
							throw new Exception("NFe nao esta cancelada.");
						}
					} else {
						obj = getDanfe(notaXml);
						nome = chave + ".pdf";
					}
					resp.addHeader("Content-Disposition", "attachment; filename=" + nome);
					resp.addHeader("Pragma", "no-cache");
					resp.addIntHeader("Expires", 0);
					resp.addHeader("Content-Type", "application/octet-stream");
				} else {
					obj = ("<script>alert('O status na NFe nao esta autorizado ou cancelado!!');</script>").getBytes();
					resp.addHeader("Content-Type", "text/html");
				}
			} catch (Exception e) {
				UtilServer.LOG.debug("Nenhuma NFe encontrada", e);
				obj = ("<script>alert('Nenhuma NFe encontrada no status solicitado!');</script>").getBytes();
				resp.addHeader("Content-Type", "text/html");
			} finally {
				// codificando e enviando
				resp.setCharacterEncoding("utf-8");
				resp.getOutputStream().write(obj);
				resp.flushBuffer();
			}
		} else {
			resp.addHeader("Content-Type", "text/html");
			resp.getOutputStream().write(("<script>alert('Codigo informado invalido!');</script>").getBytes());
			resp.flushBuffer();
		}
	}

	public String backup(E classe, IFiltro filtro) throws ExportacaoException {
		try {
			// realiza a busca
			Lista<E> lista = selecionar(classe, 0, 0, filtro, false);
			Map<String, byte[]> arquivos = new HashMap<String, byte[]>();
			byte[] obj = null;
			String chave = "";
			String xml = "";
			String cancelada = "";

			for (Dados d : lista.getLista()) {
				if (d instanceof FisNotaSaida) {
					FisNotaSaida nota = (FisNotaSaida) d;
					chave = nota.getFisNotaSaidaChave();
					xml = nota.getFisNotaSaidaXml();
					cancelada = nota.getFisNotaSaidaXmlCancelado();
				} else {
					FisNotaEntrada nota = (FisNotaEntrada) d;
					chave = nota.getFisNotaEntradaChave();
					xml = nota.getFisNotaEntradaXml();
					cancelada = nota.getFisNotaEntradaXmlCancelado();
				}
				// zipa o xml principal
				arquivos.put(chave + ".xml", xml.getBytes());
				// caso tenha o xml de cancelamento
				if (!cancelada.equals("")) {
					arquivos.put(chave + "_cancelada.xml", cancelada.getBytes());
				}
			}
			obj = UtilServer.getZIP(arquivos);
			// valida se teve resultado
			if (obj != null) {
				HttpSession sessao = getThreadLocalRequest().getSession();
				String retorno = sessao.getId() + new Date().getTime();
				sessao.setAttribute(retorno, obj);
				sessao.setAttribute(retorno + "arquivo", "backup_notas.zip");
				return retorno;
			} else {
				UtilServer.LOG.debug("Sem registros.");
				throw new FiscalException(getAuth().getConf().get("msgRegistro"));
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao zipar", ex);
			throw new ExportacaoException(ex.getMessage());
		}
	}

	public String status(int ambiente, int uf, int empresa) throws FiscalException {
		// gerar o objeto
		TConsStatServ consStatServ = new TConsStatServ();
		consStatServ.setTpAmb(ambiente + "");
		consStatServ.setCUF(uf + "");
		consStatServ.setVersao(getAuth().getConf().get("nfe.versao"));
		consStatServ.setXServ("STATUS");
		return status(consStatServ, empresa);
	}

	public String status(TConsStatServ consStatServ, int empresa) throws FiscalException {
		// gerar o objeto
		try {
			JAXBElement<TConsStatServ> element = new br.com.opensig.consstatserv.ObjectFactory().createConsStatServ(consStatServ);
			String xml = UtilServer.objToXml(element, "br.com.opensig.consstatserv");

			return Sefaz.getInstancia(getAuth()).status(xml);
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public String validar(int ambiente, IFiltro filtro, int empresa) throws FiscalException {
		try {
			FiltroTexto ft = new FiltroTexto("fisNotaEntradaRecibo", ECompara.DIFERENTE, "OK");
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft, filtro });
			Lista<FisNotaEntrada> entradas = (Lista<FisNotaEntrada>) selecionar(new FisNotaEntrada(), 0, 0, gf, false);
			int ok = 0;
			int erro = 0;
			int prov = 0;

			for (FisNotaEntrada nota : entradas.getLista()) {
				String resp = situacao(ambiente, nota.getFisNotaEntradaChave(), empresa);
				TRetConsSitNFe situacao = UtilServer.xmlToObj(resp, "br.com.opensig.retconssitnfe");

				// verifica se o status na sefaz é igual ao informado
				if (situacao.getCStat().equals("100")) {
					if (nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId() && situacao.getProtNFe() != null) {
						// valida se a data da nota ainda pode ser cancelada
						int dias = Integer.valueOf(getAuth().getConf().get("nfe.tempo_cancela"));
						Calendar cal = Calendar.getInstance();
						cal.setTime(nota.getFisNotaEntradaData());
						cal.add(Calendar.DATE, dias);

						Date hoje = new Date();
						if (hoje.compareTo(cal.getTime()) > 0) {
							nota.setFisNotaEntradaRecibo("OK");
							ok++;
						} else {
							nota.setFisNotaEntradaRecibo("PROVISORIO");
							prov++;
						}
					} else if (nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.CANCELADO.getId() && situacao.getRetCancNFe() != null) {
						nota.setFisNotaEntradaRecibo("OK");
						ok++;
					} else {
						nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
						nota.setFisNotaEntradaErro("O status da nota de entrada esta diferente da sefaz.");
						erro++;
					}
				} else {
					nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
					nota.setFisNotaEntradaErro("Nao achou a nota na sefaz ou problemas na hora do acesso ao servidor.");
					erro++;
				}
			}
			// salva todas
			salvar((Collection<E>) entradas.getLista(), false);
			return "OK=" + ok + " PROVISORIO=" + prov + " ERRO=" + erro;
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public String situacao(int ambiente, String chave, int empresa) throws FiscalException {
		TConsSitNFe consSitNfe = new TConsSitNFe();
		consSitNfe.setTpAmb(ambiente + "");
		consSitNfe.setChNFe(chave);
		consSitNfe.setVersao(getAuth().getConf().get("nfe.versao"));
		consSitNfe.setXServ("CONSULTAR");
		return situacao(consSitNfe, empresa);
	}

	public String situacao(TConsSitNFe consSitNfe, int empresa) throws FiscalException {
		// gerar o objeto
		try {
			JAXBElement<TConsSitNFe> element = new br.com.opensig.conssitnfe.ObjectFactory().createConsSitNFe(consSitNfe);
			String xml = UtilServer.objToXml(element, "br.com.opensig.conssitnfe");

			return Sefaz.getInstancia(getAuth()).situacao(xml);
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public String cadastro(int ambiente, int ibge, String uf, String tipo, String doc, int empresa) throws FiscalException {
		InfCons infCons = new InfCons();
		infCons.setUF(TUfCons.valueOf(uf));
		infCons.setXServ("CONS-CAD");
		doc = doc.replaceAll("\\D", "");
		if (tipo.equals("CNPJ")) {
			infCons.setCNPJ(doc);
		} else if (tipo.equals("CPF")) {
			infCons.setCPF(doc);
		} else {
			infCons.setIE(doc);
		}

		TConsCad consCad = new TConsCad();
		consCad.setVersao(getAuth().getConf().get("nfe.versao"));
		consCad.setInfCons(infCons);
		return cadastro(consCad, empresa, ambiente, ibge);
	}

	public String cadastro(TConsCad consCad, int empresa, int ambiente, int ibge) throws FiscalException {
		// gerar o objeto
		try {
			JAXBElement<TConsCad> element = new br.com.opensig.conscad.ObjectFactory().createConsCad(consCad);
			String xml = UtilServer.objToXml(element, "br.com.opensig.conscad");

			return Sefaz.getInstancia(getAuth()).cadastro(xml, ibge, ambiente);
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public String enviarNFe(String xml, int empresa) throws FiscalException {
		// gerar o objeto
		try {
			return Sefaz.getInstancia(getAuth()).enviarNFe(xml);
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public Map<String, String> analisarNFeSaida(FisNotaSaida saida) throws FiscalException {
		String para = null;
		Map<String, String> resp = new HashMap<String, String>();

		// gerar o objeto
		try {
			String proc = situacao(Integer.valueOf(getAuth().getConf().get("nfe.tipoamb")), saida.getFisNotaSaidaChave(), saida.getEmpEmpresa().getEmpEmpresaId());
			TRetConsSitNFe sit = UtilServer.xmlToObj(proc, "br.com.opensig.retconssitnfe");

			// verifica se sucesso
			if (sit.getProtNFe() != null) {
				InfProt inf = sit.getProtNFe().getInfProt();
				if (inf.getCStat().equals("100")) {
					saida.setFisNotaSaidaXml(RetornarNfeSaida.montaProcNfe(saida.getFisNotaSaidaXml(), proc, getAuth().getConf().get("nfe.versao")));
					saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.AUTORIZADO));
					saida.setFisNotaSaidaProtocolo(inf.getNProt());

					Document doc = UtilServer.getXml(saida.getFisNotaSaidaXml());
					para = UtilServer.getValorTag(doc.getDocumentElement(), "email", false);
				} else {
					saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
					saida.setFisNotaSaidaErro(inf.getXMotivo());
				}
			} else if (sit.getRetCancNFe() != null) {
				InfCanc inf = sit.getRetCancNFe().getInfCanc();
				if (inf.getCStat().equals("100")) {
					saida.setFisNotaSaidaXml(EnviarNfeCanceladaSaida.montaProcCancNfe(saida.getFisNotaSaidaXml(), proc, getAuth().getConf().get("nfe.versao")));
					saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.CANCELADO));
					saida.setFisNotaSaidaProtocoloCancelado(inf.getNProt());

					Document doc = UtilServer.getXml(saida.getFisNotaSaidaXml());
					para = UtilServer.getValorTag(doc.getDocumentElement(), "email", false);
				} else {
					saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
					saida.setFisNotaSaidaErro(inf.getXMotivo());
				}
			} else {
				saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
				saida.setFisNotaSaidaErro(sit.getXMotivo());
			}
		} catch (Exception e) {
			saida.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
			saida.setFisNotaSaidaErro(e.getMessage());
		} finally {
			if (saida.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
				resp.put("status", ENotaStatus.ERRO.name());
				resp.put("msg", saida.getFisNotaSaidaErro());

			} else {
				resp.put("status", ENotaStatus.AUTORIZADO.name());
				resp.put("msg", ENotaStatus.AUTORIZADO.name());
			}

			try {
				salvar((E) saida, false);
				if (para != null) {
					EnviarEmail email = new EnviarEmail(this, saida, getAuth());
					Thread enviar = new Thread(email);
					enviar.start();
				}
			} catch (OpenSigException e) {
				UtilServer.LOG.error("Problemas ao salvar a saida apos consultar situacao.", e);
			}
		}

		return resp;
	}

	public Map<String, String> analisarNFeEntrada(FisNotaEntrada entrada) throws FiscalException {
		Map<String, String> resp = new HashMap<String, String>();

		// gerar o objeto
		try {
			String proc = situacao(Integer.valueOf(getAuth().getConf().get("nfe.tipoamb")), entrada.getFisNotaEntradaChave(), entrada.getEmpEmpresa().getEmpEmpresaId());
			TRetConsSitNFe sit = UtilServer.xmlToObj(proc, "br.com.opensig.retconssitnfe");

			// verifica se sucesso
			if (sit.getProtNFe() != null) {
				InfProt inf = sit.getProtNFe().getInfProt();
				if (inf.getCStat().equals("100")) {
					entrada.setFisNotaEntradaXml(RetornarNfeEntrada.montaProcNfe(entrada.getFisNotaEntradaXml(), proc, getAuth().getConf().get("nfe.versao")));
					entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.AUTORIZADO));
					entrada.setFisNotaEntradaProtocolo(inf.getNProt());
				} else {
					entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
					entrada.setFisNotaEntradaErro(inf.getXMotivo());
				}
			} else if (sit.getRetCancNFe() != null) {
				InfCanc inf = sit.getRetCancNFe().getInfCanc();
				if (inf.getCStat().equals("100")) {
					entrada.setFisNotaEntradaXml(EnviarNfeCanceladaEntrada.montaProcCancNfe(entrada.getFisNotaEntradaXml(), proc, getAuth().getConf().get("nfe.versao")));
					entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.CANCELADO));
					entrada.setFisNotaEntradaProtocoloCancelado(inf.getNProt());
				} else {
					entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
					entrada.setFisNotaEntradaErro(inf.getXMotivo());
				}
			} else {
				entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
				entrada.setFisNotaEntradaErro(sit.getXMotivo());
			}
		} catch (Exception e) {
			entrada.setFisNotaStatus(new FisNotaStatus(ENotaStatus.ERRO));
			entrada.setFisNotaEntradaErro(e.getMessage());
		} finally {
			if (entrada.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
				resp.put("status", ENotaStatus.ERRO.name());
				resp.put("msg", entrada.getFisNotaEntradaErro());

			} else {
				resp.put("status", ENotaStatus.AUTORIZADO.name());
				resp.put("msg", ENotaStatus.AUTORIZADO.name());
			}

			try {
				salvar((E) entrada, false);
			} catch (OpenSigException e) {
				UtilServer.LOG.error("Problemas ao salvar a entrada apos consultar situacao.", e);
			}
		}

		return resp;
	}

	public String receberNFe(String xml, int empresa, String recibo) throws FiscalException {
		// gerar o objeto
		try {
			Document doc = UtilServer.getXml(xml);
			String uf = UtilServer.getValorTag(doc.getDocumentElement(), "cUF", false);
			String serie = UtilServer.getValorTag(doc.getDocumentElement(), "serie", false);
			String ambiente = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);

			// adicionando dados ao xml
			TConsReciNFe reci = new TConsReciNFe();
			reci.setVersao(getAuth().getConf().get("nfe.versao"));
			reci.setTpAmb(ambiente);
			reci.setNRec(recibo);

			JAXBElement<TConsReciNFe> element = new br.com.opensig.consrecinfe.ObjectFactory().createConsReciNFe(reci);
			xml = UtilServer.objToXml(element, "br.com.opensig.consrecinfe");

			return Sefaz.getInstancia(getAuth()).retornoNFe(xml, uf, ambiente, serie);
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public Map<String, String> cancelarSaida(FisNotaSaida saida, String motivo) throws FiscalException {
		try {
			GerarNfeCanceladaSaida gerar = new GerarNfeCanceladaSaida(null, this, saida, motivo, getAuth());
			gerar.execute();
			saida = gerar.getNota();

			Map<String, String> resp = new HashMap<String, String>();
			if (saida.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
				resp.put("status", ENotaStatus.ERRO.name());
			} else {
				resp.put("status", ENotaStatus.CANCELADO.name());
			}
			resp.put("msg", saida.getFisNotaSaidaErro());
			return resp;
		} catch (OpenSigException e) {
			UtilServer.LOG.error("Erro ao cancelar nfe saida.", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public Map<String, String> cancelarEntrada(FisNotaEntrada entrada, String motivo) throws FiscalException {
		try {
			GerarNfeCanceladaEntrada gerar = new GerarNfeCanceladaEntrada(null, this, entrada, motivo, getAuth());
			gerar.execute();
			entrada = gerar.getNota();

			Map<String, String> resp = new HashMap<String, String>();
			if (entrada.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
				resp.put("status", ENotaStatus.ERRO.name());
			} else {
				resp.put("status", ENotaStatus.CANCELADO.name());
			}
			resp.put("msg", entrada.getFisNotaEntradaErro());
			return resp;
		} catch (OpenSigException e) {
			UtilServer.LOG.error("Erro ao cancelar nfe entrada.", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public String cancelar(String xml, int empresa) throws FiscalException {
		// gerar o objeto
		try {
			return Sefaz.getInstancia(getAuth()).cancelar(xml);
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public Map<String, String> inutilizarSaida(FisNotaSaida saida, String motivo, int ini, int fim) throws FiscalException {
		try {
			// pega a ultima nfe para usar dados como referencia
			if (saida == null) {
				saida = new FisNotaSaida();
				FiltroObjeto fo = new FiltroObjeto("fisNotaStatus", ECompara.DIFERENTE, new FisNotaStatus(ENotaStatus.INUTILIZANDO));
				FiltroObjeto fo1 = new FiltroObjeto("fisNotaStatus", ECompara.DIFERENTE, new FisNotaStatus(ENotaStatus.INUTILIZADO));
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fo1 });
				Number max = buscar(saida, "fisNotaSaidaNumero", EBusca.MAXIMO, gf);
				FiltroNumero fn = new FiltroNumero("fisNotaSaidaNumero", ECompara.IGUAL, max);
				saida = (FisNotaSaida) selecionar(saida, fn, false);
			}

			GerarNfeInutilizadaSaida gerar = new GerarNfeInutilizadaSaida(null, this, saida, motivo, ini, fim, getAuth());
			gerar.execute();
			saida = gerar.getNota();

			Map<String, String> resp = new HashMap<String, String>();
			if (saida.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
				resp.put("status", ENotaStatus.ERRO.name());
			} else {
				resp.put("status", ENotaStatus.INUTILIZADO.name());
			}
			resp.put("msg", saida.getFisNotaSaidaErro());
			return resp;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao inutilizar nfe.", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public Map<String, String> inutilizarEntrada(FisNotaEntrada entrada, String motivo, int ini, int fim) throws FiscalException {
		try {
			// pega a ultima nfe para usar dados como referencia
			if (entrada == null) {
				entrada = new FisNotaEntrada();
				FiltroObjeto fo = new FiltroObjeto("fisNotaStatus", ECompara.DIFERENTE, new FisNotaStatus(ENotaStatus.INUTILIZANDO));
				FiltroObjeto fo1 = new FiltroObjeto("fisNotaStatus", ECompara.DIFERENTE, new FisNotaStatus(ENotaStatus.INUTILIZADO));
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fo1 });
				Number max = buscar(entrada, "fisNotaEntradaNumero", EBusca.MAXIMO, gf);
				FiltroNumero fn = new FiltroNumero("fisNotaEntradaNumero", ECompara.IGUAL, max);
				entrada = (FisNotaEntrada) selecionar(entrada, fn, false);
			}

			GerarNfeInutilizadaEntrada gerar = new GerarNfeInutilizadaEntrada(null, this, entrada, motivo, ini, fim, getAuth());
			gerar.execute();
			entrada = gerar.getNota();

			Map<String, String> resp = new HashMap<String, String>();
			if (entrada.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
				resp.put("status", ENotaStatus.ERRO.name());
			} else {
				resp.put("status", ENotaStatus.INUTILIZADO.name());
			}
			resp.put("msg", entrada.getFisNotaEntradaErro());
			return resp;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao inutilizar nfe.", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public String inutilizar(String xml, int empresa) throws FiscalException {
		// gerar o objeto
		try {
			return Sefaz.getInstancia(getAuth()).inutilizar(xml);
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	public Map<String, String> salvarSaida(String xml, FisNotaStatus status, EmpEmpresa empresa) throws FiscalException {
		try {
			SalvarSaida salvarSaida = new SalvarSaida(null, xml, status, getAuth());
			salvarSaida.execute();
			FisNotaSaida saida = salvarSaida.getNota();

			Map<String, String> resp = new HashMap<String, String>();
			resp.put("status", saida.getFisNotaStatus().getFisNotaStatusDescricao());
			resp.put("msg", saida.getFisNotaSaidaErro());
			return resp;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao inutilizar nfe.", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public Map<String, String> salvarEntrada(String xml, FisNotaStatus status, EmpEmpresa empresa) throws FiscalException {
		try {
			SalvarEntrada salvarEntrada = new SalvarEntrada(null, xml, status, getAuth());
			salvarEntrada.execute();
			FisNotaEntrada entrada = salvarEntrada.getNota();

			Map<String, String> resp = new HashMap<String, String>();
			resp.put("status", entrada.getFisNotaStatus().getFisNotaStatusDescricao());
			resp.put("msg", entrada.getFisNotaEntradaErro());
			return resp;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao inutilizar nfe.", e);
			throw new FiscalException(e.getMessage());
		}
	}

	public void salvarCertificado(FisCertificado certificado) throws FiscalException {
		try {
			// pega o certificado
			String cnpj = certificado.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("\\D", "");
			String pfx = UtilServer.PATH_EMPRESA + cnpj + "/certificado.pfx";
			// seta o tipo
			KeyStore keystore = KeyStore.getInstance(("PKCS12"));
			keystore.load(new FileInputStream(pfx), certificado.getFisCertificadoSenha().toCharArray());
			// acha as datas
			Enumeration<String> eAliases = keystore.aliases();
			while (eAliases.hasMoreElements()) {
				String alias = eAliases.nextElement();
				X509Certificate cert = (X509Certificate) keystore.getCertificate(alias);
				certificado.setFisCertificadoInicio(cert.getNotBefore());
				certificado.setFisCertificadoFim(cert.getNotAfter());
				break;
			}
			// valida se esta dentro do prazo de utilizacao
			Date hoje = new Date();
			if (hoje.before(certificado.getFisCertificadoInicio())) {
				throw new FiscalException("O certificado somente sera valido a partir de " + UtilServer.formataData(certificado.getFisCertificadoInicio(), DateFormat.MEDIUM));
			} else if (hoje.after(certificado.getFisCertificadoFim())) {
				throw new FiscalException("O certificado era valido ate a data de " + UtilServer.formataData(certificado.getFisCertificadoFim(), DateFormat.MEDIUM));
			} else {
				// criptografa a senha
				BasicTextEncryptor seguranca = new BasicTextEncryptor();
				seguranca.setPassword(UtilServer.CHAVE);
				String encriptado = seguranca.encrypt(certificado.getFisCertificadoSenha());
				certificado.setFisCertificadoSenha(encriptado);
				salvar((E) certificado);
			}
		} catch (Exception e) {
			throw new FiscalException(e.getMessage());
		}
	}

	@Override
	public void deletar(E unidade) throws CoreException {
		super.deletar(unidade);
		if (unidade instanceof FisSpedFiscal) {
			FisSpedFiscal sped = (FisSpedFiscal) unidade;
			File f = getEfd(sped);
			f.delete();
		}
	}

	@Override
	public E salvar(E unidade) throws CoreException {
		super.salvar(unidade, false);
		if (unidade instanceof FisSpedFiscal) {
			// deleta o antigo efd
			FisSpedFiscal sped = (FisSpedFiscal) unidade;
			sped.setId(unidade.getId());
			File f = getEfd(sped);
			f.delete();

			// deleta o recibo se existir
			String novoPath = f.getPath().replace("ZIP", "REC");
			f = new File(novoPath);
			f.delete();

			// seta o arquivo para o novo TXT e chama a thread que gera o EFD
			f = new File(f.getPath().replace("REC", "TXT"));
			f.delete();
			Thread efd = new Thread(new EFD(f, sped, getAuth()));
			efd.start();
		}

		return unidade;
	}

	private File getEfd(FisSpedFiscal sped) {
		// identifica o usuario/empresa
		HttpSession sessao = getThreadLocalRequest().getSession();
		Autenticacao auth = SessionManager.LOGIN.get(sessao);

		// controi o path do arquivo
		String cnpj = auth.getEmpresa()[5].replaceAll("\\D", "");
		String nome = sped.getFisSpedFiscalTipo();
		if (sped.getFisSpedFiscalMes() > 9) {
			nome += sped.getFisSpedFiscalAno() + "" + sped.getFisSpedFiscalMes();
		} else {
			nome += sped.getFisSpedFiscalAno() + "0" + sped.getFisSpedFiscalMes();
		}
		String path = UtilServer.PATH_EMPRESA + cnpj + "/sped/" + nome + ".ZIP";

		// deletando o arquivo sped
		File f = new File(path);
		return f;
	}
}
