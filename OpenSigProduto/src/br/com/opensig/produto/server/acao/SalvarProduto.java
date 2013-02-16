package br.com.opensig.produto.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
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
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;
import br.com.opensig.produto.shared.modelo.ProdGrade;
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
			List<ProdGrade> grades = produto.getProdGrades();
			boolean novo = produto.getProdProdutoId() == 0;
			produto.setProdEstoques(null);
			produto.setProdPrecos(null);
			produto.setProdComposicoes(null);
			produto.setProdGrades(null);
			servico.salvar(em, produto);

			// valida se o codigo de barras existe na tabela de precos
			FiltroTexto ft = new FiltroTexto("prodPrecoBarra", ECompara.IGUAL, produto.getProdProdutoBarra());
			if (produto.getProdProdutoBarra() != null && servico.selecionar(new ProdPreco(), 0, 0, ft, true).getTotal() > 0) {
				throw new Exception("DUPLICATE preco auxiliar com o mesmo codigo de barras.");
			}

			// valida se o codigo de barras existe na tabela de grade
			FiltroTexto ft1 = new FiltroTexto("prodGradeBarra", ECompara.IGUAL, produto.getProdProdutoBarra());
			if (produto.getProdProdutoBarra() != null && servico.selecionar(new ProdGrade(), 0, 0, ft1, true).getTotal() > 0) {
				throw new Exception("DUPLICATE item na grade com o mesmo codigo de barras.");
			}

			// estoque
			salvarEstoques(em, estoques, novo);
			// precos
			salvarPrecos(em, precos);
			// composicoes
			salvarComposicoes(em, composicoes);
			// grades
			salvarGrades(em, grades);
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

		// insere
		if (precos != null && !precos.isEmpty()) {
			for (ProdPreco prodPre : precos) {
				// valida se o codigo de barras existe na tabela de produto
				FiltroTexto ft = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, prodPre.getProdPrecoBarra());
				if (prodPre.getProdPrecoBarra() != null && servico.selecionar(new ProdProduto(), 0, 0, ft, true).getTotal() > 0) {
					throw new OpenSigException("DUPLICATE produto com o mesmo codigo de barras do preco auxiliar.");
				} else {
					prodPre.setProdProduto(produto);
				}
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

	private void salvarGrades(EntityManager em, List<ProdGrade> grades) throws OpenSigException {
		// deleta
		if (produto.getProdProdutoId() > 0) {
			FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, produto);
			Sql sql = new Sql(new ProdGrade(), EComando.EXCLUIR, fo);
			servico.executar(em, sql);
		}

		// insere
		if (grades != null && !grades.isEmpty()) {
			for (ProdGrade grade : grades) {
				List<ProdEstoqueGrade> estoques = grade.getProdEstoqueGrades();
				grade.setProdEstoqueGrades(null);

				// valida se o codigo de barras existe na tabela de produto
				FiltroTexto ft = new FiltroTexto("prodProdutoBarra", ECompara.IGUAL, grade.getProdGradeBarra());
				if (grade.getProdGradeBarra() != null && servico.selecionar(new ProdProduto(), 0, 0, ft, true).getTotal() > 0) {
					throw new OpenSigException("DUPLICATE produto com o mesmo codigo de barras do item da grade.");
				} else {
					grade.setProdProduto(produto);
				}
				grade = (ProdGrade) servico.salvar(em, grade);
				// estoque
				salvarEstoqueGrades(em, estoques, grade);
			}
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

	private void salvarEstoqueGrades(EntityManager em, List<ProdEstoqueGrade> estoques, ProdGrade grade) throws OpenSigException {
		Lista<EmpEmpresa> empresas = servico.selecionar(new EmpEmpresa(), 0, 0, null, false);
		for (EmpEmpresa emp : empresas.getLista()) {
			ProdEstoqueGrade est = new ProdEstoqueGrade();
			est.setEmpEmpresa(emp);
			est.setProdGrade(grade);
			est.setProdEstoqueGradeQuantidade(0.00);
			servico.salvar(em, est);
		}

		if (estoques != null && !estoques.isEmpty()) {
			for (ProdEstoqueGrade est : estoques) {
				// atualiza a quantidade se passou algun estoque
				ParametroNumero pn = new ParametroNumero("prodEstoqueGradeQuantidade", est.getProdEstoqueGradeQuantidade());
				FiltroObjeto fo = new FiltroObjeto("prodGrade", ECompara.IGUAL, grade);
				FiltroObjeto fo1 = new FiltroObjeto("empEmpresa", ECompara.IGUAL, est.getEmpEmpresa());
				GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fo1 });

				Sql sql = new Sql(est, EComando.ATUALIZAR, gf, pn);
				servico.executar(em, sql);
			}
		}
	}
}