package br.com.opensig.financeiro.client.controlador.comando.boleto;

import java.util.Map;

public class ComandoGerarPdf extends ComandoGerar {

	public ComandoGerarPdf() {
		super("pdf", false);
	}

	public void execute(Map contexto) {
		super.execute(contexto);
		getGerar().execute(contexto);
	}
}
