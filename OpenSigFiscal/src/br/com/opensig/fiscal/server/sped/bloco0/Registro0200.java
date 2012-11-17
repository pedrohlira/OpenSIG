package br.com.opensig.fiscal.server.sped.bloco0;

import java.util.ArrayList;
import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfNotaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.server.UtilServer;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class Registro0200 extends ARegistro<Dados0200, ProdProduto> {

	private List<Integer> produtos;

	@Override
	public void executar() {
		produtos = new ArrayList<Integer>();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			// compras
			for (ComCompra compra : compras) {
				for (ComCompraProduto cProd : compra.getComCompraProdutos()) {
					if (!produtos.contains(cProd.getProdProduto().getProdProdutoId())) {
						out.write(getDados(cProd.getProdProduto()));
						out.flush();
						produtos.add(cProd.getProdProduto().getProdProdutoId());
					}
				}
			}
			// vendas
			for (ComVenda venda : vendas) {
				if (!venda.getComVendaCancelada() && !venda.getComVendaNfe()) {
					for (ComVendaProduto vProd : venda.getComVendaProdutos()) {
						if (vProd.getProdProduto().getProdComposicoes().size() == 0 && !produtos.contains(vProd.getProdProduto().getProdProdutoId())) {
							out.write(getDados(vProd.getProdProduto()));
							out.flush();
							produtos.add(vProd.getProdProduto().getProdProdutoId());
						}
					}
				}
			}
			// zs
			for (ComEcfZ z : zs) {
				for (ComEcfVenda venda : z.getComEcfVendas()) {
					if (venda.getComEcfVendaFechada() && !venda.getComEcfVendaCancelada() && venda.getEmpCliente() != null) {
						for (ComEcfVendaProduto eProd : venda.getComEcfVendaProdutos()) {
							if (!eProd.getComEcfVendaProdutoCancelado() && !produtos.contains(eProd.getProdProduto().getProdProdutoId())) {
								out.write(getDados(eProd.getProdProduto()));
								out.flush();
								produtos.add(eProd.getProdProduto().getProdProdutoId());
							}
						}
					}
				}
			}
			// notas
			for (ComEcfNota nota : notas) {
				if (!nota.getComEcfNotaCancelada()) {
					for (ComEcfNotaProduto eProd : nota.getComEcfNotaProdutos()) {
						if (!produtos.contains(eProd.getProdProduto().getProdProdutoId())) {
							out.write(getDados(eProd.getProdProduto()));
							out.flush();
							produtos.add(eProd.getProdProduto().getProdProdutoId());
						}
					}
				}
			}
			// estoque
			if (estoque != null) {
				for (ProdProduto prod : estoque) {
					if (prod.getProdComposicoes() == null && !produtos.contains(prod.getProdProdutoId())) {
						out.write(getDados(prod));
						out.flush();
						produtos.add(prod.getProdProdutoId());
					}
				}
			}
		} catch (Exception e) {
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected Dados0200 getDados(ProdProduto prod) {
		Dados0200 d = new Dados0200();
		d.setCod_item(prod.getProdProdutoId() + "");
		d.setDescr_item(prod.getProdProdutoDescricao());
		d.setCod_barra(prod.getProdProdutoBarra());
		d.setCod_ant_item("");
		d.setUnid_inv(prod.getProdEmbalagem().getProdEmbalagemNome());
		d.setTipo_item(Integer.valueOf(prod.getProdTipo().getProdTipoValor()));
		if (prod.getProdProdutoNcm().length() == 8) {
			d.setCod_ncm(prod.getProdProdutoNcm());
			d.setCod_gen(prod.getProdProdutoNcm().substring(0, 2));
		} else {
			d.setCod_gen(prod.getProdProdutoNcm());
		}
		d.setEx_ipi("");
		d.setAliq_icms(prod.getProdTributacao().getProdTributacaoDentro());

		normalizar(d);
		qtdLinhas++;
		return d;
	}

}
