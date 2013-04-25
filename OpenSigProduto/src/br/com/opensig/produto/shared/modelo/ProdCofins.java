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
 * Classe que representa o cofins no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_cofins")
public class ProdCofins extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_cofins_id")
	private int prodCofinsId;

	@Column(name = "prod_cofins_decreto")
	private String prodCofinsDecreto;

	@Column(name = "prod_cofins_aliquota")
	private Double prodCofinsAliquota;

	@Column(name = "prod_cofins_nome")
	private String prodCofinsNome;

	@Column(name = "prod_cofins_cst_entrada")
	private String prodCofinsCstEntrada;

	@Column(name = "prod_cofins_cst_saida")
	private String prodCofinsCstSaida;
	
	public ProdCofins() {
		this(0);
	}

	public ProdCofins(int prodCofinsId) {
		super("pu_produto", "ProdCofins", "prodCofinsId", "prodCofinsCstEntrada");
		this.prodCofinsId = prodCofinsId;
	}

	public int getProdCofinsId() {
		return prodCofinsId;
	}

	public void setProdCofinsId(int prodCofinsId) {
		this.prodCofinsId = prodCofinsId;
	}

	public String getProdCofinsDecreto() {
		return prodCofinsDecreto;
	}

	public void setProdCofinsDecreto(String prodCofinsDecreto) {
		this.prodCofinsDecreto = prodCofinsDecreto;
	}

	public Double getProdCofinsAliquota() {
		return prodCofinsAliquota;
	}

	public void setProdCofinsAliquota(Double prodCofinsAliquota) {
		this.prodCofinsAliquota = prodCofinsAliquota;
	}

	public String getProdCofinsNome() {
		return prodCofinsNome;
	}

	public void setProdCofinsNome(String prodCofinsNome) {
		this.prodCofinsNome = prodCofinsNome;
	}

	public String getProdCofinsCstEntrada() {
		return prodCofinsCstEntrada;
	}

	public void setProdCofinsCstEntrada(String prodCofinsCstEntrada) {
		this.prodCofinsCstEntrada = prodCofinsCstEntrada;
	}

	public String getProdCofinsCstSaida() {
		return prodCofinsCstSaida;
	}

	public void setProdCofinsCstSaida(String prodCofinsCstSaida) {
		this.prodCofinsCstSaida = prodCofinsCstSaida;
	}

	public Number getId() {
		return this.prodCofinsId;
	}

	public void setId(Number id) {
		this.prodCofinsId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodCofinsId + "", prodCofinsNome, prodCofinsCstEntrada, prodCofinsCstSaida, prodCofinsAliquota + "", prodCofinsDecreto };
	}

	public void anularDependencia() {
	}
}