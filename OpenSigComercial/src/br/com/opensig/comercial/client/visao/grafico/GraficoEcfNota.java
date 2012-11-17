package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoEcfNota extends AGrafico<ComEcfNota> {

	public GraficoEcfNota(IListagem<ComEcfNota> lista) {
		super(new ComEcfNota(), lista);
		inicializar();
	}
}
