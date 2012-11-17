package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioPais;
import br.com.opensig.empresa.client.visao.lista.ListagemPais;

public class ComandoPais extends ComandoFuncao {

	
	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioPais(DADOS);
		LISTA = new ListagemPais(FORM);
		super.execute(contexto);
	}
}
