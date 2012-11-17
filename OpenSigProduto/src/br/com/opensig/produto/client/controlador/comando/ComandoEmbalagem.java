package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioEmbalagem;
import br.com.opensig.produto.client.visao.lista.ListagemEmbalagem;

public class ComandoEmbalagem extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioEmbalagem(DADOS);
		LISTA = new ListagemEmbalagem(FORM);
		super.execute(contexto);
	}
}
