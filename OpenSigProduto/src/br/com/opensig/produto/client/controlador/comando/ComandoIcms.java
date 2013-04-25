package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioIcms;
import br.com.opensig.produto.client.visao.lista.ListagemIcms;

public class ComandoIcms extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioIcms(DADOS);
        LISTA = new ListagemIcms(FORM);
        super.execute(contexto);
    }
}
