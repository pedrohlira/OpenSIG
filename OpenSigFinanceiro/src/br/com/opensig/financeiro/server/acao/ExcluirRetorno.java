package br.com.opensig.financeiro.server.acao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroData;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
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
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;
import br.com.opensig.financeiro.shared.modelo.FinRetorno;

public class ExcluirRetorno extends Chain {

	private CoreServiceImpl servico;
	private FinRetorno retorno;
	private Autenticacao auth;

	public ExcluirRetorno(Chain next, CoreServiceImpl servico, FinRetorno retorno, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.retorno = retorno;
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// seleciona os recebimentos
			FiltroNumero fn1 = new FiltroNumero("finRetornoId", ECompara.IGUAL, retorno.getId());
			retorno = (FinRetorno) servico.selecionar(retorno, fn1, false);

			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(retorno.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			// gerando o filtro e atualizando os recebimentos
			GrupoFiltro gf = new GrupoFiltro();
			FinRecebimento recebimento = new FinRecebimento();
			for (String bol : retorno.getFinRetornoBoletos().split(" ")) {
				FiltroNumero fn = new FiltroNumero("finRecebimentoId", ECompara.IGUAL, bol);
				FiltroTexto ft = new FiltroTexto("finRecebimentoStatus", ECompara.DIFERENTE, auth.getConf().get("txtAberto"));
				GrupoFiltro gf1 = new GrupoFiltro(EJuncao.E, new IFiltro[] { fn, ft });

				ParametroTexto pt = new ParametroTexto("finRecebimentoStatus", auth.getConf().get("txtAberto").toUpperCase());
				ParametroData pd = new ParametroData("finRecebimentoRealizado", (Date) null);
				ParametroData pd1 = new ParametroData("finRecebimentoConciliado", (Date) null);
				ParametroTexto pt1 = new ParametroTexto("finRecebimentoObservacao", "");
				GrupoParametro gp = new GrupoParametro(new IParametro[] { pt, pd, pd1, pt1 });

				Sql sql = new Sql(recebimento, EComando.ATUALIZAR, gf1, gp);
				servico.executar(em, sql);
				gf.add(gf1, EJuncao.OU);
			}

			// atualizando a conta
			Number valor = servico.buscar(recebimento, "t.finRecebimentoValor", EBusca.SOMA, gf);
			FiltroNumero fn = new FiltroNumero("finContaId", ECompara.IGUAL, retorno.getFinConta().getId());
			ParametroFormula pnc = new ParametroFormula("finContaSaldo", -1 * valor.doubleValue());
			Sql sql = new Sql(new FinConta(), EComando.ATUALIZAR, fn, pnc);
			servico.executar(em, sql);

			// deleta retorno
			servico.deletar(em, retorno);
			
			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao excluir retorno", ex);
			throw new FinanceiroException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}
}
