package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoFrete extends AGrafico<ComFrete> {

	public GraficoFrete(IListagem<ComFrete> lista) {
		super(new ComFrete(), lista);
		inicializar();
	}
}
