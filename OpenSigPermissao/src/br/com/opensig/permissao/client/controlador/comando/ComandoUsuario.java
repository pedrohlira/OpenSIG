package br.com.opensig.permissao.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.permissao.client.visao.form.FormularioUsuario;
import br.com.opensig.permissao.client.visao.lista.ListagemUsuario;

public class ComandoUsuario extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioUsuario(DADOS);
		LISTA = new ListagemUsuario(FORM);
		super.execute(contexto);
	}
}
