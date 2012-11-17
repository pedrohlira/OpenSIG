package br.com.opensig.empresa.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.visao.form.FormularioMunicipio;
import br.com.opensig.empresa.client.visao.lista.ListagemMunicipio;

public class ComandoMunicipio extends ComandoFuncao {

	
	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioMunicipio(DADOS);
		LISTA = new ListagemMunicipio(FORM);
		super.execute(contexto);
	}
}
