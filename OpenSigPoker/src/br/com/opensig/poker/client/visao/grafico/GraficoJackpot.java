package br.com.opensig.poker.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerJackpot;

public class GraficoJackpot extends AGrafico<PokerJackpot> {

	public GraficoJackpot(IListagem<PokerJackpot> lista) {
		super(new PokerJackpot(), lista);
		inicializar();
	}

}
