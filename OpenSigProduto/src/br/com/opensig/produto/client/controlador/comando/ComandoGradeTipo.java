package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioGradeTipo;
import br.com.opensig.produto.client.visao.lista.ListagemGradeTipo;

public class ComandoGradeTipo extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioGradeTipo(DADOS);
		LISTA = new ListagemGradeTipo(FORM);
		super.execute(contexto);
	}
}
