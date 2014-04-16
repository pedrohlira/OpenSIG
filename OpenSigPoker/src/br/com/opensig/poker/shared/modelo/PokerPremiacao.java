package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma premiacao no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_premiacao")
public class PokerPremiacao extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_premiacao_id")
	private int pokerPremiacaoId;

	@Column(name = "poker_premiacao_ponto")
	private int pokerPremiacaoPonto;

	@Column(name = "poker_premiacao_posicao")
	private int pokerPremiacaoPosicao;

	@Column(name = "poker_premiacao_valor")
	private double pokerPremiacaoValor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_torneio_id")
	@XmlInverseReference(mappedBy = "pokerPremiacoes")
	private PokerTorneio pokerTorneio;

	public PokerPremiacao() {
		this(0);
	}

	public PokerPremiacao(int pokerPremiacaoId) {
		super("pu_poker", "PokerPremiacao", "pokerPremiacaoId", "pokerPremiacaoPosicao");
		this.pokerPremiacaoId = pokerPremiacaoId;
	}

	public int getPokerPremiacaoId() {
		return this.pokerPremiacaoId;
	}

	public void setPokerPremiacaoId(int pokerPremiacaoId) {
		this.pokerPremiacaoId = pokerPremiacaoId;
	}

	public int getPokerPremiacaoPonto() {
		return this.pokerPremiacaoPonto;
	}

	public void setPokerPremiacaoPonto(int pokerPremiacaoPonto) {
		this.pokerPremiacaoPonto = pokerPremiacaoPonto;
	}

	public int getPokerPremiacaoPosicao() {
		return this.pokerPremiacaoPosicao;
	}

	public void setPokerPremiacaoPosicao(int pokerPremiacaoPosicao) {
		this.pokerPremiacaoPosicao = pokerPremiacaoPosicao;
	}

	public double getPokerPremiacaoValor() {
		return this.pokerPremiacaoValor;
	}

	public void setPokerPremiacaoValor(double pokerPremiacaoValor) {
		this.pokerPremiacaoValor = pokerPremiacaoValor;
	}

	public PokerTorneio getPokerTorneio() {
		return this.pokerTorneio;
	}

	public void setPokerTorneio(PokerTorneio pokerTorneio) {
		this.pokerTorneio = pokerTorneio;
	}

	public Number getId() {
		return pokerPremiacaoId;
	}

	public void setId(Number id) {
		pokerPremiacaoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerPremiacaoId + "", pokerPremiacaoPosicao + "", pokerPremiacaoValor + "", pokerPremiacaoPonto + "" };
	}

	public void anularDependencia() {
		pokerTorneio = null;
	}

}