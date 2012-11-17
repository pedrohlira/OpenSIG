package br.com.opensig.core.client.servico;

import java.util.Collection;

import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Interface que padroniza a persistencia dos dados.
 * 
 * @param <E>
 *            a tipagem do objeto a ser persistido um POJO.
 * @author Pedro H. Lira
 * @version 1.0
 */
public interface CoreService<E extends Dados> extends RemoteService {

	/**
	 * Metodo que recupera todos os objetos no banco de dados da entidade tipada.
	 * 
	 * @param classe
	 *            um objeto com os dados da entidade.
	 * @param inicio
	 *            a posição de inicio de recuperação dos registros.
	 * @param limite
	 *            o total de registros requeridos.
	 * @param filtro
	 *            os filtros a serem usados na seleção dos registros.
	 * @param removeDepedencia
	 *            informa se é pra anular as dependencias, se true anula
	 * @return uma coleção de objetos do tipo da entidade.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na pesquisa dos dados.
	 * @throws ParametroException
	 *             dispara uma exceção caso ocorra erro nos filtros da pesquisa dos dados.
	 */
	public Lista<E> selecionar(Dados classe, int inicio, int limite, IFiltro filtro, boolean removeDepedencia) throws CoreException, ParametroException;

	/**
	 * Metodo que recupera o objeto no banco de dados da entidade tipada referenciada pelo Id.
	 * 
	 * @param classe
	 *            um objeto com os dados da entidade.
	 * @param filtro
	 *            os filtros a serem usados na seleção dos registros.
	 * @param removeDepedencia
	 *            informa se é pra anular as dependencias, se true anula
	 * @return uma objeto do tipo da entidade.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na pesquisa dos dados.
	 * @throws ParametroException
	 *             dispara uma exceção caso ocorra erro nos filtros da pesquisa dos dados.
	 */
	public E selecionar(Dados classe, IFiltro filtro, boolean removeDepedencia) throws CoreException, ParametroException;

	/**
	 * Metodo que retorna uma valor de acordo com a operação solicitada.
	 * 
	 * @param classe
	 *            um objeto com os dados da entidade.
	 * @param campo
	 *            o nome do campo que será usado no modo de busca.
	 * @param busca
	 *            o mode da busca executada.
	 * @param filtro
	 *            os filtros a serem usados na seleção dos registros.
	 * @return um numero que pode ser somatoria, contagem, media e etc.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na pesquisa dos dados.
	 * @throws ParametroException
	 *             dispara uma exceção caso ocorra erro nos filtros da pesquisa dos dados.
	 */
	public Number buscar(Dados classe, String campo, EBusca busca, IFiltro filtro) throws CoreException, ParametroException;

	/**
	 * Metodo que retorna uma lista de valores agrupados por campoX e de acordo com a operação solicitada no campoY.
	 * 
	 * @param classe
	 *            um objeto com os dados da entidade.
	 * @param campoX
	 *            o nome do campo que será usado para agrupamento.
	 * @param grupoX
	 *            o nome do do tipo de agrupamento.
	 * @param campoSubX
	 *            o nome do campo que será usadao para reagrupamento.
	 * @param campoY
	 *            o nome do campo que será usado para a operacao solicitada.
	 * @param busca
	 *            o mode da busca executada.
	 * @param direcao
	 *            a direção da ordenação (CRESCENTE ou DECRESCENTE).
	 * @param filtro
	 *            os filtros a serem usados na seleção dos registros.
	 * @return uma colecao de array de Strings com os dados agrupados, normalmente o par de chaves String e Numeric.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na pesquisa dos dados.
	 * @throws ParametroException
	 *             dispara uma exceção caso ocorra erro nos filtros da pesquisa dos dados.
	 */
	public Collection<String[]> buscar(Dados classe, String campoX, String campoSubX, String grupoX, String campoY, EBusca busca, EDirecao direcao, IFiltro filtro) throws CoreException,
			ParametroException;

	/**
	 * Metodo que persiste os objetos no banco de dados.
	 * 
	 * @param unidades
	 *            uma coleção de classes a serem persistidas.
	 * @return uma coleção do tipo da entidade.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na gravação dos dados.
	 */
	public Collection<E> salvar(Collection<E> unidades) throws CoreException;

	/**
	 * Metodo que persiste o objeto no banco de dados.
	 * 
	 * @param unidade
	 *            a classe a ser persistida.
	 * @return uma objeto do tipo da entidade.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na gravação dos dados.
	 */
	public E salvar(E unidade) throws CoreException;

	/**
	 * Metodo que deleta vaŕios objetos no banco de dados.
	 * 
	 * @param unidades
	 *            uma coleção de classes a serem removidas.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na exclusão dos dados.
	 */
	public void deletar(Collection<E> unidades) throws CoreException;

	/**
	 * Metodo que deleta o objeto no banco de dados.
	 * 
	 * @param unidade
	 *            a classe a ser removida.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na exclusão dos dados.
	 */
	public void deletar(E unidade) throws CoreException;

	/**
	 * Metodo para executar instruções de atualizacao e exclusao no BD
	 * 
	 * @param sqls
	 *            um array de instruções SQL
	 * @return a quantidade de registros afetados por instrução enviada.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na execução do comando.
	 */
	public Integer[] executar(Sql[] sqls) throws CoreException;
	
	/**
	 * Metodo para executar instruções de atualizacao e exclusao no BD modo nativo
	 * 
	 * @param sql
	 *            uma String com a de instrucao SQL
	 * @return a quantidade de registros afetados pela instrução enviada.
	 * @throws CoreException
	 *             dispara uma exceção caso ocorra erro na execução do comando.
	 */
	public Integer executar(String sql) throws CoreException;

	/**
	 * Metodo que recupera a autenticacao atual.
	 * 
	 * @return o objeto de autenticacao.
	 */
	public Autenticacao getAuth();
}
