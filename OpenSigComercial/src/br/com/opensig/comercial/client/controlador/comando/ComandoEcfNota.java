package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.grafico.GraficoEcfNota;
import br.com.opensig.comercial.client.visao.lista.ListagemEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoEcfNota extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ComEcfNota>(new ComEcfNota(), DADOS);
		LISTA = new ListagemEcfNota(FORM);
		GRAFICO = new GraficoEcfNota(LISTA);
		super.execute(contexto);
	}
}
