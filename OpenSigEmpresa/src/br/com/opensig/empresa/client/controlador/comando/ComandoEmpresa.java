package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioEmpresa;
import br.com.opensig.empresa.client.visao.lista.ListagemEmpresa;


public class ComandoEmpresa extends ComandoFuncao {

    public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioEmpresa(DADOS);
        LISTA =  new ListagemEmpresa(FORM);
        super.execute(contexto);
    }
}
