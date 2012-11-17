package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.fiscal.client.visao.grafico.GraficoEntrada;
import br.com.opensig.fiscal.client.visao.lista.ListagemEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;

public class ComandoEntrada extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<FisNotaEntrada>(new FisNotaEntrada(), DADOS);
		LISTA = new ListagemEntrada(FORM);
		GRAFICO = new GraficoEntrada(LISTA);
		super.execute(contexto);
	}
}
