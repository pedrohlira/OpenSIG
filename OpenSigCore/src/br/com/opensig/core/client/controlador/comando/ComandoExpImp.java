package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.visao.form.FormularioExpImp;
import br.com.opensig.core.client.visao.lista.ListagemExpImp;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoExpImp extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioExpImp(DADOS);
		LISTA = new ListagemExpImp(FORM);
		super.execute(contexto);
	}
}
