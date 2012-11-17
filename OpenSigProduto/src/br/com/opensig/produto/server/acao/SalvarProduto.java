package br.com.opensig.produto.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroNumero;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.produto.client.servico.ProdutoException;
import br.com.opensig.produto.shared.modelo.ProdCategoria;
import br.com.opensig.produto.shared.modelo.ProdComposicao;
import br.com.opensig.produto.shared.modelo.ProdEstoque;
import br.com.opensig.produto.shared.modelo.ProdPreco;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class SalvarProduto extends Chain {

	private CoreServiceImpl servico;
	private ProdProduto produto;
	private List<ProdCategoria> categorias;

	public SalvarProduto(Chain next, CoreServiceImpl servico, ProdProduto produto, List<ProdCategoria> categorias) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.produto = produto;
		this.categorias = categorias;
	}

	@Override
	public void execute() throws OpenSigException {
		EntityManagerFactory emf = null;
		EntityManager em = null;

		try {
			// recupera uma instância do gerenciador de entidades
			emf = Conexao.getInstancia(produto.getPu());
			em = emf.createEntityManager();
			em.getTransaction().begin();

			// salva
			List<ProdEstoque> estoques = produto.getProdEstoques();
			List<ProdPreco> precos = produto.getProdPrecos();
			List<ProdComposicao> composicoes = produto.getProdComposicoes();
			boolean novo = produto.getProdProdutoId() == 0;
			produto.setProdEstoques(null);
			produto.setProdPrecos(null);
			produto.setProdComposicoes(null);
			servico.salvar(em, produto);

			// estoque
			salvarEstoques(em, estoques, novo);
			// precos
			salvarPrecos(em, precos);
			// composicoes
			salvarComposicoes(em, composicoes);
			// categorias
			if (categorias != null && !categorias.isEmpty()) {
				servico.salvar(em, categorias);
			}

			if (next != null) {
				next.execute();
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			UtilServer.LOG.error("Erro ao salvar o produto", ex);
			throw new ProdutoException(ex.getMessage());
		} finally {
			em.close();
			emf.close();
		}
	}

	private void salvarPrecos(EntityManager em, List<ProdPreco> precos) throws OpenSigException {
		// deleta
		if (produto.getProdProdutoId() > 0) {
			FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, produto);
			Sql sql = new Sql(new ProdPreco(), EComando.EXCLUIR, fo);
			servico.executar(em, sql);
		}
		
		if (precos != null && !precos.isEmpty()) {
			// insere
			for (ProdPreco prodPre : precos) {
				prodPre.setProdProduto(produto);
			}
			servico.salvar(em, precos);
		}
	}
	
	private void salvarComposicoes(EntityManager em, List<ProdComposicao> composicoes) throws OpenSigException {
		// deleta
		if (produto.getProdProdutoId() > 0) {
			FiltroObjeto fo = new FiltroObjeto("prodProdutoPrincipal", ECompara.IGUAL, produto);
			Sql sql = new Sql(new ProdComposicao(), EComando.EXCLUIR, fo);
			servico.executar(em, sql);
		}
		
		if (composicoes != null && !composicoes.isEmpty()) {
			// insere
			for (ProdComposicao prodComp : composicoes) {
				prodComp.setProdProdutoPrincipal(produto);
			}
			servico.salvar(em, composicoes);
		}
	}

	private void salvarEstoques(EntityManager em, List<ProdEstoque> estoques, boolean novo) throws OpenSigException {
		if (novo) {
			Lista<EmpEmpresa> empresas = servico.selecionar(new EmpEmpresa(), 0, 0, null, false);

			for (EmpEmpresa emp : empresas.getLista()) {
				ProdEstoque est = new ProdEstoque();
				est.setEmpEmpresa(emp);
				est.setProdProduto(produto);
				est.setProdEstoqueQuantidade(0.00);
				servico.salvar(em, est);
			}
		}

		if (estoques != null && !estoques.isEmpty()) {
			for (ProdEstoque est : estoques) {
				// atualiza a quantidade se passou algun estoque
				ParametroNumero pn = new ParametroNumero("prodEstoqueQuantidade", est.getProdEstoqueQuantidade());
				FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, produto);
				FiltroObjeto fo1 = new FiltroObjeto("empEmpresa", ECompara.IGUAL, est.getEmpEmpresa());
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fo1 });

				Sql sql = new Sql(est, EComando.ATUALIZAR, gf, pn);
				servico.executar(em, sql);
			}
		}
	}
}