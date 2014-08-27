package br.com.opensig.financeiro.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.client.visao.form.FormularioCartaoPresente;
import br.com.opensig.financeiro.client.visao.lista.ListagemCartaoPresente;

public class ComandoCartaoPresente extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioCartaoPresente(DADOS);
		LISTA = new ListagemCartaoPresente(FORM);
		super.execute(contexto);
	}
}
