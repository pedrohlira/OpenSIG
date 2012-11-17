package br.com.opensig.fiscal.server.sped;

public abstract class Bean {

	protected String padrao;
	protected String reg;
	
	public Bean() {
	}
	
	public String getPadrao() {
		return padrao;
	}

	public void setPadrao(String padrao) {
		this.padrao = padrao;
	}
	
	public String getReg() {
		return reg;
	}
	
	public void setReg(String reg) {
		this.reg = reg;
	}
}
