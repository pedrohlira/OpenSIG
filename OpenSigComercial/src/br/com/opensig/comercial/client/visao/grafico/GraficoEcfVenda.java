package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoEcfVenda extends AGrafico<ComEcfVenda> {

	public GraficoEcfVenda(IListagem<ComEcfVenda> lista) {
		super(new ComEcfVenda(), lista);
		inicializar();
	}
}
