package br.com.opensig.poker.client.servico;

import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.poker.shared.modelo.PokerCash;
import br.com.opensig.poker.shared.modelo.PokerParticipante;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Classe que é utilizada para acionar as acoes do jogo.
 * 
 * @author Pedro H. Lira
 */
public class PokerProxy extends CoreProxy implements PokerServiceAsync {

	private static final PokerServiceAsync async = (PokerServiceAsync) GWT.create(PokerService.class);
	private static final ServiceDefTarget sdf = (ServiceDefTarget) async;

	/**
	 * Construtor padrão
	 */
	public PokerProxy() {
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "PokerService");
	}

	@Override
	public void salvarTorneio(PokerTorneio torneio, AsyncCallback<PokerTorneio> asyncCallback) {
		async.salvarTorneio(torneio, asyncCallback);
	}

	@Override
	public void salvarParticipante(PokerParticipante participante, AsyncCallback<PokerParticipante> asyncCallback) {
		async.salvarParticipante(participante, asyncCallback);
	}

	@Override
	public void fecharTorneio(PokerTorneio torneio, AsyncCallback asyncCallback) {
		async.fecharTorneio(torneio, asyncCallback);
	}

	@Override
	public void fecharCash(PokerCash cash, AsyncCallback asyncCallback) {
		async.fecharCash(cash, asyncCallback);
	}
}
