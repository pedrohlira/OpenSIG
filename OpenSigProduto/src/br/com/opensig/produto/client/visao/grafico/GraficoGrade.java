package br.com.opensig.produto.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;

public class GraficoGrade extends AGrafico<ProdEstoqueGrade> {

	public GraficoGrade(IListagem<ProdEstoqueGrade> lista) {
		super(new ProdEstoqueGrade(), lista);
		inicializar();
	}
}
