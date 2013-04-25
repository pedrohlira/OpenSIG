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
 * Classe que representa o pis  no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_pis")
public class ProdPis extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_pis_id")
	private int prodPisId;

	@Column(name = "prod_pis_decreto")
	private String prodPisDecreto;

	@Column(name = "prod_pis_aliquota")
	private Double prodPisAliquota;

	@Column(name = "prod_pis_nome")
	private String prodPisNome;

	@Column(name = "prod_pis_cst_entrada")
	private String prodPisCstEntrada;

	@Column(name = "prod_pis_cst_saida")
	private String prodPisCstSaida;
	
	public ProdPis() {
		this(0);
	}

	public ProdPis(int prodPisId) {
		super("pu_produto", "ProdPis", "prodPisId", "prodPisCstEntrada");
		this.prodPisId = prodPisId;
	}

	public int getProdPisId() {
		return prodPisId;
	}

	public void setProdPisId(int prodPisId) {
		this.prodPisId = prodPisId;
	}

	public String getProdPisDecreto() {
		return prodPisDecreto;
	}

	public void setProdPisDecreto(String prodPisDecreto) {
		this.prodPisDecreto = prodPisDecreto;
	}

	public Double getProdPisAliquota() {
		return prodPisAliquota;
	}

	public void setProdPisAliquota(Double prodPisAliquota) {
		this.prodPisAliquota = prodPisAliquota;
	}

	public String getProdPisNome() {
		return prodPisNome;
	}

	public void setProdPisNome(String prodPisNome) {
		this.prodPisNome = prodPisNome;
	}

	public String getProdPisCstEntrada() {
		return prodPisCstEntrada;
	}

	public void setProdPisCstEntrada(String prodPisCstEntrada) {
		this.prodPisCstEntrada = prodPisCstEntrada;
	}

	public String getProdPisCstSaida() {
		return prodPisCstSaida;
	}

	public void setProdPisCstSaida(String prodPisCstSaida) {
		this.prodPisCstSaida = prodPisCstSaida;
	}

	public Number getId() {
		return this.prodPisId;
	}

	public void setId(Number id) {
		this.prodPisId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodPisId + "", prodPisNome, prodPisCstEntrada, prodPisCstSaida, prodPisAliquota + "", prodPisDecreto };
	}

	public void anularDependencia() {
	}
}