package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioTransportadora;
import br.com.opensig.empresa.client.visao.lista.ListagemTransportadora;

public class ComandoTransportadora extends ComandoFuncao {

    
    public void execute(Map contexto) {
        DADOS = (SisFuncao) contexto.get("dados");
        FORM = new FormularioTransportadora(DADOS);
        LISTA = new ListagemTransportadora(FORM);
        super.execute(contexto);
    }
}
