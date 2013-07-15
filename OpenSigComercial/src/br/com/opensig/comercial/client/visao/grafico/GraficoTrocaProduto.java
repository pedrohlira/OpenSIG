package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComTrocaProduto;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoTrocaProduto extends AGrafico<ComTrocaProduto> {

	public GraficoTrocaProduto(IListagem<ComTrocaProduto> lista) {
		super(new ComTrocaProduto(), lista);
		inicializar();
	}
}
