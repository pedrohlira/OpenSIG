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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um participante no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_participante")
@XmlRootElement
public class PokerParticipante extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_participante_id")
	private int pokerParticipanteId;

	@Column(name = "poker_participante_adicional")
	@XmlElement(type = Boolean.class)
	private int pokerParticipanteAdicional;
	
	@Column(name = "poker_participante_dealer")
	@XmlElement(type = Boolean.class)
	private int pokerParticipanteDealer;

	@Column(name = "poker_participante_ativo")
	@XmlElement(type = Boolean.class)
	private int pokerParticipanteAtivo;

	@Column(name = "poker_participante_ponto")
	private int pokerParticipantePonto;

	@Column(name = "poker_participante_posicao")
	private int pokerParticipantePosicao;

	@Column(name = "poker_participante_premio")
	private double pokerParticipantePremio;

	@Column(name = "poker_participante_bonus")
	private int pokerParticipanteBonus;

	@Column(name = "poker_participante_reentrada")
	private int pokerParticipanteReentrada;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_torneio_id")
	@XmlInverseReference(mappedBy = "pokerParticipantes")
	private PokerTorneio pokerTorneio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_cliente_id")
	private PokerCliente pokerCliente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_mesa_id")
	@XmlInverseReference(mappedBy = "pokerParticipantes")
	private PokerMesa pokerMesa;

	public PokerParticipante() {
		this(0);
	}

	public PokerParticipante(int pokerParticipanteId) {
		super("pu_poker", "PokerParticipante", "pokerParticipanteId", "pokerCliente.pokerClienteNome");
		this.pokerParticipanteId = pokerParticipanteId;
	}

	public int getPokerParticipanteId() {
		return this.pokerParticipanteId;
	}

	public void setPokerParticipanteId(int pokerParticipanteId) {
		this.pokerParticipanteId = pokerParticipanteId;
	}

	public int getPokerParticipanteAdicional() {
		return pokerParticipanteAdicional;
	}

	public void setPokerParticipanteAdicional(int pokerParticipanteAdicional) {
		this.pokerParticipanteAdicional = pokerParticipanteAdicional;
	}

	public int getPokerParticipanteDealer() {
		return pokerParticipanteDealer;
	}

	public void setPokerParticipanteDealer(int pokerParticipanteDealer) {
		this.pokerParticipanteDealer = pokerParticipanteDealer;
	}

	public boolean getPokerParticipanteAtivo() {
		return this.pokerParticipanteAtivo == 0 ? false : true;
	}

	public void setPokerParticipanteAtivo(boolean pokerParticipanteAtivo) {
		this.pokerParticipanteAtivo = pokerParticipanteAtivo == false ? 0 : 1;
	}

	public int getPokerParticipantePonto() {
		return this.pokerParticipantePonto;
	}

	public void setPokerParticipantePonto(int pokerParticipantePonto) {
		this.pokerParticipantePonto = pokerParticipantePonto;
	}

	public int getPokerParticipantePosicao() {
		return this.pokerParticipantePosicao;
	}

	public void setPokerParticipantePosicao(int pokerParticipantePosicao) {
		this.pokerParticipantePosicao = pokerParticipantePosicao;
	}

	public double getPokerParticipantePremio() {
		return this.pokerParticipantePremio;
	}

	public void setPokerParticipantePremio(double pokerParticipantePremio) {
		this.pokerParticipantePremio = pokerParticipantePremio;
	}

	public int getPokerParticipanteBonus() {
		return pokerParticipanteBonus;
	}

	public void setPokerParticipanteBonus(int pokerParticipanteBonus) {
		this.pokerParticipanteBonus = pokerParticipanteBonus;
	}

	public int getPokerParticipanteReentrada() {
		return this.pokerParticipanteReentrada;
	}

	public void setPokerParticipanteReentrada(int pokerParticipanteReentrada) {
		this.pokerParticipanteReentrada = pokerParticipanteReentrada;
	}

	public PokerTorneio getPokerTorneio() {
		return this.pokerTorneio;
	}

	public void setPokerTorneio(PokerTorneio pokerTorneio) {
		this.pokerTorneio = pokerTorneio;
	}

	public PokerCliente getPokerCliente() {
		return this.pokerCliente;
	}

	public void setPokerCliente(PokerCliente pokerCliente) {
		this.pokerCliente = pokerCliente;
	}

	public PokerMesa getPokerMesa() {
		return this.pokerMesa;
	}

	public void setPokerMesa(PokerMesa pokerMesa) {
		this.pokerMesa = pokerMesa;
	}

	public Number getId() {
		return pokerParticipanteId;
	}

	public void setId(Number id) {
		pokerParticipanteId = id.intValue();
	}

	public String[] toArray() {
		int mesaId = pokerMesa == null ? 0 : pokerMesa.getPokerMesaId();
		int mesaNumero = pokerMesa == null ? 0 : pokerMesa.getPokerMesaNumero();

		return new String[] { pokerParticipanteId + "", pokerTorneio.getPokerTorneioId() + "", pokerTorneio.getPokerTorneioTipo().getPokerTorneioTipoNome(), pokerTorneio.getPokerTorneioCodigo(),
				UtilClient.getDataGrid(pokerTorneio.getPokerTorneioData()), pokerTorneio.getPokerTorneioFechado() + "", pokerCliente.getPokerClienteId() + "",
				pokerCliente.getPokerClienteCodigo() + "", pokerCliente.getPokerClienteNome(), mesaId + "", mesaNumero + "", pokerParticipanteBonus + "", pokerParticipanteReentrada + "",
				pokerParticipanteAdicional + "", pokerParticipanteDealer + "", pokerParticipantePosicao + "", pokerParticipantePonto + "", pokerParticipantePremio + "", getPokerParticipanteAtivo() + "" };
	}

	public void anularDependencia() {
		pokerCliente = null;
		pokerMesa = null;
		pokerTorneio = null;
	}
}