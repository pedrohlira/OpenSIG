package br.com.opensig.fiscal.server.sped.bloco9;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados9999 extends Bean {

	private int qtd_lin;

	public Dados9999() {
		reg = "9999";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
