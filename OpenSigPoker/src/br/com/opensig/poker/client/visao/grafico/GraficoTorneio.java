package br.com.opensig.poker.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

public class GraficoTorneio extends AGrafico<PokerTorneio> {

	public GraficoTorneio(IListagem<PokerTorneio> lista) {
		super(new PokerTorneio(), lista);
		inicializar();
	}

}
