package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosF550 extends Bean {

	private Double vl_rec_comp;
	private String cst_pis;
	private Double vl_desc_pis;
	private Double vl_bc_pis;
	private Double aliq_pis;
	private Double vl_pis;
	private String cst_cofins;
	private Double vl_desc_cofins;
	private Double vl_bc_cofins;
	private Double aliq_cofins;
	private Double vl_cofins;
	private String cod_mod;
	private int cfop;
	private String cod_cta;
	private String info_compl;

	public DadosF550() {
		reg = "F550";
	}

	public Double getVl_rec_comp() {
		return vl_rec_comp;
	}

	public void setVl_rec_comp(Double vl_rec_comp) {
		this.vl_rec_comp = vl_rec_comp;
	}

	public String getCst_pis() {
		return cst_pis;
	}

	public void setCst_pis(String cst_pis) {
		this.cst_pis = cst_pis;
	}

	public Double getVl_desc_pis() {
		return vl_desc_pis;
	}

	public void setVl_desc_pis(Double vl_desc_pis) {
		this.vl_desc_pis = vl_desc_pis;
	}

	public Double getVl_bc_pis() {
		return vl_bc_pis;
	}

	public void setVl_bc_pis(Double vl_bc_pis) {
		this.vl_bc_pis = vl_bc_pis;
	}

	public Double getAliq_pis() {
		return aliq_pis;
	}

	public void setAliq_pis(Double aliq_pis) {
		this.aliq_pis = aliq_pis;
	}

	public Double getVl_pis() {
		return vl_pis;
	}

	public void setVl_pis(Double vl_pis) {
		this.vl_pis = vl_pis;
	}

	public String getCst_cofins() {
		return cst_cofins;
	}

	public void setCst_cofins(String cst_cofins) {
		this.cst_cofins = cst_cofins;
	}

	public Double getVl_desc_cofins() {
		return vl_desc_cofins;
	}

	public void setVl_desc_cofins(Double vl_desc_cofins) {
		this.vl_desc_cofins = vl_desc_cofins;
	}

	public Double getVl_bc_cofins() {
		return vl_bc_cofins;
	}

	public void setVl_bc_cofins(Double vl_bc_cofins) {
		this.vl_bc_cofins = vl_bc_cofins;
	}

	public Double getAliq_cofins() {
		return aliq_cofins;
	}

	public void setAliq_cofins(Double aliq_cofins) {
		this.aliq_cofins = aliq_cofins;
	}

	public Double getVl_cofins() {
		return vl_cofins;
	}

	public void setVl_cofins(Double vl_cofins) {
		this.vl_cofins = vl_cofins;
	}

	public String getCod_mod() {
		return cod_mod;
	}

	public void setCod_mod(String cod_mod) {
		this.cod_mod = cod_mod;
	}

	public int getCfop() {
		return cfop;
	}

	public void setCfop(int cfop) {
		this.cfop = cfop;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

	public String getInfo_compl() {
		return info_compl;
	}

	public void setInfo_compl(String info_compl) {
		this.info_compl = info_compl;
	}

}
