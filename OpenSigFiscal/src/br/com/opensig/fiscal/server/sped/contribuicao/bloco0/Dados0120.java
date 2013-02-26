package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0120 extends Bean {

	private String mes_dispensa;
	private String inf_comp;

	public Dados0120() {
		reg = "0120";
	}

	public String getMes_dispensa() {
		return mes_dispensa;
	}

	public void setMes_dispensa(String mes_dispensa) {
		this.mes_dispensa = mes_dispensa;
	}

	public String getInf_comp() {
		return inf_comp;
	}

	public void setInf_comp(String inf_comp) {
		this.inf_comp = inf_comp;
	}

}
