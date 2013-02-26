package br.com.opensig.fiscal.server.sped.contribuicao.blocoA;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosA990 extends Bean {

	private int qtd_lin;

	public DadosA990() {
		reg = "A990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
