package br.com.opensig.financeiro.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma categoria do financeiro no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 16/07/2009
 */
@Entity
@Table(name = "fin_categoria")
public class FinCategoria extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_categoria_id")
	private int finCategoriaId;

	@Column(name = "fin_categoria_descricao")
	private String finCategoriaDescricao;
	
	public FinCategoria() {
		this(0);
	}

	public FinCategoria(int finCategoriaId) {
		super("pu_financeiro", "FinCategoria", "finCategoriaId", "finCategoriaDescricao");
		this.finCategoriaId = finCategoriaId;
	}

	public int getFinCategoriaId() {
		return finCategoriaId;
	}

	public void setFinCategoriaId(int finCategoriaId) {
		this.finCategoriaId = finCategoriaId;
	}

	public String getFinCategoriaDescricao() {
		return finCategoriaDescricao;
	}

	public void setFinCategoriaDescricao(String finCategoriaDescricao) {
		this.finCategoriaDescricao = finCategoriaDescricao;
	}
	
	public Number getId() {
		return finCategoriaId;
	}

	public void setId(Number id) {
		finCategoriaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { finCategoriaId + "", finCategoriaDescricao };
	}

	public String toString(){
		return finCategoriaDescricao;
	}
}