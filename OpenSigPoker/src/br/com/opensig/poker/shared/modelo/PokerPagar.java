package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;

/**
 * Classe que representa um pagar no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_pagar")
@XmlRootElement
public class PokerPagar extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_pagar_id")
	private int pokerPagarId;

	@Column(name = "poker_pagar_ativo")
	private int pokerPagarAtivo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "poker_pagar_cadastrado")
	private Date pokerPagarCadastrado;

	@Column(name = "poker_pagar_descricao")
	private String pokerPagarDescricao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "poker_pagar_realizado")
	private Date pokerPagarRealizado;

	@Column(name = "poker_pagar_valor")
	private double pokerPagarValor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_forma_id")
	private PokerForma pokerForma;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_cash_id")
	private PokerCash pokerCash;

	public PokerPagar() {
		this(0);
	}

	public PokerPagar(int pokerPagarId) {
		super("pu_poker", "PokerPagar", "pokerPagarId", "pokerPagarCadastrado", EDirecao.DESC);
		this.pokerPagarId = pokerPagarId;
	}

	public int getPokerPagarId() {
		return this.pokerPagarId;
	}

	public void setPokerPagarId(int pokerPagarId) {
		this.pokerPagarId = pokerPagarId;
	}

	public boolean getPokerPagarAtivo() {
		return this.pokerPagarAtivo == 0 ? false : true;
	}

	public void setPokerPagarAtivo(boolean pokerPagarAtivo) {
		this.pokerPagarAtivo = pokerPagarAtivo == false ? 0 : 1;
	}

	public Date getPokerPagarCadastrado() {
		return this.pokerPagarCadastrado;
	}

	public void setPokerPagarCadastrado(Date pokerPagarCadastrado) {
		this.pokerPagarCadastrado = pokerPagarCadastrado;
	}

	public String getPokerPagarDescricao() {
		return this.pokerPagarDescricao;
	}

	public void setPokerPagarDescricao(String pokerPagarDescricao) {
		this.pokerPagarDescricao = pokerPagarDescricao;
	}

	public Date getPokerPagarRealizado() {
		return this.pokerPagarRealizado;
	}

	public void setPokerPagarRealizado(Date pokerPagarRealizado) {
		this.pokerPagarRealizado = pokerPagarRealizado;
	}

	public double getPokerPagarValor() {
		return this.pokerPagarValor;
	}

	public void setPokerPagarValor(double pokerPagarValor) {
		this.pokerPagarValor = pokerPagarValor;
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
		return pokerPagarId;
	}

	public void setId(Number id) {
		pokerPagarId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerPagarId + "", pokerForma.getPokerFormaId() + "", pokerForma.getPokerFormaNome(), pokerCash.getPokerCashId() + "", pokerCash.getPokerCashMesa(), pokerPagarDescricao,
				pokerPagarValor + "", UtilClient.getDataHoraGrid(pokerPagarCadastrado), UtilClient.getDataHoraGrid(pokerPagarRealizado), getPokerPagarAtivo() + "" };
	}

	public void anularDependencia() {
		pokerForma = null;
		pokerCash = null;
	}
}