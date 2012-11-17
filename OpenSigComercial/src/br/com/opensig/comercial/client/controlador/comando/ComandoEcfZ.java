package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioEcfZ;
import br.com.opensig.comercial.client.visao.grafico.GraficoEcfZ;
import br.com.opensig.comercial.client.visao.lista.ListagemEcfZ;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoEcfZ extends ComandoFuncao{

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioEcfZ(DADOS);
		LISTA = new ListagemEcfZ(FORM);
		GRAFICO = new GraficoEcfZ(LISTA);
		super.execute(contexto);
	}
}
