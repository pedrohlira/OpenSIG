package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC420 extends Bean {

	private String cod_tot_par;
	private double vlr_acum_tot;
	private String nr_tot;
	private String descr_nr_tot;
	
	public DadosC420() {
		reg = "C420";
	}

	public String getCod_tot_par() {
		return cod_tot_par;
	}

	public void setCod_tot_par(String cod_tot_par) {
		this.cod_tot_par = cod_tot_par;
	}

	public double getVlr_acum_tot() {
		return vlr_acum_tot;
	}

	public void setVlr_acum_tot(double vlr_acum_tot) {
		this.vlr_acum_tot = vlr_acum_tot;
	}

	public String getNr_tot() {
		return nr_tot;
	}

	public void setNr_tot(String nr_tot) {
		this.nr_tot = nr_tot;
	}

	public String getDescr_nr_tot() {
		return descr_nr_tot;
	}

	public void setDescr_nr_tot(String descr_nr_tot) {
		this.descr_nr_tot = descr_nr_tot;
	}

}
