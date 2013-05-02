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
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;
import br.com.opensig.fiscal.server.acao.GerarNfeCanceladaSaida;
import br.com.opensig.fiscal.server.acao.GerarNfeInutilizadaSaida;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.produto.shared.modelo.ProdComposicao;
import br.com.opensig.produto.shared.modelo.ProdEstoque;
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;
import br.com.opensig.produto.shared.modelo.ProdGrade;

public class CancelarVenda extends Chain {

	private CoreServiceImpl servico;
	private ComercialServiceImpl impl;
	private ComVenda venda;
	private Autenticacao auth;

	public CancelarVenda(Chain next, CoreServiceImpl servico, ComVenda venda, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.auth = auth;
		this.impl = new ComercialServiceImpl();

		// seta a venda
		FiltroNumero fn = new FiltroNumero("comVendaId", ECompara.IGUAL, venda.getId());
		String motivo = venda.getComVendaObservacao();
		venda = (ComVenda) servico.selecionar(venda, fn, false);
		venda.setComVendaObservacao(motivo);
		this.venda = venda;
		
		// atualiza venda
		AtualizarVenda atuVenda = new AtualizarVenda(next);
		if (venda.getFisNotaSaida() != null) {
			if (venda.getFisNotaSaida().getFisNotaStatus().getFisNotaStatusId() == ENotaStatus.AUTORIZADO.getId()) {
				// cancela nota
				GerarNfeCanceladaSaida canNota = new GerarNfeCanceladaSaida(next, servico, venda.getFisNotaSaida(), venda.getComVendaObservacao(), auth);
				atuVenda.setNext(canNota);
			} else {
				// inutiliza nota
				int num = venda.getFisNotaSaida().getFisNotaSaidaNumero();
				GerarNfeInutilizadaSaida inuNota = new GerarNfeInutilizadaSaida(next, servico, venda.getFisNotaSaida(), venda.getComVendaObservacao(), num, num, auth);
				atuVenda.setNext(inuNota);
			}
		}
		// atualiza o estoque
		AtualizarEstoque atuEst = new AtualizarEstoque(atuVenda);
		// valida os recebimentos
		ValidarRecebimentos valReceber = new ValidarRecebimentos(null);
		
		if (!auth.getConf().get("estoque.ativo").equalsIgnoreCase("ignorar")) {
			valReceber.setNext(atuEst);
		} else {
			valReceber.setNext(atuVenda);
		}
		this.next = valReceber;
	}

	@Override
	public void execute() throws OpenSigException {
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
				FiltroObjeto fo1 = new FiltroObjeto("empEmpresa", ECompara.IGUAL, venda.getEmpEmpresa());
				emf = Conexao.getInstancia(new ProdEstoque().getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				for (ComVendaProduto vp : venda.getComVendaProdutos()) {
					// fatorando a quantida no estoque
					double qtd = vp.getComVendaProdutoQuantidade();
					if (vp.getProdEmbalagem().getProdEmbalagemId() != vp.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
						qtd *= impl.getQtdEmbalagem(vp.getProdEmbalagem().getProdEmbalagemId());
						qtd /= impl.getQtdEmbalagem(vp.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
					}
					// formando os parametros
					ParametroFormula pn1 = new ParametroFormula("prodEstoqueQuantidade", qtd);
					// formando o filtro
					FiltroObjeto fo2 = new FiltroObjeto("prodProduto", ECompara.IGUAL, vp.getProdProduto());
					GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo1, fo2 });
					// busca o item
					ProdEstoque est = new ProdEstoque();
					// formando o sql
					Sql sql = new Sql(est, EComando.ATUALIZAR, gf, pn1);
					servico.executar(em, sql);

					// remove estoque da grade caso o produto tenha
					for (ProdGrade grade : vp.getProdProduto().getProdGrades()) {
						if (grade.getProdGradeBarra().equals(vp.getComVendaProdutoBarra())) {
							// formando os parametros
							ParametroFormula pn2 = new ParametroFormula("prodEstoqueGradeQuantidade", qtd);
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

	private class ValidarRecebimentos extends Chain {

		public ValidarRecebimentos(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			// valida se tem a receber
			if (venda.getFinReceber() != null) {
				// valida se os recebimentos tem algum conciliado
				for (FinRecebimento recebimento : venda.getFinReceber().getFinRecebimentos()) {
					if (recebimento.getFinRecebimentoStatus().equalsIgnoreCase(auth.getConf().get("txtConciliado"))) {
						throw new OpenSigException("Existe recebimentos conciliados! Estorne antes de cancelar a venda.");
					}
				}
			}

			if (next != null) {
				next.execute();
			}
		}
	}

	private class AtualizarVenda extends Chain {

		public AtualizarVenda(Chain next) throws OpenSigException {
			super(next);
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
				FinReceber receber = venda.getFinReceber();

				venda.setComVendaCancelada(true);
				venda.setFinReceber(null);
				servico.salvar(em, venda);

				if (next != null) {
					next.execute();
				}

				em.getTransaction().commit();
				if (receber != null) {
					deletarReceber(receber);
				}
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao atualizar a venda.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}

		private void deletarReceber(FinReceber receber) {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				emf = Conexao.getInstancia(receber.getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();
				servico.deletar(receber);
			} catch (Exception ex) {
				UtilServer.LOG.error("Erro ao deletar o receber.", ex);
			} finally {
				em.close();
				emf.close();
			}
		}
	}
}
