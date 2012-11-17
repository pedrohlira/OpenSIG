package br.com.opensig.fiscal.server.sped.blocoH;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosH010 extends Bean {

	private String cod_item;
	private String unid;
	private double qtd;
	private double vl_unit;
	private double vl_item;
	private String ind_prop;
	private String cod_part;
	private String txt_compl;
	private String cod_cta;
	
	public DadosH010() {
		reg = "H010";
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public double getQtd() {
		return qtd;
	}

	public void setQtd(double qtd) {
		this.qtd = qtd;
	}

	public double getVl_unit() {
		return vl_unit;
	}

	public void setVl_unit(double vl_unit) {
		this.vl_unit = vl_unit;
	}

	public double getVl_item() {
		return vl_item;
	}

	public void setVl_item(double vl_item) {
		this.vl_item = vl_item;
	}

	public String getInd_prop() {
		return ind_prop;
	}

	public void setInd_prop(String ind_prop) {
		this.ind_prop = ind_prop;
	}

	public String getCod_part() {
		return cod_part;
	}

	public void setCod_part(String cod_part) {
		this.cod_part = cod_part;
	}

	public String getTxt_compl() {
		return txt_compl;
	}

	public void setTxt_compl(String txt_compl) {
		this.txt_compl = txt_compl;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

}
