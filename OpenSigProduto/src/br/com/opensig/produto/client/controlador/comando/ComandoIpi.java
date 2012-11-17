package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioIpi;
import br.com.opensig.produto.client.visao.lista.ListagemIpi;

public class ComandoIpi extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioIpi(DADOS);
		LISTA = new ListagemIpi(FORM);
		super.execute(contexto);
	}
}
