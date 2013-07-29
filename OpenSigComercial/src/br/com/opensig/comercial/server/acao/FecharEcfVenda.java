package br.com.opensig.comercial.server.acao;

import javax.persistence.EntityManager;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.server.ComercialServiceImpl;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
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
import br.com.opensig.produto.shared.modelo.ProdEstoque;
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;
import br.com.opensig.produto.shared.modelo.ProdGrade;

public class FecharEcfVenda extends Chain {

	private CoreServiceImpl servico;
	private ComercialServiceImpl impl;
	private ComEcfVenda venda;

	public FecharEcfVenda(Chain next, CoreServiceImpl servico, ComEcfVenda venda, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.venda = venda;
		this.impl = new ComercialServiceImpl();

		// seta a venda
		FiltroNumero fn = new FiltroNumero("comEcfVendaId", ECompara.IGUAL, venda.getId());
		this.venda = (ComEcfVenda) servico.selecionar(venda, fn, false);
		
		// atualiza venda
		AtualizarVenda atuVen = new AtualizarVenda(next);
		// atauliza estoque
		AtualizarEstoque atuEst = new AtualizarEstoque(atuVen);
		if (!auth.getConf().get("estoque.ativo").equalsIgnoreCase("ignorar")) {
			this.next = atuEst;
		} else {
			this.next = atuVen;
		}
	}

	@Override
	public void execute() throws OpenSigException {
		if (next != null) {
			next.execute();
		}
	}

	private class AtualizarEstoque extends Chain {

		public AtualizarEstoque(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				FiltroObjeto fo1 = new FiltroObjeto("empEmpresa", ECompara.IGUAL, venda.getComEcf().getEmpEmpresa());
				em = Conexao.EMFS.get(new ProdEstoque().getPu()).createEntityManager();
				em.getTransaction().begin();

				for (ComEcfVendaProduto vp : venda.getComEcfVendaProdutos()) {
					if (!vp.getComEcfVendaProdutoCancelado()) {
						// fatorando a quantida no estoque
						double qtd = vp.getComEcfVendaProdutoQuantidade();
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
						if (vp.getProdProduto().getProdGrades() != null) {
							for (ProdGrade grade : vp.getProdProduto().getProdGrades()) {
								if (grade.getProdGradeBarra().equals(vp.getComEcfVendaProdutoBarra())) {
									// formando os parametros
									ParametroFormula pn2 = new ParametroFormula("prodEstoqueGradeQuantidade", -1 * qtd);
									// formando o filtro
									FiltroObjeto fo3 = new FiltroObjeto("prodGrade", ECompara.IGUAL, grade);
									GrupoFiltro gf1 = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo1, fo3 });
									// formando o sql
									Sql sql1 = new Sql(new ProdEstoqueGrade(), EComando.ATUALIZAR, gf1, pn2);
									servico.executar(em, sql1);
									break;
								}
							}
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
				if (em != null) {
					em.close();
				}
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
			FiltroNumero fn = new FiltroNumero("comEcfVendaId", ECompara.IGUAL, venda.getId());
			ParametroBinario pb = new ParametroBinario("comEcfVendaFechada", 1);
			Sql sql = new Sql(venda, EComando.ATUALIZAR, fn, pb);
			servico.executar(new Sql[] { sql });

			if (next != null) {
				next.execute();
			}
		}
	}
}
