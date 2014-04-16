package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioPagar;
import br.com.opensig.poker.client.visao.grafico.GraficoPagar;
import br.com.opensig.poker.client.visao.lista.ListagemPagar;

public class ComandoPagar extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioPagar(DADOS);
		LISTA = new ListagemPagar(FORM);
		GRAFICO = new GraficoPagar(LISTA);
		super.execute(contexto);
	}
}
