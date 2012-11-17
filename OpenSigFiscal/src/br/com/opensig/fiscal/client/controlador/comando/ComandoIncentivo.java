package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.fiscal.client.visao.form.FormularioIncentivo;
import br.com.opensig.fiscal.client.visao.lista.ListagemIncentivo;

public class ComandoIncentivo extends ComandoFuncao {

	public void execute(Map contexto) {
		DADOS = (SisFuncao) contexto.get("dados");
		FORM = new FormularioIncentivo(DADOS);
		LISTA = new ListagemIncentivo(FORM);
		super.execute(contexto);
	}
}
