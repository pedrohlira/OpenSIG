package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.grafico.GraficoEcfNotaProduto;
import br.com.opensig.comercial.client.visao.lista.ListagemEcfNotaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfNotaProduto;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoEcfNotaProduto extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ComEcfNotaProduto>(new ComEcfNotaProduto(), DADOS);
		LISTA = new ListagemEcfNotaProduto(FORM);
		GRAFICO = new GraficoEcfNotaProduto(LISTA);
		super.execute(contexto);
	}
}
