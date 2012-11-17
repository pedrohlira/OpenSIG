package br.com.opensig.fiscal.server.sped.bloco9;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados9900 extends Bean {

	private String reg_bcl;
	private int qtd_reg_bcl;
	
	public Dados9900() {
		reg = "9900";
	}

	public String getReg_bcl() {
		return reg_bcl;
	}

	public void setReg_bcl(String reg_bcl) {
		this.reg_bcl = reg_bcl;
	}

	public int getQtd_reg_bcl() {
		return qtd_reg_bcl;
	}

	public void setQtd_reg_bcl(int qtd_reg_bcl) {
		this.qtd_reg_bcl = qtd_reg_bcl;
	}

}
