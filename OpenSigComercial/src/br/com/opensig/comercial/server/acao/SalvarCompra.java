package br.com.opensig.comercial.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.produto.server.acao.SalvarProduto;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class SalvarCompra extends Chain {

	private CoreServiceImpl servico;
	private ComCompra compra;

	public SalvarCompra(Chain next, CoreServiceImpl servico, ComCompra compra) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.compra = compra;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		validarProduto();

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(compra.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			List<ComCompraProduto> produtos = compra.getComCompraProdutos();
			// deleta
			if (compra.getComCompraId() > 0) {
				FiltroObjeto fo = new FiltroObjeto("comCompra", ECompara.IGUAL, compra);
				Sql sql = new Sql(new ComCompraProduto(), EComando.EXCLUIR, fo);
				servico.executar(em, sql);
			}

			// salva
			compra.setComCompraProdutos(null);
			servico.salvar(em, compra);

			// insere
			for (ComCompraProduto comProd : produtos) {
				comProd.setComCompra(compra);
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

			UtilServer.LOG.error("Erro ao salvar a compra.", ex);
			throw new ComercialException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

	private void validarProduto() throws ComercialException {
		try {
			// salva os produtos novos
			for (ComCompraProduto comProd : compra.getComCompraProdutos()) {
				ProdProduto prod = comProd.getProdProduto();
				if (prod.getProdProdutoId() == 0) {
					SalvarProduto salProduto = new SalvarProduto(null, servico, prod, null);
					salProduto.execute();
				}
			}
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro ao validar o produto.", ex);
			throw new ComercialException(ex.getMessage());
		}
	}
}
