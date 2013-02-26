package br.com.opensig.fiscal.server.sped.fiscal.blocoD;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosD500 extends Bean {

	private String ind_oper;
	private String ind_emit;
	private String cod_part;
	private String cod_mod;
	private String cod_sit;
	private String ser;
	private String sub;
	private int num_doc;
	private Date dt_doc;
	private Date dt_a_p;
	private Double vl_doc;
	private Double vl_desc;
	private Double vl_serv;
	private Double vl_serv_nt;
	private Double vl_terc;
	private Double vl_da;
	private Double vl_bc_icms;
	private Double vl_icms;
	private String cod_inf;
	private Double vl_pis;
	private Double vl_cofins;
	private String cod_cta;
	private int tp_assinante;

	public DadosD500() {
		reg = "D500";
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

	public Double getVl_doc() {
		return vl_doc;
	}

	public void setVl_doc(Double vl_doc) {
		this.vl_doc = vl_doc;
	}

	public Double getVl_desc() {
		return vl_desc;
	}

	public void setVl_desc(Double vl_desc) {
		this.vl_desc = vl_desc;
	}

	public Double getVl_serv() {
		return vl_serv;
	}

	public void setVl_serv(Double vl_serv) {
		this.vl_serv = vl_serv;
	}

	public Double getVl_serv_nt() {
		return vl_serv_nt;
	}

	public void setVl_serv_nt(Double vl_serv_nt) {
		this.vl_serv_nt = vl_serv_nt;
	}

	public Double getVl_terc() {
		return vl_terc;
	}

	public void setVl_terc(Double vl_terc) {
		this.vl_terc = vl_terc;
	}

	public Double getVl_da() {
		return vl_da;
	}

	public void setVl_da(Double vl_da) {
		this.vl_da = vl_da;
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

	public String getCod_inf() {
		return cod_inf;
	}

	public void setCod_inf(String cod_inf) {
		this.cod_inf = cod_inf;
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

	public String getCod_cta() {
		return cod_cta;
	}

	public void setCod_cta(String cod_cta) {
		this.cod_cta = cod_cta;
	}

	public int getTp_assinante() {
		return tp_assinante;
	}

	public void setTp_assinante(int tp_assinante) {
		this.tp_assinante = tp_assinante;
	}

}
