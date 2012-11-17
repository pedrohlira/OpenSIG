package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.FormularioRetorno;
import br.com.opensig.financeiro.client.visao.lista.ListagemRetorno;

public class ComandoRetorno extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioRetorno(DADOS);
		LISTA = new ListagemRetorno(FORM);
		super.execute(contexto);
	}
}
