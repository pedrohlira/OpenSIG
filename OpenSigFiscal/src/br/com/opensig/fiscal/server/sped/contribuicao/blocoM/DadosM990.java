package br.com.opensig.fiscal.server.sped.contribuicao.blocoM;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosM990 extends Bean {

	private int qtd_lin;

	public DadosM990() {
		reg = "M990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
