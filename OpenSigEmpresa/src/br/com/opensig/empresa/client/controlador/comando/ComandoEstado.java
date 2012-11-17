package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioEstado;
import br.com.opensig.empresa.client.visao.lista.ListagemEstado;

public class ComandoEstado extends ComandoFuncao {
	
	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioEstado(DADOS);
		LISTA = new ListagemEstado(FORM);
		super.execute(contexto);
	}
}
