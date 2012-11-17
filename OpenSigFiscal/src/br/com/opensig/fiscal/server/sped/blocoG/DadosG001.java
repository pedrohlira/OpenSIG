package br.com.opensig.fiscal.server.sped.blocoG;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosG001 extends Bean {

	private int ind_mov;

	public DadosG001() {
		reg = "G001";
	}

	public int getInd_mov() {
		return ind_mov;
	}

	public void setInd_mov(int ind_mov) {
		this.ind_mov = ind_mov;
	}

}
