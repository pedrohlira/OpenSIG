package br.com.opensig.poker.server;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.controlador.parametro.ParametroNumero;
import br.com.opensig.core.client.servico.CoreException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.poker.client.servico.PokerException;
import br.com.opensig.poker.client.servico.PokerService;
import br.com.opensig.poker.shared.modelo.PokerCash;
import br.com.opensig.poker.shared.modelo.PokerJogador;
import br.com.opensig.poker.shared.modelo.PokerMesa;
import br.com.opensig.poker.shared.modelo.PokerNivel;
import br.com.opensig.poker.shared.modelo.PokerPagar;
import br.com.opensig.poker.shared.modelo.PokerParticipante;
import br.com.opensig.poker.shared.modelo.PokerPremiacao;
import br.com.opensig.poker.shared.modelo.PokerReceber;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

/**
 * Classe que implementa a chamada no servidor da função de jogo no sistema.
 * 
 * @author Pedro H. Lira
 */

public class PokerServiceImpl extends CoreServiceImpl implements PokerService {

	public PokerServiceImpl() {
	}

	public PokerServiceImpl(Autenticacao auth) {
		super(auth);
	}

	@Override
	public PokerTorneio salvarTorneio(PokerTorneio torneio) throws PokerException {
		EntityManager em = null;

		try {
			em = Conexao.EMFS.get(torneio.getPu()).createEntityManager();
			em.getTransaction().begin();

			boolean novo = torneio.getPokerTorneioId() == 0;
			List<PokerNivel> niveis = torneio.getPokerNiveis();
			List<PokerMesa> mesas = torneio.getPokerMesas();
			List<PokerPremiacao> premios = torneio.getPokerPremiacoes();
			torneio.setPokerNiveis(null);
			torneio.setPokerMesas(null);
			torneio.setPokerPremiacoes(null);
			torneio.setPokerParticipantes(null);
			torneio = (PokerTorneio) salvar(em, torneio);

			salvarNiveis(em, torneio, niveis, novo);
			salvarMesas(em, torneio, mesas, novo);
			salvarPremios(em, torneio, premios, novo);

			em.getTransaction().commit();
			return torneio;
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar o torneio", ex);
			throw new PokerException(ex.getMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public PokerParticipante salvarParticipante(PokerParticipante participante) throws PokerException {
		try {
			// aumenta em 1 a possicao de todos que ja sairam ao adicionar um
			// novo
			FiltroNumero fn = new FiltroNumero("pokerParticipantePosicao", ECompara.MAIOR, 0);
			ParametroFormula pf = new ParametroFormula("pokerParticipantePosicao", 1);
			Sql sql = new Sql(new PokerParticipante(), EComando.ATUALIZAR, fn, pf);
			executar(new Sql[] { sql });

			return (PokerParticipante) salvar(participante);
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao salvar o participante", ex);
			throw new PokerException(ex.getMessage());
		}
	}

	@Override
	public void fecharTorneio(PokerTorneio torneio) throws PokerException {
		try {
			// recupera o torneio
			FiltroNumero fn = new FiltroNumero("pokerTorneioId", ECompara.IGUAL, torneio.getId());
			torneio = (PokerTorneio) selecionar(new PokerTorneio(), fn, false);

			// calcula o arrecadado
			double arrecadado = 0.00;
			for (int i = 0; i < torneio.getPokerParticipantes().size(); i++) {
				PokerParticipante parti = (PokerParticipante) torneio.getPokerParticipantes().get(i);
				arrecadado += torneio.getPokerTorneioEntrada();
				arrecadado += parti.getPokerParticipanteReentrada() * torneio.getPokerTorneioReentrada();
				arrecadado += parti.getPokerParticipanteAdicional() * torneio.getPokerTorneioAdicional();
			}

			// valida o premio, taxa e comissao
			double premio = torneio.getPokerTorneioPremio();
			double taxa = torneio.getPokerTorneioTaxa();
			double comissao = 0.00;
			if (arrecadado == premio) {
				taxa = 0.00;
				comissao = 0.00;
			} else if (arrecadado > premio) {
				if ((arrecadado - premio) >= (arrecadado * taxa / 100)) {
					comissao = arrecadado * taxa / 100;
					premio = arrecadado - comissao;
				} else {
					comissao = arrecadado - premio;
					taxa = comissao * 100 / arrecadado;
				}
			} else {
				comissao = arrecadado - premio;
				taxa = comissao * 100 / arrecadado;
			}

			// atualiza o torneio
			FiltroNumero fn1 = new FiltroNumero("pokerTorneioId", ECompara.IGUAL, torneio.getId());
			ParametroBinario pb = new ParametroBinario("pokerTorneioFechado", 1);
			ParametroNumero pn = new ParametroNumero("pokerTorneioArrecadado", arrecadado);
			ParametroNumero pn1 = new ParametroNumero("pokerTorneioPremio", premio);
			ParametroNumero pn2 = new ParametroNumero("pokerTorneioTaxa", taxa);
			ParametroNumero pn3 = new ParametroNumero("pokerTorneioComissao", comissao);
			GrupoParametro gp = new GrupoParametro(new IParametro[] { pb, pn, pn1, pn2, pn3 });
			Sql sql = new Sql(torneio, EComando.ATUALIZAR, fn1, gp);
			executar(new Sql[] { sql });
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao fechar o toneio.", ex);
			throw new PokerException(ex.getMessage());
		}
	}

	@Override
	public void fecharCash(PokerCash cash) throws PokerException {
		try {
			boolean vazio = true;
			// recupera o cash
			FiltroNumero fn = new FiltroNumero("pokerCashId", ECompara.IGUAL, cash.getId());
			cash = (PokerCash) selecionar(new PokerCash(), fn, false);

			for (PokerJogador jogador : cash.getPokerJogadores()) {
				if (jogador.getPokerJogadorAtivo()) {
					vazio = false;
					break;
				}
			}

			if (vazio) {
				// soma os valores pagos e recebidos
				FiltroObjeto fo = new FiltroObjeto("pokerCash", ECompara.IGUAL, cash);
				Number pago = buscar(new PokerPagar(), "pokerPagarValor", EBusca.SOMA, fo);
				double pagoValor = pago == null ? 0.00 : pago.doubleValue();
				Number recebido = buscar(new PokerReceber(), "pokerReceberValor", EBusca.SOMA, fo);
				double recebidoValor = recebido == null ? 0.00 : recebido.doubleValue();

				// atualiza o cash
				cash.setPokerCashFim(new Date());
				cash.setPokerCashFechado(true);
				cash.setPokerCashPago(pagoValor);
				cash.setPokerCashRecebido(recebidoValor);
				cash.setPokerCashSaldo(recebidoValor - pagoValor);
				cash.setPokerJogadores(null);
				salvar(cash);
			} else {
				throw new PokerException("Ainda tem jogados ativos!");
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao fechar o cash.", ex);
			throw new PokerException(ex.getMessage());
		}
	}

	private void salvarNiveis(EntityManager em, PokerTorneio torneio, List<PokerNivel> niveis, boolean novo) throws CoreException {
		if (!novo) {
			FiltroObjeto fo = new FiltroObjeto("pokerTorneio", ECompara.IGUAL, torneio);
			Sql sql = new Sql(new PokerNivel(), EComando.EXCLUIR, fo);
			executar(em, sql);
		}
		for (PokerNivel nivel : niveis) {
			nivel.setPokerNivelId(0);
			nivel.setPokerTorneio(torneio);
			salvar(em, nivel);
		}
	}

	private void salvarPremios(EntityManager em, PokerTorneio torneio, List<PokerPremiacao> premios, boolean novo) throws CoreException {
		if (!novo) {
			FiltroObjeto fo = new FiltroObjeto("pokerTorneio", ECompara.IGUAL, torneio);
			Sql sql = new Sql(new PokerPremiacao(), EComando.EXCLUIR, fo);
			executar(em, sql);
		}
		for (PokerPremiacao premio : premios) {
			premio.setPokerPremiacaoId(0);
			premio.setPokerTorneio(torneio);
			salvar(em, premio);
		}
	}

	private void salvarMesas(EntityManager em, PokerTorneio torneio, List<PokerMesa> mesas, boolean novo) throws CoreException {
		for (PokerMesa mesa : mesas) {
			if (novo) {
				mesa.setPokerMesaId(0);
			}
			mesa.setPokerTorneio(torneio);
			salvar(em, mesa);
		}
	}

}
