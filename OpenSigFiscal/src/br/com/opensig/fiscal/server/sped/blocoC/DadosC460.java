package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC460 extends Bean {

	private String cod_mod;
	private String cod_sit;
	private int num_doc;
	private Date dt_doc;
	private Double vl_doc;
	private Double vl_pis;
	private Double vl_cofins;
	private String cpf_cnpj;
	private String nom_adq;

	public DadosC460() {
		reg = "C460";
	}

	public String getCod_mod() {
		return cod_mod;
	}

	public void setCod_mod(String cod_mod) {
		this.cod_mod = cod_mod;
	}

	public String getCod_sit() {
		return cod_sit;
	}

	public void setCod_sit(String cod_sit) {
		this.cod_sit = cod_sit;
	}

	public int getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(int num_doc) {
		this.num_doc = num_doc;
	}

	public Date getDt_doc() {
		return dt_doc;
	}

	public void setDt_doc(Date dt_doc) {
		this.dt_doc = dt_doc;
	}

	public Double getVl_doc() {
		return vl_doc;
	}

	public void setVl_doc(Double vl_doc) {
		this.vl_doc = vl_doc;
	}

	public Double getVl_pis() {
		return vl_pis;
	}

	public void setVl_pis(Double vl_pis) {
		this.vl_pis = vl_pis;
	}

	public Double getVl_cofins() {
		return vl_cofins;
	}

	public void setVl_cofins(Double vl_cofins) {
		this.vl_cofins = vl_cofins;
	}

	public String getCpf_cnpj() {
		return cpf_cnpj;
	}

	public void setCpf_cnpj(String cpf_cnpj) {
		this.cpf_cnpj = cpf_cnpj;
	}

	public String getNom_adq() {
		return nom_adq;
	}

	public void setNom_adq(String nom_adq) {
		this.nom_adq = nom_adq;
	}

}
