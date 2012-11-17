package br.com.opensig.fiscal.server.sped.blocoE;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosE990 extends Bean {

	private int qtd_lin;

	public DadosE990() {
		reg = "E990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
