package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoCompraProduto extends AGrafico<ComCompraProduto> {

	public GraficoCompraProduto(IListagem<ComCompraProduto> lista) {
		super(new ComCompraProduto(), lista);
		inicializar();
	}
}
