package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0140 extends Bean {

	private String cod_est;
	private String nome;
	private String cnpj;
	private String uf;
	private String ie;
	private int cod_mun;
	private String im;
	private String suframa;

	public Dados0140() {
		reg = "0140";
	}

	public String getCod_est() {
		return cod_est;
	}

	public void setCod_est(String cod_est) {
		this.cod_est = cod_est;
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

}
