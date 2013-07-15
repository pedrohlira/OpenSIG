package br.com.opensig.comercial.server.acao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComTroca;
import br.com.opensig.comercial.shared.modelo.ComTrocaProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

public class SalvarTroca extends Chain {

	private CoreServiceImpl servico;
	private ComTroca troca;

	public SalvarTroca(Chain next, CoreServiceImpl servico, ComTroca troca) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.troca = troca;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			em = Conexao.EMFS.get(troca.getPu()).createEntityManager();
			em.getTransaction().begin();

			List<ComTrocaProduto> produtos = troca.getComTrocaProdutos();
			// deleta
			if (troca.getComTrocaId() > 0) {
				FiltroObjeto fo = new FiltroObjeto("comTroca", ECompara.IGUAL, troca);
				Sql sql = new Sql(new ComTrocaProduto(), EComando.EXCLUIR, fo);
				servico.executar(em, sql);
			}

			// salva
			troca.setComTrocaProdutos(null);
			servico.salvar(em, troca);

			// insere
			for (ComTrocaProduto comProd : produtos) {
				comProd.setComTroca(troca);
			}
			servico.salvar(em, produtos);

			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar a troca.", ex);
			throw new ComercialException(ex.getMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
