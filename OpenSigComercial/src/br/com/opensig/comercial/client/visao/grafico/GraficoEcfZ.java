package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoEcfZ extends AGrafico<ComEcfZ> {

	public GraficoEcfZ(IListagem<ComEcfZ> lista) {
		super(new ComEcfZ(), lista);
		inicializar();
	}
}
