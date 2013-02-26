package br.com.opensig.fiscal.server.sped.contribuicao.blocoP;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosP990 extends Bean {

	private int qtd_lin;

	public DadosP990() {
		reg = "P990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
