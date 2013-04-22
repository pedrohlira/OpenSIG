package br.com.opensig.fiscal.server.sped.contribuicao.bloco1;

import br.com.opensig.fiscal.server.sped.Bean;

public class Dados1900 extends Bean {

	private String cnpj;
	private String cod_mod;
	private String ser;
	private int sub_ser;
	private String cod_sit;
	private Double vl_tot_rec;
	private int quant_doc;
	private String cst_pis;
	private String cst_cofins;
	private Integer cfop;
	private String inf_compl;
	private String cod_cta;

	public Dados1900() {
		reg = "1900";
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCod_mod() {
		return cod_mod;
	}

	public void setCod_mod(String cod_mod) {
		this.cod_mod = cod_mod;
	}

	public String getSer() {
		return ser;
	}

	public void setSer(String ser) {
		this.ser = ser;
	}

	public int getSub_ser() {
		return sub_ser;
	}

	public void setSub_ser(int sub_ser) {
		this.sub_ser = sub_ser;
	}

	public String getCod_sit() {
		return cod_sit;
	}

	public void setCod_sit(String cod_sit) {
		this.cod_sit = cod_sit;
	}

	public Double getVl_tot_rec() {
		return vl_tot_rec;
	}

	public void setVl_tot_rec(Double vl_tot_rec) {
		this.vl_tot_rec = vl_tot_rec;
	}

	public int getQuant_doc() {
		return quant_doc;
	}

	public void setQuant_doc(int quant_doc) {
		this.quant_doc = quant_doc;
	}

	public String getCst_pis() {
		return cst_pis;
	}

	public void setCst_pis(String cst_pis) {
		this.cst_pis = cst_pis;
	}

	public String getCst_cofins() {
		return cst_cofins;
	}

	public void setCst_cofins(String cst_cofins) {
		this.cst_cofins = cst_cofins;
	}

	public Integer getCfop() {
		return cfop;
	}

	public void setCfop(Integer cfop) {
		this.cfop = cfop;
	}

	public String getInf_compl() {
		return inf_compl;
	}

	public void setInf_compl(String inf_compl) {
		this.inf_compl = inf_compl;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

}
