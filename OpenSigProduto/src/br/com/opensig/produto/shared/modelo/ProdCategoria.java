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
 * Classe que representa uma categoria do produto no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 16/07/2009
 */
@Entity
@Table(name = "prod_categoria")
public class ProdCategoria extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_categoria_id")
	private int prodCategoriaId;

	@Column(name = "prod_categoria_descricao")
	private String prodCategoriaDescricao;

	public ProdCategoria() {
		this(0);
	}

	public ProdCategoria(int prodCategoriaId) {
		super("pu_produto", "ProdCategoria", "prodCategoriaId", "prodCategoriaDescricao");
		this.prodCategoriaId = prodCategoriaId;
	}

	public int getProdCategoriaId() {
		return this.prodCategoriaId;
	}

	public void setProdCategoriaId(int prodCategoriaId) {
		this.prodCategoriaId = prodCategoriaId;
	}

	public String getProdCategoriaDescricao() {
		return this.prodCategoriaDescricao;
	}

	public void setProdCategoriaDescricao(String prodCategoriaDescricao) {
		this.prodCategoriaDescricao = prodCategoriaDescricao;
	}

	public Number getId() {
		return prodCategoriaId;
	}

	public void setId(Number id) {
		prodCategoriaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodCategoriaId + "", prodCategoriaDescricao };
	}
	
	public String toString(){
		return prodCategoriaDescricao;
	}
}