package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioForma;
import br.com.opensig.poker.client.visao.lista.ListagemForma;

public class ComandoForma extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioForma(DADOS);
		LISTA = new ListagemForma(FORM);
		super.execute(contexto);
	}
}
