package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.FormularioBanco;
import br.com.opensig.financeiro.client.visao.lista.ListagemBanco;

public class ComandoBanco extends ComandoFuncao {

	
	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioBanco(DADOS);
		LISTA = new ListagemBanco(FORM);
		super.execute(contexto);
	}
}
