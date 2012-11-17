package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.grafico.GraficoCompraProduto;
import br.com.opensig.comercial.client.visao.lista.ListagemCompraProduto;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoCompraProduto extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ComCompraProduto>(new ComCompraProduto(), DADOS);
		LISTA = new ListagemCompraProduto(FORM);
		GRAFICO = new GraficoCompraProduto(LISTA);
		super.execute(contexto);
	}
}
