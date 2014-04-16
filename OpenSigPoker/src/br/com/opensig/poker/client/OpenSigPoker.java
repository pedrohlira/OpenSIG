package br.com.opensig.poker.client;

import java.util.ArrayList;
import java.util.Collection;

import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoNovoDuplicar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.poker.client.controlador.comando.ComandoAddOn;
import br.com.opensig.poker.client.controlador.comando.ComandoCash;
import br.com.opensig.poker.client.controlador.comando.ComandoCashFechar;
import br.com.opensig.poker.client.controlador.comando.ComandoCliente;
import br.com.opensig.poker.client.controlador.comando.ComandoForma;
import br.com.opensig.poker.client.controlador.comando.ComandoJackpot;
import br.com.opensig.poker.client.controlador.comando.ComandoJackpotPlay;
import br.com.opensig.poker.client.controlador.comando.ComandoJogador;
import br.com.opensig.poker.client.controlador.comando.ComandoPagar;
import br.com.opensig.poker.client.controlador.comando.ComandoParticipante;
import br.com.opensig.poker.client.controlador.comando.ComandoReBuy;
import br.com.opensig.poker.client.controlador.comando.ComandoTorneioPlay;
import br.com.opensig.poker.client.controlador.comando.ComandoReceber;
import br.com.opensig.poker.client.controlador.comando.ComandoTorneio;
import br.com.opensig.poker.client.controlador.comando.ComandoTorneioFechar;
import br.com.opensig.poker.client.controlador.comando.ComandoTorneioTipo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Classe que inicializa o módulo sigpoker.
 * 
 * @author Pedro H. Lira
 */
public class OpenSigPoker implements EntryPoint {

	/**
	 * Metodo que é disparado ao iniciar o projeto que contém este módulo. Usar
	 * este método para adicionar as classes de comando na fábrica de comandos.
	 */
	public void onModuleLoad() {
		FabricaComando fc = FabricaComando.getInstancia();
		// funcoes do torneio
		fc.addComando(ComandoTorneio.class.getName(), (IComando) GWT.create(ComandoTorneio.class));
		fc.addComando(ComandoParticipante.class.getName(), (IComando) GWT.create(ComandoParticipante.class));
		fc.addComando(ComandoReBuy.class.getName(), (IComando) GWT.create(ComandoReBuy.class));
		fc.addComando(ComandoAddOn.class.getName(), (IComando) GWT.create(ComandoAddOn.class));
		fc.addComando(ComandoCliente.class.getName(), (IComando) GWT.create(ComandoCliente.class));
		fc.addComando(ComandoTorneioTipo.class.getName(), (IComando) GWT.create(ComandoTorneioTipo.class));
		fc.addComando(ComandoTorneioFechar.class.getName(), (IComando) GWT.create(ComandoTorneioFechar.class));
		fc.addComando(ComandoTorneioPlay.class.getName(), (IComando) GWT.create(ComandoTorneioPlay.class));
		
		// funcoes do cash
		fc.addComando(ComandoCash.class.getName(), (IComando) GWT.create(ComandoCash.class));
		fc.addComando(ComandoForma.class.getName(), (IComando) GWT.create(ComandoForma.class));
		fc.addComando(ComandoJackpot.class.getName(), (IComando) GWT.create(ComandoJackpot.class));
		fc.addComando(ComandoJogador.class.getName(), (IComando) GWT.create(ComandoJogador.class));
		fc.addComando(ComandoPagar.class.getName(), (IComando) GWT.create(ComandoPagar.class));
		fc.addComando(ComandoReceber.class.getName(), (IComando) GWT.create(ComandoReceber.class));
		fc.addComando(ComandoCashFechar.class.getName(), (IComando) GWT.create(ComandoCashFechar.class));
		fc.addComando(ComandoJackpotPlay.class.getName(), (IComando) GWT.create(ComandoJackpotPlay.class));

		// ações que não devem ter na função de cash
		Collection<Class> acoes = new ArrayList<Class>();
		acoes.add(ComandoNovoDuplicar.class);
		acoes.add(ComandoEditar.class);
		Ponte.setAcoesProibidas(ComandoCash.class.getName(), acoes);
		Ponte.setAcoesProibidas(ComandoJackpot.class.getName(), acoes);
		
		// nao pode excluir os jogadores
		Collection<Class> acoes2 = new ArrayList<Class>();
		acoes2.add(ComandoExcluir.class);
		Ponte.setAcoesProibidas(ComandoJogador.class.getName(), acoes2);
	}
}
