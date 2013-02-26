package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosF990 extends Bean {

	private int qtd_lin;

	public DadosF990() {
		reg = "F990";
	}

	public int getQtd_lin() {
		return qtd_lin;
	}

	public void setQtd_lin(int qtd_lin) {
		this.qtd_lin = qtd_lin;
	}
}
