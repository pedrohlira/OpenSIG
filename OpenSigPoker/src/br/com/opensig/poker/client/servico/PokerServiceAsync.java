package br.com.opensig.poker.client.servico;

import br.com.opensig.core.client.servico.CoreServiceAsync;
import br.com.opensig.poker.shared.modelo.PokerCash;
import br.com.opensig.poker.shared.modelo.PokerParticipante;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface que define o jogo no sistema de forma assincrona.
 * 
 * @author Pedro H. Lira
 */
public interface PokerServiceAsync extends CoreServiceAsync {
	
	public abstract void salvarTorneio(PokerTorneio torneio, AsyncCallback<PokerTorneio> asyncCallback);
	
	public abstract void salvarParticipante(PokerParticipante participante, AsyncCallback<PokerParticipante> asyncCallback);
	
	public abstract void fecharTorneio(PokerTorneio torneio, AsyncCallback asyncCallback);
	
	public abstract void fecharCash(PokerCash cash, AsyncCallback asyncCallback);
}
