package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;

import java.util.Date;

/**
 * Classe que representa um receber no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_receber")
@XmlRootElement
public class PokerReceber extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_receber_id")
	private int pokerReceberId;

	@Column(name = "poker_receber_ativo")
	private int pokerReceberAtivo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "poker_receber_cadastrado")
	private Date pokerReceberCadastrado;

	@Column(name = "poker_receber_descricao")
	private String pokerReceberDescricao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "poker_receber_realizado")
	private Date pokerReceberRealizado;

	@Column(name = "poker_receber_valor")
	private double pokerReceberValor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_forma_id")
	private PokerForma pokerForma;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_cash_id")
	private PokerCash pokerCash;

	public PokerReceber() {
		this(0);
	}

	public PokerReceber(int pokerReceberId) {
		super("pu_poker", "PokerReceber", "pokerReceberId", "pokerReceberCadastrado", EDirecao.DESC);
		this.pokerReceberId = pokerReceberId;
	}

	public int getPokerReceberId() {
		return this.pokerReceberId;
	}

	public void setPokerReceberId(int pokerReceberId) {
		this.pokerReceberId = pokerReceberId;
	}

	public boolean getPokerReceberAtivo() {
		return this.pokerReceberAtivo == 0 ? false : true;
	}

	public void setPokerReceberAtivo(boolean pokerReceberAtivo) {
		this.pokerReceberAtivo = pokerReceberAtivo == false ? 0 : 1;
	}

	public Date getPokerReceberCadastrado() {
		return this.pokerReceberCadastrado;
	}

	public void setPokerReceberCadastrado(Date pokerReceberCadastrado) {
		this.pokerReceberCadastrado = pokerReceberCadastrado;
	}

	public String getPokerReceberDescricao() {
		return this.pokerReceberDescricao;
	}

	public void setPokerReceberDescricao(String pokerReceberDescricao) {
		this.pokerReceberDescricao = pokerReceberDescricao;
	}

	public Date getPokerReceberRealizado() {
		return this.pokerReceberRealizado;
	}

	public void setPokerReceberRealizado(Date pokerReceberRealizado) {
		this.pokerReceberRealizado = pokerReceberRealizado;
	}

	public double getPokerReceberValor() {
		return this.pokerReceberValor;
	}

	public void setPokerReceberValor(double pokerReceberValor) {
		this.pokerReceberValor = pokerReceberValor;
	}

	public PokerCash getPokerCash() {
		return pokerCash;
	}

	public void setPokerCash(PokerCash pokerCash) {
		this.pokerCash = pokerCash;
	}

	public PokerForma getPokerForma() {
		return this.pokerForma;
	}

	public void setPokerForma(PokerForma pokerForma) {
		this.pokerForma = pokerForma;
	}

	public Number getId() {
		return pokerReceberId;
	}

	public void setId(Number id) {
		pokerReceberId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerReceberId + "", pokerForma.getPokerFormaId() + "", pokerForma.getPokerFormaNome(), pokerCash.getPokerCashId() + "", pokerCash.getPokerCashMesa(),
				pokerReceberDescricao, pokerReceberValor + "", UtilClient.getDataHoraGrid(pokerReceberCadastrado), UtilClient.getDataHoraGrid(pokerReceberRealizado), getPokerReceberAtivo() + "" };
	}

	public void anularDependencia() {
		pokerForma = null;
		pokerCash = null;
	}
}