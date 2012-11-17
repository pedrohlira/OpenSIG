package br.com.opensig.comercial.server.acao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;

public class ExcluirFrete extends Chain {

	private CoreServiceImpl servico;
	private ComFrete frete;
	private Autenticacao auth;

	public ExcluirFrete(Chain next, CoreServiceImpl servico, ComFrete frete, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.frete = frete;
		this.auth = auth;
		
		// atualiza frete
		DeletarFrete delFrete = new DeletarFrete(next);
		// atualiza os conta
		AtualizarConta atuConta = new AtualizarConta(delFrete);
		// seleciona os produtos
		this.next = atuConta;
	}

	public void execute() throws OpenSigException {
		FiltroNumero fn = new FiltroNumero("comFreteId", ECompara.IGUAL, frete.getId());
		frete = (ComFrete) servico.selecionar(frete, fn, false);
		if (next != null) {
			next.execute();
		}
	}

	private class AtualizarConta extends Chain {

		public AtualizarConta(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				FinConta conta = new FinConta();
				emf = Conexao.getInstancia(conta.getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				if (frete.getFinPagar() != null) {
					conta = frete.getFinPagar().getFinConta();
					double valPag = 0.00;
					for (FinPagamento pag : frete.getFinPagar().getFinPagamentos()) {
						if (!pag.getFinPagamentoStatus().equalsIgnoreCase(auth.getConf().get("txtAberto"))) {
							valPag += pag.getFinPagamentoValor();
						}
					}

					if (valPag > 0) {
						conta.setFinContaSaldo(conta.getFinContaSaldo() + valPag);
						servico.salvar(em, conta);
					}
				}

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao atualiza a conta.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	}

	private class DeletarFrete extends Chain {

		public DeletarFrete(Chain next) throws OpenSigException {
			super(next);
		}
		
		@Override
		public void execute() throws OpenSigException {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				emf = Conexao.getInstancia(frete.getPu());
				em = emf.createEntityManager();
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
				em.close();
				emf.close();
			}
		}
	}
}
