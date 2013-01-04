package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC425 extends Bean {

	private String cod_item;
	private Double qtd;
	private String unid;
	private Double vl_item;
	private Double vl_pis;
	private Double vl_cofins;

	public DadosC425() {
		reg = "C425";
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
