package br.com.opensig.comercial.server.acao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.server.ComercialServiceImpl;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.produto.shared.modelo.ProdComposicao;
import br.com.opensig.produto.shared.modelo.ProdEstoque;
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;
import br.com.opensig.produto.shared.modelo.ProdGrade;

public class FecharVenda extends Chain {

	private CoreServiceImpl servico;
	private ComercialServiceImpl impl;
	private ComVenda venda;
	private List<String[]> invalidos;

	public FecharVenda(Chain next, CoreServiceImpl servico, ComVenda venda, List<String[]> invalidos, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.venda = venda;
		this.invalidos = invalidos;
		this.impl = new ComercialServiceImpl();

		// atualiza venda
		AtualizarVenda atuVen = new AtualizarVenda(next);
		// atualiza estoque
		AtualizarEstoque atuEst = new AtualizarEstoque(atuVen);
		// valida o estoque
		ValidarEstoque valEst = new ValidarEstoque(atuEst);
		if (auth.getConf().get("estoque.ativo").equalsIgnoreCase("sim")) {
			this.next = valEst;
		} else if (auth.getConf().get("estoque.ativo").equalsIgnoreCase("nao")) {
			this.next = atuEst;
		} else {
			this.next = atuVen;
		}
	}

	@Override
	public void execute() throws OpenSigException {
		FiltroNumero fn = new FiltroNumero("comVendaId", ECompara.IGUAL, venda.getId());
		venda = (ComVenda) servico.selecionar(venda, fn, false);
		
		// verifica se tem produtos com composicoes
		List<ComVendaProduto> auxProdutos = new ArrayList<ComVendaProduto>();
		for (ComVendaProduto vp : venda.getComVendaProdutos()) {
			auxProdutos.add(vp);
			for (ProdComposicao comp : vp.getProdProduto().getProdComposicoes()) {
				ComVendaProduto auxVenProd = new ComVendaProduto();
				auxVenProd.setProdProduto(comp.getProdProduto());
				auxVenProd.setProdEmbalagem(comp.getProdEmbalagem());
				double qtd = vp.getComVendaProdutoQuantidade() * comp.getProdComposicaoQuantidade();
				auxVenProd.setComVendaProdutoQuantidade(qtd);
				auxProdutos.add(auxVenProd);
			}
		}
		venda.setComVendaProdutos(auxProdutos);
		
		if (next != null) {
			next.execute();
		}
	}

	private class ValidarEstoque extends Chain {

		public ValidarEstoque(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			try {
				for (ComVendaProduto vp : venda.getComVendaProdutos()) {
					// formando o filtro
					FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, venda.getEmpEmpresa());
					FiltroObjeto fo1 = new FiltroObjeto("prodProduto", ECompara.IGUAL, vp.getProdProduto());
					GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo, fo1 });
					// busca o item
					ProdEstoque est = (ProdEstoque) servico.selecionar(new ProdEstoque(), gf, false);
					// fatorando a quantida no estoque
					double qtd = vp.getComVendaProdutoQuantidade();
					if (vp.getProdEmbalagem().getProdEmbalagemId() != vp.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
						qtd *= impl.getQtdEmbalagem(vp.getProdEmbalagem().getProdEmbalagemId());
						qtd /= impl.getQtdEmbalagem(vp.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
					}
					// verificar a qtd do estoque
					if (qtd > est.getProdEstoqueQuantidade()) {
						invalidos.add(new String[] { est.getProdEstoqueId() + "", vp.getProdProduto().getProdProdutoDescricao(), vp.getProdProduto().getProdProdutoReferencia(),
								est.getProdEstoqueQuantidade().toString(), qtd + "" });
					} else {
						vp.setComVendaProdutoQuantidade(qtd);
					}
				}
			} catch (Exception ex) {
				UtilServer.LOG.error("Erro ao validar o estoque.", ex);
			}

			if (next != null && invalidos.isEmpty()) {
				next.execute();
			}
		}
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
				emf = Conexao.getInstancia(new ProdEstoque().getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				// recupera uma instância do gerenciador de entidades
				FiltroObjeto fo1 = new FiltroObjeto("empEmpresa", ECompara.IGUAL, venda.getEmpEmpresa());
				for (ComVendaProduto vp : venda.getComVendaProdutos()) {
					// fatorando a quantida no estoque
					double qtd = vp.getComVendaProdutoQuantidade();
					if (vp.getProdEmbalagem().getProdEmbalagemId() != vp.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
						qtd *= impl.getQtdEmbalagem(vp.getProdEmbalagem().getProdEmbalagemId());
						qtd /= impl.getQtdEmbalagem(vp.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
					}
					// formando os parametros
					ParametroFormula pn1 = new ParametroFormula("prodEstoqueQuantidade", -1 * qtd);
					// formando o filtro
					FiltroObjeto fo2 = new FiltroObjeto("prodProduto", ECompara.IGUAL, vp.getProdProduto());
					GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo1, fo2 });
					// formando o sql
					Sql sql = new Sql(new ProdEstoque(), EComando.ATUALIZAR, gf, pn1);
					servico.executar(em, sql);

					// remove estoque da grade caso o produto tenha
					for (ProdGrade grade : vp.getProdProduto().getProdGrades()) {
						if (grade.getProdGradeBarra().equals(vp.getComVendaProdutoBarra())) {
							// formando os parametros
							ParametroFormula pn2 = new ParametroFormula("prodEstoqueGradeQuantidade", -1 * qtd);
							// formando o filtro
							FiltroObjeto fo3 = new FiltroObjeto("prodGrade", ECompara.IGUAL, grade);
							GrupoFiltro gf1 = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo1, fo3 });
							// busca o item
							Sql sql1 = new Sql(new ProdEstoqueGrade(), EComando.ATUALIZAR, gf1, pn2);
							servico.executar(em, sql1);
							break;
						}
					}
				}

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao atualizar o estoque.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	}

	private class AtualizarVenda extends Chain {

		public AtualizarVenda(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			// atualiza o status para fechada
			FiltroNumero fn = new FiltroNumero("comVendaId", ECompara.IGUAL, venda.getId());
			ParametroBinario pb = new ParametroBinario("comVendaFechada", 1);
			Sql sql = new Sql(venda, EComando.ATUALIZAR, fn, pb);
			servico.executar(new Sql[] { sql });

			if (next != null) {
				next.execute();
			}
		}
	}
	
}
