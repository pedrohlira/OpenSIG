package br.com.opensig.fiscal.server.sped.fiscal.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC590 extends Bean {

	private String cst_icms;
	private int cfop;
	private Double aliq_icms;
	private Double vl_opr;
	private Double vl_bc_icms;
	private Double vl_icms;
	private Double vl_bc_icms_st;
	private Double vl_icms_st;
	private Double vl_red_bc;
	private String cod_obs;

	public DadosC590() {
		reg = "C590";
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

	public Double getVl_opr() {
		return vl_opr;
	}

	public void setVl_opr(Double vl_opr) {
		this.vl_opr = vl_opr;
	}

	public Double getVl_bc_icms() {
		return vl_bc_icms;
	}

	public void setVl_bc_icms(Double vl_bc_icms) {
		this.vl_bc_icms = vl_bc_icms;
	}

	public Double getVl_icms() {
		return vl_icms;
	}

	public void setVl_icms(Double vl_icms) {
		this.vl_icms = vl_icms;
	}

	public Double getVl_bc_icms_st() {
		return vl_bc_icms_st;
	}

	public void setVl_bc_icms_st(Double vl_bc_icms_st) {
		this.vl_bc_icms_st = vl_bc_icms_st;
	}

	public Double getVl_icms_st() {
		return vl_icms_st;
	}

	public void setVl_icms_st(Double vl_icms_st) {
		this.vl_icms_st = vl_icms_st;
	}

	public Double getVl_red_bc() {
		return vl_red_bc;
	}

	public void setVl_red_bc(Double vl_red_bc) {
		this.vl_red_bc = vl_red_bc;
	}

	public String getCod_obs() {
		return cod_obs;
	}

	public void setCod_obs(String cod_obs) {
		this.cod_obs = cod_obs;
	}

}
