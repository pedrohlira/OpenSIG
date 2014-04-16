package br.com.opensig.poker.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerJogador;

public class GraficoJogador extends AGrafico<PokerJogador> {

	public GraficoJogador(IListagem<PokerJogador> lista) {
		super(new PokerJogador(), lista);
		inicializar();
	}

}
