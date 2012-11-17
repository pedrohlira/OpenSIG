package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioEnderecoTipo;
import br.com.opensig.empresa.client.visao.lista.ListagemEnderecoTipo;

public class ComandoEnderecoTipo extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioEnderecoTipo(DADOS);
        LISTA = new ListagemEnderecoTipo(FORM);
        super.execute(contexto);
    }
}
