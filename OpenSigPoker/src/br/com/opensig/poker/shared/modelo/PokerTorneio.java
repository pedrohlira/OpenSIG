package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;

/**
 * Classe que representa um torneio no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_torneio")
@XmlRootElement
public class PokerTorneio extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_torneio_id")
	private int pokerTorneioId;

	@Column(name = "poker_torneio_adicional")
	private double pokerTorneioAdicional;

	@Column(name = "poker_torneio_codigo")
	private String pokerTorneioCodigo;

	@Column(name = "poker_torneio_taxa")
	private double pokerTorneioTaxa;

	@Column(name = "poker_torneio_comissao")
	private double pokerTorneioComissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "poker_torneio_data")
	private Date pokerTorneioData;

	@Column(name = "poker_torneio_entrada")
	private double pokerTorneioEntrada;

	@Column(name = "poker_torneio_entrada_ficha")
	private int pokerTorneioEntradaFicha;

	@Column(name = "poker_torneio_reentrada_ficha")
	private int pokerTorneioReentradaFicha;

	@Column(name = "poker_torneio_adicional_ficha")
	private int pokerTorneioAdicionalFicha;

	@Column(name = "poker_torneio_dealer")
	private double pokerTorneioDealer;

	@Column(name = "poker_torneio_dealer_ficha")
	private int pokerTorneioDealerFicha;

	@Column(name = "poker_torneio_nome")
	private String pokerTorneioNome;

	@Column(name = "poker_torneio_ponto")
	private int pokerTorneioPonto;

	@Column(name = "poker_torneio_premio")
	private double pokerTorneioPremio;

	@Column(name = "poker_torneio_arrecadado")
	private double pokerTorneioArrecadado;

	@Column(name = "poker_torneio_reentrada")
	private double pokerTorneioReentrada;

	@Column(name = "poker_torneio_ativo")
	@XmlElement(type = Boolean.class)
	private int pokerTorneioAtivo;

	@Column(name = "poker_torneio_fechado")
	@XmlElement(type = Boolean.class)
	private int pokerTorneioFechado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poker_torneio_tipo_id")
	private PokerTorneioTipo pokerTorneioTipo;

	@OneToMany(mappedBy = "pokerTorneio", cascade = CascadeType.REMOVE)
	private List<PokerMesa> pokerMesas;

	@OneToMany(mappedBy = "pokerTorneio", cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<PokerNivel> pokerNiveis;

	@OneToMany(mappedBy = "pokerTorneio", cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<PokerPremiacao> pokerPremiacoes;

	@OneToMany(mappedBy = "pokerTorneio", cascade = CascadeType.REMOVE)
	@XmlTransient
	private List<PokerParticipante> pokerParticipantes;

	public PokerTorneio() {
		this(0);
	}

	public PokerTorneio(int pokerTorneioId) {
		super("pu_poker", "PokerTorneio", "pokerTorneioId", "pokerTorneioData", EDirecao.DESC);
		this.pokerTorneioId = pokerTorneioId;
	}

	public int getPokerTorneioId() {
		return this.pokerTorneioId;
	}

	public void setPokerTorneioId(int pokerTorneioId) {
		this.pokerTorneioId = pokerTorneioId;
	}

	public double getPokerTorneioAdicional() {
		return this.pokerTorneioAdicional;
	}

	public void setPokerTorneioAdicional(double pokerTorneioAdicional) {
		this.pokerTorneioAdicional = pokerTorneioAdicional;
	}

	public String getPokerTorneioCodigo() {
		return this.pokerTorneioCodigo;
	}

	public void setPokerTorneioCodigo(String pokerTorneioCodigo) {
		this.pokerTorneioCodigo = pokerTorneioCodigo;
	}

	public double getPokerTorneioTaxa() {
		return pokerTorneioTaxa;
	}

	public void setPokerTorneioTaxa(double pokerTorneioTaxa) {
		this.pokerTorneioTaxa = pokerTorneioTaxa;
	}

	public double getPokerTorneioComissao() {
		return this.pokerTorneioComissao;
	}

	public void setPokerTorneioComissao(double pokerTorneioComissao) {
		this.pokerTorneioComissao = pokerTorneioComissao;
	}

	public Date getPokerTorneioData() {
		return this.pokerTorneioData;
	}

	public void setPokerTorneioData(Date pokerTorneioData) {
		this.pokerTorneioData = pokerTorneioData;
	}

	public double getPokerTorneioEntrada() {
		return this.pokerTorneioEntrada;
	}

	public void setPokerTorneioEntrada(double pokerTorneioEntrada) {
		this.pokerTorneioEntrada = pokerTorneioEntrada;
	}

	public int getPokerTorneioEntradaFicha() {
		return pokerTorneioEntradaFicha;
	}

	public void setPokerTorneioEntradaFicha(int pokerTorneioEntradaFicha) {
		this.pokerTorneioEntradaFicha = pokerTorneioEntradaFicha;
	}

	public int getPokerTorneioAdicionalFicha() {
		return pokerTorneioAdicionalFicha;
	}

	public void setPokerTorneioAdicionalFicha(int pokerTorneioAdicionalFicha) {
		this.pokerTorneioAdicionalFicha = pokerTorneioAdicionalFicha;
	}

	public int getPokerTorneioReentradaFicha() {
		return pokerTorneioReentradaFicha;
	}

	public void setPokerTorneioReentradaFicha(int pokerTorneioReentradaFicha) {
		this.pokerTorneioReentradaFicha = pokerTorneioReentradaFicha;
	}

	public int getPokerTorneioPonto() {
		return pokerTorneioPonto;
	}

	public void setPokerTorneioPonto(int pokerTorneioPonto) {
		this.pokerTorneioPonto = pokerTorneioPonto;
	}

	public String getPokerTorneioNome() {
		return this.pokerTorneioNome;
	}

	public void setPokerTorneioNome(String pokerTorneioNome) {
		this.pokerTorneioNome = pokerTorneioNome;
	}

	public double getPokerTorneioPremio() {
		return this.pokerTorneioPremio;
	}

	public void setPokerTorneioPremio(double pokerTorneioPremio) {
		this.pokerTorneioPremio = pokerTorneioPremio;
	}

	public double getPokerTorneioArrecadado() {
		return pokerTorneioArrecadado;
	}

	public void setPokerTorneioArrecadado(double pokerTorneioArrecadado) {
		this.pokerTorneioArrecadado = pokerTorneioArrecadado;
	}

	public double getPokerTorneioReentrada() {
		return this.pokerTorneioReentrada;
	}

	public void setPokerTorneioReentrada(double pokerTorneioReentrada) {
		this.pokerTorneioReentrada = pokerTorneioReentrada;
	}

	public boolean getPokerTorneioFechado() {
		return this.pokerTorneioFechado == 0 ? false : true;
	}

	public void setPokerTorneioFechado(boolean pokerTorneioFechado) {
		this.pokerTorneioFechado = pokerTorneioFechado == false ? 0 : 1;
	}

	public boolean getPokerTorneioAtivo() {
		return this.pokerTorneioAtivo == 0 ? false : true;
	}

	public void setPokerTorneioAtivo(boolean pokerTorneioAtivo) {
		this.pokerTorneioAtivo = pokerTorneioAtivo == false ? 0 : 1;
	}

	public double getPokerTorneioDealer() {
		return pokerTorneioDealer;
	}

	public void setPokerTorneioDealer(double pokerTorneioDealer) {
		this.pokerTorneioDealer = pokerTorneioDealer;
	}

	public int getPokerTorneioDealerFicha() {
		return pokerTorneioDealerFicha;
	}

	public void setPokerTorneioDealerFicha(int pokerTorneioDealerFicha) {
		this.pokerTorneioDealerFicha = pokerTorneioDealerFicha;
	}

	public PokerTorneioTipo getPokerTorneioTipo() {
		return pokerTorneioTipo;
	}

	public void setPokerTorneioTipo(PokerTorneioTipo pokerTorneioTipo) {
		this.pokerTorneioTipo = pokerTorneioTipo;
	}

	public List<PokerMesa> getPokerMesas() {
		return this.pokerMesas;
	}

	public void setPokerMesas(List<PokerMesa> pokerMesas) {
		this.pokerMesas = pokerMesas;
	}

	public List<PokerNivel> getPokerNiveis() {
		return this.pokerNiveis;
	}

	public void setPokerNiveis(List<PokerNivel> pokerNiveis) {
		this.pokerNiveis = pokerNiveis;
	}

	public List<PokerPremiacao> getPokerPremiacoes() {
		return pokerPremiacoes;
	}

	public void setPokerPremiacoes(List<PokerPremiacao> pokerPremiacoes) {
		this.pokerPremiacoes = pokerPremiacoes;
	}

	public List<PokerParticipante> getPokerParticipantes() {
		return this.pokerParticipantes;
	}

	public void setPokerParticipantes(List<PokerParticipante> pokerParticipantes) {
		this.pokerParticipantes = pokerParticipantes;
	}

	public Number getId() {
		return pokerTorneioId;
	}

	public void setId(Number id) {
		pokerTorneioId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerTorneioId + "", pokerTorneioTipo.getPokerTorneioTipoId() + "", pokerTorneioTipo.getPokerTorneioTipoNome(), pokerTorneioCodigo + "", pokerTorneioNome,
				pokerTorneioEntrada + "", pokerTorneioEntradaFicha + "", pokerTorneioReentrada + "", pokerTorneioReentradaFicha + "", pokerTorneioAdicional + "", pokerTorneioAdicionalFicha + "",
				pokerTorneioDealer + "", pokerTorneioDealerFicha + "", pokerTorneioPonto + "", pokerTorneioArrecadado + "", pokerTorneioPremio + "", pokerTorneioTaxa + "", pokerTorneioComissao + "",
				UtilClient.getDataGrid(pokerTorneioData), getPokerTorneioFechado() + "", getPokerTorneioAtivo() + "" };
	}

	public void anularDependencia() {
		pokerTorneioTipo = null;
		pokerMesas = null;
		pokerNiveis = null;
		pokerPremiacoes = null;
		pokerParticipantes = null;
	}
}