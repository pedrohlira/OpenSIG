package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioFrete;
import br.com.opensig.comercial.client.visao.grafico.GraficoFrete;
import br.com.opensig.comercial.client.visao.lista.ListagemFrete;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;


public class ComandoFrete extends ComandoFuncao {

    public void execute(Map contexto) {
        DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioFrete(DADOS);
        LISTA = new ListagemFrete(FORM);
        GRAFICO = new GraficoFrete(LISTA);
        super.execute(contexto);
    }
}
