package br.com.opensig.core.client.servico;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

/**
 * Interface que padroniza as exportacoes dos dados.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface ExportacaoService<E extends Dados> extends CoreService<E> {

	/**
	 * Metodo que realiza a exportacao de listagem em modo simples.
	 * 
	 * @param modo
	 *            as configuracoes da exportacao.
	 * @param expLista
	 *            os detalhes visuais da exportacao.
	 * @return O identificador para realizar o download.
	 * @throws ExportacaoException
	 *             ocorre caso tenha erro na geracao do arquivo.
	 */
	public String exportar(SisExpImp modo, ExpListagem<E> expLista) throws ExportacaoException;

	/**
	 * Metodo que realiza a exportacao de listagem em modo simples.
	 * 
	 * @param modo
	 *            as configuracoes da exportacao.
	 * @param expRegistro
	 *            os detalhes visuais da exportacao.
	 * @return O identificador para realizar o download.
	 * @throws ExportacaoException
	 *             ocorre caso tenha erro na geracao do arquivo.
	 */
	public String exportar(SisExpImp modo, ExpRegistro<E> expRegistro) throws ExportacaoException;

	/**
	 * 
	 * @param arquivo
	 *            o conteudo do arquivo a ser gerado.
	 * @param nome
	 *            o nome do arquivo a ser gerado.
	 * @return O identificador para realizar o download.
	 * @throws ExportacaoException
	 *             ocorre caso tenha erro na geracao do arquivo.
	 */
	public String exportar(String arquivo, String nome) throws ExportacaoException;
}
