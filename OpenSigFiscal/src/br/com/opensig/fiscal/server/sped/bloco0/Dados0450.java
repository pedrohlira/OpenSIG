package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0450 extends Bean {

	private String cod_inf;
	private String txt;

	public Dados0450() {
		reg = "0450";
	}
	
	public String getCod_inf() {
		return cod_inf;
	}

	public void setCod_inf(String cod_inf) {
		this.cod_inf = cod_inf;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

}
