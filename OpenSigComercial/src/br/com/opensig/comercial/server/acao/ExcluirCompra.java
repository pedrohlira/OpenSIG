package br.com.opensig.comercial.server.acao;

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
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.Conexao;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdEstoque;

public class ExcluirCompra extends Chain {

	private CoreServiceImpl servico;
	private ComCompra compra;
	private Autenticacao auth;

	public ExcluirCompra(Chain next, CoreServiceImpl servico, ComCompra compra, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.compra = compra;
		this.auth = auth;

		// deletar compra
		DeletarCompra delComp = new DeletarCompra(next);
		// deletar nota
		DeletarNota delNota = new DeletarNota(delComp);
		// atualiza os conta
		AtualizarConta atuConta = new AtualizarConta(delNota);
		// atauliza estoque
		AtualizarEstoque atuEst = new AtualizarEstoque(atuConta);
		// seleciona os produtos
		this.next = atuEst;
	}

	public void execute() throws OpenSigException {
		FiltroNumero fn = new FiltroNumero("comCompraId", ECompara.IGUAL, compra.getId());
		compra = (ComCompra) servico.selecionar(compra, fn, false);
		if (next != null) {
			next.execute();
		}
	}

	private class AtualizarEstoque extends Chain {

		private List<ProdEmbalagem> embalagens;
		
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

				if (compra.getComCompraFechada()) {
					for (ComCompraProduto comProd : compra.getComCompraProdutos()) {
						// fatorando a quantida no estoque
						double qtd = comProd.getComCompraProdutoQuantidade();
						if(comProd.getProdEmbalagem().getProdEmbalagemId() != comProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId()){
							qtd *= getQtdEmbalagem(comProd.getProdEmbalagem().getProdEmbalagemId());
							qtd /= getQtdEmbalagem(comProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
						}
						// formando os parametros
						ParametroFormula pn1 = new ParametroFormula("prodEstoqueQuantidade", -1 * qtd);
						// formando o filtro
						FiltroObjeto fo2 = new FiltroObjeto("prodProduto", ECompara.IGUAL, comProd.getProdProduto());
						GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo1, fo2 });
						// busca o item
						ProdEstoque est = new ProdEstoque();
						// formando o sql
						Sql sql = new Sql(est, EComando.ATUALIZAR, gf, pn1);
						servico.executar(em, sql);
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

				UtilServer.LOG.error("Erro ao excluir a compra.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
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
	}

	private class AtualizarConta extends Chain {

		public AtualizarConta(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				FinConta conta = new FinConta();
				emf = Conexao.getInstancia(conta.getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				if (compra.getFinPagar() != null) {
					conta = compra.getFinPagar().getFinConta();
					double valPag = 0.00;
					for (FinPagamento pag : compra.getFinPagar().getFinPagamentos()) {
						if (!pag.getFinPagamentoStatus().equalsIgnoreCase(auth.getConf().get("txtAberto"))) {
							valPag += pag.getFinPagamentoValor();
						}
					}

					if (valPag > 0) {
						conta.setFinContaSaldo(conta.getFinContaSaldo() + valPag);
						servico.salvar(em, conta);
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

				UtilServer.LOG.error("Erro ao atualizar a conta.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	}

	private class DeletarNota extends Chain {

		public DeletarNota(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				emf = Conexao.getInstancia(new FisNotaEntrada().getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();
				
				if (compra.getFisNotaEntrada() != null) {
					servico.deletar(em, compra.getFisNotaEntrada());
				}

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao excluir a nota.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	}

	private class DeletarCompra extends Chain {

		public DeletarCompra(Chain next) throws OpenSigException {
			super(next);
		}

		@Override
		public void execute() throws OpenSigException {
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				// recupera uma instância do gerenciador de entidades
				emf = Conexao.getInstancia(compra.getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();
				servico.deletar(em, compra);

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao excluir a compra.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	}

}
