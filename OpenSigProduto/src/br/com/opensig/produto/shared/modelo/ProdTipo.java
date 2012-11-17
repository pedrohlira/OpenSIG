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
 * Classe que representa um tipo de produto no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/03/2012
 */
@Entity
@Table(name = "prod_tipo")
public class ProdTipo extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_tipo_id")
	private int prodTipoId;

	@Column(name = "prod_tipo_valor")
	private String prodTipoValor;

	@Column(name = "prod_tipo_descricao")
	private String prodTipoDescricao;

	public ProdTipo() {
		this(0);
	}

	public ProdTipo(int prodTipoId) {
		super("pu_produto", "ProdTipo", "prodTipoId", "prodTipoValor");
		this.prodTipoId = prodTipoId;
	}

	public int getProdTipoId() {
		return prodTipoId;
	}

	public void setProdTipoId(int prodTipoId) {
		this.prodTipoId = prodTipoId;
	}

	public String getProdTipoValor() {
		return prodTipoValor;
	}

	public void setProdTipoValor(String prodTipoValor) {
		this.prodTipoValor = prodTipoValor;
	}

	public String getProdTipoDescricao() {
		return prodTipoDescricao;
	}

	public void setProdTipoDescricao(String prodTipoDescricao) {
		this.prodTipoDescricao = prodTipoDescricao;
	}

	public Number getId() {
		return this.prodTipoId;
	}

	public void setId(Number id) {
		this.prodTipoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { prodTipoId + "", prodTipoValor, prodTipoDescricao };
	}

	public void anularDependencia() {
	}
	
	@Override
	public String toString() {
		return prodTipoValor;
	}
}