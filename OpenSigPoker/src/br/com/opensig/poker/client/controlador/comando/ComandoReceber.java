package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioReceber;
import br.com.opensig.poker.client.visao.grafico.GraficoReceber;
import br.com.opensig.poker.client.visao.lista.ListagemReceber;

public class ComandoReceber extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioReceber(DADOS);
		LISTA = new ListagemReceber(FORM);
		GRAFICO = new GraficoReceber(LISTA);
		super.execute(contexto);
	}
}
