package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC400 extends Bean {

	private String cod_mod;
	private String ecf_mod;
	private String ecf_fab;
	private int ecf_cx;

	public DadosC400() {
		reg = "C400";
	}

	public String getCod_mod() {
		return cod_mod;
	}

	public void setCod_mod(String cod_mod) {
		this.cod_mod = cod_mod;
	}

	public String getEcf_mod() {
		return ecf_mod;
	}

	public void setEcf_mod(String ecf_mod) {
		this.ecf_mod = ecf_mod;
	}

	public String getEcf_fab() {
		return ecf_fab;
	}

	public void setEcf_fab(String ecf_fab) {
		this.ecf_fab = ecf_fab;
	}

	public int getEcf_cx() {
		return ecf_cx;
	}

	public void setEcf_cx(int ecf_cx) {
		this.ecf_cx = ecf_cx;
	}

}
