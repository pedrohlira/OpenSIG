package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoConsumo extends AGrafico<ComConsumo> {

	public GraficoConsumo(IListagem<ComConsumo> lista) {
		super(new ComConsumo(), lista);
		inicializar();
	}
}
