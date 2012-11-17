package br.com.opensig.core.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe que gerencia os upload de arquivos para o sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class UploadServiceImpl extends HttpServlet {

	/**
	 * Local do arquivo.
	 */
	protected String pathArquivo;
	/**
	 * Nome do arquivo.
	 */
	protected String nomeArquivo;
	/**
	 * Objeto JSON usapra para retorno.
	 */
	protected JSONObject json;
	/**
	 * Os dados em bytes do arquivo.
	 */
	protected ByteArrayOutputStream baos;
	/**
	 * O mapa de parametros passados para definir o tipo de upload.
	 */
	protected SortedMap<String, String> params;

	/**
	 * Passe os parametros que precisar via post, mas existem dois usados pelo
	 * sistema acao = salvar -> para salvar o arquivo em algum lugar, local =
	 * sessao -> para salvar na sessao o arquivo, se nao passar sera salvo na
	 * temp
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();
		json = new JSONObject();
		baos = new ByteArrayOutputStream();
		params = new TreeMap<String, String>();

		try {
			FileItemIterator iter = upload.getItemIterator(req);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String campo = UtilServer.normaliza(item.getFieldName());
				InputStream valor = item.openStream();

				if (item.isFormField()) {
					params.put(campo, Streams.asString(valor));
				} else {
					byte[] dados = new byte[1024];
					nomeArquivo = item.getName();
					int i = 0;

					while ((i = valor.read(dados)) != -1) {
						baos.write(dados, 0, i);
					}
					valor.close();
					UtilServer.LOG.debug("Arquivo lido com sucesso " + nomeArquivo);
				}
			}

			// defino o nome e path do arquivo
			nomeArquivo = params.get("nomeArquivo") != null ? params.get("nomeArquivo") : nomeArquivo;
			pathArquivo = params.get("pathRelativo") != null ? params.get("pathRelativo") : "/upload/temp/";
			pathArquivo = params.get("pathReal") != null ? params.get("pathReal") + nomeArquivo : UtilServer.getRealPath(pathArquivo) + nomeArquivo;

			if ("salvar".equals(params.get("acao"))) {
				if ("sessao".equals(params.get("local"))) {
					req.getSession().setAttribute(nomeArquivo, baos.toByteArray());
				} else {
					File arquivo = new File(pathArquivo);
					if (arquivo.exists()) {
						arquivo.delete();
					}
					FileOutputStream fos = new FileOutputStream(arquivo);
					fos.write(baos.toByteArray());
					fos.flush();
					fos.close();
				}
				json.put("dados", nomeArquivo);
			} else {
				// valida o tipo do arquivo
				SortedMap<String, byte[]> arquivos = new TreeMap<String, byte[]>();
				if (nomeArquivo.endsWith(".zip")) {
					arquivos = UtilServer.getArquivos(baos.toByteArray());
				} else {
					arquivos.put(nomeArquivo, baos.toByteArray());
				}
				
				// gera a string contendo os dados dos arquivos
				StringBuffer sb = new StringBuffer();
				String sep = params.get("separador") != null ? params.get("separador") : "";
				for (Entry<String, byte[]> arquivo : arquivos.entrySet()) {
					sb.append(new String(arquivo.getValue()));
					sb.append(sep);
				}
				
				json.put("dados", sb.toString());
			}

			json.put("success", true);
			json.put("error", "OK");
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception ex) {
			try {
				json.put("success", false);
				json.put("error", "ERRO");
				json.put("dados", ex.getMessage());
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				UtilServer.LOG.debug("Nao teve sucesso na subida do arquivo.", ex);
			} catch (JSONException e) {
				UtilServer.LOG.error("Erro na geracao do JSON.", e);
				throw new ServletException(e.getMessage());
			}
		} finally {
			finalizar(req, resp);
		}
	}

	/**
	 * Metodo usado para finalizar a importacao do arquivo.
	 * 
	 * @param req
	 *            o objeto de requisicao.
	 * @param resp
	 *            o objeto de resposta.
	 * @throws IOException
	 *             dispara uma excecao caso ocorra um erro.
	 */
	protected void finalizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Writer w = new OutputStreamWriter(resp.getOutputStream());
		w.write(json.toString());
		w.close();
		baos.close();
	}
}