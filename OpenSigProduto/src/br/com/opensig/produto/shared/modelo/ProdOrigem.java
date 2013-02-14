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
 * Classe que representa um origem de produtos no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name="prod_origem")
public class ProdOrigem extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="prod_origem_id")
	private int prodOrigemId;

	@Column(name="prod_origem_valor")
	private int prodOrigemValor;
	
	@Column(name="prod_origem_descricao")
	private String prodOrigemDescricao;

    public ProdOrigem() {
    	this(0);
    }
    
	public ProdOrigem(int prodOrigemId) {
		super("pu_produto", "ProdOrigem", "prodOrigemId", "prodOrigemValor");
		this.prodOrigemId = prodOrigemId;
	}

	public int getProdOrigemId() {
		return this.prodOrigemId;
	}

	public void setProdOrigemId(int prodOrigemId) {
		this.prodOrigemId = prodOrigemId;
	}

	public int getProdOrigemValor() {
		return prodOrigemValor;
	}

	public void setProdOrigemValor(int prodOrigemValor) {
		this.prodOrigemValor = prodOrigemValor;
	}

	public String getProdOrigemDescricao() {
		return this.prodOrigemDescricao;
	}

	public void setProdOrigemDescricao(String prodOrigemDescricao) {
		this.prodOrigemDescricao = prodOrigemDescricao;
	}

	public Number getId() {
		return prodOrigemId;
	}

	public void setId(Number id) {
		prodOrigemId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodOrigemId + "", prodOrigemValor + "", prodOrigemDescricao };
	}
	
}