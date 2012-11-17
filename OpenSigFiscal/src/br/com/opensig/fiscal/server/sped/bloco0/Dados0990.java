package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0990 extends Bean {

	private int qtd_lin;

	public Dados0990() {
		reg = "0990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
