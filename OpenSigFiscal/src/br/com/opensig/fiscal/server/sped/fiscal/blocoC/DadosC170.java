package br.com.opensig.fiscal.server.sped.fiscal.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC170 extends Bean {

	private int num_item;
	private String cod_item;
	private String descr_compl;
	private Double qtd;
	private String unid;
	private Double vl_item;
	private Double vl_desc;
	private Double vl_frete;
	private Double vl_seguro;
	private Double vl_outros;
	private String ind_mov;
	private String cst_icms;
	private int cfop;
	private String cod_nat;
	private Double vl_bc_icms;
	private Double aliq_icms;
	private Double vl_icms;
	private Double vl_bc_icms_st;
	private Double aliq_st;
	private Double vl_icms_st;
	private String ind_apur;
	private String cst_ipi;
	private String cod_enq;
	private Double vl_bc_ipi;
	private Double aliq_ipi;
	private Double vl_ipi;
	private String cst_pis;
	private Double vl_bc_pis;
	private Double aliq_pis;
	private Double quant_bc_pis;
	private Double aliq2_pis;
	private Double vl_pis;
	private String cst_cofins;
	private Double vl_bc_cofins;
	private Double aliq_cofins;
	private Double quant_bc_cofins;
	private Double aliq2_cofins;
	private Double vl_cofins;
	private String cod_cta;
	
	public DadosC170() {
		reg = "C170";
	}

	public int getNum_item() {
		return num_item;
	}

	public void setNum_item(int num_item) {
		this.num_item = num_item;
	}

	public String getCod_item() {
		return cod_item;
	}

	public void setCod_item(String cod_item) {
		this.cod_item = cod_item;
	}

	public String getDescr_compl() {
		return descr_compl;
	}

	public void setDescr_compl(String descr_compl) {
		this.descr_compl = descr_compl;
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

	public Double getVl_desc() {
		return vl_desc;
	}

	public void setVl_desc(Double vl_desc) {
		this.vl_desc = vl_desc;
	}

	public Double getVl_frete() {
		return vl_frete;
	}

	public void setVl_frete(Double vl_frete) {
		this.vl_frete = vl_frete;
	}

	public Double getVl_seguro() {
		return vl_seguro;
	}

	public void setVl_seguro(Double vl_seguro) {
		this.vl_seguro = vl_seguro;
	}

	public Double getVl_outros() {
		return vl_outros;
	}

	public void setVl_outros(Double vl_outros) {
		this.vl_outros = vl_outros;
	}

	public String getInd_mov() {
		return ind_mov;
	}

	public void setInd_mov(String ind_mov) {
		this.ind_mov = ind_mov;
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

	public String getCod_nat() {
		return cod_nat;
	}

	public void setCod_nat(String cod_nat) {
		this.cod_nat = cod_nat;
	}

	public Double getVl_bc_icms() {
		return vl_bc_icms;
	}

	public void setVl_bc_icms(Double vl_bc_icms) {
		this.vl_bc_icms = vl_bc_icms;
	}

	public Double getAliq_icms() {
		return aliq_icms;
	}

	public void setAliq_icms(Double aliq_icms) {
		this.aliq_icms = aliq_icms;
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

	public Double getAliq_st() {
		return aliq_st;
	}

	public void setAliq_st(Double aliq_st) {
		this.aliq_st = aliq_st;
	}

	public Double getVl_icms_st() {
		return vl_icms_st;
	}

	public void setVl_icms_st(Double vl_icms_st) {
		this.vl_icms_st = vl_icms_st;
	}

	public String getInd_apur() {
		return ind_apur;
	}

	public void setInd_apur(String ind_apur) {
		this.ind_apur = ind_apur;
	}

	public String getCst_ipi() {
		return cst_ipi;
	}

	public void setCst_ipi(String cst_ipi) {
		this.cst_ipi = cst_ipi;
	}

	public String getCod_enq() {
		return cod_enq;
	}

	public void setCod_enq(String cod_enq) {
		this.cod_enq = cod_enq;
	}

	public Double getVl_bc_ipi() {
		return vl_bc_ipi;
	}

	public void setVl_bc_ipi(Double vl_bc_ipi) {
		this.vl_bc_ipi = vl_bc_ipi;
	}

	public Double getAliq_ipi() {
		return aliq_ipi;
	}

	public void setAliq_ipi(Double aliq_ipi) {
		this.aliq_ipi = aliq_ipi;
	}

	public Double getVl_ipi() {
		return vl_ipi;
	}

	public void setVl_ipi(Double vl_ipi) {
		this.vl_ipi = vl_ipi;
	}

	public String getCst_pis() {
		return cst_pis;
	}

	public void setCst_pis(String cst_pis) {
		this.cst_pis = cst_pis;
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

	public Double getQuant_bc_pis() {
		return quant_bc_pis;
	}

	public void setQuant_bc_pis(Double quant_bc_pis) {
		this.quant_bc_pis = quant_bc_pis;
	}

	public Double getAliq2_pis() {
		return aliq2_pis;
	}

	public void setAliq2_pis(Double aliq2_pis) {
		this.aliq2_pis = aliq2_pis;
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

	public Double getQuant_bc_cofins() {
		return quant_bc_cofins;
	}

	public void setQuant_bc_cofins(Double quant_bc_cofins) {
		this.quant_bc_cofins = quant_bc_cofins;
	}

	public Double getAliq2_cofins() {
		return aliq2_cofins;
	}

	public void setAliq2_cofins(Double aliq2_cofins) {
		this.aliq2_cofins = aliq2_cofins;
	}

	public Double getVl_cofins() {
		return vl_cofins;
	}

	public void setVl_cofins(Double vl_cofins) {
		this.vl_cofins = vl_cofins;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}
	
}
