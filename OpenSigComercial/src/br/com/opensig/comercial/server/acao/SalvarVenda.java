package br.com.opensig.comercial.server.acao;

import java.util.List;

import javax.persistence.EntityManager;

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
import br.com.opensig.produto.server.acao.SalvarProduto;
import br.com.opensig.produto.shared.modelo.ProdProduto;

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
		EntityManager em = null;
		validarProduto();

		try {
			// recupera uma instância do gerenciador de entidades
			em = Conexao.EMFS.get(venda.getPu()).createEntityManager();
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
			servico.salvar(em, venda);

			// insere
			for (ComVendaProduto vp : produtos) {
				vp.setComVenda(venda);
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
			if (em != null) {
				em.close();
			}
		}
	}

	private void validarProduto() throws ComercialException {
		try {
			// salva os produtos novos
			for (ComVendaProduto vp : venda.getComVendaProdutos()) {
				ProdProduto prod = vp.getProdProduto();
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
