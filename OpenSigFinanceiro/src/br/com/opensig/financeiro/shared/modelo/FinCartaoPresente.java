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
 * Classe que representa um cartao presente no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "fin_cartao_presente")
public class FinCartaoPresente extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_cartao_presente_id")
	private int finCartaoPresenteId;

	@Column(name = "fin_cartao_presente_ativo")
	private int finCartaoPresenteAtivo;

	@Column(name = "fin_cartao_presente_numero")
	private String finCartaoPresenteNumero;

	@Column(name = "fin_cartao_presente_valor")
	private Double finCartaoPresenteValor;

	public FinCartaoPresente() {
		this(0);
	}

	public FinCartaoPresente(int finCartaoPresenteId) {
		super("pu_financeiro", "FinCartaoPresente", "finCartaoPresenteId", "finCartaoPresenteNumero");
		this.finCartaoPresenteId = finCartaoPresenteId;
	}

	public int getFinCartaoPresenteId() {
		return this.finCartaoPresenteId;
	}

	public void setFinCartaoPresenteId(int finCartaoPresenteId) {
		this.finCartaoPresenteId = finCartaoPresenteId;
	}

	public boolean getFinCartaoPresenteAtivo() {
		return this.finCartaoPresenteAtivo == 0 ? false : true;
	}

	public void setFinCartaoPresenteAtivo(boolean finCartaoPresenteAtivo) {
		this.finCartaoPresenteAtivo = finCartaoPresenteAtivo == false ? 0 : 1;
	}

	public String getFinCartaoPresenteNumero() {
		return this.finCartaoPresenteNumero;
	}

	public void setFinCartaoPresenteNumero(String finCartaoPresenteNumero) {
		this.finCartaoPresenteNumero = finCartaoPresenteNumero;
	}

	public Double getFinCartaoPresenteValor() {
		return this.finCartaoPresenteValor;
	}

	public void setFinCartaoPresenteValor(Double finCartaoPresenteValor) {
		this.finCartaoPresenteValor = finCartaoPresenteValor;
	}

	public Number getId() {
		return finCartaoPresenteId;
	}

	public void setId(Number id) {
		finCartaoPresenteId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { finCartaoPresenteId + "", finCartaoPresenteNumero, finCartaoPresenteValor.toString(), getFinCartaoPresenteAtivo() + "" };
	}
}