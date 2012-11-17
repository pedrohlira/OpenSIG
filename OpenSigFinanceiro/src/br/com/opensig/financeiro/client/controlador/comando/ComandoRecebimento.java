package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanciado;
import br.com.opensig.financeiro.client.visao.form.FormularioRecebimento;
import br.com.opensig.financeiro.client.visao.grafico.GraficoRecebimento;
import br.com.opensig.financeiro.client.visao.lista.ListagemRecebimento;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class ComandoRecebimento extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioRecebimento(DADOS);
		LISTA = new ListagemRecebimento((AFormularioFinanciado<FinRecebimento>) FORM);
		GRAFICO = new GraficoRecebimento(LISTA);
		super.execute(contexto);
	}
}
