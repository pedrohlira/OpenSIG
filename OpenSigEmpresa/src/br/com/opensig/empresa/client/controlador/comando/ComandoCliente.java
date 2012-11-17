package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioCliente;
import br.com.opensig.empresa.client.visao.lista.ListagemCliente;

public class ComandoCliente extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioCliente(DADOS);
        LISTA = new ListagemCliente(FORM);
        super.execute(contexto);
    }
}
