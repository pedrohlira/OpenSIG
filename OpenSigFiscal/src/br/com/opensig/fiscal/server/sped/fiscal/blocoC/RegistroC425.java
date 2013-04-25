package br.com.opensig.fiscal.server.sped.fiscal.blocoC;

import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC425 extends ARegistro<DadosC425, ComEcfVendaProduto> {

	@Override
	protected DadosC425 getDados(ComEcfVendaProduto dados) throws Exception {
		DadosC425 d = new DadosC425();
		d.setCod_item(dados.getProdProduto().getProdProdutoId() + "");
		d.setQtd(dados.getComEcfVendaProdutoQuantidade());
		d.setUnid(dados.getProdEmbalagem().getProdEmbalagemNome());
		d.setVl_item(dados.getComEcfVendaProdutoTotal());
		d.setVl_pis(dados.getComEcfVendaProdutoTotal() * dados.getProdProduto().getProdPis().getProdPisAliquota() / 100);
		d.setVl_cofins(dados.getComEcfVendaProdutoTotal() * dados.getProdProduto().getProdCofins().getProdCofinsAliquota() / 100);
		return d;
	}
}
