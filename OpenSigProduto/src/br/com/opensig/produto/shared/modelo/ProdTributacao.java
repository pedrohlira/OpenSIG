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
 * Classe que representa uma tributação no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 16/07/2009
 */
@Entity
@Table(name = "prod_tributacao")
public class ProdTributacao extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_tributacao_id")
	private int prodTributacaoId;

	@Column(name = "prod_tributacao_decreto")
	private String prodTributacaoDecreto;

	@Column(name = "prod_tributacao_dentro")
	private Double prodTributacaoDentro;

	@Column(name = "prod_tributacao_fora")
	private Double prodTributacaoFora;

	@Column(name = "prod_tributacao_nome")
	private String prodTributacaoNome;

	@Column(name = "prod_tributacao_cfop")
	private int prodTributacaoCfop;

	@Column(name = "prod_tributacao_cst")
	private String prodTributacaoCst;

	@Column(name = "prod_tributacao_cson")
	private String prodTributacaoCson;

	@Column(name = "prod_tributacao_ecf")
	private String prodTributacaoEcf;

	public ProdTributacao() {
		this(0);
	}

	public ProdTributacao(int prodTributacaoId) {
		super("pu_produto", "ProdTributacao", "prodTributacaoId", "prodTributacaoNome");
		this.prodTributacaoId = prodTributacaoId;
	}

	public int getProdTributacaoId() {
		return this.prodTributacaoId;
	}

	public void setProdTributacaoId(int prodTributacaoId) {
		this.prodTributacaoId = prodTributacaoId;
	}

	public String getProdTributacaoDecreto() {
		return this.prodTributacaoDecreto;
	}

	public void setProdTributacaoDecreto(String prodTributacaoDecreto) {
		this.prodTributacaoDecreto = prodTributacaoDecreto;
	}

	public double getProdTributacaoDentro() {
		return this.prodTributacaoDentro;
	}

	public void setProdTributacaoDentro(double prodTributacaoDentro) {
		this.prodTributacaoDentro = prodTributacaoDentro;
	}

	public double getProdTributacaoFora() {
		return this.prodTributacaoFora;
	}

	public void setProdTributacaoFora(double prodTributacaoFora) {
		this.prodTributacaoFora = prodTributacaoFora;
	}

	public String getProdTributacaoNome() {
		return this.prodTributacaoNome;
	}

	public void setProdTributacaoNome(String prodTributacaoNome) {
		this.prodTributacaoNome = prodTributacaoNome;
	}

	public int getProdTributacaoCfop() {
		return prodTributacaoCfop;
	}

	public void setProdTributacaoCfop(int prodTributacaoCfop) {
		this.prodTributacaoCfop = prodTributacaoCfop;
	}

	public String getProdTributacaoCst() {
		return prodTributacaoCst;
	}

	public void setProdTributacaoCst(String prodTributacaoCst) {
		this.prodTributacaoCst = prodTributacaoCst;
	}

	public String getProdTributacaoCson() {
		return prodTributacaoCson;
	}

	public void setProdTributacaoCson(String prodTributacaoCson) {
		this.prodTributacaoCson = prodTributacaoCson;
	}

	public String getProdTributacaoEcf() {
		return prodTributacaoEcf;
	}

	public void setProdTributacaoEcf(String prodTributacaoEcf) {
		this.prodTributacaoEcf = prodTributacaoEcf;
	}

	public Number getId() {
		return prodTributacaoId;
	}

	public void setId(Number id) {
		prodTributacaoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodTributacaoId + "", prodTributacaoNome, prodTributacaoCst, prodTributacaoCson, prodTributacaoCfop + "", prodTributacaoEcf, prodTributacaoDentro + "",
				prodTributacaoFora + "", prodTributacaoDecreto };
	}

	public void anularDependencia() {
	}
}