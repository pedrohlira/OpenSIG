package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioNatureza;
import br.com.opensig.comercial.client.visao.lista.ListagemNatureza;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoNatureza extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioNatureza(DADOS);
		LISTA = new ListagemNatureza(FORM);
		super.execute(contexto);
	}
}
