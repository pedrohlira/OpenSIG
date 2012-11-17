package br.com.opensig.produto.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa a embalagem de preco no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 28/07/2009
 */
@Entity
@Table(name = "prod_embalagem")
@XmlRootElement
public class ProdEmbalagem extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_embalagem_id")
	private int prodEmbalagemId;

	@Column(name = "prod_embalagem_nome")
	private String prodEmbalagemNome;

	@Column(name = "prod_embalagem_unidade")
	private int prodEmbalagemUnidade;

	@Column(name = "prod_embalagem_descricao")
	private String prodEmbalagemDescricao;

	public ProdEmbalagem() {
		this(0);
	}

	public ProdEmbalagem(int prodEmbalagemId) {
		super("pu_produto", "ProdEmbalagem", "prodEmbalagemId", "prodEmbalagemNome");
		this.prodEmbalagemId = prodEmbalagemId;
	}

	public int getProdEmbalagemId() {
		return this.prodEmbalagemId;
	}

	public void setProdEmbalagemId(int prodEmbalagemId) {
		this.prodEmbalagemId = prodEmbalagemId;
	}

	public String getProdEmbalagemNome() {
		return prodEmbalagemNome;
	}

	public void setProdEmbalagemNome(String prodEmbalagemNome) {
		this.prodEmbalagemNome = prodEmbalagemNome;
	}

	public int getProdEmbalagemUnidade() {
		return prodEmbalagemUnidade;
	}

	public void setProdEmbalagemUnidade(int prodEmbalagemUnidade) {
		this.prodEmbalagemUnidade = prodEmbalagemUnidade;
	}

	public String getProdEmbalagemDescricao() {
		return this.prodEmbalagemDescricao;
	}

	public void setProdEmbalagemDescricao(String prodEmbalagemDescricao) {
		this.prodEmbalagemDescricao = prodEmbalagemDescricao;
	}

	public Number getId() {
		return prodEmbalagemId;
	}

	public void setId(Number id) {
		prodEmbalagemId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodEmbalagemId + "", prodEmbalagemNome, prodEmbalagemUnidade + "", prodEmbalagemDescricao };
	}

	public void anularDependencia() {
	}
}