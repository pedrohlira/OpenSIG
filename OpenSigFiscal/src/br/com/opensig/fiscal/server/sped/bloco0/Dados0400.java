package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0400 extends Bean {

	private String cod_nat;
	private String descr_nat;

	public Dados0400() {
		reg = "0400";
	}
	
	public String getCod_nat() {
		return cod_nat;
	}

	public String getDescr_nat() {
		return descr_nat;
	}

	public void setCod_nat(String cod_nat) {
		this.cod_nat = cod_nat;
	}

	public void setDescr_nat(String descr_nat) {
		this.descr_nat = descr_nat;
	}

}
