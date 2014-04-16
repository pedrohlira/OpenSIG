package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioJackpot;
import br.com.opensig.poker.client.visao.grafico.GraficoJackpot;
import br.com.opensig.poker.client.visao.lista.ListagemJackpot;

public class ComandoJackpot extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioJackpot(DADOS);
		LISTA = new ListagemJackpot(FORM);
		GRAFICO = new GraficoJackpot(LISTA);
		super.execute(contexto);
	}
}
