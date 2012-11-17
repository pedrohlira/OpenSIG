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
 * Classe que representa um banco no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 18/11/2009
 */
@Entity
@Table(name = "fin_banco")
public class FinBanco extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_banco_id")
	private int finBancoId;

	@Column(name = "fin_banco_numero")
	private String finBancoNumero;

	@Column(name = "fin_banco_descricao")
	private String finBancoDescricao;

	public FinBanco() {
		this(0);
	}

	public FinBanco(int finBancoId) {
		super("pu_financeiro", "FinBanco", "finBancoId", "finBancoDescricao");
		this.finBancoId = finBancoId;
	}

	public int getFinBancoId() {
		return finBancoId;
	}

	public void setFinBancoId(int finBancoId) {
		this.finBancoId = finBancoId;
	}

	public String getFinBancoNumero() {
		return finBancoNumero;
	}

	public void setFinBancoNumero(String finBancoNumero) {
		this.finBancoNumero = finBancoNumero;
	}

	public String getFinBancoDescricao() {
		return finBancoDescricao;
	}

	public void setFinBancoDescricao(String finBancoDescricao) {
		this.finBancoDescricao = finBancoDescricao;
	}

	public Number getId() {
		return finBancoId;
	}

	public void setId(Number id) {
		finBancoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { finBancoId + "", finBancoNumero, finBancoDescricao };
	}
}
