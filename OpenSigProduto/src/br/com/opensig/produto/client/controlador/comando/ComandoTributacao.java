package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioTributacao;
import br.com.opensig.produto.client.visao.lista.ListagemTributacao;

public class ComandoTributacao extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioTributacao(DADOS);
        LISTA = new ListagemTributacao(FORM);
        super.execute(contexto);
    }
}
