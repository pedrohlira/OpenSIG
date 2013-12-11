package br.com.opensig.comercial.server.acao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.opensig.comercial.client.servico.ComercialException;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.padroes.Chain;
import br.com.opensig.core.client.servico.OpenSigException;
import br.com.opensig.core.server.CoreServiceImpl;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class GerarVenda extends Chain {

	private CoreServiceImpl servico;
	private IFiltro filtro;
	private ComVenda venda;
	private Autenticacao auth;

	public GerarVenda(Chain next, CoreServiceImpl servico, IFiltro filtro, Autenticacao auth) throws OpenSigException {
		super(next);
		this.servico = servico;
		this.filtro = filtro;
		this.venda = new ComVenda();
		this.auth = auth;
	}

	@Override
	public void execute() throws OpenSigException {
		try {
			// seleciona a compra pelo filtro
			ComCompra compra = (ComCompra) servico.selecionar(new ComCompra(), filtro, false);
			List<ComVendaProduto> produtos = new ArrayList<ComVendaProduto>();

			// objetos
			FiltroNumero fn = new FiltroNumero("sisUsuarioId", ECompara.IGUAL, auth.getUsuario()[0]);
			SisUsuario usuario = (SisUsuario) servico.selecionar(new SisUsuario(), fn, false);

			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, compra.getEmpEmpresa());
			Lista<ComNatureza> natureza = servico.selecionar(new ComNatureza(), 0, 1, fo, false);

			// variaveis
			double bruto = 0.00;
			double liquido = 0.00;
			int ordem = 1;

			// percorre os produtos
			for (ComCompraProduto cp : compra.getComCompraProdutos()) {
				ProdProduto pp = cp.getProdProduto();
				// cria os produtos da compra
				ComVendaProduto vp = new ComVendaProduto();
				vp.setComVenda(null);
				vp.setProdProduto(pp);
				vp.setProdEmbalagem(cp.getProdEmbalagem());
				vp.setComVendaProdutoBarra(cp.getComCompraProdutoBarra());
				vp.setComVendaProdutoQuantidade(cp.getComCompraProdutoQuantidade());
				vp.setComVendaProdutoBruto(cp.getComCompraProdutoPreco());
				vp.setComVendaProdutoLiquido(cp.getComCompraProdutoValor());
				double desconto = (cp.getComCompraProdutoPreco() - cp.getComCompraProdutoValor());
				desconto = desconto * 100 / cp.getComCompraProdutoPreco();
				vp.setComVendaProdutoDesconto(desconto);
				vp.setComVendaProdutoTotalBruto(cp.getComCompraProdutoQuantidade() * cp.getComCompraProdutoPreco());
				vp.setComVendaProdutoTotalLiquido(cp.getComCompraProdutoQuantidade() * cp.getComCompraProdutoValor());
				vp.setComVendaProdutoCfop(cp.getComCompraProdutoCfop() + 4000);
				vp.setComVendaProdutoIcmsCst(auth.getConf().get("nfe.crt").equals("1") ? pp.getProdIcms().getProdIcmsCson() : pp.getProdIcms().getProdIcmsCst());
				vp.setComVendaProdutoIcms(cp.getComCompraProdutoIcms());
				vp.setComVendaProdutoIpiCst(cp.getComCompraProdutoIcmsCst());
				vp.setComVendaProdutoIpi(cp.getComCompraProdutoIpi());
				vp.setComVendaProdutoPisCst(cp.getComCompraProdutoPisCst());
				vp.setComVendaProdutoPis(cp.getComCompraProdutoPis());
				vp.setComVendaProdutoCofinsCst(cp.getComCompraProdutoCofinsCst());
				vp.setComVendaProdutoCofins(cp.getComCompraProdutoCofins());
				vp.setComVendaProdutoOrdem(ordem++);
				produtos.add(vp);
				// totais
				bruto += vp.getComVendaProdutoTotalBruto();
				liquido += vp.getComVendaProdutoTotalLiquido();
			}

			// cria o objeto da venda
			venda.setSisUsuario(usuario);
			venda.setSisVendedor(usuario);
			venda.setEmpCliente(new EmpCliente(Integer.valueOf(auth.getConf().get("cliente.padrao"))));
			venda.setEmpEmpresa(compra.getEmpEmpresa());
			venda.setComNatureza(natureza.getLista().get(0));
			venda.setFinReceber(null);
			venda.setFisNotaSaida(null);
			venda.setComVendaProdutos(produtos);
			venda.setComVendaData(new Date());
			venda.setComVendaValorBruto(bruto);
			venda.setComVendaValorLiquido(liquido);
			venda.setComVendaFechada(false);
			venda.setComVendaRecebida(false);
			venda.setComVendaNfe(false);
			venda.setComVendaCancelada(false);
			venda.setComVendaObservacao("");

			// salva a compra
			SalvarVenda salvar = new SalvarVenda(next, servico, venda);
			salvar.execute();
		} catch (Exception ex) {
			UtilServer.LOG.error("Erro na geracao da venda.", ex);
			throw new ComercialException(ex.getMessage());
		}

	}
}
