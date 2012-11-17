package br.com.opensig.fiscal.server.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import br.com.opensig.comercial.server.rest.ARest;
import br.com.opensig.comercial.server.rest.RestException;
import br.com.opensig.conscad.TConsCad;
import br.com.opensig.conssitnfe.TConsSitNFe;
import br.com.opensig.consstatserv.TConsStatServ;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoImportar;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.server.SessionManager;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.controlador.comando.ComandoCadastro;
import br.com.opensig.fiscal.client.controlador.comando.ComandoSituacao;
import br.com.opensig.fiscal.client.controlador.comando.ComandoStatus;
import br.com.opensig.fiscal.client.servico.FiscalService;
import br.com.opensig.fiscal.server.FiscalServiceImpl;
import br.com.opensig.fiscal.server.NFe;
import br.com.opensig.fiscal.server.acao.SalvarEntrada;
import br.com.opensig.fiscal.server.acao.SalvarSaida;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.permissao.client.controlador.comando.ComandoPermiteWS;
import br.com.opensig.permissao.server.PermissaoServiceImpl;

import com.sun.jersey.core.util.Base64;

/**
 * Classe que representa a comunicacao via Rest
 * 
 * @author Pedro H. Lira
 */
@Provider
@Path("/")
public class RestFiscal extends ARest {

	// origem do pedido
	@Context
	private HttpServletRequest request;
	// empresa do usuario
	private EmpEmpresa empresa;
	// autenticacao
	private Autenticacao auth;

	/**
	 * Construtor padrao.
	 */
	public RestFiscal() {
		super();
		log = Logger.getLogger(RestFiscal.class);
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Override
	public String ajuda() throws RestException {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<center>Acesse a URL abaixo:</center><br>");
		sb.append("<center><a href='/openfe/application.wadl'>WADL</a></center>");
		sb.append("</html>");
		return sb.toString();
	}

	/**
	 * Metodo que verifica o status dos servidores da Sefaz.
	 * 
	 * @param xml
	 *            dados para efetuar a cosulta com o tipo consStatServ_v2.00.xsd Exemplo:
	 * 
	 *            <code>
	 *            	<?xml version="1.0" encoding="UTF-8"?>
	 *            	<consStatServ versao="2.00" xmlns="http://www.portalfiscal.inf.br/nfe">
	 *            		<tpAmb>2</tpAmb>
	 *            		<cUF>27</cUF>
	 *            		<xServ>STATUS</xServ>
	 *            	</consStatServ>
	 *            </code>
	 * @return o xml do tipo retConsStatServ_v2.00.xsd
	 * @exception RestException
	 *                caso nao consiga recuperar o status.
	 */
	@Path("status")
	@PUT
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String status(String xml) throws RestException {
		logar();

		// valida se o usuario tem permissao ao comando
		if (validaFuncao(ComandoStatus.class)) {
			try {
				// valida o xml e gera o objeto
				String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_status"));
				NFe.validarXML(xml, xsd);
				TConsStatServ nStatus = UtilServer.xmlToObj(xml, "br.com.opensig.consstatserv");
				// faz a chamada na sefaz
				FiscalServiceImpl fiscal = new FiscalServiceImpl(auth);
				xml = fiscal.status(nStatus, empresa.getEmpEmpresaId());
				return xml;
			} catch (Exception ex) {
				log.error(ex);
				throw new RestException(ex.getMessage());
			}
		} else {
			throw new RestException("O usuario nao tem permissao para usar o comando [status]");
		}
	}

	/**
	 * Metodo que verifica os dados das empresas na Sefaz.
	 * 
	 * @param xml
	 *            dados para efetuar a cosultado com o tipo consCad_v2.00.xsd Exemplo:
	 * 
	 *            <code>
	 *            	<?xml version="1.0" encoding="UTF-8"?>
	 *            	<ConsCad versao="2.00" xmlns="http://www.portalfiscal.inf.br/nfe">
	 *            		<infCons>
	 *            			<xServ>CONS-CAD</xServ>
	 *            			<UF>SP</UF>
	 *            			<CNPJ>04062314000170</CNPJ>
	 *            			<tpAmb>1</tpAmb>
	 *            			<cUF>35</cUF>
	 *            		</infCons>
	 *            	</ConsCad>
	 *            </code>
	 * @return o xml do tipo retConsCad_v2.00.xsd
	 * @exception RestException
	 *                caso nao consiga recuperar o cadastro.
	 */
	@Path("cadastro")
	@PUT
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String cadastro(String xml) throws RestException {
		logar();

		// valida se o usuario tem permissao ao comando
		if (validaFuncao(ComandoCadastro.class)) {
			try {
				// achando o ambiente e o ibge e removendo
				Document doc = UtilServer.getXml(xml);
				String amb = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);
				String ibge = UtilServer.getValorTag(doc.getDocumentElement(), "cUF", false);
				xml = UtilServer.getXml(doc);
				xml = xml.replaceAll("<tpAmb>\\d{1}</tpAmb>|<cUF>\\d{2}</cUF>", "");
				// valida o xml e gera o objeto
				String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_cadastro"));
				NFe.validarXML(xml, xsd);
				TConsCad cadastro = UtilServer.xmlToObj(xml, "br.com.opensig.conscad");
				// faz a chamada na sefaz
				FiscalServiceImpl fiscal = new FiscalServiceImpl(auth);
				xml = fiscal.cadastro(cadastro, empresa.getEmpEmpresaId(), Integer.valueOf(amb), Integer.valueOf(ibge));
				return xml;
			} catch (Exception ex) {
				log.error(ex);
				throw new RestException(ex.getMessage());
			}
		} else {
			throw new RestException("O usuario nao tem permissao para usar o comando [cadastro]");
		}
	}

	/**
	 * Metodo que verifica a situacao de uma NFe
	 * 
	 * @param xml
	 *            dados para efetuar a cosultada com o tipo consSitNFe_v2.00.xsd
	 * 
	 *            <code>
	 *            	<?xml version="1.0" encoding="UTF-8"?>
	 *            	<consSitNFe versao="2.00" xmlns=\"http://www.portalfiscal.inf.br/nfe">
	 *            		<tpAmb>1</tpAmb>
	 *            		<xServ>CONSULTAR</xServ>
	 *            		<chNFe>COLOQUE AQUI A CHAVE DA NFE</chNFe>
	 *            	</consSitNFe>
	 *            </code>
	 * @return num xml do tipo retConsSitNFe_v2.00.xsd
	 * @exception RestException
	 *                caso nao consiga recuperar a situacao.
	 */
	@Path("situacao")
	@PUT
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String situacao(String xml) throws RestException {
		logar();

		// valida se o usuario tem permissao ao comando
		if (validaFuncao(ComandoSituacao.class)) {
			try {
				// valida o xml e gera o objeto
				String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_situacao"));
				NFe.validarXML(xml, xsd);
				TConsSitNFe situacao = UtilServer.xmlToObj(xml, "br.com.opensig.conssitnfe");
				// faz a chamada na sefaz
				FiscalServiceImpl fiscal = new FiscalServiceImpl(auth);
				xml = fiscal.situacao(situacao, empresa.getEmpEmpresaId());
				return xml;
			} catch (Exception ex) {
				log.error(ex);
				throw new RestException(ex.getMessage());
			}
		} else {
			throw new RestException("O usuario nao tem permissao para usar o comando [situacao]");
		}
	}

	/**
	 * Metodo que salva um registro no modulo Fiscal, funcao Saida NFe.
	 * 
	 * @param xml
	 *            um registro de NFe [AUTORIZANDO, AUTORIZADO, CANCELANDO, CANCELADO, INUTILIZANDO ou INUTILIZADO].
	 * @return um xml do tipo resp.xsd, se for uma autorizando ou ja processada pela sefaz, para cancelando e inutilizando sera respondido com o xml confirmado da sefaz
	 * @exception RestException
	 *                caso nao consiga salvar a saida.
	 */
	@Path("saida")
	@PUT
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String saida(String xml) throws RestException {
		logar();

		// valida se o usuario tem permissao ao comando
		if (validaAcao(ComandoImportar.class)) {
			try {
				// cria o doc e acha o status
				Document doc = UtilServer.getXml(xml);
				ENotaStatus nStatus = NFe.validarSaida(doc, empresa.getEmpEntidade().getEmpEntidadeDocumento1());
				if (nStatus == ENotaStatus.AUTORIZANDO || nStatus == ENotaStatus.CANCELANDO || nStatus == ENotaStatus.INUTILIZANDO) {
					// caso o xml nao esteja assinado
					if (doc.getElementsByTagName("Signature").item(0) == null) {
						// assina
						xml = NFe.assinarXML(doc, nStatus, auth);
					}
					// valida
					String xsd = UtilServer.getRealPath(auth.getConf().get("nfe.xsd_" + nStatus.toString().toLowerCase()));
					NFe.validarXML(xml, xsd);
				}
				// salva no sistema
				FisNotaStatus nfStatus = new FisNotaStatus(nStatus);
				SalvarSaida saida = new SalvarSaida(null, xml, nfStatus, auth);
				saida.execute();

				// analisa e retorna o resultado
				String msg;
				if (nStatus == ENotaStatus.AUTORIZANDO) {
					msg = "Utilize o comando [sinc] apos " + auth.getConf().get("nfe.tempo_retorno") + " segundos para obter a resposta/xml autorizacao da sefaz.";
				} else if (nStatus == ENotaStatus.INUTILIZANDO) {
					msg = saida.getNota().getFisNotaSaidaXml();
				} else if (nStatus == ENotaStatus.CANCELANDO) {
					msg = saida.getNota().getFisNotaSaidaXmlCancelado();
				} else {
					msg = "Registro salvo com sucesso.";
				}
				return resposta(msg);
			} catch (Exception ex) {
				log.error(ex);
				throw new RestException(ex.getMessage());
			}
		} else {
			throw new RestException("O usuario nao tem permissao para usar o comando [saida]");
		}
	}

	/**
	 * Metodo que salva um registro no modulo Fiscal, funcao Entrada NFe.
	 * 
	 * @param xml
	 *            um registro de NFe [AUTORIZADO ou CANCELADO].
	 * @return um xml do tipo resp.xsd, e sera feita a validacao na sefaz, podendo estar OK, PROVISORIO ou ERRO
	 * @exception RestException
	 *                caso nao consiga salvar a entrada.
	 */
	@Path("entrada")
	@PUT
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	public String entrada(String xml) throws RestException {
		logar();

		// valida se o usuario tem permissao ao comando
		if (validaAcao(ComandoImportar.class)) {
			try {
				// cria o doc e acha o status
				Document doc = UtilServer.getXml(xml);
				ENotaStatus nStatus = NFe.validarEntrada(doc, empresa.getEmpEntidade().getEmpEntidadeDocumento1());
				// salva no sistema
				FisNotaStatus nfStatus = new FisNotaStatus(nStatus);
				SalvarEntrada entrada = new SalvarEntrada(null, xml, nfStatus, auth);
				entrada.execute();

				// analisa e retorna o resultado
				String msg;
				if (nStatus == ENotaStatus.AUTORIZANDO) {
					msg = "Utilize o comando [sinc] apos " + auth.getConf().get("nfe.tempo_retorno") + " segundos para obter a resposta/xml autorizacao da sefaz.";
				} else if (nStatus == ENotaStatus.INUTILIZANDO) {
					msg = entrada.getNota().getFisNotaEntradaXml();
				} else if (nStatus == ENotaStatus.CANCELANDO) {
					msg = entrada.getNota().getFisNotaEntradaXmlCancelado();
				} else {
					msg = "Registro salvo com sucesso.";
					if (entrada.getNota().getFisNotaEntradaRecibo().equals("ERRO")) {
						msg += "Nao foi possivel validar na sefaz, ou os status sao diferentes.";
					}
				}
				return resposta(msg);
			} catch (Exception ex) {
				log.error(ex);
				throw new RestException(ex.getMessage());
			}
		} else {
			throw new RestException("O usuario nao tem permissao para usar o comando [entrada]");
		}
	}

	/**
	 * Metodo que faz a busca dentro do sistema para devolver o status atual de uma NFe, para que seja sincronizado com o ERP solicitante.
	 * 
	 * @param chave
	 *            o texto com 44 digitos da chave NFe ou o numero da nota inutilizada.
	 * @return o XML da NFe ou a mensagem de erro.
	 * @exception RestException
	 *                caso nao consiga recuperar a NFe.
	 */
	@Path("sinc")
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_XML)
	public String sinc(String chave) throws RestException {
		logar();

		// valida se o usuario tem permissao ao comando
		if (validaAcao(ComandoImportar.class)) {
			try {
				String msg;

				// verifica se saida ou entrada
				if (chave.length() == 44) {
					IFiltro filtro;
					FiscalService service = new FiscalServiceImpl(auth);
					if (!chave.substring(6, 20).equals(cnpj) || chave.substring(22, 25).equals("000")) {
						// entrada
						filtro = new FiltroTexto("fisNotaEntradaChave", ECompara.IGUAL, chave);
						FisNotaEntrada nota = (FisNotaEntrada) service.selecionar(new FisNotaEntrada(), filtro, false);

						if (nota != null) {
							if (nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
								msg = nota.getFisNotaEntradaErro();
							} else if (nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.CANCELANDO.getId() || nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.CANCELADO.getId()) {
								msg = nota.getFisNotaEntradaXmlCancelado();
							} else {
								msg = nota.getFisNotaEntradaXml();
							}
						} else {
							msg = "Nao foi encontrado a nota de entrada com a chave informada :: " + chave;
						}
					} else {
						// saida
						filtro = new FiltroTexto("fisNotaSaidaChave", ECompara.IGUAL, chave);
						FisNotaSaida nota = (FisNotaSaida) service.selecionar(new FisNotaSaida(), filtro, false);

						if (nota != null) {
							if (nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
								msg = nota.getFisNotaSaidaErro();
							} else if (nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.CANCELANDO.getId() || nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.CANCELADO.getId()) {
								msg = nota.getFisNotaSaidaXmlCancelado();
							} else {
								msg = nota.getFisNotaSaidaXml();
							}
						} else {
							msg = "Nao foi encontrado a nota de saida com a chave informada :: " + chave;
						}
					}
				} else {
					// inutilizacao
					FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, empresa);
					FiltroNumero fn = new FiltroNumero("fisNotaSaidaNumero", ECompara.IGUAL, chave);
					GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fn });
					FisNotaSaida nota = (FisNotaSaida) service.selecionar(new FisNotaSaida(), gf, false);

					if (nota != null) {
						if (nota.getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.ERRO.getId()) {
							msg = nota.getFisNotaSaidaErro();
						} else {
							msg = nota.getFisNotaSaidaXml();
						}
					} else {
						msg = "Nao foi encontrado a nota de inutilizada com o numero informado :: " + chave;
					}
				}
				return resposta(msg);
			} catch (Exception ex) {
				log.error(ex);
				throw new RestException(ex.getMessage());
			}
		} else {
			throw new RestException("O usuario nao tem permissao para usar o comando [entrada]");
		}
	}

	/**
	 * Metodo que faz a busca dentro do sistema para devolver o danfe em formato PDF de uma NFe.
	 * 
	 * @param chave
	 *            o texto com 44 digitos da chave NFe.
	 * @return o danfe da NFe em formato PDF.
	 * @exception RestException
	 *                caso nao consiga gerar o Danfe da NFe.
	 */
	@Path("sinc")
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] danfe(String chave) throws RestException {
		// verifica se saida ou entrada
		if (chave.length() == 44) {
			String msg = sinc(chave);
			int posIni = msg.indexOf("<msg>") + 5;
			int posFin = msg.indexOf("</msg>");
			String xml = msg.substring(posIni, posFin);
			return new FiscalServiceImpl(auth).getDanfe(xml);
		} else {
			return null;
		}
	}

	/**
	 * Metodo que autoriza o acesso do cliente ao servidor REST, usando as informacoes enviadas pela Autorization Basic (usuario@cnpj do cliente com a senha criptografa com SHA-1)
	 * 
	 * @throws RestException
	 *             dispara um erro caso nao consiga.
	 */
	private void logar() throws RestException {
		try {
			auth = SessionManager.LOGIN.get(request.getSession());
			// recupera os dados de autenticacao
			String header = headers.getRequestHeader("authorization").get(0);
			header = header.substring("Basic ".length());
			String[] creds = Base64.base64Decode(header).split(":");
			String[] login = creds[0].split("@");
			// separado os dados
			String usuario = login[0];
			cnpj = login[1].replaceAll("[^0-9]", "");
			String senha = creds[1];

			// seleciona a empresa
			FiltroTexto ft = new FiltroTexto("empEntidade.empEntidadeDocumento1", ECompara.IGUAL, UtilServer.formataTexto(cnpj, "##.###.###/####-##"));
			empresa = (EmpEmpresa) service.selecionar(new EmpEmpresa(), ft, false);

			if (auth == null) {
				PermissaoServiceImpl permisao = new PermissaoServiceImpl();
				auth = permisao.entrar(request.getSession(), usuario, senha, "", empresa.getEmpEmpresaId(), false);
			}
			// realiza a validacao
			if (auth == null) {
				throw new RestException(Status.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException("Informar os dados de autenticacao! usuario@cnpj + sha1(senha)");
		}

		// valida a permissao de uso do WS
		if (!validaAcao(ComandoPermiteWS.class)) {
			throw new RestException("O usuario nao tem permissao para acessar o sistema pelo WS.");
		}
	}

	/**
	 * Metodo que valida se a funcao solicitada esta permitida para o usuario logado.
	 * 
	 * @param comando
	 *            a classe do comando de funcao.
	 * @return verdadeiro se tiver permissao, falso caso contrario.
	 */
	private boolean validaFuncao(Class<? extends IComando> comando) {
		for (SisModulo modulo : auth.getModulos()) {
			for (SisFuncao funcao : modulo.getSisFuncoes()) {
				if (funcao.getSisFuncaoClasse().equals(comando.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metodo que valida se a acao solicitada esta permitida para o usuario logado.
	 * 
	 * @param comando
	 *            a classe do comando de acao.
	 * @return verdadeiro se tiver permissao, falso caso contrario.
	 */
	private boolean validaAcao(Class<? extends IComando> comando) {
		for (SisModulo modulo : auth.getModulos()) {
			for (SisFuncao funcao : modulo.getSisFuncoes()) {
				SisAcao acao = UtilClient.getAcaoPermitida(funcao, comando);
				if (acao != null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metodo que gera a resposta em XML.
	 * 
	 * @param msg
	 *            a mensagem do retorno.
	 * @return a resposta em formato XML.
	 */
	private String resposta(String msg) {
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<opensig>");
		sb.append("<msg><![CDATA[" + msg + "]]></msg>");
		sb.append("</opensig>");
		return sb.toString();
	}
}
