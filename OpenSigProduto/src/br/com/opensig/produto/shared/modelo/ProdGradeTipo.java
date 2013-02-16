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
 * Classe que representa um sub tipo de produto no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "prod_grade_tipo")
public class ProdGradeTipo extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_grade_tipo_id")
	private int prodGradeTipoId;

	@Column(name = "prod_grade_tipo_nome")
	private String prodGradeTipoNome;

	@Column(name = "prod_grade_tipo_opcao")
	private String prodGradeTipoOpcao;

	public ProdGradeTipo() {
		this(0);
	}

	public ProdGradeTipo(int prodGradeTipoId) {
		super("pu_produto", "ProdGradeTipo", "prodGradeTipoId", "prodGradeTipoNome");
		this.prodGradeTipoId = prodGradeTipoId;
	}

	public int getProdGradeTipoId() {
		return prodGradeTipoId;
	}

	public void setProdGradeTipoId(int prodGradeTipoId) {
		this.prodGradeTipoId = prodGradeTipoId;
	}

	public String getProdGradeTipoNome() {
		return prodGradeTipoNome;
	}

	public void setProdGradeTipoNome(String prodGradeTipoNome) {
		this.prodGradeTipoNome = prodGradeTipoNome;
	}

	public String getProdGradeTipoOpcao() {
		return prodGradeTipoOpcao;
	}

	public void setProdGradeTipoOpcao(String prodGradeTipoOpcao) {
		this.prodGradeTipoOpcao = prodGradeTipoOpcao;
	}

	@Override
	public Number getId() {
		return prodGradeTipoId;
	}

	@Override
	public void setId(Number id) {
		this.prodGradeTipoId = id.intValue();
	}

	@Override
	public String[] toArray() {
		return new String[] { prodGradeTipoId + "", prodGradeTipoNome, prodGradeTipoOpcao };
	}

}
