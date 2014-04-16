package br.com.opensig.poker.client.visao.grafico;

import br.com.opensig.core.client.visao.abstrato.AGrafico;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.poker.shared.modelo.PokerParticipante;

public class GraficoParticipante extends AGrafico<PokerParticipante> {

	public GraficoParticipante(IListagem<PokerParticipante> lista) {
		super(new PokerParticipante(), lista);
		inicializar();
	}

}
