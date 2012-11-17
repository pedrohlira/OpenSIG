package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.comercial.shared.modelo.ComEcfNotaProduto;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC370 extends ARegistro<DadosC370, ComEcfNotaProduto> {

	@Override
	protected DadosC370 getDados(ComEcfNotaProduto dados) throws Exception {
		DadosC370 d = new DadosC370();
		d.setNum_item(dados.getComEcfNotaProdutoOrdem());
		d.setCod_item(dados.getProdProduto().getProdProdutoId() + "");
		d.setQtd(dados.getComEcfNotaProdutoQuantidade());
		d.setUnid(dados.getProdEmbalagem().getProdEmbalagemNome());
		d.setVl_item(dados.getComEcfNotaProdutoBruto());
		d.setVl_desc(dados.getComEcfNotaProdutoDesconto());
		return d;
	}

}
