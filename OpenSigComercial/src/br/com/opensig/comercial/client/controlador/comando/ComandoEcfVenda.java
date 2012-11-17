package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioEcfVenda;
import br.com.opensig.comercial.client.visao.grafico.GraficoEcfVenda;
import br.com.opensig.comercial.client.visao.lista.ListagemEcfVenda;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoEcfVenda extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioEcfVenda(DADOS);
		LISTA = new ListagemEcfVenda(FORM);
		GRAFICO = new GraficoEcfVenda(LISTA);
		super.execute(contexto);
	}
}
