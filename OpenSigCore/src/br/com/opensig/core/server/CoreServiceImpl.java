package br.com.opensig.core.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroCampo;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroException;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Colecao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.EData;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ELetra;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Classe que implementa na parte do servidor a resposta a chamada de procedimento do cliente, executando os comandos de persistencia no banco de dados.
 * 
 * @param <E>
 * @author Pedro H. Lira
 * @version 1.0
 */
public class CoreServiceImpl<E extends Dados> extends RemoteServiceServlet implements CoreService<E> {

	private Autenticacao auth;

	public CoreServiceImpl() {
	}

	public CoreServiceImpl(Autenticacao auth) {
		this.auth = auth;
	}

	@Override
	public Lista<E> selecionar(Dados classe, int inicio, int limite, IFiltro filtro, boolean removeDependencia) throws CoreException, ParametroException {
		// mosta a instrução padrão
		String sql = String.format("SELECT DISTINCT t FROM %s t ", classe.getTabela());
		sql += getColecao(classe.getColecao());
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(classe.getPu());
			em = emf.createEntityManager();

			// caso tenha filtros, recupera no padrão sql e adiciona a instrução
			if (filtro != null) {
				sql += String.format(" WHERE %s", filtro.getSql());
			}

			// caso seja passado um campo para ordenar, adiciona o comando a
			// instrução
			if (classe.getCampoOrdem() != null && !classe.getCampoOrdem().isEmpty()) {
				String ordem = classe.getCampoOrdem();
				Pattern pat = Pattern.compile("^t\\d*\\.");
				Matcher mat = pat.matcher(ordem);
				if (!mat.find()) {
					ordem = "t." + ordem;
				}
				EDirecao direcao = classe.getOrdemDirecao() == null ? EDirecao.ASC : classe.getOrdemDirecao();
				sql += String.format(" ORDER BY %s %s", ordem, direcao.toString());
			}

			// pega a transação padrão e inicia
			em.getTransaction().begin();
			// gera um query
			UtilServer.LOG.debug("Sql gerado: " + sql);
			Query rs = em.createQuery(sql);

			// se foi definido um limete de resgistros, caso contrario recupera
			// todos.
			if (limite > 0) {
				inicio = inicio < 0 ? 0 : inicio;
				// seta a posição inicial de recuperação dos registros
				// (paginação)
				rs.setFirstResult(inicio);
				// seta a posição a quantidade total de registros (paginação)
				rs.setMaxResults(limite);
			}

			// se foi passados filtros coloca agora os valores nos devidos
			// campos
			if (filtro != null) {
				Collection<IFiltro> params = filtro.getParametro();
				for (IFiltro fil : params) {
					if (!(fil instanceof FiltroCampo) && fil.getCompara() != ECompara.NULO && fil.getCompara() != ECompara.VAZIO) {
						rs.setParameter(fil.getCampoId(), fil.getValor());
					}
				}
			}

			// realiza toda a operação caso tudo tenha sucesso
			em.getTransaction().commit();
			// recupera a lista de dados
			List<E> lista = (List<E>) rs.getResultList();
			// chama o método que retorna o total de registros sem paginação
			int total = buscar(classe, classe.getCampoId(), EBusca.CONTAGEM, filtro).intValue();
			UtilServer.LOG.debug("Total de registros: " + total);

			// insere os dados num modelo de objeto e retorna
			Lista<E> listagem = new Lista<E>();
			listagem.setTotal(total);
			limite = limite <= 0 || total < limite ? total : limite;

			if (removeDependencia) {
				// transformando o resultado numa matrix de Strings
				String[][] dados = new String[limite][];
				int fim = lista.size() > limite ? limite : lista.size();
				for (int i = 0; i < fim; i++) {
					lista.get(i).setEmpresa(classe.getEmpresa());
					dados[i] = lista.get(i).toArray();
				}

				listagem.setDados(dados);
				UtilServer.LOG.debug("Retornando textos.");
			} else {
				listagem.setLista(lista);
				UtilServer.LOG.debug("Retornando objetos.");
			}

			return listagem;
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao selecionar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}
	}

	@Override
	public E selecionar(Dados classe, IFiltro filtro, boolean removeDependencia) throws CoreException, ParametroException {
		// formata o sql
		String sql = String.format("SELECT DISTINCT t FROM %s t ", classe.getTabela());
		sql += getColecao(classe.getColecao());

		// pega o resultado
		E obj = (E) getResultado(classe.getPu(), sql, filtro);
		if (removeDependencia && obj != null) {
			obj.anularDependencia();
		}

		return obj;
	}

	@Override
	public Number buscar(Dados classe, String campo, EBusca busca, IFiltro filtro) throws CoreException, ParametroException {
		Pattern pat = Pattern.compile("^t\\d*\\.");
		Matcher mat = pat.matcher(campo);
		if (!mat.find()) {
			campo = "t." + campo;
		}

		// verifica se é contagem
		String conta = EBusca.CONTAGEM == busca ? "DISTINCT " : "";

		// formata o sql
		String sql = String.format("SELECT %s(%s%s) FROM %s t ", busca.toString(), conta, campo, classe.getTabela());
		sql += getColecao(classe.getColecao());

		// pega o resultado
		Number obj = (Number) getResultado(classe.getPu(), sql, filtro);
		return obj;
	}

	@Override
	public Collection<String[]> buscar(Dados classe, String campoX, String campoSubX, String grupoX, String campoY, EBusca busca, EDirecao direcao, IFiltro filtro) throws CoreException,
			ParametroException {

		if (campoSubX == null || campoSubX.equals("")) {
			campoSubX = campoX;
		}

		// sql principal
		String sql = String.format("SELECT %s, %s, COUNT(%s), SUM(%s), AVG(%s) FROM %s t ", campoX, campoSubX, campoY, campoY, campoY, classe.getTabela());
		sql += getColecao(classe.getColecao());
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(classe.getPu());
			em = emf.createEntityManager();

			// caso tenha filtros, recupera no padrão sql e adiciona a instrução
			if (filtro != null) {
				sql += String.format(" WHERE %s", filtro.getSql());
			}

			// agrupamento
			sql += String.format(" GROUP BY %s, %s", campoX, campoSubX);

			// ordem
			sql += String.format(" ORDER BY %s, %s", campoX, campoSubX);

			// pega a transação padrão e inicia
			em.getTransaction().begin();
			// gera um query
			UtilServer.LOG.debug("Sql gerado: " + sql);
			Query rs = em.createQuery(sql);

			// se foi passados filtros coloca os valores nos campos
			if (filtro != null) {
				Collection<IFiltro> params = filtro.getParametro();
				for (IFiltro fil : params) {
					if (!(fil instanceof FiltroCampo) && fil.getCompara() != ECompara.NULO && fil.getCompara() != ECompara.VAZIO) {
						rs.setParameter(fil.getCampoId(), fil.getValor());
					}
				}
			}

			// realiza toda a operação caso tudo tenha sucesso
			em.getTransaction().commit();
			// recupera a lista de dados
			List<Vector> resultado = rs.getResultList();
			Collection<String[]> lista = new ArrayList<String[]>();

			boolean campoData = false;
			for (Object v : resultado) {
				String[] linha = new String[5];
				Object[] val = (Object[]) v;
				try {
					linha[0] = getSubData((Date) val[0], grupoX);
					linha[1] = linha[0];
					campoData = true;
				} catch (Exception ex) {
					linha[0] = val[0] == null ? "" : val[0].toString();
					try {
						linha[1] = getSubData((Date) val[1], grupoX);
					} catch (Exception ex1) {
						linha[1] = val[1] == null ? "" : val[1].toString();
					}
				}
				linha[2] = val[2] == null ? "" : val[2].toString();
				linha[3] = val[3] == null ? "" : val[3].toString();
				linha[4] = val[4] == null ? "" : val[4].toString();
				lista.add(linha);
			}

			// agrupando pelo subcampo se necessario
			if (!campoX.equals(campoSubX) || campoData) {
				lista = agrupar(lista, grupoX);
			} else {
				// ordenando pelo campo e direcao corretos
				Collections.sort((List<String[]>) lista, ordenar(busca, direcao));
			}

			return lista;
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao buscar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}
	}

	/**
	 * Metodo padrão para recuperar um registro.
	 * 
	 * @param pu
	 *            o nome da unidade de persistencia.
	 * @param sql
	 *            a instrução em EQL formatada.
	 * @param filtro
	 *            o filtro a ser usado.
	 * @return um unidade de classe de acordo com a generic.
	 * @throws CoreException
	 *             ocorre em erros no acesso aos dados.
	 * @throws ParametroException
	 *             ocorre em caso de filtro incorreto.
	 */
	public Object getResultado(String pu, String sql, IFiltro filtro) throws CoreException, ParametroException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(pu);
			em = emf.createEntityManager();

			// caso tenha filtros, recupera no padrão sql e adiciona a instrução
			if (filtro != null) {
				sql += " WHERE " + filtro.getSql();
			}

			// pega a transação padrão e inicia
			em.getTransaction().begin();
			// gera um query
			UtilServer.LOG.debug("Sql gerado: " + sql);
			Query rs = em.createQuery(sql);

			// se foi passados filtros coloca agora os valores nos devidos
			// campos
			if (filtro != null) {
				Collection<IFiltro> params = filtro.getParametro();

				for (IFiltro fil : params) {
					if (!(fil instanceof FiltroCampo) && fil.getCompara() != ECompara.NULO && fil.getCompara() != ECompara.VAZIO) {
						rs.setParameter(fil.getCampoId(), fil.getValor());
					}
				}
			}

			// realiza toda a operação caso tudo tenha sucesso
			em.getTransaction().commit();
			try {
				return rs.getSingleResult();
			} catch (Exception e) {
				return null;
			}
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao pegar resultado", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}
	}

	@Override
	public Collection<E> salvar(Collection<E> unidades) throws CoreException {
		return salvar(unidades, true);
	}

	/**
	 * @see CoreService#salvar(Collection)
	 */
	public Collection<E> salvar(Collection<E> unidades, boolean removeDependencia) throws CoreException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			if (unidades != null && !unidades.isEmpty()) {
				Dados[] d = unidades.toArray(new Dados[] {});
				emf = Conexao.getInstancia(d[0].getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();
				salvar(em, unidades);
				em.getTransaction().commit();
			}
			return unidades;
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (removeDependencia && unidades != null) {
				for (E unidade : unidades) {
					unidade.anularDependencia();
				}
			}
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}
	}

	/**
	 * Metodo que salva uma colecao de entidades usando a mesma transacao.
	 * 
	 * @param em
	 *            o gerenciado de entidade.
	 * @param unidades
	 *            a colecao de entidades.
	 * @return a colecao de entidades com valores salvos.
	 * @throws CoreException
	 *             dispara uma excecao em caso de erro.
	 */
	public Collection<E> salvar(EntityManager em, Collection<E> unidades) throws CoreException {
		for (E unidade : unidades) {
			salvar(em, unidade);
		}
		return unidades;
	}

	@Override
	public E salvar(E unidade) throws CoreException {
		return salvar(unidade, true);
	}

	/**
	 * @see CoreService#salvar(Dados)
	 */
	public E salvar(E unidade, boolean removeDependencia) throws CoreException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			emf = Conexao.getInstancia(unidade.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();
			salvar(em, unidade);
			em.getTransaction().commit();
			return unidade;
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (removeDependencia) {
				unidade.anularDependencia();
			}
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}
	}

	/**
	 * Metodo que salva a entidade usando a mesma transacao passada.
	 * 
	 * @param em
	 *            o gerenciado de entidade.
	 * @param unidade
	 *            a entidade
	 * @return a entidade com valores salvos.
	 * @throws CoreException
	 *             dispara uma excecao em caso de erro.
	 */
	public E salvar(EntityManager em, E unidade) throws CoreException {
		if (unidade.getId().intValue() == 0) {
			padronizaLetras(unidade, unidade.getTipoLetra(), unidade.isLimpaBranco());
			em.persist(unidade);
		} else {
			padronizaLetras(unidade, unidade.getTipoLetra(), unidade.isLimpaBranco());
			em.merge(unidade);
		}

		return unidade;
	}

	@Override
	public void deletar(Collection<E> unidades) throws CoreException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			if (unidades != null && !unidades.isEmpty()) {
				Dados[] d = unidades.toArray(new Dados[] {});
				emf = Conexao.getInstancia(d[0].getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				deletar(em, unidades);
				em.getTransaction().commit();
			}
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao deletar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}
	}

	/**
	 * Metodo que deleta uma colecao de entidades com a mesma transacao.
	 * 
	 * @param em
	 *            o gerenciador de entidades.
	 * @param unidades
	 *            a colecao de entidades.
	 * @throws CoreException
	 *             dispara uma excecao em caso de erro.
	 */
	public void deletar(EntityManager em, Collection<E> unidades) throws CoreException {
		for (E unidade : unidades) {
			deletar(em, unidade);
		}
	}

	@Override
	public void deletar(E unidade) throws CoreException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			emf = Conexao.getInstancia(unidade.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			deletar(em, unidade);
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao deletar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}
	}

	/**
	 * Metodo que deleta a entidade com a mesma transacao passada.
	 * 
	 * @param em
	 *            o gerenciador de entidades.
	 * @param unidade
	 *            a entidade
	 * @throws CoreException
	 *             dispara uma excecao em caso de erro.
	 */
	public void deletar(EntityManager em, E unidade) throws CoreException {
		unidade = (E) em.find(unidade.getClass(), unidade.getId());
		em.remove(unidade);
	}

	@Override
	public Integer[] executar(Sql[] sqls) throws CoreException {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		Integer[] resultado = null;
		int pos = 0;

		try {
			if (sqls != null && sqls.length > 0) {
				resultado = new Integer[sqls.length];
				emf = Conexao.getInstancia(sqls[0].getClasse().getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				for (Sql sql : sqls) {
					resultado[pos++] = executar(em, sql);
				}

				em.getTransaction().commit();
			}
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao executar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}

		return resultado;
	}

	@Override
	public Integer executar(String sql) throws CoreException {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		Integer resultado = null;

		try {
			if (sql != null && !sql.equals("")) {
				emf = Conexao.getInstancia("pu_core");
				em = emf.createEntityManager();
				em.getTransaction().begin();
				Query rs = em.createNativeQuery(sql);
				resultado = rs.executeUpdate();
				em.getTransaction().commit();
			}
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao executar", ex);
			throw new CoreException(ex.getMessage());
		} finally {
			if (em != null && emf != null) {
				em.close();
				emf.close();
			}
		}

		return resultado;
	}

	/**
	 * Metodo para executar instruções diretas no BD com a mesma transacao.
	 * 
	 * @param em
	 *            o gerenciador de entidades.
	 * @param sql
	 *            a instrucao Sql em formato de objeto.
	 * @return um inteiro informando a quantidade de registros afetados.
	 * @throws CoreException
	 *             dispara uma excecao em caso de erro.
	 */
	public int executar(EntityManager em, Sql sql) throws CoreException {
		int resultado = 0;

		if (sql != null) {
			// recupera uma instância do gerenciador de entidades
			Dados dados = sql.getClasse();
			Pattern pat = Pattern.compile("t\\d+\\.");

			// gerando a acao
			String acao = "";
			if (sql.getComando() == EComando.ATUALIZAR) {
				// caso a acao seja atualizar um campo de colecao
				Matcher mat = pat.matcher(sql.getParametro().getSql());
				if (mat.find()) {
					return atualizar(em, sql);
				} else {
					acao = "UPDATE " + dados.getTabela() + " t SET " + sql.getParametro().getSql();
				}
			} else {
				acao = "DELETE FROM " + dados.getTabela() + " t ";
			}

			// caso tenha filtros, recupera no padrão sql e adiciona a
			// instrução
			if (sql.getFiltro() != null) {
				Matcher mat = pat.matcher(sql.getFiltro().getSql());
				if (mat.find()) {
					if (sql.getComando() == EComando.ATUALIZAR) {
						return atualizar(em, sql);
					} else {
						return excluir(em, sql);
					}
				} else {
					acao += String.format(" WHERE %s", sql.getFiltro().getSql());
				}
			}

			// gera um query
			UtilServer.LOG.debug("Sql gerado: " + acao);
			Query rs = em.createQuery(acao);

			// se foi passados filtros coloca agora os valores nos
			// devidos campos
			if (sql.getFiltro() != null) {
				Collection<IFiltro> params = sql.getFiltro().getParametro();
				for (IFiltro fil : params) {
					if (!(fil instanceof FiltroCampo) && fil.getCompara() != ECompara.NULO && fil.getCompara() != ECompara.VAZIO) {
						rs.setParameter(fil.getCampoId(), fil.getValor());
					}
				}
			}

			// se foi passados parametros coloca agora os valores nos
			// devidos campos
			if (sql.getParametro() != null) {
				Collection<IParametro> params = sql.getParametro().getParametro();
				for (IParametro par : params) {
					rs.setParameter(par.getCampoId(), par.getValor());
				}
			}

			// executa o comando
			resultado = rs.executeUpdate();
		}

		return resultado;
	}

	/**
	 * Metodo para executar instruções de atualizacao diretas no BD com a mesma transacao.
	 * 
	 * @param em
	 *            o gerenciador de entidades.
	 * @param sql
	 *            a instrucao Sql em formato de objeto.
	 * @return um inteiro informando a quantidade de registros afetados.
	 * @throws CoreException
	 *             dispara uma excecao em caso de erro.
	 */
	protected int atualizar(EntityManager em, Sql sql) throws CoreException {
		int resultado = 0;
		String nMet = "set" + sql.getParametro().getCampo().replaceAll("t\\d*\\.", "");

		// faz a selecao dos objetos
		Dados dado = sql.getClasse();
		Lista<E> lista = selecionar(dado, 0, 0, sql.getFiltro(), false);

		try {
			// percorre cada um para atualizar o campo
			for (E obj : lista.getLista()) {
				// os metodos do objeto
				for (Method met : obj.getClass().getMethods()) {
					// verifica se é o get e retorna List
					if (UtilServer.isGetter(met) && met.getReturnType() == List.class) {
						List<E> vMet = (List<E>) met.invoke(obj, new Object[] {});
						// percorre as colecoes
						for (Colecao col : dado.getColecao()) {
							// verifica se tem valor e compativel com o objeto
							if (vMet != null && !vMet.isEmpty() && vMet.get(0).getTabela().equals(col.getTabela())) {
								// percorre os objetos
								for (E subObj : vMet) {
									// percorre os metodos do objeto final
									for (Method subMet : subObj.getClass().getMethods()) {
										// verifica se é set e tem o mesmo nome
										if (UtilServer.isSetter(subMet) && subMet.getName().equalsIgnoreCase(nMet)) {
											// seta o valor
											setValor(subMet, subObj, sql);
											break;
										}
									}
								}

								// salva os objetos altualizados
								salvar(em, vMet);
								resultado += vMet.size();
							}
						}
						// verifica se é set e tem o mesmo nome
					} else if (UtilServer.isSetter(met) && met.getName().equalsIgnoreCase(nMet)) {
						// seta o valor
						setValor(met, obj, sql);
						resultado++;
						break;
					}
				}
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao atualizar", ex);
			resultado = 0;
		}

		return resultado;
	}

	/**
	 * Metodo que faz o set do valor no objeto identificando o verdadeiro tipo.
	 * 
	 * @param sMet
	 *            o Metodo a ser chamado.
	 * @param obj
	 *            o objeto a ser atingindo.
	 * @param sql
	 *            a instrucao que contem o valor.
	 * @throws Exception
	 *             caso ocorra alguma excecao.
	 */
	protected void setValor(Method sMet, E obj, Sql sql) throws Exception {
		String nomeGet = sMet.getName().replace("set", "get");
		Method gMet = obj.getClass().getMethod(nomeGet);
		String valor = sql.getParametro().getValor().toString();

		if (gMet.getReturnType() == Boolean.class || gMet.getReturnType() == boolean.class) {
			sMet.invoke(obj, new Object[] { Boolean.valueOf(valor) });
		} else if (gMet.getReturnType() == Byte.class || gMet.getReturnType() == byte.class) {
			sMet.invoke(obj, new Object[] { Byte.valueOf(valor) });
		} else if (gMet.getReturnType() == Short.class || gMet.getReturnType() == short.class) {
			sMet.invoke(obj, new Object[] { Short.valueOf(valor) });
		} else if (gMet.getReturnType() == Integer.class || gMet.getReturnType() == int.class) {
			sMet.invoke(obj, new Object[] { Integer.valueOf(valor) });
		} else if (gMet.getReturnType() == Long.class || gMet.getReturnType() == long.class) {
			sMet.invoke(obj, new Object[] { Long.valueOf(valor) });
		} else if (gMet.getReturnType() == Float.class || gMet.getReturnType() == float.class) {
			sMet.invoke(obj, new Object[] { Float.valueOf(valor) });
		} else if (gMet.getReturnType() == Double.class || gMet.getReturnType() == double.class) {
			sMet.invoke(obj, new Object[] { Double.valueOf(valor) });
		} else if (gMet.getReturnType() == Date.class) {
			sMet.invoke(obj, new Object[] { (Date) sql.getParametro().getValor() });
		} else if (gMet.getReturnType() == Character.class || gMet.getReturnType() == char.class) {
			sMet.invoke(obj, new Object[] { valor.charAt(0) });
		} else {
			sMet.invoke(obj, new Object[] { valor });
		}
	}

	/**
	 * Metodo para executar instruções de exclusoes diretas no BD com a mesma transacao.
	 * 
	 * @param em
	 *            o gerenciador de entidades.
	 * @param sql
	 *            a instrucao Sql em formato de objeto.
	 * @return um inteiro informando a quantidade de registros afetados.
	 * @throws CoreException
	 *             dispara uma excecao em caso de erro.
	 */
	protected int excluir(EntityManager em, Sql sql) throws CoreException {
		// faz a selecao dos objetos
		Dados dado = sql.getClasse();
		Lista<E> lista = selecionar(dado, 0, 0, sql.getFiltro(), false);
		deletar(em, lista.getLista());

		return lista.getTotal();
	}

	/**
	 * Metodo que gera a instrucao de colecoes em JQL.
	 * 
	 * @param colecao
	 *            um array de colecoes de tabelas.
	 * @return uma string no formato de busca.
	 */
	protected String getColecao(Colecao[] colecao) {
		String sql = "";
		if (colecao != null) {
			for (Colecao col : colecao) {
				sql += String.format("%s %s %s ", col.getJuncao(), col.getCampo(), col.getPrefixo());
			}
		}
		return sql;
	}

	/**
	 * Metodo que agrupa os resultados de buscas para o grafico.
	 * 
	 * @param lista
	 *            uma colecao de string com os dados detalhados.
	 * @param grupo
	 *            o nome do campo do eixoX a ser agrupado.
	 * @return a colecao agrupada.
	 */
	protected Collection<String[]> agrupar(Collection<String[]> lista, String grupo) {
		Collection<String[]> aux = new ArrayList<String[]>();

		String campoX = "";
		String campoSubX = "";
		double valCount = 0.00;
		double valSum = 0.00;
		double valAvg = 0.00;

		for (String[] reg : lista) {
			if (reg[0].equals(campoX) && reg[1].equals(campoSubX)) {
				valCount += Double.valueOf(reg[2]);
				valSum += Double.valueOf(reg[3]);
				valAvg = valSum / valCount;
			} else {
				if (!campoX.equals("")) {
					aux.add(new String[] { campoX, campoSubX, valCount + "", valSum + "", valAvg + "" });
				}

				campoX = reg[0];
				campoSubX = reg[1];
				valCount = Double.valueOf(reg[2]);
				valSum = Double.valueOf(reg[3]);
				valAvg = Double.valueOf(reg[4]);
			}
		}
		aux.add(new String[] { campoX, campoSubX, valCount + "", valSum + "", valAvg + "" });

		return aux;
	}

	/**
	 * Metodo que ordena a busca do grafico.
	 * 
	 * @param busca
	 *            o tipo de busca.
	 * @param direcao
	 *            a direcao da ordenacao.
	 * @return o comparador.
	 */
	protected Comparator ordenar(final EBusca busca, final EDirecao direcao) {
		Comparator comp = new Comparator() {
			public int compare(Object o1, Object o2) {
				int dir = direcao == EDirecao.ASC ? 1 : -1;
				String[] obj1 = (String[]) o1;
				String[] obj2 = (String[]) o2;
				double val1;
				double val2;

				switch (busca) {
				case CONTAGEM:
					val1 = Double.valueOf(obj1[2]);
					val2 = Double.valueOf(obj2[2]);
					break;
				case SOMA:
					val1 = Double.valueOf(obj1[3]);
					val2 = Double.valueOf(obj2[3]);
					break;
				default:
					val1 = Double.valueOf(obj1[4]);
					val2 = Double.valueOf(obj2[4]);
				}

				if (val1 < val2) {
					return -1 * dir;
				} else if (val1 > val2) {
					return 1 * dir;
				} else {
					return 0;
				}

			}
		};

		return comp;
	}

	/**
	 * Metodo que gera a string para usar sub buscas por data.
	 * 
	 * @param data
	 *            a data do registro.
	 * @param parte
	 *            a parte da data a ser usada.
	 * @return a string com a parte da instrucao.
	 */
	protected String getSubData(Date data, String parte) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);

		if (parte.equals(EData.DIA.toString())) {
			return cal.get(Calendar.DAY_OF_MONTH) + "";
		} else if (parte.equals(EData.MES.toString())) {
			return (cal.get(Calendar.MONTH) + 1) + "";
		} else {
			return cal.get(Calendar.YEAR) + "";
		}
	}

	/**
	 * Metodo que padrozina os tamanhos da letras ao salvar os dados.
	 * 
	 * @param unidade
	 *            o objeto a ser salvo.
	 * @param tipo
	 *            o tipo de letra padrao usado.
	 * @param limpar
	 *            se deve remover os espacos em branco do comeco e fim.
	 */
	protected void padronizaLetras(Object unidade, ELetra tipo, boolean limpar) {
		for (Method metodo : unidade.getClass().getMethods()) {
			try {
				if (UtilServer.isGetter(metodo)) {
					Object valorMetodo = metodo.invoke(unidade, new Object[] {});
					if (valorMetodo != null) {
						if (metodo.getReturnType() == String.class) {
							String nomeMetodo = metodo.getName().replaceFirst("get", "set");
							Method set = unidade.getClass().getMethod(nomeMetodo, new Class[] { String.class });
							String valor = valorMetodo.toString();

							if (tipo == ELetra.GRANDE) {
								valor = valor.toUpperCase();
							} else if (tipo == ELetra.PEQUENA) {
								valor = valor.toLowerCase();
							}

							set.invoke(unidade, new Object[] { limpar ? valor.trim() : valor });
						} else if (metodo.getReturnType().getSuperclass() == Dados.class && valorMetodo != null) {
							padronizaLetras(valorMetodo, tipo, limpar);
						}
					}
				}
			} catch (Exception ex) {
				// nao precisa modificar.
			}
		}
	}

	@Override
	public Autenticacao getAuth() {
		try {
			HttpSession sessao = getThreadLocalRequest().getSession();
			auth = SessionManager.LOGIN.get(sessao);
		} catch (Exception e) {
			// caso nao consiga, nao altera o valor atual, que pode ser null ou nao.
		}
		return auth;
	}
}
