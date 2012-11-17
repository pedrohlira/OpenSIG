package br.com.opensig.financeiro.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;

public class GraficoPagamento extends AGrafico<FinPagamento> {

	public GraficoPagamento(IListagem<FinPagamento> lista) {
		super(new FinPagamento(), lista);
		inicializar();
	}
}
