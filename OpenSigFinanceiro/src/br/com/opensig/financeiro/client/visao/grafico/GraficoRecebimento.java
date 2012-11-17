package br.com.opensig.financeiro.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class GraficoRecebimento extends AGrafico<FinRecebimento> {

	public GraficoRecebimento(IListagem<FinRecebimento> lista) {
		super(new FinRecebimento(), lista);
		inicializar();
	}
}
