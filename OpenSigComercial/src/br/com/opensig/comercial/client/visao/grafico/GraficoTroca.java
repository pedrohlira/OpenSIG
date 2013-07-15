package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComTroca;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoTroca extends AGrafico<ComTroca> {

	public GraficoTroca(IListagem<ComTroca> lista) {
		super(new ComTroca(), lista);
		inicializar();
	}
}
