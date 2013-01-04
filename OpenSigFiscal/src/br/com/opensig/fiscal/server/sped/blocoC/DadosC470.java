package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC470 extends Bean {

	private String cod_item;
	private Double qtd;
	private Double qtd_canc;
	private String unid;
	private Double vl_item;
	private String cst_icms;
	private int cfop;
	private Double aliq_icms;
	private Double vl_pis;
	private Double vl_cofins;

	public DadosC470() {
		reg = "C470";
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public Double getQtd() {
		return qtd;
	}

	public void setQtd(Double qtd) {
		this.qtd = qtd;
	}

	public Double getQtd_canc() {
		return qtd_canc;
	}

	public void setQtd_canc(Double qtd_canc) {
		this.qtd_canc = qtd_canc;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public Double getVl_item() {
		return vl_item;
	}

	public void setVl_item(Double vl_item) {
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

	public Double getAliq_icms() {
		return aliq_icms;
	}

	public void setAliq_icms(Double aliq_icms) {
		this.aliq_icms = aliq_icms;
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

}
