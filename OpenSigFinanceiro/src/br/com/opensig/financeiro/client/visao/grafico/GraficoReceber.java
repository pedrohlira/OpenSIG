package br.com.opensig.financeiro.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.financeiro.shared.modelo.FinReceber;

public class GraficoReceber extends AGrafico<FinReceber> {

	public GraficoReceber(IListagem<FinReceber> lista) {
		super(new FinReceber(), lista);
		inicializar();
	}
}
