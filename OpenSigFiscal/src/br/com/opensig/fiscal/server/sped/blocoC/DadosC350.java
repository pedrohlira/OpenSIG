package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC350 extends Bean {

	private String ser;
	private String sub;
	private int num_doc;
	private Date dt_doc;
	private String cnpj_cpf;
	private double vl_merc;
	private double vl_doc;
	private double vl_desc;
	private double vl_pis;
	private double vl_cofins;
	private String cod_cta;

	public DadosC350() {
		reg = "C350";
	}

	public String getCnpj_cpf() {
		return cnpj_cpf;
	}

	public void setCnpj_cpf(String cnpj_cpf) {
		this.cnpj_cpf = cnpj_cpf;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

	public Date getDt_doc() {
		return dt_doc;
	}

	public void setDt_doc(Date dt_doc) {
		this.dt_doc = dt_doc;
	}

	public int getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(int num_doc) {
		this.num_doc = num_doc;
	}

	public String getSer() {
		return ser;
	}

	public void setSer(String ser) {
		this.ser = ser;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public double getVl_cofins() {
		return vl_cofins;
	}

	public void setVl_cofins(double vl_cofins) {
		this.vl_cofins = vl_cofins;
	}

	public double getVl_desc() {
		return vl_desc;
	}

	public void setVl_desc(double vl_desc) {
		this.vl_desc = vl_desc;
	}

	public double getVl_doc() {
		return vl_doc;
	}

	public void setVl_doc(double vl_doc) {
		this.vl_doc = vl_doc;
	}

	public double getVl_merc() {
		return vl_merc;
	}

	public void setVl_merc(double vl_merc) {
		this.vl_merc = vl_merc;
	}

	public double getVl_pis() {
		return vl_pis;
	}

	public void setVl_pis(double vl_pis) {
		this.vl_pis = vl_pis;
	}
}