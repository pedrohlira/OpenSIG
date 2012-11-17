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
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class ExcluirReceber extends Chain {

	private CoreServiceImpl servico;
	private FinReceber receber;
	private Autenticacao auth;

	public ExcluirReceber(Chain next, CoreServiceImpl servico, FinReceber receber, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.receber = receber;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// seleciona os recebimentos
			FiltroNumero fn1 = new FiltroNumero("finReceberId", ECompara.IGUAL, receber.getId());
			receber = (FinReceber) servico.selecionar(receber, fn1, false);

			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(receber.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			// gerando o filtro
			FiltroObjeto fo = new FiltroObjeto("finReceber", ECompara.IGUAL, receber);
			FiltroTexto ft = new FiltroTexto("finRecebimentoStatus", ECompara.DIFERENTE, auth.getConf().get("txtAberto"));
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, ft });

			// atualizando a conta
			Number valor = servico.buscar(new FinRecebimento(), "t.finRecebimentoValor", EBusca.SOMA, gf);
			if (valor != null) {
				FiltroNumero fn = new FiltroNumero("finContaId", ECompara.IGUAL, receber.getFinConta().getId());
				ParametroFormula pnc = new ParametroFormula("finContaSaldo", -1 * valor.doubleValue());
				Sql sql = new Sql(new FinConta(), EComando.ATUALIZAR, fn, pnc);
				servico.executar(em, sql);
			}

			// deleta retorno
			servico.deletar(em, receber);
			
			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao excluir receber", ex);
			throw new FinanceiroException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}
}
