package br.com.opensig.fiscal.server.sped.bloco1;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados1001 extends Bean {

	private int ind_mov;

	public Dados1001() {
		reg = "1001";
	}

	public int getInd_mov() {
		return ind_mov;
	}

	public void setInd_mov(int ind_mov) {
		this.ind_mov = ind_mov;
	}

}
