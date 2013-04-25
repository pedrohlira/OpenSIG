package br.com.opensig.fiscal.server.sped.fiscal.blocoC;

import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.fiscal.server.sped.ARegistro;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class RegistroC470 extends ARegistro<DadosC470, ComEcfVendaProduto> {

	@Override
	protected DadosC470 getDados(ComEcfVendaProduto dados) throws Exception {
		ProdProduto produto = dados.getProdProduto();
		DadosC470 d = new DadosC470();
		d.setCod_item(produto.getProdProdutoId() + "");
		d.setQtd(dados.getComEcfVendaProdutoQuantidade());
		d.setUnid(dados.getProdEmbalagem().getProdEmbalagemNome());
		d.setCfop(produto.getProdIcms().getProdIcmsCfop());
		d.setVl_item(dados.getComEcfVendaProdutoTotal());
		String cstCson = auth.getConf().get("nfe.crt").equals("1") ? produto.getProdIcms().getProdIcmsCson() : (produto.getProdOrigem().getProdOrigemId() - 1)
				+ produto.getProdIcms().getProdIcmsCst();
		d.setCst_icms(cstCson);
		d.setAliq_icms(produto.getProdIcms().getProdIcmsDentro());
		return d;
	}
}
