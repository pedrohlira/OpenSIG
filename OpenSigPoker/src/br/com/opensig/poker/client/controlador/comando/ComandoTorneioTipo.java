package br.com.opensig.poker.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.visao.form.FormularioTorneioTipo;
import br.com.opensig.poker.client.visao.lista.ListagemTorneioTipo;

public class ComandoTorneioTipo extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioTorneioTipo(DADOS);
		LISTA = new ListagemTorneioTipo(FORM);
		super.execute(contexto);
	}
}
