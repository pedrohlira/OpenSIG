package br.com.opensig.fiscal.server.sped.blocoG;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosG990 extends Bean {

	private int qtd_lin;

	public DadosG990() {
		reg = "G990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
