package br.com.opensig.fiscal.server.sped.bloco0;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0000 extends Bean {

	private int cod_ver;
	private int cod_fin;
	private Date dt_ini;
	private Date dt_fin;
	private String nome;
	private String cnpj;
	private String cpf;
	private String uf;
	private String ie;
	private int cod_mun;
	private String im;
	private String suframa;
	private String ind_perfil;
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

	public int getCod_fin() {
		return cod_fin;
	}

	public void setCod_fin(int cod_fin) {
		this.cod_fin = cod_fin;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	public int getCod_mun() {
		return cod_mun;
	}

	public void setCod_mun(int cod_mun) {
		this.cod_mun = cod_mun;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public String getSuframa() {
		return suframa;
	}

	public void setSuframa(String suframa) {
		this.suframa = suframa;
	}

	public String getInd_perfil() {
		return ind_perfil;
	}

	public void setInd_perfil(String ind_perfil) {
		this.ind_perfil = ind_perfil;
	}

	public int getInd_ativ() {
		return ind_ativ;
	}

	public void setInd_ativ(int ind_ativ) {
		this.ind_ativ = ind_ativ;
	}

}
