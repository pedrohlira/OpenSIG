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
import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um jackpot no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_jackpot")
@XmlRootElement
public class PokerJackpot extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_jackpot_id")
	private int pokerJackpotId;

	@Column(name = "poker_jackpot_total")
	private double pokerJackpotTotal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_forma_id")
	private PokerForma pokerForma;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_pagar_id")
	private PokerPagar pokerPagar;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_receber_id")
	private PokerReceber pokerReceber;

	public PokerJackpot() {
		this(0);
	}

	public PokerJackpot(int pokerJackpotId) {
		super("pu_poker", "PokerJackpot", "pokerJackpotId", "pokerForma.pokerFormaNome");
		this.pokerJackpotId = pokerJackpotId;
	}

	public int getPokerJackpotId() {
		return this.pokerJackpotId;
	}

	public void setPokerJackpotId(int pokerJackpotId) {
		this.pokerJackpotId = pokerJackpotId;
	}

	public double getPokerJackpotTotal() {
		return this.pokerJackpotTotal;
	}

	public void setPokerJackpotTotal(double pokerJackpotTotal) {
		this.pokerJackpotTotal = pokerJackpotTotal;
	}

	public PokerForma getPokerForma() {
		return pokerForma;
	}

	public void setPokerForma(PokerForma pokerForma) {
		this.pokerForma = pokerForma;
	}

	public PokerPagar getPokerPagar() {
		return pokerPagar;
	}

	public void setPokerPagar(PokerPagar pokerPagar) {
		this.pokerPagar = pokerPagar;
	}

	public PokerReceber getPokerReceber() {
		return pokerReceber;
	}

	public void setPokerReceber(PokerReceber pokerReceber) {
		this.pokerReceber = pokerReceber;
	}

	public Number getId() {
		return pokerJackpotId;
	}

	public void setId(Number id) {
		pokerJackpotId = id.intValue();
	}

	public String[] toArray() {
		int rId = pokerReceber == null ? 0 : pokerReceber.getPokerReceberId();
		double rValor = pokerReceber == null ? 0 : pokerReceber.getPokerReceberValor();
		int pId = pokerPagar == null ? 0 : pokerPagar.getPokerPagarId();
		double pValor = pokerPagar == null ? 0 : pokerPagar.getPokerPagarValor();

		return new String[] { pokerJackpotId + "", pokerForma.getPokerFormaId() + "", pokerForma.getPokerFormaNome(), rId + "", rValor + "", pId + "", pValor + "", pokerJackpotTotal + "" };
	}

	public void anularDependencia() {
		pokerForma = null;
		pokerPagar = null;
		pokerReceber = null;
	}
}