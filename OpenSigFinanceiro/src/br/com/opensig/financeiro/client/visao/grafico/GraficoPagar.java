package br.com.opensig.financeiro.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

public class GraficoPagar extends AGrafico<FinPagar> {

	public GraficoPagar(IListagem<FinPagar> lista) {
		super(new FinPagar(), lista);
		inicializar();
	}
}
