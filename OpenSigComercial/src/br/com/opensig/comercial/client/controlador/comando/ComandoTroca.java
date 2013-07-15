package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioTroca;
import br.com.opensig.comercial.client.visao.grafico.GraficoTroca;
import br.com.opensig.comercial.client.visao.lista.ListagemTroca;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoTroca extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioTroca(DADOS);
		LISTA = new ListagemTroca(FORM);
		GRAFICO = new GraficoTroca(LISTA);
		super.execute(contexto);
	}
}
