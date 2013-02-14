package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioOrigem;
import br.com.opensig.produto.client.visao.lista.ListagemOrigem;

public class ComandoOrigem extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioOrigem(DADOS);
		LISTA = new ListagemOrigem(FORM);
		super.execute(contexto);
	}
}
