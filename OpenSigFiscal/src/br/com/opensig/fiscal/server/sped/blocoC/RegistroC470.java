package br.com.opensig.fiscal.server.sped.blocoC;

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
		d.setCfop(produto.getProdTributacao().getProdTributacaoCfop());
		d.setVl_item(dados.getComEcfVendaProdutoTotal());
		String cstCson = auth.getConf().get("nfe.crt").equals("1") ? produto.getProdTributacao().getProdTributacaoCson() : (produto.getProdOrigem().getProdOrigemId() - 1)
				+ produto.getProdTributacao().getProdTributacaoCst();
		d.setCst_icms(cstCson);
		d.setAliq_icms(produto.getProdTributacao().getProdTributacaoDentro());
		return d;
	}
}
