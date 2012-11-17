package br.com.opensig.comercial.server.acao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
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
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdEstoque;

public class ExcluirEcfVenda extends Chain {

	private CoreServiceImpl servico;
	private ComEcfVenda venda;
	private Autenticacao auth;

	public ExcluirEcfVenda(Chain next, CoreServiceImpl servico, ComEcfVenda venda, Autenticacao auth) throws OpenSigException {
		super(null);
		this.servico = servico;
		this.venda = venda;
		this.auth = auth;

		// atualiza venda
		DeletarVenda delVen = new DeletarVenda(next);
		// valida o estoque
		AtualizarEstoque atuEst = new AtualizarEstoque(delVen);
		this.next = atuEst;
	}

	@Override
	public void execute() throws OpenSigException {
		// seta a venda
		FiltroNumero fn = new FiltroNumero("comEcfVendaId", ECompara.IGUAL, venda.getId());
		venda = (ComEcfVenda) servico.selecionar(venda, fn, false);

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
				FiltroObjeto fo1 = new FiltroObjeto("empEmpresa", ECompara.IGUAL, venda.getComEcf().getEmpEmpresa());
				emf = Conexao.getInstancia(new ProdEstoque().getPu());
				em = emf.createEntityManager();
				em.getTransaction().begin();

				if (auth.getConf().get("estoque.ativo").equalsIgnoreCase("sim") && venda.getComEcfVendaFechada() && !venda.getComEcfVendaCancelada()) {
					for (ComEcfVendaProduto comVen : venda.getComEcfVendaProdutos()) {
						if (comVen.getProdProduto() != null && !comVen.getComEcfVendaProdutoCancelado()) {
							// fatorando a quantida no estoque
							double qtd = comVen.getComEcfVendaProdutoQuantidade();
							if (comVen.getProdEmbalagem().getProdEmbalagemId() != comVen.getProdProduto().getProdEmbalagem().getProdEmbalagemId()) {
								qtd *= getQtdEmbalagem(comVen.getProdEmbalagem().getProdEmbalagemId());
								qtd /= getQtdEmbalagem(comVen.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
							}
							// formando os parametros
							ParametroFormula pn1 = new ParametroFormula("prodEstoqueQuantidade", qtd);
							// formando o filtro
							FiltroObjeto fo2 = new FiltroObjeto("prodProduto", ECompara.IGUAL, comVen.getProdProduto());
							GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { fo1, fo2 });
							// busca o item
							ProdEstoque est = new ProdEstoque();
							// formando o sql
							Sql sql = new Sql(est, EComando.ATUALIZAR, gf, pn1);
							servico.executar(em, sql);
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

	private class DeletarVenda extends Chain {

		public DeletarVenda(Chain next) throws OpenSigException {
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
				servico.deletar(em, venda);

				if (next != null) {
					next.execute();
				}
				em.getTransaction().commit();
			} catch (Exception ex) {
				if (em != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}

				UtilServer.LOG.error("Erro ao excluir a venda.", ex);
				throw new ComercialException(ex.getMessage());
			} finally {
				em.close();
				emf.close();
			}
		}
	}
}
