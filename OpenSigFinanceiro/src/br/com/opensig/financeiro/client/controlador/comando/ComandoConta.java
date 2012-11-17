package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.FormularioConta;
import br.com.opensig.financeiro.client.visao.lista.ListagemConta;

public class ComandoConta extends ComandoFuncao {

	
	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioConta(DADOS);
		LISTA = new ListagemConta(FORM);
		super.execute(contexto);
	}
}
