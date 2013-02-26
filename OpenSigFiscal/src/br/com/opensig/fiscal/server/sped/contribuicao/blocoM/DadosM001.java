package br.com.opensig.fiscal.server.sped.contribuicao.blocoM;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosM001 extends Bean {

	private int ind_mov;

	public DadosM001() {
		reg = "M001";
	}

	public int getInd_mov() {
		return ind_mov;
	}

	public void setInd_mov(int ind_mov) {
		this.ind_mov = ind_mov;
	}

}
