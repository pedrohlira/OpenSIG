package br.com.opensig.comercial.server.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;

import com.sun.jersey.core.util.Base64;

/**
 * Classe abstrata que fornece metodos padroes do REST e sua validacao no servidor.
 * 
 * @author Pedro H. Lira
 */
public abstract class ARest {

	/**
	 * O cabecalho de envio de dados no contexto atual.
	 */
	@Context
	protected HttpHeaders headers;
	/**
	 * O objeto de persistencia dos dados.
	 */
	protected CoreServiceImpl service;
	/**
	 * O cnpj informado na validacao.
	 */
	protected String cnpj;
	/**
	 * O serial do ECF informado na validacao.
	 */
	protected String serie;
	/**
	 * A impressora reconhecida na autorizacao.
	 */
	protected ComEcf ecf;
	/**
	 * O objeto de log do sistema.
	 */
	protected Logger log;

	/**
	 * Construtor padrao.
	 */
	public ARest() {
		service = new CoreServiceImpl();
	}

	/**
	 * Metodo que retorna o WADL para acessar o REST.
	 * 
	 * @return um texto em formato XML.
	 * 
	 * @throws RestException
	 *             dispara um erro caso nao consiga.
	 */
	protected String ajuda() throws RestException {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<center>Acesse a URL abaixo:</center><br>");
		sb.append("<center><a href='/openpdv/application.wadl'>WADL</a></center>");
		sb.append("</html>");
		return sb.toString();
	}

	/**
	 * Metodo que recupera no sistema a impressora pela serie informada.
	 * 
	 * @param serie
	 *            o campos usado como filtro.
	 * 
	 * @return um objeto de impressora.
	 * 
	 * @throws RestException
	 *             dispara um erro caso nao consiga.
	 */
	protected ComEcf getImp(String serie) throws RestException {
		try {
			FiltroTexto ft = new FiltroTexto("comEcfSerie", ECompara.IGUAL, serie);
			FiltroBinario fb = new FiltroBinario("comEcfAtivo", ECompara.IGUAL, 1);
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft, fb });
			return (ComEcf) service.selecionar(new ComEcf(), gf, false);
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que autoriza o acesso do cliente ao servidor REST, usando as informacoes enviadas pela 
	 * Autorization Basic (CNPJ do cliente, SERIE do ECF criptograda com a chave privada)
	 * 
	 * @throws RestException
	 *             dispara um erro caso nao consiga.
	 */
	protected void autorizar() throws RestException {
		try {
			// recupera os dados de autenticacao
			String header = headers.getRequestHeader("authorization").get(0);
			header = header.substring("Basic ".length());
			String[] creds = Base64.base64Decode(header).split(":");
			cnpj = creds[0];
			serie = UtilServer.descriptar(creds[1]);
			ecf = getImp(serie);
			
			// realiza a validacao
			if (ecf != null) {
				if (!cnpj.equals(ecf.getEmpEmpresa().getEmpEntidade().getEmpEntidadeDocumento1().replaceAll("[^0-9]", ""))) {
					throw new RestException(Status.UNAUTHORIZED);
				}
			} else {
				throw new RestException(Status.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			log.error(ex);
			throw new RestException("Informar os dados de autenticacao!");
		}
	}
}
