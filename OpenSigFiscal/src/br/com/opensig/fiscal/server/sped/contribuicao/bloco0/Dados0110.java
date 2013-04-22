package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0110 extends Bean {

	private int cod_inc_trib;
	private Integer ind_apro_cred;
	private int cod_tipo_cont;
	private int ind_reg_cum;

	public Dados0110() {
		reg = "0110";
	}

	public int getCod_inc_trib() {
		return cod_inc_trib;
	}

	public void setCod_inc_trib(int cod_inc_trib) {
		this.cod_inc_trib = cod_inc_trib;
	}

	public Integer getInd_apro_cred() {
		return ind_apro_cred;
	}

	public void setInd_apro_cred(Integer ind_apro_cred) {
		this.ind_apro_cred = ind_apro_cred;
	}

	public int getCod_tipo_cont() {
		return cod_tipo_cont;
	}

	public void setCod_tipo_cont(int cod_tipo_cont) {
		this.cod_tipo_cont = cod_tipo_cont;
	}

	public int getInd_reg_cum() {
		return ind_reg_cum;
	}

	public void setInd_reg_cum(int ind_reg_cum) {
		this.ind_reg_cum = ind_reg_cum;
	}

}
