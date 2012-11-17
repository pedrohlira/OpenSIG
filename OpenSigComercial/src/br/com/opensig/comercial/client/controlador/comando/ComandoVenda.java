package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioVenda;
import br.com.opensig.comercial.client.visao.grafico.GraficoVenda;
import br.com.opensig.comercial.client.visao.lista.ListagemVenda;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoVenda extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVenda(DADOS);
		LISTA = new ListagemVenda(FORM);
		GRAFICO = new GraficoVenda(LISTA);
		super.execute(contexto);
	}
}
