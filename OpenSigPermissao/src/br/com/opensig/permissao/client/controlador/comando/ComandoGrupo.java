package br.com.opensig.permissao.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.permissao.client.visao.form.FormularioGrupo;
import br.com.opensig.permissao.client.visao.lista.ListagemGrupo;

public class ComandoGrupo extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioGrupo(DADOS);
		LISTA = new ListagemGrupo(FORM);
		super.execute(contexto);
	}
}
