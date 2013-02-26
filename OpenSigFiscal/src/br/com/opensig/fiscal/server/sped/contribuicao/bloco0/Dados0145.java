package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0145 extends Bean {

	private int cod_inc_trib;
	private Double vl_rec_tot;
	private Double vl_rec_ativ;
	private Double vl_rec_demais_ativ;
	private String info_compl;

	public Dados0145() {
		reg = "0145";
	}

	public int getCod_inc_trib() {
		return cod_inc_trib;
	}

	public void setCod_inc_trib(int cod_inc_trib) {
		this.cod_inc_trib = cod_inc_trib;
	}

	public Double getVl_rec_tot() {
		return vl_rec_tot;
	}

	public void setVl_rec_tot(Double vl_rec_tot) {
		this.vl_rec_tot = vl_rec_tot;
	}

	public Double getVl_rec_ativ() {
		return vl_rec_ativ;
	}

	public void setVl_rec_ativ(Double vl_rec_ativ) {
		this.vl_rec_ativ = vl_rec_ativ;
	}

	public Double getVl_rec_demais_ativ() {
		return vl_rec_demais_ativ;
	}

	public void setVl_rec_demais_ativ(Double vl_rec_demais_ativ) {
		this.vl_rec_demais_ativ = vl_rec_demais_ativ;
	}

	public String getInfo_compl() {
		return info_compl;
	}

	public void setInfo_compl(String info_compl) {
		this.info_compl = info_compl;
	}

}
