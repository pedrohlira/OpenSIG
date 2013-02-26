package br.com.opensig.fiscal.server.sped.contribuicao.blocoP;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosP001 extends Bean {

	private int ind_mov;

	public DadosP001() {
		reg = "P001";
	}

	public int getInd_mov() {
		return ind_mov;
	}

	public void setInd_mov(int ind_mov) {
		this.ind_mov = ind_mov;
	}

}
