package br.com.opensig.financeiro.server.acao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.financeiro.client.servico.FinanceiroException;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

public class ExcluirPagar extends Chain {

	private CoreServiceImpl servico;
	private FinPagar pagar;
	private Autenticacao auth;

	public ExcluirPagar(Chain next, CoreServiceImpl servico, FinPagar pagar, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.pagar = pagar;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// seleciona os recebimentos
			FiltroNumero fn1 = new FiltroNumero("finPagarId", ECompara.IGUAL, pagar.getId());
			pagar = (FinPagar) servico.selecionar(pagar, fn1, false);

			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(pagar.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			// gerando o filtro
			FiltroObjeto fo = new FiltroObjeto("finPagar", ECompara.IGUAL, pagar);
			FiltroTexto ft = new FiltroTexto("finPagamentoStatus", ECompara.DIFERENTE, auth.getConf().get("txtAberto"));
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft });

			// atualizando a conta
			Number valor = servico.buscar(new FinPagamento(), "t.finPagamentoValor", EBusca.SOMA, gf);
			if (valor != null) {
				FiltroNumero fn = new FiltroNumero("finContaId", ECompara.IGUAL, pagar.getFinConta().getId());
				ParametroFormula pnc = new ParametroFormula("finContaSaldo", valor.doubleValue());
				Sql sql = new Sql(new FinConta(), EComando.ATUALIZAR, fn, pnc);
				servico.executar(em, sql);
			}

			// deleta retorno
			servico.deletar(em, pagar);
			
			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao excluir pagar", ex);
			throw new FinanceiroException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}
}
