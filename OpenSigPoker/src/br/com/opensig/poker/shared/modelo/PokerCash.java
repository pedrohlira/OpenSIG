package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;

/**
 * Classe que representa um turno de cash no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_cash")
@XmlRootElement
public class PokerCash extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_cash_id")
	private int pokerCashId;

	@Column(name = "poker_cash_codigo")
	private String pokerCashCodigo;

	@Temporal(TemporalType.DATE)
	@Column(name = "poker_cash_data")
	private Date pokerCashData;

	@Column(name = "poker_cash_fechado")
	private int pokerCashFechado;

	@Column(name = "poker_cash_pago")
	private double pokerCashPago;

	@Column(name = "poker_cash_recebido")
	private double pokerCashRecebido;

	@Column(name = "poker_cash_saldo")
	private double pokerCashSaldo;

	@OneToMany(mappedBy = "pokerCash", cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<PokerJogador> pokerJogadores;

	public PokerCash() {
		this(0);
	}

	public PokerCash(int pokerCashId) {
		super("pu_poker", "PokerCash", "pokerCashId", "pokerCashData", EDirecao.DESC);
		this.pokerCashId = pokerCashId;
	}

	public int getPokerCashId() {
		return this.pokerCashId;
	}

	public void setPokerCashId(int pokerCashId) {
		this.pokerCashId = pokerCashId;
	}

	public String getPokerCashCodigo() {
		return pokerCashCodigo;
	}

	public void setPokerCashCodigo(String pokerCashCodigo) {
		this.pokerCashCodigo = pokerCashCodigo;
	}

	public Date getPokerCashData() {
		return this.pokerCashData;
	}

	public void setPokerCashData(Date pokerCashData) {
		this.pokerCashData = pokerCashData;
	}

	public boolean getPokerCashFechado() {
		return this.pokerCashFechado == 0 ? false : true;
	}

	public void setPokerCashFechado(boolean pokerCashFechado) {
		this.pokerCashFechado = pokerCashFechado == false ? 0 : 1;
	}

	public double getPokerCashPago() {
		return pokerCashPago;
	}

	public void setPokerCashPago(double pokerCashPago) {
		this.pokerCashPago = pokerCashPago;
	}

	public double getPokerCashRecebido() {
		return pokerCashRecebido;
	}

	public void setPokerCashRecebido(double pokerCashRecebido) {
		this.pokerCashRecebido = pokerCashRecebido;
	}

	public double getPokerCashSaldo() {
		return pokerCashSaldo;
	}

	public void setPokerCashSaldo(double pokerCashSaldo) {
		this.pokerCashSaldo = pokerCashSaldo;
	}

	public List<PokerJogador> getPokerJogadores() {
		return pokerJogadores;
	}

	public void setPokerJogadores(List<PokerJogador> pokerJogadores) {
		this.pokerJogadores = pokerJogadores;
	}

	public Number getId() {
		return pokerCashId;
	}

	public void setId(Number id) {
		pokerCashId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerCashId + "", pokerCashCodigo, UtilClient.getDataGrid(pokerCashData), pokerCashPago + "", pokerCashRecebido + "", pokerCashSaldo + "", getPokerCashFechado() + "" };
	}

	public void anularDependencia() {
		pokerJogadores = null;
	}

}