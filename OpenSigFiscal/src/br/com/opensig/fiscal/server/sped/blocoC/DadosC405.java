package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.Date;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC405 extends Bean {

	private Date dt_doc;
	private int cro;
	private int crz;
	private int num_coo_fin;
	private double gt_fin;
	private double vl_brt;

	public DadosC405() {
		reg = "C405";
	}

	public Date getDt_doc() {
		return dt_doc;
	}

	public void setDt_doc(Date dt_doc) {
		this.dt_doc = dt_doc;
	}

	public int getCro() {
		return cro;
	}

	public void setCro(int cro) {
		this.cro = cro;
	}

	public int getCrz() {
		return crz;
	}

	public void setCrz(int crz) {
		this.crz = crz;
	}

	public int getNum_coo_fin() {
		return num_coo_fin;
	}

	public void setNum_coo_fin(int num_coo_fin) {
		this.num_coo_fin = num_coo_fin;
	}

	public double getGt_fin() {
		return gt_fin;
	}

	public void setGt_fin(double gt_fin) {
		this.gt_fin = gt_fin;
	}

	public double getVl_brt() {
		return vl_brt;
	}

	public void setVl_brt(double vl_brt) {
		this.vl_brt = vl_brt;
	}

}
