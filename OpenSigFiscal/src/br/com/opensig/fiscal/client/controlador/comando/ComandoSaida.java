package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.fiscal.client.visao.grafico.GraficoSaida;
import br.com.opensig.fiscal.client.visao.lista.ListagemSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

public class ComandoSaida extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<FisNotaSaida>(new FisNotaSaida(), DADOS);
		LISTA = new ListagemSaida(FORM);
		GRAFICO = new GraficoSaida(LISTA);
		super.execute(contexto);
	}
}
