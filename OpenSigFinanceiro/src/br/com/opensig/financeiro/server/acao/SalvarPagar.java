package br.com.opensig.financeiro.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.financeiro.client.servico.FinanceiroException;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

public class SalvarPagar extends Chain {

	private CoreServiceImpl servico;
	private FinPagar pagar;
	private List<FinCategoria> categorias;

	public SalvarPagar(Chain next, CoreServiceImpl servico, FinPagar pagar, List<FinCategoria> categorias) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.pagar = pagar;
		this.categorias = categorias;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(pagar.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			List<FinPagamento> pagamentos = pagar.getFinPagamentos();
			// deleta
			if (pagar.getFinPagarId() > 0) {
				FiltroObjeto fo = new FiltroObjeto("finPagar", ECompara.IGUAL, pagar);
				Sql sql = new Sql(new FinPagamento(), EComando.EXCLUIR, fo);
				servico.executar(em, sql);
			}

			// salva
			pagar.setFinPagamentos(null);
			servico.salvar(em, pagar);

			// insere
			for (FinPagamento finPag : pagamentos) {
				finPag.setFinPagamentoId(0);
				finPag.setFinPagar(pagar);
			}
			servico.salvar(em, pagamentos);

			// categorias
			if (categorias != null && !categorias.isEmpty()) {
				servico.salvar(em, categorias);
			}

			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar pagar", ex);
			throw new FinanceiroException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

	public CoreServiceImpl getServico() {
		return servico;
	}

	public void setServico(CoreServiceImpl servico) {
		this.servico = servico;
	}

	public FinPagar getPagar() {
		return pagar;
	}

	public void setPagar(FinPagar pagar) {
		this.pagar = pagar;
	}

	public List<FinCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<FinCategoria> categorias) {
		this.categorias = categorias;
	}
}
