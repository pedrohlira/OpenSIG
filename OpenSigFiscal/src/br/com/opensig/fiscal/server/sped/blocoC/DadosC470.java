package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC470 extends Bean {

	private String cod_item;
	private double qtd;
	private double qtd_canc;
	private String unid;
	private double vl_item;
	private String cst_icms;
	private int cfop;
	private double aliq_icms;
	private double vl_pis;
	private double vl_cofins;

	public DadosC470() {
		reg = "C470";
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public double getQtd() {
		return qtd;
	}

	public void setQtd(double qtd) {
		this.qtd = qtd;
	}

	public double getQtd_canc() {
		return qtd_canc;
	}

	public void setQtd_canc(double qtd_canc) {
		this.qtd_canc = qtd_canc;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public double getVl_item() {
		return vl_item;
	}

	public void setVl_item(double vl_item) {
		this.vl_item = vl_item;
	}

	public String getCst_icms() {
		return cst_icms;
	}

	public void setCst_icms(String cst_icms) {
		this.cst_icms = cst_icms;
	}

	public int getCfop() {
		return cfop;
	}

	public void setCfop(int cfop) {
		this.cfop = cfop;
	}

	public double getAliq_icms() {
		return aliq_icms;
	}

	public void setAliq_icms(double aliq_icms) {
		this.aliq_icms = aliq_icms;
	}

	public double getVl_pis() {
		return vl_pis;
	}

	public void setVl_pis(double vl_pis) {
		this.vl_pis = vl_pis;
	}

	public double getVl_cofins() {
		return vl_cofins;
	}

	public void setVl_cofins(double vl_cofins) {
		this.vl_cofins = vl_cofins;
	}

}
