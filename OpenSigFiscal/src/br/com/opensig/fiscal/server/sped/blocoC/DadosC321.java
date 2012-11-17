package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC321 extends Bean {

	private String cod_item;
	private double qtd;
	private String unid;
	private double vl_item;
	private double desc;
	private double vl_bc_icms;
	private double vl_icms;
	private double vl_pis;
	private double vl_cofins;

	public DadosC321() {
		reg = "C321";
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public double getDesc() {
		return desc;
	}

	public void setDesc(double desc) {
		this.desc = desc;
	}

	public double getQtd() {
		return qtd;
	}

	public void setQtd(double qtd) {
		this.qtd = qtd;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public double getVl_bc_icms() {
		return vl_bc_icms;
	}

	public void setVl_bc_icms(double vl_bc_icms) {
		this.vl_bc_icms = vl_bc_icms;
	}

	public double getVl_cofins() {
		return vl_cofins;
	}

	public void setVl_cofins(double vl_cofins) {
		this.vl_cofins = vl_cofins;
	}

	public double getVl_icms() {
		return vl_icms;
	}

	public void setVl_icms(double vl_icms) {
		this.vl_icms = vl_icms;
	}

	public double getVl_item() {
		return vl_item;
	}

	public void setVl_item(double vl_item) {
		this.vl_item = vl_item;
	}

	public double getVl_pis() {
		return vl_pis;
	}

	public void setVl_pis(double vl_pis) {
		this.vl_pis = vl_pis;
	}
}
