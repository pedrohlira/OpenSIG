package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoVendaProduto extends AGrafico<ComVendaProduto> {

	public GraficoVendaProduto(IListagem<ComVendaProduto> lista) {
		super(new ComVendaProduto(), lista);
		inicializar();
	}
}
