package br.com.opensig.fiscal.server.sped.contribuicao.blocoA;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosA001 extends Bean {

	private int ind_mov;

	public DadosA001() {
		reg = "A001";
	}

	public int getInd_mov() {
		return ind_mov;
	}

	public void setInd_mov(int ind_mov) {
		this.ind_mov = ind_mov;
	}

}
