package br.com.opensig.produto.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

public class GraficoProduto extends AGrafico<ProdProduto> {

	public GraficoProduto(IListagem<ProdProduto> lista) {
		super(new ProdProduto(), lista);
		inicializar();
	}

}
