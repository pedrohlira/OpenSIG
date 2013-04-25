package br.com.opensig.produto.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um icms no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_icms")
public class ProdIcms extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_icms_id")
	private int prodIcmsId;

	@Column(name = "prod_icms_decreto")
	private String prodIcmsDecreto;

	@Column(name = "prod_icms_dentro")
	private Double prodIcmsDentro;

	@Column(name = "prod_icms_fora")
	private Double prodIcmsFora;

	@Column(name = "prod_icms_nome")
	private String prodIcmsNome;

	@Column(name = "prod_icms_cfop")
	private int prodIcmsCfop;

	@Column(name = "prod_icms_cst")
	private String prodIcmsCst;

	@Column(name = "prod_icms_cson")
	private String prodIcmsCson;

	@Column(name = "prod_icms_ecf")
	private String prodIcmsEcf;

	public ProdIcms() {
		this(0);
	}

	public ProdIcms(int prodIcmsId) {
		super("pu_produto", "ProdIcms", "prodIcmsId", "prodIcmsCst");
		this.prodIcmsId = prodIcmsId;
	}

	public int getProdIcmsId() {
		return this.prodIcmsId;
	}

	public void setProdIcmsId(int prodIcmsId) {
		this.prodIcmsId = prodIcmsId;
	}

	public String getProdIcmsDecreto() {
		return this.prodIcmsDecreto;
	}

	public void setProdIcmsDecreto(String prodIcmsDecreto) {
		this.prodIcmsDecreto = prodIcmsDecreto;
	}

	public double getProdIcmsDentro() {
		return this.prodIcmsDentro;
	}

	public void setProdIcmsDentro(double prodIcmsDentro) {
		this.prodIcmsDentro = prodIcmsDentro;
	}

	public double getProdIcmsFora() {
		return this.prodIcmsFora;
	}

	public void setProdIcmsFora(double prodIcmsFora) {
		this.prodIcmsFora = prodIcmsFora;
	}

	public String getProdIcmsNome() {
		return this.prodIcmsNome;
	}

	public void setProdIcmsNome(String prodIcmsNome) {
		this.prodIcmsNome = prodIcmsNome;
	}

	public int getProdIcmsCfop() {
		return prodIcmsCfop;
	}

	public void setProdIcmsCfop(int prodIcmsCfop) {
		this.prodIcmsCfop = prodIcmsCfop;
	}

	public String getProdIcmsCst() {
		return prodIcmsCst;
	}

	public void setProdIcmsCst(String prodIcmsCst) {
		this.prodIcmsCst = prodIcmsCst;
	}

	public String getProdIcmsCson() {
		return prodIcmsCson;
	}

	public void setProdIcmsCson(String prodIcmsCson) {
		this.prodIcmsCson = prodIcmsCson;
	}

	public String getProdIcmsEcf() {
		return prodIcmsEcf;
	}

	public void setProdIcmsEcf(String prodIcmsEcf) {
		this.prodIcmsEcf = prodIcmsEcf;
	}

	public Number getId() {
		return prodIcmsId;
	}

	public void setId(Number id) {
		prodIcmsId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodIcmsId + "", prodIcmsNome, prodIcmsCst, prodIcmsCson, prodIcmsCfop + "", prodIcmsEcf, prodIcmsDentro + "",
				prodIcmsFora + "", prodIcmsDecreto };
	}

	public void anularDependencia() {
	}
}