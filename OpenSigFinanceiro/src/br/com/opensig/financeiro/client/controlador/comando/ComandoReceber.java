package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanceiro;
import br.com.opensig.financeiro.client.visao.form.FormularioReceber;
import br.com.opensig.financeiro.client.visao.grafico.GraficoReceber;
import br.com.opensig.financeiro.client.visao.lista.ListagemReceber;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class ComandoReceber extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioReceber(DADOS);
		LISTA = new ListagemReceber((AFormularioFinanceiro<FinReceber, FinRecebimento>) FORM);
		GRAFICO = new GraficoReceber(LISTA);
		super.execute(contexto);
	}
}
