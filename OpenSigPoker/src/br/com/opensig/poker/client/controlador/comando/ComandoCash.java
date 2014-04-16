package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.lista.ListagemCash;
import br.com.opensig.poker.shared.modelo.PokerCash;

public class ComandoCash extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio(new PokerCash(), DADOS);
		LISTA = new ListagemCash(FORM);
		super.execute(contexto);
	}
}
