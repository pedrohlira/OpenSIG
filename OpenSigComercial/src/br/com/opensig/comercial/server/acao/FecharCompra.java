package br.com.opensig.comercial.server.acao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroData;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.controlador.parametro.ParametroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdEstoque;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class FecharCompra extends Chain {

	private CoreServiceImpl servico;
	private ComCompra compra;
	private List<ProdEmbalagem> embalagens;

	public FecharCompra(Chain next, CoreServiceImpl servico, ComCompra compra) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.compra = compra;

		// atualiza compra
		AtualizarCompra atuComp = new AtualizarCompra(next);
		// atualiza os produros
		AtualizarProduto atuProd = new AtualizarProduto(atuComp);
		// atauliza estoque
		AtualizarEstoque atuEst = new AtualizarEstoque(atuProd);
		// seleciona os produtos
		this.next = atuEst;
	}

	@Override
	public void execute() throws OpenSigException {
		FiltroNumero fn = new FiltroNumero("comCompraId", ECompara.IGUAL, compra.getId());
		compra = (ComCompra) servico.selecionar(compra, fn, false);
		if (next != null) {
			next.execute();
		}
	}

	private int getQtdEmbalagem(int embalagemId) throws Exception {
		int unid = 1;
		if (embalagens == null) {
			embalagens = servico.selecionar(new ProdEmbalagem(), 0, 0, null, false).getLista();
		}

		for (ProdEmbalagem emb : embalagens) {
			if (emb.getProdEmbalagemId() == embalagemId) {
				unid = emb.getProdEmbalagemUnidade();
				break;
			}
		}
		return unid;
	}

	private class AtualizarEstoque extends Chain {

		public AtualizarEstoque(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				FiltroObjeto fo1 = new FiltroObjeto("empEmpresa", ECompara.IGUAL, compra.getEmpEmpresa());
				emf = Conexao.getInstancia(new ProdEstoque().getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				for (ComCompraProduto comProd : compra.getComCompraProdutos()) {
					// formando o filtro
					FiltroObjeto fo2 = new FiltroObjeto("prodProduto", ECompara.IGUAL, comProd.getProdProduto());
					GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo1, fo2 });

					// fatorando a quantida no estoque
					double qtd = comProd.getComCompraProdutoQuantidade();
					if (comProd.getProdEmbalagem().getProdEmbalagemId() != comProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
						qtd *= getQtdEmbalagem(comProd.getProdEmbalagem().getProdEmbalagemId());
						qtd /= getQtdEmbalagem(comProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
					}

					// formando o sql
					ParametroFormula pf = new ParametroFormula("prodEstoqueQuantidade", qtd);
					Sql sql = new Sql(new ProdEstoque(), EComando.ATUALIZAR, gf, pf);
					servico.executar(em, sql);
				}

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao atualizar estoque.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	}

	private class AtualizarProduto extends Chain {

		public AtualizarProduto(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				ProdProduto prod = new ProdProduto();
				emf = Conexao.getInstancia(prod.getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				for (ComCompraProduto comProd : compra.getComCompraProdutos()) {
					// fatorando pela embalagem
					double custo = comProd.getComCompraProdutoValor();
					double preco = comProd.getComCompraProdutoPreco();
					if (comProd.getProdEmbalagem().getProdEmbalagemId() != comProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
						custo /= getQtdEmbalagem(comProd.getProdEmbalagem().getProdEmbalagemId());
						custo *= getQtdEmbalagem(comProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
						preco /= getQtdEmbalagem(comProd.getProdEmbalagem().getProdEmbalagemId());
						preco *= getQtdEmbalagem(comProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
					}
					// formando os parametros
					ParametroNumero pn1 = new ParametroNumero("prodProdutoCusto", custo);
					ParametroNumero pn2 = new ParametroNumero("prodProdutoPreco", preco);
					ParametroData pd = new ParametroData("prodProdutoAlterado", new Date());
					GrupoParametro gp = new GrupoParametro(new IParametro[] { pn1, pn2, pd });
					// formando o filtro
					FiltroNumero fn = new FiltroNumero("prodProdutoId", ECompara.IGUAL, comProd.getProdProduto().getId());
					// formando o sql
					Sql sql = new Sql(prod, EComando.ATUALIZAR, fn, gp);
					servico.executar(em, sql);
				}

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao atualizar produto.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}

	}

	private class AtualizarCompra extends Chain {

		public AtualizarCompra(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			// atualiza o status para fechada
			FiltroNumero fn = new FiltroNumero("comCompraId", ECompara.IGUAL, compra.getId());
			ParametroBinario pb = new ParametroBinario("comCompraFechada", 1);
			Sql sql = new Sql(compra, EComando.ATUALIZAR, fn, pb);
			servico.executar(new Sql[] { sql });

			if (next != null) {
				next.execute();
			}
		}
	}
}
