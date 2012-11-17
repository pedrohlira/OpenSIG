package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados0200 extends Bean {

	private String cod_item;
	private String descr_item;
	private String cod_barra;
	private String cod_ant_item;
	private String unid_inv;
	private int tipo_item;
	private String cod_ncm;
	private String ex_ipi;
	private String cod_gen;
	private int cod_lst;
	private double aliq_icms;

	public Dados0200() {
		reg = "0200";
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public String getDescr_item() {
		return descr_item;
	}

	public void setDescr_item(String descr_item) {
		this.descr_item = descr_item;
	}

	public String getCod_barra() {
		return cod_barra;
	}

	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}

	public String getCod_ant_item() {
		return cod_ant_item;
	}

	public void setCod_ant_item(String cod_ant_item) {
		this.cod_ant_item = cod_ant_item;
	}

	public String getUnid_inv() {
		return unid_inv;
	}

	public void setUnid_inv(String unid_inv) {
		this.unid_inv = unid_inv;
	}

	public int getTipo_item() {
		return tipo_item;
	}

	public void setTipo_item(int tipo_item) {
		this.tipo_item = tipo_item;
	}

	public String getCod_ncm() {
		return cod_ncm;
	}

	public void setCod_ncm(String cod_ncm) {
		this.cod_ncm = cod_ncm;
	}

	public String getEx_ipi() {
		return ex_ipi;
	}

	public void setEx_ipi(String ex_ipi) {
		this.ex_ipi = ex_ipi;
	}

	public String getCod_gen() {
		return cod_gen;
	}

	public void setCod_gen(String cod_gen) {
		this.cod_gen = cod_gen;
	}

	public int getCod_lst() {
		return cod_lst;
	}

	public void setCod_lst(int cod_lst) {
		this.cod_lst = cod_lst;
	}

	public double getAliq_icms() {
		return aliq_icms;
	}

	public void setAliq_icms(double aliq_icms) {
		this.aliq_icms = aliq_icms;
	}

}
