package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma mesa no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_mesa")
@XmlRootElement
public class PokerMesa extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_mesa_id")
	private int pokerMesaId;

	@Column(name = "poker_mesa_ativo")
	@XmlElement(type = Boolean.class)
	private int pokerMesaAtivo;

	@Column(name = "poker_mesa_numero")
	private int pokerMesaNumero;

	@Column(name = "poker_mesa_lugares")
	private int pokerMesaLugares;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_torneio_id")
	@XmlInverseReference(mappedBy = "pokerMesas")
	private PokerTorneio pokerTorneio;

	@OneToMany(mappedBy = "pokerMesa")
	private List<PokerParticipante> pokerParticipantes;

	public PokerMesa() {
		this(0);
	}

	public PokerMesa(int pokerMesaId) {
		super("pu_poker", "PokerMesa", "pokerMesaId", "pokerMesaNumero");
		this.pokerMesaId = pokerMesaId;
	}

	public int getPokerMesaId() {
		return this.pokerMesaId;
	}

	public void setPokerMesaId(int pokerMesaId) {
		this.pokerMesaId = pokerMesaId;
	}

	public boolean getPokerMesaAtivo() {
		return this.pokerMesaAtivo == 0 ? false : true;
	}

	public void setPokerMesaAtivo(boolean pokerMesaAtivo) {
		this.pokerMesaAtivo = pokerMesaAtivo == false ? 0 : 1;
	}

	public int getPokerMesaNumero() {
		return this.pokerMesaNumero;
	}

	public int getPokerMesaLugares() {
		return pokerMesaLugares;
	}

	public void setPokerMesaLugares(int pokerMesaLugares) {
		this.pokerMesaLugares = pokerMesaLugares;
	}

	public void setPokerMesaNumero(int pokerMesaNumero) {
		this.pokerMesaNumero = pokerMesaNumero;
	}

	public PokerTorneio getPokerTorneio() {
		return this.pokerTorneio;
	}

	public void setPokerTorneio(PokerTorneio pokerTorneio) {
		this.pokerTorneio = pokerTorneio;
	}

	public List<PokerParticipante> getPokerParticipantes() {
		return this.pokerParticipantes;
	}

	public void setPokerParticipantes(List<PokerParticipante> pokerParticipantes) {
		this.pokerParticipantes = pokerParticipantes;
	}

	public Number getId() {
		return pokerMesaId;
	}

	public void setId(Number id) {
		pokerMesaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerMesaId + "", pokerMesaNumero + "", pokerMesaLugares + "", pokerParticipantes.size() + "", getPokerMesaAtivo() + "" };
	}

	public void anularDependencia() {
		pokerTorneio = null;
		pokerParticipantes = null;
	}

}