package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioPis;
import br.com.opensig.produto.client.visao.lista.ListagemPis;

public class ComandoPis extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioPis(DADOS);
		LISTA = new ListagemPis(FORM);
		super.execute(contexto);
	}
}
