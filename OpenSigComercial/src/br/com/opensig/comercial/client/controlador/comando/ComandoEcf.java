package br.com.opensig.comercial.client.controlador.comando;

import java.util.Map;

import br.com.opensig.comercial.client.visao.form.FormularioEcf;
import br.com.opensig.comercial.client.visao.lista.ListagemEcf;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;

public class ComandoEcf extends ComandoFuncao {
	
	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioEcf(DADOS);
		LISTA = new ListagemEcf(FORM);
		super.execute(contexto);
	}
}
