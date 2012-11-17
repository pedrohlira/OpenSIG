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
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class Registro0220 extends ARegistro<Dados0220, ProdEmbalagem> {

	private List<Integer> embalagens;

	@Override
	public void executar() {
		embalagens = new ArrayList<Integer>();

		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getClass().getResourceAsStream(bean));
			BeanWriter out = factory.createWriter("EFD", escritor);
			// compras
			for (ComCompra compra : compras) {
				for (ComCompraProduto cProd : compra.getComCompraProdutos()) {
					if (!embalagens.contains(cProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId())) {
						out.write(getDados(cProd.getProdProduto().getProdEmbalagem()));
						out.flush();
						embalagens.add(cProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
					}
				}
			}
			// vendas
			for (ComVenda venda : vendas) {
				if (!venda.getComVendaCancelada() && !venda.getComVendaNfe()) {
					for (ComVendaProduto vProd : venda.getComVendaProdutos()) {
						if (vProd.getProdProduto().getProdComposicoes().size() == 0 && !embalagens.contains(vProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId())) {
							out.write(getDados(vProd.getProdProduto().getProdEmbalagem()));
							out.flush();
							embalagens.add(vProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
						}
					}
				}
			}
			// zs
			for (ComEcfZ z : zs) {
				for (ComEcfVenda venda : z.getComEcfVendas()) {
					if (venda.getComEcfVendaFechada() && !venda.getComEcfVendaCancelada() && venda.getEmpCliente() != null) {
						for (ComEcfVendaProduto eProd : venda.getComEcfVendaProdutos()) {
							if (!eProd.getComEcfVendaProdutoCancelado() && !embalagens.contains(eProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId())) {
								out.write(getDados(eProd.getProdProduto().getProdEmbalagem()));
								out.flush();
								embalagens.add(eProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
							}
						}
					}
				}
			}
			// notas
			for (ComEcfNota nota : notas) {
				if (!nota.getComEcfNotaCancelada()) {
					for (ComEcfNotaProduto eProd : nota.getComEcfNotaProdutos()) {
						if (!embalagens.contains(eProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId())) {
							out.write(getDados(eProd.getProdProduto().getProdEmbalagem()));
							out.flush();
							embalagens.add(eProd.getProdProduto().getProdEmbalagem().getProdEmbalagemId());
						}
					}
				}
			}
			// estoque
			if (estoque != null) {
				for (ProdProduto prod : estoque) {
					if (prod.getProdComposicoes() == null && !embalagens.contains(prod.getProdEmbalagem().getProdEmbalagemId())) {
						out.write(getDados(prod.getProdEmbalagem()));
						out.flush();
						embalagens.add(prod.getProdEmbalagem().getProdEmbalagemId());
					}
				}
			}
		} catch (Exception e) {
			UtilServer.LOG.error("Erro na geracao do Registro -> " + bean, e);
		}
	}

	@Override
	protected Dados0220 getDados(ProdEmbalagem emb) {
		Dados0220 d = new Dados0220();
		d.setUnid_conv(emb.getProdEmbalagemNome());
		d.setFat_conv(emb.getProdEmbalagemUnidade());

		normalizar(d);
		qtdLinhas++;
		return d;
	}
}
