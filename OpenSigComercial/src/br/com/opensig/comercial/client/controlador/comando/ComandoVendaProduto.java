package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.grafico.GraficoVendaProduto;
import br.com.opensig.comercial.client.visao.lista.ListagemVendaProduto;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoVendaProduto extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ComVendaProduto>(new ComVendaProduto(), DADOS);
		LISTA = new ListagemVendaProduto(FORM);
		GRAFICO = new GraficoVendaProduto(LISTA);
		super.execute(contexto);
	}
}
