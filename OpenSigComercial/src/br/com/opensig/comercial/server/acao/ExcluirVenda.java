package br.com.opensig.comercial.server.acao;

import javax.persistence.EntityManager;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;

public class ExcluirVenda extends Chain {

	private CoreServiceImpl servico;
	private ComVenda venda;

	public ExcluirVenda(Chain next, CoreServiceImpl servico, ComVenda venda) throws OpenSigException {
		super(null);
		this.servico = servico;

		// seleciona a venda
		FiltroNumero fn = new FiltroNumero("comVendaId", ECompara.IGUAL, venda.getId());
		this.venda = (ComVenda) servico.selecionar(venda, fn, false);
		
		// atualiza venda
		DeletarVenda delVen = new DeletarVenda(next);
		// seleciona os produtos
		this.next = delVen;
	}

	@Override
	public void execute() throws OpenSigException {
		if (next != null) {
			next.execute();
		}
	}

	private class DeletarVenda extends Chain {

		public DeletarVenda(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				em = Conexao.EMFS.get(venda.getPu()).createEntityManager();
				em.getTransaction().begin();
				servico.deletar(em, venda);

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao excluir a venda.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				if (em != null) {
					em.close();
				}
			}
		}
	}
}
