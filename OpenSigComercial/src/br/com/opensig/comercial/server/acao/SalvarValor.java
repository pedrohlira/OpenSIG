package br.com.opensig.comercial.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComValorArredonda;
import br.com.opensig.comercial.shared.modelo.ComValorProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

public class SalvarValor extends Chain {

	private CoreServiceImpl servico;
	private ComValorProduto valor;

	public SalvarValor(Chain next, CoreServiceImpl servico, ComValorProduto venda) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.valor = venda;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(valor.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			List<ComValorArredonda> arredonda = valor.getComValorArredondas();
			// deleta
			if (valor.getComValorProdutoId() > 0) {
				FiltroObjeto fo = new FiltroObjeto("comValorProduto", ECompara.IGUAL, valor);
				Sql sql = new Sql(new ComValorArredonda(), EComando.EXCLUIR, fo);
				servico.executar(em, sql);
			}

			// salva
			valor.setComValorArredondas(null);
			servico.salvar(em, valor);

			// insere
			for (ComValorArredonda valArre : arredonda) {
				valArre.setComValorProduto(valor);
			}
			servico.salvar(em, arredonda);

			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar o valor.", ex);
			throw new ComercialException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

}
