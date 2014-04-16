package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma forma de pagar/receber no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_forma")
@XmlRootElement
public class PokerForma extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_forma_id")
	private int pokerFormaId;

	@Column(name = "poker_forma_nome")
	private String pokerFormaNome;

	@Column(name = "poker_forma_realizado")
	private int pokerFormaRealizado;

	@Column(name = "poker_forma_jackpot")
	private int pokerFormaJackpot;
	
	public PokerForma() {
		this(0);
	}

	public PokerForma(int pokerFormaId) {
		super("pu_poker", "PokerForma", "pokerFormaId", "pokerFormaNome");
		this.pokerFormaId = pokerFormaId;
	}

	public int getPokerFormaId() {
		return this.pokerFormaId;
	}

	public void setPokerFormaId(int pokerFormaId) {
		this.pokerFormaId = pokerFormaId;
	}

	public String getPokerFormaNome() {
		return pokerFormaNome;
	}

	public void setPokerFormaNome(String pokerFormaNome) {
		this.pokerFormaNome = pokerFormaNome;
	}

	public boolean getPokerFormaRealizado() {
		return this.pokerFormaRealizado == 0 ? false : true;
	}

	public void setPokerFormaRealizado(boolean pokerFormaRealizado) {
		this.pokerFormaRealizado = pokerFormaRealizado == false ? 0 : 1;
	}
	
	public boolean getPokerFormaJackpot() {
		return this.pokerFormaJackpot == 0 ? false : true;
	}

	public void setPokerFormaJackpot(boolean pokerFormaJackpot) {
		this.pokerFormaJackpot = pokerFormaJackpot == false ? 0 : 1;
	}

	public Number getId() {
		return pokerFormaId;
	}

	public void setId(Number id) {
		pokerFormaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerFormaId + "", pokerFormaNome, getPokerFormaRealizado() + "", getPokerFormaJackpot() + "" };
	}
}