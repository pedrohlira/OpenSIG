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
 * Classe que representa o ipi  no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 01/02/2012
 */
@Entity
@Table(name = "prod_ipi")
public class ProdIpi extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_ipi_id")
	private int prodIpiId;

	@Column(name = "prod_ipi_decreto")
	private String prodIpiDecreto;

	@Column(name = "prod_ipi_aliquota")
	private Double prodIpiAliquota;

	@Column(name = "prod_ipi_nome")
	private String prodIpiNome;

	@Column(name = "prod_ipi_cst_entrada")
	private String prodIpiCstEntrada;

	@Column(name = "prod_ipi_cst_saida")
	private String prodIpiCstSaida;
	
	@Column(name = "prod_ipi_enq")
	private String prodIpiEnq;

	public ProdIpi() {
		this(0);
	}

	public ProdIpi(int prodIpiId) {
		super("pu_produto", "ProdIpi", "prodIpiId", "prodIpiNome");
		this.prodIpiId = prodIpiId;
	}

	public int getProdIpiId() {
		return prodIpiId;
	}

	public void setProdIpiId(int prodIpiId) {
		this.prodIpiId = prodIpiId;
	}

	public String getProdIpiDecreto() {
		return prodIpiDecreto;
	}

	public void setProdIpiDecreto(String prodIpiDecreto) {
		this.prodIpiDecreto = prodIpiDecreto;
	}

	public Double getProdIpiAliquota() {
		return prodIpiAliquota;
	}

	public void setProdIpiAliquota(Double prodIpiAliquota) {
		this.prodIpiAliquota = prodIpiAliquota;
	}

	public String getProdIpiNome() {
		return prodIpiNome;
	}

	public void setProdIpiNome(String prodIpiNome) {
		this.prodIpiNome = prodIpiNome;
	}

	public String getProdIpiCstEntrada() {
		return prodIpiCstEntrada;
	}

	public void setProdIpiCstEntrada(String prodIpiCstEntrada) {
		this.prodIpiCstEntrada = prodIpiCstEntrada;
	}

	public String getProdIpiCstSaida() {
		return prodIpiCstSaida;
	}

	public void setProdIpiCstSaida(String prodIpiCstSaida) {
		this.prodIpiCstSaida = prodIpiCstSaida;
	}

	public String getProdIpiEnq() {
		return prodIpiEnq;
	}
	
	public void setProdIpiEnq(String prodIpiEnq) {
		this.prodIpiEnq = prodIpiEnq;
	}
	
	public Number getId() {
		return this.prodIpiId;
	}

	public void setId(Number id) {
		this.prodIpiId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodIpiId + "", prodIpiNome, prodIpiCstEntrada, prodIpiCstSaida, prodIpiAliquota + "", prodIpiEnq, prodIpiDecreto };
	}

	public void anularDependencia() {
	}
}