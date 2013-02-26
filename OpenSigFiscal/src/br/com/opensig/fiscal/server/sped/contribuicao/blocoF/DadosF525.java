package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosF525 extends Bean {

	private Double vl_rec;
	private String ind_rec;
	private String cnpj_cpf;
	private String num_doc;
	private String cod_item;
	private Double vl_rec_det;
	private String cst_pis;
	private String cst_cofins;
	private String info_compl;
	private String cod_cta;

	public DadosF525() {
		reg = "F525";
	}

	public Double getVl_rec() {
		return vl_rec;
	}

	public void setVl_rec(Double vl_rec) {
		this.vl_rec = vl_rec;
	}

	public String getInd_rec() {
		return ind_rec;
	}

	public void setInd_rec(String ind_rec) {
		this.ind_rec = ind_rec;
	}

	public String getCnpj_cpf() {
		return cnpj_cpf;
	}

	public void setCnpj_cpf(String cnpj_cpf) {
		this.cnpj_cpf = cnpj_cpf;
	}

	public String getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(String num_doc) {
		this.num_doc = num_doc;
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public Double getVl_rec_det() {
		return vl_rec_det;
	}

	public void setVl_rec_det(Double vl_rec_det) {
		this.vl_rec_det = vl_rec_det;
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

	public String getInfo_compl() {
		return info_compl;
	}

	public void setInfo_compl(String info_compl) {
		this.info_compl = info_compl;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

}
