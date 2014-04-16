package br.com.opensig.poker.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerReceber;

public class GraficoReceber extends AGrafico<PokerReceber> {

	public GraficoReceber(IListagem<PokerReceber> lista) {
		super(new PokerReceber(), lista);
		inicializar();
	}

}
