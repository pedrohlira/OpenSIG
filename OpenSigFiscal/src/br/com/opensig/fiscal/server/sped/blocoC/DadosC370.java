package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC370 extends Bean {

	private int num_item;
	private String cod_item;
	private double qtd;
	private String unid;
	private double vl_item;
	private double vl_desc;

	public DadosC370() {
		reg = "C370";
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public int getNum_item() {
		return num_item;
	}

	public void setNum_item(int num_item) {
		this.num_item = num_item;
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

	public double getVl_desc() {
		return vl_desc;
	}

	public void setVl_desc(double vl_desc) {
		this.vl_desc = vl_desc;
	}

	public double getVl_item() {
		return vl_item;
	}

	public void setVl_item(double vl_item) {
		this.vl_item = vl_item;
	}
}