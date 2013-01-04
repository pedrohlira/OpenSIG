package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC500 extends Bean {

	private String ind_oper;
	private String ind_emit;
	private String cod_part;
	private String cod_mod;
	private String cod_sit;
	private String ser;
	private int sub;
	private String cod_cons;
	private int num_doc;
	private Date dt_doc;
	private Date dt_e_s;
	private Double vl_doc;
	private Double vl_desc;
	private Double vl_forn;
	private Double vl_serv_nt;
	private Double vl_terc;
	private Double vl_da;
	private Double vl_bc_icms;
	private Double vl_icms;
	private Double vl_bc_icms_st;
	private Double vl_icms_st;
	private String cod_inf;
	private Double vl_pis;
	private Double vl_cofins;
	private String tp_ligacao;
	private String cod_grupo_tensao;

	public DadosC500() {
		reg = "C500";
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

	public int getSub() {
		return sub;
	}

	public void setSub(int sub) {
		this.sub = sub;
	}

	public String getCod_cons() {
		return cod_cons;
	}

	public void setCod_cons(String cod_cons) {
		this.cod_cons = cod_cons;
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

	public Double getVl_desc() {
		return vl_desc;
	}

	public void setVl_desc(Double vl_desc) {
		this.vl_desc = vl_desc;
	}

	public Double getVl_forn() {
		return vl_forn;
	}

	public void setVl_forn(Double vl_forn) {
		this.vl_forn = vl_forn;
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

	public String getTp_ligacao() {
		return tp_ligacao;
	}

	public void setTp_ligacao(String tp_ligacao) {
		this.tp_ligacao = tp_ligacao;
	}

	public String getCod_grupo_tensao() {
		return cod_grupo_tensao;
	}

	public void setCod_grupo_tensao(String cod_grupo_tensao) {
		this.cod_grupo_tensao = cod_grupo_tensao;
	}

}
