package br.com.opensig.permissao.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.permissao.client.visao.form.FormularioConfiguracao;
import br.com.opensig.permissao.client.visao.lista.ListagemConfiguracao;

public class ComandoConfiguracao extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioConfiguracao(DADOS);
		LISTA = new ListagemConfiguracao(FORM);
		super.execute(contexto);
	}
}
