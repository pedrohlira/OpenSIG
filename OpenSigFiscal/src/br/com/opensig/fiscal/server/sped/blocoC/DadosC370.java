package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC370 extends Bean {

	private int num_item;
	private String cod_item;
	private Double qtd;
	private String unid;
	private Double vl_item;
	private Double vl_desc;

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

	public Double getVl_desc() {
		return vl_desc;
	}

	public void setVl_desc(Double vl_desc) {
		this.vl_desc = vl_desc;
	}

	public Double getVl_item() {
		return vl_item;
	}

	public void setVl_item(Double vl_item) {
		this.vl_item = vl_item;
	}
}