package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanceiro;
import br.com.opensig.financeiro.client.visao.form.FormularioPagar;
import br.com.opensig.financeiro.client.visao.grafico.GraficoPagar;
import br.com.opensig.financeiro.client.visao.lista.ListagemPagar;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

public class ComandoPagar extends ComandoFuncao {

	
	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioPagar(DADOS);
		LISTA = new ListagemPagar((AFormularioFinanceiro<FinPagar, FinPagamento>) FORM);
		GRAFICO = new GraficoPagar(LISTA);
		super.execute(contexto);
	}
}
