package br.com.opensig.fiscal.server.sped.blocoD;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosD100 extends Bean {

	private String ind_oper;
	private String ind_emit;
	private String cod_part;
	private String cod_mod;
	private String cod_sit;
	private String ser;
	private String sub;
	private int num_doc;
	private String chv_cte;
	private Date dt_doc;
	private Date dt_a_p;
	private String tp_cte;
	private String chv_cte_ref;
	private double vl_doc;
	private double vl_desc;
	private String ind_frt;
	private double vl_serv;
	private double vl_bc_icms;
	private double vl_icms;
	private double vl_nt;
	private String cod_inf;
	private String cod_cta; 
	
	public DadosD100() {
		reg = "D100";
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

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public int getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(int num_doc) {
		this.num_doc = num_doc;
	}

	public String getChv_cte() {
		return chv_cte;
	}

	public void setChv_cte(String chv_cte) {
		this.chv_cte = chv_cte;
	}

	public Date getDt_doc() {
		return dt_doc;
	}

	public void setDt_doc(Date dt_doc) {
		this.dt_doc = dt_doc;
	}

	public Date getDt_a_p() {
		return dt_a_p;
	}

	public void setDt_a_p(Date dt_a_p) {
		this.dt_a_p = dt_a_p;
	}

	public String getTp_cte() {
		return tp_cte;
	}

	public void setTp_cte(String tp_cte) {
		this.tp_cte = tp_cte;
	}

	public String getChv_cte_ref() {
		return chv_cte_ref;
	}

	public void setChv_cte_ref(String chv_cte_ref) {
		this.chv_cte_ref = chv_cte_ref;
	}

	public double getVl_doc() {
		return vl_doc;
	}

	public void setVl_doc(double vl_doc) {
		this.vl_doc = vl_doc;
	}

	public double getVl_desc() {
		return vl_desc;
	}

	public void setVl_desc(double vl_desc) {
		this.vl_desc = vl_desc;
	}

	public String getInd_frt() {
		return ind_frt;
	}

	public void setInd_frt(String ind_frt) {
		this.ind_frt = ind_frt;
	}

	public double getVl_serv() {
		return vl_serv;
	}

	public void setVl_serv(double vl_serv) {
		this.vl_serv = vl_serv;
	}

	public double getVl_bc_icms() {
		return vl_bc_icms;
	}

	public void setVl_bc_icms(double vl_bc_icms) {
		this.vl_bc_icms = vl_bc_icms;
	}

	public double getVl_icms() {
		return vl_icms;
	}

	public void setVl_icms(double vl_icms) {
		this.vl_icms = vl_icms;
	}

	public double getVl_nt() {
		return vl_nt;
	}

	public void setVl_nt(double vl_nt) {
		this.vl_nt = vl_nt;
	}

	public String getCod_inf() {
		return cod_inf;
	}

	public void setCod_inf(String cod_inf) {
		this.cod_inf = cod_inf;
	}

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

}
