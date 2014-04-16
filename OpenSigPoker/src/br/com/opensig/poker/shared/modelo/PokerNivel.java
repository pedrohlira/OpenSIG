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
 * Classe que representa um nivel no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_nivel")
public class PokerNivel extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_nivel_id")
	private int pokerNivelId;

	@Column(name = "poker_nivel_ante")
	private int pokerNivelAnte;

	@Column(name = "poker_nivel_grande")
	private int pokerNivelGrande;

	@Column(name = "poker_nivel_numero")
	private int pokerNivelNumero;

	@Column(name = "poker_nivel_pequeno")
	private int pokerNivelPequeno;

	@Column(name = "poker_nivel_tempo")
	private int pokerNivelTempo;

	@Column(name = "poker_nivel_espera")
	private int pokerNivelEspera;

	@Column(name = "poker_nivel_ativo")
	private int pokerNivelAtivo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_torneio_id")
	@XmlInverseReference(mappedBy = "pokerNiveis")
	private PokerTorneio pokerTorneio;

	public PokerNivel() {
		this(0);
	}

	public PokerNivel(int pokerNivelId) {
		super("pu_poker", "PokerNivel", "pokerNivelId", "pokerNivelNumero");
		this.pokerNivelId = pokerNivelId;
	}

	public int getPokerNivelId() {
		return this.pokerNivelId;
	}

	public void setPokerNivelId(int pokerNivelId) {
		this.pokerNivelId = pokerNivelId;
	}

	public int getPokerNivelAnte() {
		return this.pokerNivelAnte;
	}

	public void setPokerNivelAnte(int pokerNivelAnte) {
		this.pokerNivelAnte = pokerNivelAnte;
	}

	public int getPokerNivelGrande() {
		return this.pokerNivelGrande;
	}

	public void setPokerNivelGrande(int pokerNivelGrande) {
		this.pokerNivelGrande = pokerNivelGrande;
	}

	public int getPokerNivelNumero() {
		return this.pokerNivelNumero;
	}

	public void setPokerNivelNumero(int pokerNivelNumero) {
		this.pokerNivelNumero = pokerNivelNumero;
	}

	public int getPokerNivelPequeno() {
		return this.pokerNivelPequeno;
	}

	public void setPokerNivelPequeno(int pokerNivelPequeno) {
		this.pokerNivelPequeno = pokerNivelPequeno;
	}

	public int getPokerNivelTempo() {
		return this.pokerNivelTempo;
	}

	public void setPokerNivelTempo(int pokerNivelTempo) {
		this.pokerNivelTempo = pokerNivelTempo;
	}

	public int getPokerNivelEspera() {
		return pokerNivelEspera;
	}

	public void setPokerNivelEspera(int pokerNivelEspera) {
		this.pokerNivelEspera = pokerNivelEspera;
	}

	public boolean getPokerNivelAtivo() {
		return this.pokerNivelAtivo == 0 ? false : true;
	}

	public void setPokerNivelAtivo(boolean pokerNivelAtivo) {
		this.pokerNivelAtivo = pokerNivelAtivo == false ? 0 : 1;
	}

	public PokerTorneio getPokerTorneio() {
		return this.pokerTorneio;
	}

	public void setPokerTorneio(PokerTorneio pokerTorneio) {
		this.pokerTorneio = pokerTorneio;
	}

	public Number getId() {
		return pokerNivelId;
	}

	public void setId(Number id) {
		pokerNivelId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerNivelId + "", pokerNivelNumero + "", pokerNivelPequeno + "", pokerNivelGrande + "", pokerNivelAnte + "", pokerNivelTempo + "", pokerNivelEspera + "",
				getPokerNivelAtivo() + "" };
	}

	public void anularDependencia() {
		pokerTorneio = null;
	}
}