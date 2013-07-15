package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.grafico.GraficoTrocaProduto;
import br.com.opensig.comercial.client.visao.lista.ListagemTrocaProduto;
import br.com.opensig.comercial.shared.modelo.ComTrocaProduto;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoTrocaProduto extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ComTrocaProduto>(new ComTrocaProduto(), DADOS);
		LISTA = new ListagemTrocaProduto(FORM);
		GRAFICO = new GraficoTrocaProduto(LISTA);
		super.execute(contexto);
	}
}
