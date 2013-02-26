package br.com.opensig.fiscal.server.sped.fiscal.blocoC;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC141 extends Bean {

	private int num_parc;
	private Date dt_vcto;
	private Double vl_parc;
	
	public DadosC141() {
		reg = "C141";
	}

	public int getNum_parc() {
		return num_parc;
	}

	public void setNum_parc(int num_parc) {
		this.num_parc = num_parc;
	}

	public Date getDt_vcto() {
		return dt_vcto;
	}

	public void setDt_vcto(Date dt_vcto) {
		this.dt_vcto = dt_vcto;
	}

	public Double getVl_parc() {
		return vl_parc;
	}

	public void setVl_parc(Double vl_parc) {
		this.vl_parc = vl_parc;
	}

}
