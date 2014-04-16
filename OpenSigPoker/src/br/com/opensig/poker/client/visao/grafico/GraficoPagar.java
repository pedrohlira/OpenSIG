package br.com.opensig.poker.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerPagar;

public class GraficoPagar extends AGrafico<PokerPagar> {

	public GraficoPagar(IListagem<PokerPagar> lista) {
		super(new PokerPagar(), lista);
		inicializar();
	}

}
