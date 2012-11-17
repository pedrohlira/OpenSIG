package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioCompra;
import br.com.opensig.comercial.client.visao.grafico.GraficoCompra;
import br.com.opensig.comercial.client.visao.lista.ListagemCompra;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoCompra extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioCompra(DADOS);
		LISTA = new ListagemCompra(FORM);
		GRAFICO = new GraficoCompra(LISTA);
		super.execute(contexto);
	}
}
