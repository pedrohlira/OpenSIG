package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoCompra extends AGrafico<ComCompra> {

	public GraficoCompra(IListagem<ComCompra> lista) {
		super(new ComCompra(), lista);
		inicializar();
	}
}
