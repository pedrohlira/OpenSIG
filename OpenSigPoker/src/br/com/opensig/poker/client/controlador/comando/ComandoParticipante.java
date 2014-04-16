package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioParticipante;
import br.com.opensig.poker.client.visao.grafico.GraficoParticipante;
import br.com.opensig.poker.client.visao.lista.ListagemParticipante;

public class ComandoParticipante extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioParticipante(DADOS);
		LISTA = new ListagemParticipante(FORM);
		GRAFICO = new GraficoParticipante(LISTA);
		super.execute(contexto);
	}
}
