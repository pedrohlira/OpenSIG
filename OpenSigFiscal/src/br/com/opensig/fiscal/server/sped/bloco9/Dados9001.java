package br.com.opensig.fiscal.server.sped.bloco9;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados9001 extends Bean {

	private int ind_mov;

	public Dados9001() {
		reg = "9001";
	}

	public int getInd_mov() {
		return ind_mov;
	}

	public void setInd_mov(int ind_mov) {
		this.ind_mov = ind_mov;
	}

}
