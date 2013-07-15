package br.com.opensig.comercial.server.acao;

import javax.persistence.EntityManager;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;

public class ExcluirFrete extends Chain {

	private CoreServiceImpl servico;
	private ComFrete frete;
	private Autenticacao auth;

	public ExcluirFrete(Chain next, CoreServiceImpl servico, ComFrete frete, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.auth = auth;

		// seleciona o frete
		FiltroNumero fn = new FiltroNumero("comFreteId", ECompara.IGUAL, frete.getId());
		this.frete = (ComFrete) servico.selecionar(frete, fn, false);
		
		// atualiza frete
		DeletarFrete delFrete = new DeletarFrete(next);
		// valida os pagamentos
		ValidarPagar valPagar = new ValidarPagar(delFrete);
		this.next = valPagar;
	}

	public void execute() throws OpenSigException {
		if (next != null) {
			next.execute();
		}
	}

	private class ValidarPagar extends Chain {

		public ValidarPagar(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			// valida se tem a pagar
			if (frete.getFinPagar() != null) {
				// valida se os pagamentos tem algum conciliado
				for (FinPagamento pagamento : frete.getFinPagar().getFinPagamentos()) {
					if (pagamento.getFinPagamentoStatus().equalsIgnoreCase(auth.getConf().get("txtConciliado"))) {
						throw new OpenSigException("Existe pagamentos conciliados! Estorne antes de excluir o frete.");
					}
				}
			}

			if (next != null) {
				next.execute();
			}
		}
	}

	private class DeletarFrete extends Chain {

		public DeletarFrete(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				em = Conexao.EMFS.get(frete.getPu()).createEntityManager();
				em.getTransaction().begin();
				servico.deletar(em, frete);

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				throw new ComercialException(ex.getMessage());
			} finally {
				if (em != null) {
					em.close();
				}
			}
		}
	}
}
