package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioTipo;
import br.com.opensig.produto.client.visao.lista.ListagemTipo;

public class ComandoTipo extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioTipo(DADOS);
		LISTA = new ListagemTipo(FORM);
		super.execute(contexto);
	}
}
