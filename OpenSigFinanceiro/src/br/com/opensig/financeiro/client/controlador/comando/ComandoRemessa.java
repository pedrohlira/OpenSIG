package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.FormularioRemessa;
import br.com.opensig.financeiro.client.visao.lista.ListagemRemessa;

public class ComandoRemessa extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioRemessa(DADOS);
		LISTA = new ListagemRemessa(FORM);
		super.execute(contexto);
	}
}
