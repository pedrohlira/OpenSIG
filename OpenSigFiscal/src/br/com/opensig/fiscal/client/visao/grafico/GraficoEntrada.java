package br.com.opensig.fiscal.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;

public class GraficoEntrada extends AGrafico<FisNotaEntrada> {

	public GraficoEntrada(IListagem<FisNotaEntrada> lista) {
		super(new FisNotaEntrada(), lista);
		inicializar();
	}
}
