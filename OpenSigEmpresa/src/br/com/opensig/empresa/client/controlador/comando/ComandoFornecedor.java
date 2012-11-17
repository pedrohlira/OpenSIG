package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioFornecedor;
import br.com.opensig.empresa.client.visao.lista.ListagemFornecedor;

public class ComandoFornecedor extends ComandoFuncao {

    
    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioFornecedor(DADOS);
        LISTA = new ListagemFornecedor(FORM);
        super.execute(contexto);
    }
}
