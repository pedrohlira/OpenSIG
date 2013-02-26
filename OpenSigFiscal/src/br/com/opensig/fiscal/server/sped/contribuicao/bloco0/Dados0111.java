package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0111 extends Bean {

	private Double rec_bru_ncum_trib_mi;
	private Double rec_bru_ncum_nt_mi;
	private Double rec_bru_ncum_exp;
	private Double rec_bru_cum;
	private Double rec_bru_total;

	public Dados0111() {
		reg = "0111";
	}

	public Double getRec_bru_ncum_trib_mi() {
		return rec_bru_ncum_trib_mi;
	}

	public void setRec_bru_ncum_trib_mi(Double rec_bru_ncum_trib_mi) {
		this.rec_bru_ncum_trib_mi = rec_bru_ncum_trib_mi;
	}

	public Double getRec_bru_ncum_nt_mi() {
		return rec_bru_ncum_nt_mi;
	}

	public void setRec_bru_ncum_nt_mi(Double rec_bru_ncum_nt_mi) {
		this.rec_bru_ncum_nt_mi = rec_bru_ncum_nt_mi;
	}

	public Double getRec_bru_ncum_exp() {
		return rec_bru_ncum_exp;
	}

	public void setRec_bru_ncum_exp(Double rec_bru_ncum_exp) {
		this.rec_bru_ncum_exp = rec_bru_ncum_exp;
	}

	public Double getRec_bru_cum() {
		return rec_bru_cum;
	}

	public void setRec_bru_cum(Double rec_bru_cum) {
		this.rec_bru_cum = rec_bru_cum;
	}

	public Double getRec_bru_total() {
		return rec_bru_total;
	}

	public void setRec_bru_total(Double rec_bru_total) {
		this.rec_bru_total = rec_bru_total;
	}

}
