package br.com.opensig.fiscal.server.sped.blocoD;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosD990 extends Bean {

	private int qtd_lin;

	public DadosD990() {
		reg = "D990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
