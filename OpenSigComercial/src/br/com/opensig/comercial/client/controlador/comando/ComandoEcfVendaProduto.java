package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.grafico.GraficoEcfVendaProduto;
import br.com.opensig.comercial.client.visao.lista.ListagemEcfVendaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoEcfVendaProduto extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ComEcfVendaProduto>(new ComEcfVendaProduto(), DADOS);
		LISTA = new ListagemEcfVendaProduto(FORM);
		GRAFICO = new GraficoEcfVendaProduto(LISTA);
		super.execute(contexto);
	}
}
