package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioProduto;
import br.com.opensig.produto.client.visao.grafico.GraficoProduto;
import br.com.opensig.produto.client.visao.lista.ListagemProduto;

public class ComandoProduto extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioProduto(DADOS);
        LISTA = new ListagemProduto(FORM);
        GRAFICO = new GraficoProduto(LISTA);
        super.execute(contexto);
    }
}
