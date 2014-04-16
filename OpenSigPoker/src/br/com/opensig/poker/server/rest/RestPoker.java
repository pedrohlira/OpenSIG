package br.com.opensig.poker.server.rest;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.rest.RestException;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.permissao.shared.modelo.SisGrupo;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.poker.client.servico.PokerException;
import br.com.opensig.poker.shared.modelo.PokerCash;
import br.com.opensig.poker.shared.modelo.PokerCliente;
import br.com.opensig.poker.shared.modelo.PokerForma;
import br.com.opensig.poker.shared.modelo.PokerJogador;
import br.com.opensig.poker.shared.modelo.PokerMesa;
import br.com.opensig.poker.shared.modelo.PokerPagar;
import br.com.opensig.poker.shared.modelo.PokerParticipante;
import br.com.opensig.poker.shared.modelo.PokerPremiacao;
import br.com.opensig.poker.shared.modelo.PokerReceber;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

/**
 * Classe que representa a comunicacao do Servidor para o Cliente via Rest
 * 
 * @author Pedro H. Lira
 */
@Provider
@Path("/")
public class RestPoker {

	/**
	 * O dados enviados.
	 */
	@Context
	protected HttpServletRequest request;

	/**
	 * O objeto de persistencia dos dados.
	 */
	protected CoreServiceImpl service;
	
	/**
	 * Construtor padrao.
	 */
	public RestPoker() {
		service = new CoreServiceImpl();
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String ajuda() throws RestException {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<center>Acesse a URL abaixo:</center><br>");
		sb.append("<center><a href='/openpoker/application.wadl'>WADL</a></center>");
		sb.append("</html>");
		return sb.toString();
	}
	
	/**
	 * Metodo que autoriza o acesso do cliente ao servidor REST.
	 * 
	 * @throws RestException
	 *             dispara um erro caso nao consiga.
	 */
	private void autorizar() throws RestException {
		// realiza a validacao
		HttpSession sessao = request.getSession();
		SisUsuario usuario = (SisUsuario) sessao.getAttribute("usuario");
		if (usuario == null) {
			throw new RestException(Status.UNAUTHORIZED);
		}
	}
	
	/**
	 * Metodo que valida o usuario e coloca na sessao.
	 * 
	 * @param login
	 *            o nome do usuario.
	 * @param senha
	 *            a senha do usuario.
	 * @return Um objeto de SisUsuario.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/logar")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public SisUsuario logar(@FormParam(value = "sisUsuarioLogin") String login, @FormParam(value = "sisUsuarioSenha") String senha) throws RestException {
		try {
			FiltroTexto ft = new FiltroTexto("sisUsuarioLogin", ECompara.IGUAL, login);
			FiltroTexto ft1 = new FiltroTexto("sisUsuarioSenha", ECompara.IGUAL, senha);
			FiltroBinario fb = new FiltroBinario("sisUsuarioAtivo", ECompara.IGUAL, 1);
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft, ft1, fb });
			SisUsuario usuario = (SisUsuario) service.selecionar(new SisUsuario(), gf, false);

			// realiza a validacao
			if (usuario != null) {
				boolean permite = false;
				for (SisGrupo grupo : usuario.getSisGrupos()) {
					if (grupo.getSisGrupoAtivo() && (grupo.getSisGrupoNome().equalsIgnoreCase("croupier") || grupo.getSisGrupoNome().equalsIgnoreCase("caixa"))) {
						permite = true;
						break;
					}
				}

				if (permite) {
					HttpSession sessao = request.getSession();
					sessao.setAttribute("usuario", usuario);
					return usuario;
				} else {
					throw new RestException("O usuario precisa estar no grupo [CROUPIER ou CAIXA]");
				}
			} else {
				throw new RestException("O usuario/senha estao invalidos!");
			}
		} catch (CoreException ex) {
			throw new RestException("Informar os dados de autenticacao!");
		}
	}

	/**
	 * Metodo que retira o login da sessao.
	 * 
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/deslogar")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String deslogar() throws RestException {
		HttpSession sessao = request.getSession();
		sessao.setAttribute("usuario", null);
		throw new RestException("Usuario deslogado!");
	}

	/**
	 * Metodo que retorna os torneios ativos.
	 * 
	 * @return Uma lista de torneios ativos.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/torneio")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<PokerTorneio> getTorneio() throws RestException {
		autorizar();
		try {
			FiltroBinario fb = new FiltroBinario("pokerTorneioAtivo", ECompara.IGUAL, 1);
			PokerTorneio torneio = new PokerTorneio();
			Lista<PokerTorneio> lista = service.selecionar(torneio, 0, 0, fb, false);
			return lista.getLista();
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna os mesas do torneio.
	 * 
	 * @return Uma lista das mesas.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/mesa/{idTorneio}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<PokerMesa> getMesa(@PathParam(value = "idTorneio") int idTorneio) throws RestException {
		autorizar();
		try {
			FiltroBinario fb = new FiltroBinario("pokerMesaAtivo", ECompara.IGUAL, 1);
			FiltroObjeto fo = new FiltroObjeto("pokerTorneio", ECompara.IGUAL, new PokerTorneio(idTorneio));
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fb, fo });
			PokerMesa mesa = new PokerMesa();
			Lista<PokerMesa> lista = service.selecionar(mesa, 0, 0, gf, false);
			return lista.getLista();
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna os participantes da mesa.
	 * 
	 * @return Uma lista de participantes.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/participante/{idMesa}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<PokerParticipante> getParticipante(@PathParam(value = "idMesa") int idMesa) throws RestException {
		autorizar();
		try {
			FiltroBinario fb = new FiltroBinario("pokerParticipanteAtivo", ECompara.IGUAL, 1);
			FiltroObjeto fo = new FiltroObjeto("pokerMesa", ECompara.IGUAL, new PokerMesa(idMesa));
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fb, fo });
			PokerParticipante participante = new PokerParticipante();
			Lista<PokerParticipante> lista = service.selecionar(participante, 0, 0, gf, false);
			return lista.getLista();
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza um rebuy no torneio.
	 * 
	 * @return o participante que realizou o rebuy.
	 * @param idParticipante
	 *            identificado do participante.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/rebuy/{idParticipante}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public PokerParticipante reBuy(@PathParam(value = "idParticipante") int idParticipante) throws RestException {
		autorizar();
		try {
			FiltroNumero fn = new FiltroNumero("pokerParticipanteId", ECompara.IGUAL, idParticipante);
			ParametroFormula pf = new ParametroFormula("pokerParticipanteReentrada", 1);
			Sql sql = new Sql(new PokerParticipante(), EComando.ATUALIZAR, fn, pf);
			service.executar(new Sql[] { sql });
			return (PokerParticipante) service.selecionar(new PokerParticipante(), fn, false);
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza um addon no torneio.
	 * 
	 * @return o participante que realizou o addon.
	 * @param idParticipante
	 *            identificado do participante.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/addon/{idParticipante}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public PokerParticipante addOn(@PathParam(value = "idParticipante") int idParticipante) throws RestException {
		autorizar();
		try {
			FiltroNumero fn = new FiltroNumero("pokerParticipanteId", ECompara.IGUAL, idParticipante);
			ParametroBinario pb = new ParametroBinario("pokerParticipanteAdicional", 1);
			Sql sql = new Sql(new PokerParticipante(), EComando.ATUALIZAR, fn, pb);
			service.executar(new Sql[] { sql });
			return (PokerParticipante) service.selecionar(new PokerParticipante(), fn, false);
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza uma remocao de participante do torneio.
	 * 
	 * @return o participante que foi removido.
	 * @param idParticipante
	 *            identificado do participante.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/remover/{idParticipante}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public synchronized PokerParticipante remover(@PathParam(value = "idParticipante") int idParticipante) throws RestException {
		autorizar();
		try {
			// acha o participante
			FiltroNumero fn = new FiltroNumero("pokerParticipanteId", ECompara.IGUAL, idParticipante);
			PokerParticipante participante = (PokerParticipante) service.selecionar(new PokerParticipante(), fn, false);
			PokerTorneio torneio = participante.getPokerTorneio();
			int posicao = getPosicao(torneio);

			// verifica se precisa inativar a mesa
			PokerMesa mesa = participante.getPokerMesa();
			if (mesa.getPokerParticipantes().size() == 1) {
				FiltroNumero fn1 = new FiltroNumero("pokerMesaId", ECompara.IGUAL, mesa.getId());
				ParametroBinario pb = new ParametroBinario("pokerMesaAtivo", 0);
				Sql sql = new Sql(mesa, EComando.ATUALIZAR, fn1, pb);
				service.executar(new Sql[] { sql });
			}

			// verifica se precisa inativar o torneio
			if (posicao == 1) {
				FiltroNumero fn1 = new FiltroNumero("pokerTorneioId", ECompara.IGUAL, torneio.getId());
				ParametroBinario pb = new ParametroBinario("pokerTorneioAtivo", 0);
				Sql sql = new Sql(torneio, EComando.ATUALIZAR, fn1, pb);
				service.executar(new Sql[] { sql });
			}

			// atualiza o participante
			participante.setPokerParticipanteAtivo(false);
			participante.setPokerParticipantePosicao(posicao);
			participante.setPokerParticipantePremio(getPremio(torneio, posicao));
			participante.setPokerParticipantePonto(getPonto(torneio, posicao));
			participante.setPokerMesa(null);
			return (PokerParticipante) service.salvar(participante);
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza uma mudanca do participante de mesa.
	 * 
	 * @param idParticipante
	 *            identificado do participante.
	 * @param mesaNumero
	 *            o numero da mesa.
	 * @return o participante movido.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/mudar/{idParticipante}/{mesaNumero}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public synchronized PokerParticipante mudar(@PathParam(value = "idParticipante") int idParticipante, @PathParam(value = "mesaNumero") int mesaNumero) throws RestException {
		try {
			// acha o participante
			FiltroNumero fn = new FiltroNumero("pokerParticipanteId", ECompara.IGUAL, idParticipante);
			PokerParticipante participante = (PokerParticipante) service.selecionar(new PokerParticipante(), fn, false);
			// acha a mesa destino
			FiltroObjeto fo = new FiltroObjeto("pokerTorneio", ECompara.IGUAL, participante.getPokerTorneio());
			FiltroNumero fn2 = new FiltroNumero("pokerMesaNumero", ECompara.IGUAL, mesaNumero);
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fn2 });
			PokerMesa mesaDestino = (PokerMesa) service.selecionar(new PokerMesa(), gf, false);
			if (mesaDestino != null) {
				// verifica se precisa inativar a mesa
				PokerMesa mesaOrigem = participante.getPokerMesa();
				if (mesaOrigem.getPokerParticipantes().size() == 1) {
					FiltroNumero fn1 = new FiltroNumero("pokerMesaId", ECompara.IGUAL, mesaOrigem.getId());
					ParametroBinario pb = new ParametroBinario("pokerMesaAtivo", 0);
					Sql sql = new Sql(mesaOrigem, EComando.ATUALIZAR, fn1, pb);
					service.executar(new Sql[] { sql });
				}

				participante.setPokerMesa(mesaDestino);
				return (PokerParticipante) service.salvar(participante);
			} else {
				throw new Exception("Nao achou a mesa");
			}
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna os cash abertos.
	 * 
	 * @return Uma lista de cash abertos.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/cash")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<PokerCash> getCash() throws RestException {
		autorizar();
		try {
			FiltroBinario fb = new FiltroBinario("pokerCashFechado", ECompara.IGUAL, 0);
			PokerCash cash = new PokerCash();
			Lista<PokerCash> lista = service.selecionar(cash, 0, 0, fb, false);
			return lista.getLista();
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna as formas de pagamentos.
	 * 
	 * @return Uma lista de tipos de pagamentos.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/forma")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<PokerForma> getForma() throws RestException {
		autorizar();
		try {
			FiltroBinario fb = new FiltroBinario("pokerFormaJackpot", ECompara.IGUAL, 0);
			PokerForma forma = new PokerForma();
			Lista<PokerForma> lista = service.selecionar(forma, 0, 0, fb, false);
			return lista.getLista();
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna os jogadores do cash.
	 * 
	 * @param idCash
	 *            identifica o cash atual.
	 * @return Uma lista de jogadores.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/jogador/{idCash}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<PokerJogador> getJogador(@PathParam(value = "idCash") int idCash) throws RestException {
		autorizar();
		try {
			FiltroBinario fb = new FiltroBinario("pokerJogadorAtivo", ECompara.IGUAL, 1);
			FiltroObjeto fo = new FiltroObjeto("pokerCash", ECompara.IGUAL, new PokerCash(idCash));
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fb, fo });
			PokerJogador jogador = new PokerJogador();
			Lista<PokerJogador> lista = service.selecionar(jogador, 0, 0, gf, false);
			return lista.getLista();
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que retorna os clientes do sistema.
	 * 
	 * @return Uma lista de clientes.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/cliente")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<PokerCliente> getCliente() throws RestException {
		autorizar();
		try {
			FiltroBinario fb = new FiltroBinario("pokerClienteAtivo", ECompara.IGUAL, 1);
			PokerCliente cliente = new PokerCliente();
			Lista<PokerCliente> lista = service.selecionar(cliente, 0, 0, fb, false);
			return lista.getLista();
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza uma compra de fichas.
	 * 
	 * @param idJogador
	 *            identifica o jogador.
	 * @param idForma
	 *            identifica a forma de pagamento.
	 * @param valor
	 *            identifica o valor de pagamento.
	 * @return o jogador que comprou fichas.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/receber/{idJogador}/{idForma}/{valor}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public PokerJogador receber(@PathParam(value = "idJogador") int idJogador, @PathParam(value = "idForma") int idForma, @PathParam(value = "valor") int valor) throws RestException {
		autorizar();
		try {
			if (valor > 0) {
				// acha e atualiza o jogador
				FiltroNumero fn = new FiltroNumero("pokerJogadorId", ECompara.IGUAL, idJogador);
				PokerJogador jogador = (PokerJogador) service.selecionar(new PokerJogador(), fn, false);

				// acha a forma de pagamento
				FiltroNumero fn1 = new FiltroNumero("pokerFormaId", ECompara.IGUAL, idForma);
				PokerForma forma = (PokerForma) service.selecionar(new PokerForma(), fn1, false);

				// salva o recebimento
				PokerReceber receber = new PokerReceber();
				receber.setPokerCash(jogador.getPokerCash());
				receber.setPokerForma(forma);
				receber.setPokerReceberCadastrado(new Date());
				receber.setPokerReceberDescricao(jogador.getPokerCliente().getPokerClienteNome());
				receber.setPokerReceberValor(valor);
				if (forma.getPokerFormaRealizado()) {
					receber.setPokerReceberRealizado(new Date());
					receber.setPokerReceberAtivo(true);
				}
				service.salvar(receber);
				return jogador;
			} else {
				throw new PokerException("Informe um valor maior que zero!");
			}
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza uma quitacao de compra.
	 * 
	 * @param idJogador
	 *            identifica o jogador.
	 * @param idForma
	 *            identifica a forma de pagamento.
	 * @param valor
	 *            identifica o valor de pagamento.
	 * @return o jogador que comprou fichas.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/pagar/{idJogador}/{idForma}/{valor}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public PokerJogador pagar(@PathParam(value = "idJogador") int idJogador, @PathParam(value = "idForma") int idForma, @PathParam(value = "valor") int valor) throws RestException {
		autorizar();
		try {
			if (valor > 0) {
				// acha o jogador
				FiltroNumero fn = new FiltroNumero("pokerJogadorId", ECompara.IGUAL, idJogador);
				PokerJogador jogador = (PokerJogador) service.selecionar(new PokerJogador(), fn, false);

				// acha a forma de pagamento
				FiltroNumero fn1 = new FiltroNumero("pokerFormaId", ECompara.IGUAL, idForma);
				PokerForma forma = (PokerForma) service.selecionar(new PokerForma(), fn1, false);

				// salva o pagamento
				PokerPagar pagar = new PokerPagar();
				pagar.setPokerCash(jogador.getPokerCash());
				pagar.setPokerForma(forma);
				pagar.setPokerPagarCadastrado(new Date());
				pagar.setPokerPagarDescricao(jogador.getPokerCliente().getPokerClienteNome());
				pagar.setPokerPagarValor(valor);
				if (forma.getPokerFormaRealizado()) {
					pagar.setPokerPagarRealizado(new Date());
					pagar.setPokerPagarAtivo(true);
				}
				service.salvar(pagar);
				return jogador;
			} else {
				throw new PokerException("Informe um valor maior que zero!");
			}
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza uma adicao de jogador do cash.
	 * 
	 * @param idCash
	 *            identifica o cash.
	 * @param idCliente
	 *            identifica o cliente.
	 * @return o jogador que foi adicionado.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/adicionar/{idCash}/{idCliente}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public PokerJogador adicionar(@PathParam(value = "idCash") int idCash, @PathParam(value = "idCliente") int idCliente) throws RestException {
		autorizar();
		try {
			PokerJogador jogador = new PokerJogador();
			jogador.setPokerCash(new PokerCash(idCash));
			jogador.setPokerCliente(new PokerCliente(idCliente));
			jogador.setPokerJogadorAtivo(true);
			return (PokerJogador) service.salvar(jogador);
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Metodo que realiza uma exclusao de jogador do cash.
	 * 
	 * @param idJogador
	 *            identifica o jogador.
	 * @param quitar
	 *            informa se quita os pendentes.
	 * @return o jogador que foi excluido.
	 * @throws RestException
	 *             em caso de nao conseguir acessar a informacao.
	 */
	@Path("/excluir/{idJogador}/{quitar}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public PokerJogador excluir(@PathParam(value = "idJogador") int idJogador, @PathParam(value = "quitar") boolean quitar) throws RestException {
		autorizar();
		try {
			// acha e atualiza o jogador
			FiltroNumero fn = new FiltroNumero("pokerJogadorId", ECompara.IGUAL, idJogador);
			PokerJogador jogador = (PokerJogador) service.selecionar(new PokerJogador(), fn, false);
			jogador.setPokerJogadorAtivo(false);

			if (quitar) {
				FiltroObjeto fo = new FiltroObjeto("pokerCash", ECompara.IGUAL, jogador.getPokerCash());
				FiltroTexto ft = new FiltroTexto("pokerReceberDescricao", ECompara.IGUAL, jogador.getPokerCliente().getPokerClienteNome());
				FiltroBinario fb = new FiltroBinario("pokerReceberAtivo", ECompara.IGUAL, 0);
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft, fb });
				Sql sql = new Sql(new PokerReceber(), EComando.EXCLUIR, gf);
				service.executar(new Sql[] { sql });
			}

			return (PokerJogador) service.salvar(jogador);
		} catch (Exception ex) {
			throw new RestException(ex);
		}
	}

	/**
	 * Recupera a posicao do participante no torneio.
	 * 
	 * @param torneio
	 *            o torneio que o participante se encontra.
	 * @return a posicao dele.
	 */
	private int getPosicao(PokerTorneio torneio) {
		int posicao = torneio.getPokerParticipantes().size() + 1;
		for (PokerParticipante par : torneio.getPokerParticipantes()) {
			if (par.getPokerParticipantePosicao() > 0 && par.getPokerParticipantePosicao() < posicao) {
				posicao = par.getPokerParticipantePosicao();
			}
		}
		return --posicao;
	}

	/**
	 * Recupera o premio do participante no torneio.
	 * 
	 * @param torneio
	 *            o torneio que o participante se encontra.
	 * @param posicao
	 *            a posicao do participante no torneio.
	 * @return o valor do premio pela posicao.
	 */
	private double getPremio(PokerTorneio torneio, int posicao) {
		double premio = 0.00;
		for (PokerPremiacao premiacao : torneio.getPokerPremiacoes()) {
			if (premiacao.getPokerPremiacaoPosicao() == posicao) {
				premio = premiacao.getPokerPremiacaoValor();
				break;
			}
		}
		return premio;
	}

	/**
	 * Recupera os pontos do participante no torneio.
	 * 
	 * @param torneio
	 *            o torneio que o participante se encontra.
	 * @param posicao
	 *            a posicao do participante no torneio.
	 * @return o valor do ponto pela posicao.
	 */
	private int getPonto(PokerTorneio torneio, int posicao) {
		int ponto = torneio.getPokerTorneioPonto();
		for (PokerPremiacao premiacao : torneio.getPokerPremiacoes()) {
			if (premiacao.getPokerPremiacaoPosicao() == posicao) {
				ponto += premiacao.getPokerPremiacaoPonto();
				break;
			}
		}
		return ponto;
	}

}
