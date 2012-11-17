package br.com.opensig.financeiro.client.controlador.comando.boleto;

import java.util.Map;

public class ComandoGerarHtml extends ComandoGerar {

	public ComandoGerarHtml() {
		super();
	}

	public void execute(Map contexto) {
		super.execute(contexto);
		getGerar().execute(contexto);
	}

}
