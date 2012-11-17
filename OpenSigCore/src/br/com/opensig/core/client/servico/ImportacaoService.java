package br.com.opensig.core.client.servico;

import java.util.List;
import java.util.Map;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

/**
 * Interface que padroniza as importacoes dos dados.
 * 
 * @param <E>
 *            o tipo de dados.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface ImportacaoService<E extends Dados> extends CoreService<E> {

	/**
	 * Metodo que recebe os arquivos a serem importados pelo modelo passado.
	 * 
	 * @param modo
	 *            as configuracoes da importacao.
	 * @param arquivos
	 *            a lista de arquivos que foram carregados para upload.
	 * @return a lista de arquivos processados.
	 * @throws ImportacaoException
	 *             ocorre caso tenha erro na importacao do arquivo.
	 */
	public Map<String, List<E>> importar(SisExpImp modo, List<String> arquivos) throws ImportacaoException;

}
