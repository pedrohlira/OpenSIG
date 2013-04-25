package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioCofins;
import br.com.opensig.produto.client.visao.lista.ListagemCofins;

public class ComandoCofins extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioCofins(DADOS);
		LISTA = new ListagemCofins(FORM);
		super.execute(contexto);
	}
}
