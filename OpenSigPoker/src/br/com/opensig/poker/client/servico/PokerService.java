package br.com.opensig.poker.client.servico;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.poker.shared.modelo.PokerCash;
import br.com.opensig.poker.shared.modelo.PokerParticipante;
import br.com.opensig.poker.shared.modelo.PokerTorneio;

/**
 * Interface que define o jogo no sistema.
 * 
 * @author Pedro H. Lira
 */
public interface PokerService extends CoreService {

	public PokerTorneio salvarTorneio(PokerTorneio torneio) throws PokerException;
	
	public PokerParticipante salvarParticipante(PokerParticipante participante) throws PokerException;
	
	public void fecharTorneio(PokerTorneio torneio) throws PokerException;
	
	public void fecharCash(PokerCash cash) throws PokerException;
}
