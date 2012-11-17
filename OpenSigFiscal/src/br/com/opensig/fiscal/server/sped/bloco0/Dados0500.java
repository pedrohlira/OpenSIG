package br.com.opensig.fiscal.server.sped.bloco0;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0500 extends Bean {

	private Date dt_alt;
	private String cod_nat_cc;
	private String ind_cta;
	private int nivel;
	private String cod_cta;
	private String nome_cta;

	public Dados0500() {
		reg = "0500";
	}

	public Date getDt_alt() {
		return dt_alt;
	}

	public void setDt_alt(Date dt_alt) {
		this.dt_alt = dt_alt;
	}

	public String getCod_nat_cc() {
		return cod_nat_cc;
	}

	public void setCod_nat_cc(String cod_nat_cc) {
		this.cod_nat_cc = cod_nat_cc;
	}

	public String getInd_cta() {
		return ind_cta;
	}

	public void setInd_cta(String ind_cta) {
		this.ind_cta = ind_cta;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

	public String getNome_cta() {
		return nome_cta;
	}

	public void setNome_cta(String nome_cta) {
		this.nome_cta = nome_cta;
	}
	
}
