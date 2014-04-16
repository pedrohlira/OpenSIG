package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioTorneio;
import br.com.opensig.poker.client.visao.grafico.GraficoTorneio;
import br.com.opensig.poker.client.visao.lista.ListagemTorneio;

public class ComandoTorneio extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioTorneio(DADOS);
		LISTA = new ListagemTorneio(FORM);
		GRAFICO = new GraficoTorneio(LISTA);
		super.execute(contexto);
	}
}
