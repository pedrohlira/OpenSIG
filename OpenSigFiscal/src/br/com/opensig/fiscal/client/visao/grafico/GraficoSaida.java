package br.com.opensig.fiscal.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

public class GraficoSaida extends AGrafico<FisNotaSaida> {

	public GraficoSaida(IListagem<FisNotaSaida> lista) {
		super(new FisNotaSaida(), lista);
		inicializar();
	}
}
