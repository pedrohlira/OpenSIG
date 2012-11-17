package br.com.opensig.core.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.servico.ExportacaoException;
import br.com.opensig.core.client.servico.ExportacaoService;
import br.com.opensig.core.server.exportar.IExportacao;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

/**
 * Classe que implementa as chamadas no servidor para as exportacoes do sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ExportacaoServiceImpl<E extends Dados> extends CoreServiceImpl<E> implements ExportacaoService<E> {

	public ExportacaoServiceImpl() {
	}

	public ExportacaoServiceImpl(Autenticacao auth) {
		super(auth);
	}

	@Override
	public String exportar(SisExpImp modo, ExpListagem<E> expLista) throws ExportacaoException {
		return exportar(modo, null, expLista, expLista.getNome());
	}

	@Override
	public String exportar(SisExpImp modo, ExpRegistro<E> expRegistro) throws ExportacaoException {
		return exportar(modo, expRegistro, null, expRegistro.getNome());
	}

	@Override
	public String exportar(String arquivo, String nome) throws ExportacaoException {
		String retorno = "";
		HttpSession sessao = getThreadLocalRequest().getSession();
		Autenticacao auth = SessionManager.LOGIN.get(sessao);
		byte[] obj = null;

		// se nao enteden-se como base 64
		try {
			File arq = new File(arquivo);
			if (arq.exists()) {
				InputStream is = new FileInputStream(arq);
				obj = new byte[is.available()];
				is.read(obj);
				is.close();
			} else if (arquivo.startsWith(System.getProperty("file.separator")) || arquivo.substring(1, 2).equals(":")) {
				throw new Exception(auth.getConf().get("errRegistro"));
			} else {
				obj = new BASE64Decoder().decodeBuffer(arquivo);
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao exportar", ex);
			throw new ExportacaoException(ex.getMessage());
		}

		retorno = sessao.getId() + new Date().getTime();
		sessao.setAttribute(retorno, obj);
		sessao.setAttribute(retorno + "arquivo", nome);
		return retorno;
	}

	/**
	 * Metodo que padrozina a exportacao das listagens e registros.
	 * 
	 * @param modo
	 *            informacoes para limitar os dados a exportar.
	 * @param expRegistro
	 *            um objeto de exportacao de registro.
	 * @param expLista
	 *            um objeto de expotaco de listagem.
	 * @param nome
	 *            o nome do arquivo.
	 * @return o id para ser usado no download.
	 * @throws ExportacaoException
	 *             caso ocorra um erro dispara a excecao.
	 */
	protected String exportar(SisExpImp modo, ExpRegistro<E> expRegistro, ExpListagem<E> expLista, String nome) throws ExportacaoException {
		// pegas os dados da sessao
		HttpSession sessao = getThreadLocalRequest().getSession();
		Autenticacao auth = SessionManager.LOGIN.get(sessao);
		String retorno = sessao.getId() + new Date().getTime();

		try {
			// gera os dados de cebecalho e rodape
			String[] empresa = auth.getEmpresa();
			String[][] enderecos = getEnderecos(empresa[1]);
			String[][] contatos = getContatos(empresa[1]);

			// identifica a classe de exportacao
			Class exp = Class.forName(modo.getSisExpImpClasse());
			IExportacao<E> exporta = (IExportacao<E>) exp.newInstance();
			exporta.setAuth(auth);
			byte[] obj = null;
			nome += "." + modo.getSisExpImpExtensoes();

			// verifica o tipo de exportacao
			if (expRegistro != null) {
				obj = exporta.getArquivo(this, modo, expRegistro, enderecos, contatos);
			} else {
				obj = exporta.getArquivo(this, modo, expLista, enderecos, contatos);
			}

			// seta o resultado na sessao
			sessao.setAttribute(retorno, obj);
			sessao.setAttribute(retorno + "arquivo", nome);
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao exportar", e);
			throw new ExportacaoException(e.getMessage());
		}

		return retorno;
	}

	/**
	 * Metodo que recupera os enderecos da entidade.
	 * 
	 * @param idEntidade
	 *            o identificado.
	 * @return uma matriz com os dados dos enderecos.
	 */
	protected String[][] getEnderecos(String idEntidade) {
		try {
			Dados d = new Dados("pu_empresa", "EmpEndereco", "empEnderecoId") {
				@Override
				public String[] toArray() {
					return null;
				}

				@Override
				public void setId(Number id) {
				}

				@Override
				public Number getId() {
					return null;
				}
			};

			FiltroNumero filtro = new FiltroNumero("empEntidade.empEntidadeId", ECompara.IGUAL, idEntidade);
			Lista enderecos = selecionar(d, 0, 0, filtro, true);
			return enderecos.getDados();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao pegar endereco", e);
			return null;
		}
	}

	/**
	 * Metodo que recupera os contatos da entidade.
	 * 
	 * @param idEntidade
	 *            o identificado.
	 * @return uma matriz com os dados dos contatos.
	 */
	protected String[][] getContatos(String idEntidade) {
		try {
			Dados d = new Dados("pu_empresa", "EmpContato", "empContatoId") {
				@Override
				public String[] toArray() {
					return null;
				}

				@Override
				public void setId(Number id) {
				}

				@Override
				public Number getId() {
					return null;
				}
			};

			FiltroNumero filtro = new FiltroNumero("empEntidade.empEntidadeId", ECompara.IGUAL, idEntidade);
			Lista enderecos = selecionar(d, 0, 0, filtro, true);
			return enderecos.getDados();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro ao pegar contatos", e);
			return null;
		}
	}

	/**
	 * Metodo que recupera que interage com o envio do navegador.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String imp = req.getParameter("imp");
		HttpSession sessao = req.getSession();

		try {
			if (id != null) {
				// pegando os dados e normalizando
				byte[] obj = (byte[]) sessao.getAttribute(id);
				String arquivo = sessao.getAttribute(id + "arquivo").toString();
				arquivo = UtilServer.normaliza(arquivo).replace(" ", "_");

				// setando os cabecalhos
				if (imp == null) {
					if (obj == null) {
						resp.addHeader("Content-Disposition", "attachment; filename=vazio.txt");
						obj = new String("Dados nao encontrados.").getBytes();
					} else {
						resp.addHeader("Content-Disposition", "attachment; filename=" + arquivo.toLowerCase());
					}
					resp.addHeader("Pragma", "no-cache");
					resp.addIntHeader("Expires", 0);
					resp.addHeader("Content-Type", "application/octet-stream");
				} else {
					String html = new String(obj).replace("<body>", "<body onload='this.focus(); this.print();'>");
					html = UtilServer.normaliza(html);
					obj = html.getBytes();
					resp.addHeader("Content-Type", "text/html");
				}

				// codificando e enviando
				resp.setCharacterEncoding("utf-8");
				resp.getOutputStream().write(obj);
				resp.flushBuffer();
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro na chamada", ex);
			throw new ServletException(ex.getMessage());
		} finally {
			sessao.setAttribute(id, null);
			sessao.setAttribute(id + "arquivo", null);
		}
	}
}
