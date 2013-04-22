package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0000 extends Bean {

	private int cod_ver;
	private int tipo_escrit;
	private Integer ind_sit_esp;
	private String num_rec_anterior;
	private Date dt_ini;
	private Date dt_fin;
	private String nome;
	private String cnpj;
	private String uf;
	private int cod_mun;
	private String suframa;
	private int ind_nat_pj;
	private int ind_ativ;

	public Dados0000() {
		reg = "0000";
	}
	
	public int getCod_ver() {
		return cod_ver;
	}

	public void setCod_ver(int cod_ver) {
		this.cod_ver = cod_ver;
	}

	public Date getDt_ini() {
		return dt_ini;
	}

	public void setDt_ini(Date dt_ini) {
		this.dt_ini = dt_ini;
	}

	public Date getDt_fin() {
		return dt_fin;
	}

	public void setDt_fin(Date dt_fin) {
		this.dt_fin = dt_fin;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public int getCod_mun() {
		return cod_mun;
	}

	public void setCod_mun(int cod_mun) {
		this.cod_mun = cod_mun;
	}

	public String getSuframa() {
		return suframa;
	}

	public void setSuframa(String suframa) {
		this.suframa = suframa;
	}

	public int getInd_ativ() {
		return ind_ativ;
	}

	public void setInd_ativ(int ind_ativ) {
		this.ind_ativ = ind_ativ;
	}

	public int getTipo_escrit() {
		return tipo_escrit;
	}

	public void setTipo_escrit(int tipo_escrit) {
		this.tipo_escrit = tipo_escrit;
	}

	public Integer getInd_sit_esp() {
		return ind_sit_esp;
	}

	public void setInd_sit_esp(Integer ind_sit_esp) {
		this.ind_sit_esp = ind_sit_esp;
	}

	public String getNum_rec_anterior() {
		return num_rec_anterior;
	}

	public void setNum_rec_anterior(String num_rec_anterior) {
		this.num_rec_anterior = num_rec_anterior;
	}

	public int getInd_nat_pj() {
		return ind_nat_pj;
	}

	public void setInd_nat_pj(int ind_nat_pj) {
		this.ind_nat_pj = ind_nat_pj;
	}

}
