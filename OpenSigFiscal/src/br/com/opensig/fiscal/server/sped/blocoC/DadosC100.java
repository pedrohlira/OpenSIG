package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC100 extends Bean {

	private String ind_oper;
	private String ind_emit;
	private String cod_part;
	private String cod_mod;
	private String cod_sit;
	private String ser;
	private int num_doc;
	private String chv_nfe;
	private Date dt_doc;
	private Date dt_e_s;
	private Double vl_doc;
	private String ind_pgto;
	private Double vl_desc;
	private Double vl_abat_nt;
	private Double vl_merc;
	private String ind_frt;
	private Double vl_frt;
	private Double vl_seg;
	private Double vl_out_da;
	private Double vl_bc_icms;
	private Double vl_icms;
	private Double vl_bc_icms_st;
	private Double vl_icms_st;
	private Double vl_ipi;
	private Double vl_pis;
	private Double vl_cofins;
	private Double vl_pis_st;
	private Double vl_cofins_st;
	
	public DadosC100() {
		reg = "C100";
	}
	
	public String getInd_oper() {
		return ind_oper;
	}
	public void setInd_oper(String ind_oper) {
		this.ind_oper = ind_oper;
	}
	public String getInd_emit() {
		return ind_emit;
	}
	public void setInd_emit(String ind_emit) {
		this.ind_emit = ind_emit;
	}
	public String getCod_part() {
		return cod_part;
	}
	public void setCod_part(String cod_part) {
		this.cod_part = cod_part;
	}
	public String getCod_mod() {
		return cod_mod;
	}
	public void setCod_mod(String cod_mod) {
		this.cod_mod = cod_mod;
	}
	public String getCod_sit() {
		return cod_sit;
	}
	public void setCod_sit(String cod_sit) {
		this.cod_sit = cod_sit;
	}
	public String getSer() {
		return ser;
	}
	public void setSer(String ser) {
		this.ser = ser;
	}
	public int getNum_doc() {
		return num_doc;
	}
	public void setNum_doc(int num_doc) {
		this.num_doc = num_doc;
	}
	public String getChv_nfe() {
		return chv_nfe;
	}
	public void setChv_nfe(String chv_nfe) {
		this.chv_nfe = chv_nfe;
	}
	public Date getDt_doc() {
		return dt_doc;
	}
	public void setDt_doc(Date dt_doc) {
		this.dt_doc = dt_doc;
	}
	public Date getDt_e_s() {
		return dt_e_s;
	}
	public void setDt_e_s(Date dt_e_s) {
		this.dt_e_s = dt_e_s;
	}
	public Double getVl_doc() {
		return vl_doc;
	}
	public void setVl_doc(Double vl_doc) {
		this.vl_doc = vl_doc;
	}
	public String getInd_pgto() {
		return ind_pgto;
	}
	public void setInd_pgto(String ind_pgto) {
		this.ind_pgto = ind_pgto;
	}
	public Double getVl_desc() {
		return vl_desc;
	}
	public void setVl_desc(Double vl_desc) {
		this.vl_desc = vl_desc;
	}
	public Double getVl_abat_nt() {
		return vl_abat_nt;
	}
	public void setVl_abat_nt(Double vl_abat_nt) {
		this.vl_abat_nt = vl_abat_nt;
	}
	public Double getVl_merc() {
		return vl_merc;
	}
	public void setVl_merc(Double vl_merc) {
		this.vl_merc = vl_merc;
	}
	public String getInd_frt() {
		return ind_frt;
	}
	public void setInd_frt(String ind_frt) {
		this.ind_frt = ind_frt;
	}
	public Double getVl_frt() {
		return vl_frt;
	}
	public void setVl_frt(Double vl_frt) {
		this.vl_frt = vl_frt;
	}
	public Double getVl_seg() {
		return vl_seg;
	}
	public void setVl_seg(Double vl_seg) {
		this.vl_seg = vl_seg;
	}
	public Double getVl_out_da() {
		return vl_out_da;
	}
	public void setVl_out_da(Double vl_out_da) {
		this.vl_out_da = vl_out_da;
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
	public Double getVl_ipi() {
		return vl_ipi;
	}
	public void setVl_ipi(Double vl_ipi) {
		this.vl_ipi = vl_ipi;
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
	public Double getVl_pis_st() {
		return vl_pis_st;
	}
	public void setVl_pis_st(Double vl_pis_st) {
		this.vl_pis_st = vl_pis_st;
	}
	public Double getVl_cofins_st() {
		return vl_cofins_st;
	}
	public void setVl_cofins_st(Double vl_cofins_st) {
		this.vl_cofins_st = vl_cofins_st;
	}
	
}
