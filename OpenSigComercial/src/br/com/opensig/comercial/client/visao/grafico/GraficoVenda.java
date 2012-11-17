package br.com.opensig.comercial.client.visao.grafico;

import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;

public class GraficoVenda extends AGrafico<ComVenda> {

	public GraficoVenda(IListagem<ComVenda> lista) {
		super(new ComVenda(), lista);
		inicializar();
	}
}
