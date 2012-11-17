package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioContatoTipo;
import br.com.opensig.empresa.client.visao.lista.ListagemContatoTipo;

public class ComandoContatoTipo extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioContatoTipo(DADOS);
        LISTA = new ListagemContatoTipo(FORM);
        super.execute(contexto);
    }
}
