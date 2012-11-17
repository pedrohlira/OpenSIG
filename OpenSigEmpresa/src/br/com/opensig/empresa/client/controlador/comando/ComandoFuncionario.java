package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioFuncionario;
import br.com.opensig.empresa.client.visao.lista.ListagemFuncionario;

public class ComandoFuncionario extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioFuncionario(DADOS);
        LISTA = new ListagemFuncionario(FORM);
        super.execute(contexto);
    }
}
