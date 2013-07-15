package br.com.opensig.comercial.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.client.servico.ComercialService;
import br.com.opensig.comercial.server.acao.CancelarEcfVenda;
import br.com.opensig.comercial.server.acao.CancelarVenda;
import br.com.opensig.comercial.server.acao.ExcluirCompra;
import br.com.opensig.comercial.server.acao.ExcluirConsumo;
import br.com.opensig.comercial.server.acao.ExcluirFrete;
import br.com.opensig.comercial.server.acao.ExcluirVenda;
import br.com.opensig.comercial.server.acao.FecharCompra;
import br.com.opensig.comercial.server.acao.FecharConsumo;
import br.com.opensig.comercial.server.acao.FecharEcfVenda;
import br.com.opensig.comercial.server.acao.FecharFrete;
import br.com.opensig.comercial.server.acao.FecharVenda;
import br.com.opensig.comercial.server.acao.GerarCompra;
import br.com.opensig.comercial.server.acao.GerarNfeEntrada;
import br.com.opensig.comercial.server.acao.GerarNfeSaida;
import br.com.opensig.comercial.server.acao.SalvarCompra;
import br.com.opensig.comercial.server.acao.SalvarEcfVenda;
import br.com.opensig.comercial.server.acao.SalvarEcfZ;
import br.com.opensig.comercial.server.acao.SalvarTroca;
import br.com.opensig.comercial.server.acao.SalvarValor;
import br.com.opensig.comercial.server.acao.SalvarVenda;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComTroca;
import br.com.opensig.comercial.shared.modelo.ComValorProduto;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.financeiro.server.acao.SalvarPagar;
import br.com.opensig.financeiro.server.acao.SalvarReceber;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.fiscal.server.acao.SalvarEntrada;
import br.com.opensig.fiscal.server.acao.SalvarSaida;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;

public class ComercialServiceImpl extends CoreServiceImpl implements ComercialService {

	private List<ProdEmbalagem> embalagens;

	public ComercialServiceImpl() {
	}

	public ComercialServiceImpl(Autenticacao auth) {
		super(auth);
	}

	public int getQtdEmbalagem(int embalagemId) throws Exception {
		int unid = 1;
		if (embalagens == null) {
			embalagens = selecionar(new ProdEmbalagem(), 0, 0, null, false).getLista();
		}

		for (ProdEmbalagem emb : embalagens) {
			if (emb.getProdEmbalagemId() == embalagemId) {
				unid = emb.getProdEmbalagemUnidade();
				break;
			}
		}
		return unid;
	}

	@Override
	public FisNotaSaida gerarNfe(ComVenda venda, ComFrete frete, String nfe, int[] cupom) throws ComercialException {
		try {
			GerarNfeSaida gerar = new GerarNfeSaida(null, this, venda, frete, nfe, cupom, getAuth());
			gerar.execute();
			return gerar.getNota();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando gerarNfeSaida.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public FisNotaEntrada gerarNfe(ComCompra compra, ComFrete frete, String nfe) throws ComercialException {
		try {
			GerarNfeEntrada gerar = new GerarNfeEntrada(null, this, compra, frete, nfe, getAuth());
			gerar.execute();
			return gerar.getNota();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando gerarNfeEntrada.", e);
			throw new ComercialException(e.getMessage());
		}
	}
	
	@Override
	public void gerarCompra(IFiltro filtro) throws ComercialException {
		try {
			GerarCompra gerar = new GerarCompra(null, this, filtro, getAuth());
			gerar.execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando gerarCompra.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void fecharCompra(ComCompra compra) throws ComercialException {
		try {
			new FecharCompra(null, this, compra, getAuth()).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando fecharCompra.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public String[][] fecharVenda(ComVenda venda) throws ComercialException {
		try {
			List<String[]> invalidos = new ArrayList<String[]>();
			new FecharVenda(null, this, venda, invalidos, getAuth()).execute();
			return invalidos.toArray(new String[][] {});
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando fecharVenda.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void fecharFrete(ComFrete frete) throws ComercialException {
		try {
			new FecharFrete(null, this, frete).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando fecharFrete.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void fecharConsumo(ComConsumo consumo) throws ComercialException {
		try {
			new FecharConsumo(null, this, consumo).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando fecharConsumo.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public ComCompra salvarCompra(ComCompra compra) throws ComercialException {
		FinPagar finPagar = null;
		FisNotaEntrada fisNota = null;

		try {
			// verifica se tem pagar
			if (compra.getComCompraPaga()) {
				SalvarPagar pagar = new SalvarPagar(null, this, compra.getFinPagar(), new ArrayList<FinCategoria>());
				pagar.execute();
				finPagar = pagar.getPagar();
			}
			// verifica se tem nota
			if (compra.getComCompraNfe()) {
				SalvarEntrada entrada = new SalvarEntrada(null, compra.getFisNotaEntrada().getFisNotaEntradaXml(), new FisNotaStatus(ENotaStatus.AUTORIZADO), getAuth());
				entrada.execute();
				fisNota = entrada.getNota();
			}
			// salva a compra
			compra.setFinPagar(finPagar);
			compra.setFisNotaEntrada(fisNota);
			new SalvarCompra(null, this, compra).execute();
			// verifica se fecha a compra
			if (compra.getComCompraFechada()) {
				new FecharCompra(null, this, new ComCompra(compra.getComCompraId()), getAuth()).execute();
			}

			compra.anularDependencia();
			return compra;
		} catch (Exception e) {
			try {
				if (finPagar != null) {
					deletar(finPagar);
				}
				if (fisNota != null) {
					deletar(fisNota);
				}
			} catch (Exception ex) {
			}

			UtilServer.LOG.error("Erro no comando salvarCompra.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public ComVenda salvarVenda(ComVenda venda) throws ComercialException {
		FinReceber finReceber = null;
		FisNotaSaida fisNota = null;

		try {
			// verifica se tem a receber
			if (venda.getComVendaRecebida()) {
				SalvarReceber receber = new SalvarReceber(null, this, venda.getFinReceber(), new ArrayList<FinCategoria>());
				receber.execute();
				finReceber = receber.getReceber();
			}
			// verifica se tem nota
			if (venda.getComVendaNfe()) {
				SalvarSaida saida = new SalvarSaida(null, venda.getFisNotaSaida().getFisNotaSaidaXml(), new FisNotaStatus(ENotaStatus.AUTORIZADO), getAuth());
				saida.execute();
				fisNota = saida.getNota();
			}
			// salva a venda
			venda.setFinReceber(finReceber);
			venda.setFisNotaSaida(fisNota);
			new SalvarVenda(null, this, venda).execute();
			// verifica se fecha a venda
			if (venda.getComVendaFechada()) {
				new FecharVenda(null, this, new ComVenda(venda.getComVendaId()), new ArrayList<String[]>(), getAuth()).execute();
			}

			venda.anularDependencia();
			return venda;
		} catch (Exception e) {
			try {
				if (finReceber != null) {
					deletar(finReceber);
				}
				if (fisNota != null) {
					deletar(fisNota);
				}
			} catch (Exception ex) {
			}
			
			UtilServer.LOG.error("Erro no comando salvarVenda.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public ComTroca salvarTroca(ComTroca troca) throws ComercialException {
		try {
			// salva a compra
			new SalvarTroca(null, this, troca).execute();
			troca.anularDependencia();
			return troca;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando salvarTroca.", e);
			throw new ComercialException(e.getMessage());
		}
	}
	
	@Override
	public ComValorProduto salvarValor(ComValorProduto valor) throws ComercialException {
		try {
			new SalvarValor(null, this, valor).execute();
			valor.anularDependencia();
			return valor;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando salvarValor.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public ComEcfZ salvarEcfZ(ComEcfZ z) throws ComercialException {
		try {
			new SalvarEcfZ(null, this, z).execute();
			z.anularDependencia();
			return z;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando salvarEcfZ.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void excluirCompra(ComCompra compra) throws ComercialException {
		try {
			new ExcluirCompra(null, this, compra, getAuth()).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando excluirCompra.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	public void excluirVenda(ComVenda venda) throws ComercialException {
		try {
			new ExcluirVenda(null, this, venda).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando excluirVenda.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void cancelarVenda(ComVenda venda) throws ComercialException {
		try {
			new CancelarVenda(null, this, venda, getAuth()).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando cancelarVenda.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void excluirFrete(ComFrete frete) throws ComercialException {
		try {
			new ExcluirFrete(null, this, frete, getAuth()).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando excluirFrete.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void excluirConsumo(ComConsumo consumo) throws ComercialException {
		try {
			new ExcluirConsumo(null, this, consumo, getAuth()).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando excluirConsumo.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public Map<String, Integer> importarEcfVenda(List<String> nomesArquivos) throws ComercialException {
		return null;
	}

	@Override
	public ComEcfVenda salvarEcfVenda(ComEcfVenda venda) throws ComercialException {
		try {
			new SalvarEcfVenda(null, this, venda).execute();
			venda.anularDependencia();
			return venda;
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando salvarEcfVenda.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void fecharEcfVenda(ComEcfVenda venda) throws ComercialException {
		try {
			new FecharEcfVenda(null, this, venda, getAuth()).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando fecharEcfVenda.", e);
			throw new ComercialException(e.getMessage());
		}
	}

	@Override
	public void cancelarEcfVenda(ComEcfVenda venda) throws ComercialException {
		try {
			new CancelarEcfVenda(null, this, venda, getAuth()).execute();
		} catch (Exception e) {
			UtilServer.LOG.error("Erro no comando excluirEcfVenda.", e);
			throw new ComercialException(e.getMessage());
		}
	}
}
