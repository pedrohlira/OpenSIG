package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC321 extends Bean {

	private String cod_item;
	private Double qtd;
	private String unid;
	private Double vl_item;
	private Double desc;
	private Double vl_bc_icms;
	private Double vl_icms;
	private Double vl_pis;
	private Double vl_cofins;

	public DadosC321() {
		reg = "C321";
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public Double getDesc() {
		return desc;
	}

	public void setDesc(Double desc) {
		this.desc = desc;
	}

	public Double getQtd() {
		return qtd;
	}

	public void setQtd(Double qtd) {
		this.qtd = qtd;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public Double getVl_bc_icms() {
		return vl_bc_icms;
	}

	public void setVl_bc_icms(Double vl_bc_icms) {
		this.vl_bc_icms = vl_bc_icms;
	}

	public Double getVl_cofins() {
		return vl_cofins;
	}

	public void setVl_cofins(Double vl_cofins) {
		this.vl_cofins = vl_cofins;
	}

	public Double getVl_icms() {
		return vl_icms;
	}

	public void setVl_icms(Double vl_icms) {
		this.vl_icms = vl_icms;
	}

	public Double getVl_item() {
		return vl_item;
	}

	public void setVl_item(Double vl_item) {
		this.vl_item = vl_item;
	}

	public Double getVl_pis() {
		return vl_pis;
	}

	public void setVl_pis(Double vl_pis) {
		this.vl_pis = vl_pis;
	}
}
