package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioJogador;
import br.com.opensig.poker.client.visao.grafico.GraficoJogador;
import br.com.opensig.poker.client.visao.lista.ListagemJogador;

public class ComandoJogador extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioJogador(DADOS);
		LISTA = new ListagemJogador(FORM);
		GRAFICO = new GraficoJogador(LISTA);
		super.execute(contexto);
	}
}
