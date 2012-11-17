package br.com.opensig.comercial.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

public class SalvarVenda extends Chain {

	private CoreServiceImpl servico;
	private ComVenda venda;

	public SalvarVenda(Chain next, CoreServiceImpl servico, ComVenda venda) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.venda = venda;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(venda.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			List<ComVendaProduto> produtos = venda.getComVendaProdutos();
			// deleta
			if (venda.getComVendaId() > 0) {
				FiltroObjeto fo = new FiltroObjeto("comVenda", ECompara.IGUAL, venda);
				Sql sql = new Sql(new ComVendaProduto(), EComando.EXCLUIR, fo);
				servico.executar(em, sql);
			}

			// salva
			venda.setComVendaProdutos(null);
			venda.setFinReceber(null);
			venda.setFisNotaSaida(null);
			servico.salvar(em, venda);

			// insere
			for (ComVendaProduto comProd : produtos) {
				comProd.setComVenda(venda);
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

			UtilServer.LOG.error("Erro ao salvar a venda.", ex);
			throw new ComercialException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

}
