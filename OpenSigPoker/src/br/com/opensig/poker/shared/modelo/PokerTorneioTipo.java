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
 * Classe que representa um tipo de torneio no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_torneio_tipo")
@XmlRootElement
public class PokerTorneioTipo extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_torneio_tipo_id")
	private int pokerTorneioTipoId;

	@Column(name = "poker_torneio_tipo_nome")
	private String pokerTorneioTipoNome;

	public PokerTorneioTipo() {
		this(0);
	}

	public PokerTorneioTipo(int pokerTorneioTipoId) {
		super("pu_poker", "PokerTorneioTipo", "pokerTorneioTipoId", "pokerTorneioTipoNome");
		this.pokerTorneioTipoId = pokerTorneioTipoId;
	}

	public int getPokerTorneioTipoId() {
		return this.pokerTorneioTipoId;
	}

	public void setPokerTorneioTipoId(int pokerTorneioTipoId) {
		this.pokerTorneioTipoId = pokerTorneioTipoId;
	}

	public String getPokerTorneioTipoNome() {
		return this.pokerTorneioTipoNome;
	}

	public void setPokerTorneioTipoNome(String pokerTorneioTipoNome) {
		this.pokerTorneioTipoNome = pokerTorneioTipoNome;
	}

	public Number getId() {
		return pokerTorneioTipoId;
	}

	public void setId(Number id) {
		pokerTorneioTipoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerTorneioTipoId + "", pokerTorneioTipoNome };
	}

}