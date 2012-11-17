package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoEcfVendaProduto extends AGrafico<ComEcfVendaProduto> {

	public GraficoEcfVendaProduto(IListagem<ComEcfVendaProduto> lista) {
		super(new ComEcfVendaProduto(), lista);
		inicializar();
	}
}
