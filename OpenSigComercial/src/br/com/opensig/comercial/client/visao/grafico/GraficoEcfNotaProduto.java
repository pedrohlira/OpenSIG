package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComEcfNotaProduto;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoEcfNotaProduto extends AGrafico<ComEcfNotaProduto> {

	public GraficoEcfNotaProduto(IListagem<ComEcfNotaProduto> lista) {
		super(new ComEcfNotaProduto(), lista);
		inicializar();
	}
}
