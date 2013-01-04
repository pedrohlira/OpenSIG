package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioConsumo;
import br.com.opensig.comercial.client.visao.grafico.GraficoConsumo;
import br.com.opensig.comercial.client.visao.lista.ListagemConsumo;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoConsumo extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioConsumo(DADOS);
		LISTA = new ListagemConsumo(FORM);
		GRAFICO = new GraficoConsumo(LISTA);
		super.execute(contexto);
	}
}
