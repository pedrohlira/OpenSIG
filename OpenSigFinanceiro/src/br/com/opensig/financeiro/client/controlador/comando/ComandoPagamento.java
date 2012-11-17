package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.AFormularioFinanciado;
import br.com.opensig.financeiro.client.visao.form.FormularioPagamento;
import br.com.opensig.financeiro.client.visao.grafico.GraficoPagamento;
import br.com.opensig.financeiro.client.visao.lista.ListagemPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;

public class ComandoPagamento extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioPagamento(DADOS);
		LISTA = new ListagemPagamento((AFormularioFinanciado<FinPagamento>) FORM);
		GRAFICO = new GraficoPagamento(LISTA);
		super.execute(contexto);
	}
}
