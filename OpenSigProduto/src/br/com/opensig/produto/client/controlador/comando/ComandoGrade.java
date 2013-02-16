package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.FormularioVazio;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.grafico.GraficoGrade;
import br.com.opensig.produto.client.visao.lista.ListagemGrade;
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;

public class ComandoGrade extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioVazio<ProdEstoqueGrade>(new ProdEstoqueGrade(), DADOS);
		LISTA = new ListagemGrade(FORM);
		GRAFICO = new GraficoGrade(LISTA);
		super.execute(contexto);
	}
}
