package br.com.opensig.poker.shared.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Colecao;
import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um cliente no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "poker_cliente")
@XmlRootElement
public class PokerCliente extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "poker_cliente_id")
	private int pokerClienteId;

	@Column(name = "poker_cliente_associado")
	@XmlElement(type = Boolean.class)
	private int pokerClienteAssociado;

	@Column(name = "poker_cliente_ativo")
	@XmlElement(type = Boolean.class)
	private int pokerClienteAtivo;

	@Column(name = "poker_cliente_codigo")
	private int pokerClienteCodigo;

	@Column(name = "poker_cliente_auxiliar")
	private int pokerClienteAuxiliar;

	@Column(name = "poker_cliente_contato")
	private String pokerClienteContato;

	@Column(name = "poker_cliente_documento")
	private String pokerClienteDocumento;

	@Temporal(TemporalType.DATE)
	@Column(name = "poker_cliente_data")
	private Date pokerClienteData;

	@Column(name = "poker_cliente_email")
	private String pokerClienteEmail;

	@Column(name = "poker_cliente_nome")
	private String pokerClienteNome;

	@OneToMany(mappedBy = "pokerCliente")
	@XmlInverseReference(mappedBy = "pokerCliente")
	private List<PokerParticipante> pokerParticipantes;

	@OneToMany(mappedBy = "pokerCliente")
	@XmlInverseReference(mappedBy = "pokerCliente")
	private List<PokerJogador> pokerJogadores;
	
	public PokerCliente() {
		this(0);
	}

	public PokerCliente(int pokerClienteId) {
		super("pu_poker", "PokerCliente", "pokerClienteId", "pokerClienteNome");
		Colecao col1 = new Colecao("PokerParticipante", "t.pokerParticipantes", "LEFT JOIN", "t1");
		Colecao col2 = new Colecao("PokerJogador", "t.pokerJogadores", "LEFT JOIN", "t2");
		setColecao(new Colecao[] { col1, col2 });
		this.pokerClienteId = pokerClienteId;
	}

	public int getPokerClienteId() {
		return this.pokerClienteId;
	}

	public void setPokerClienteId(int pokerClienteId) {
		this.pokerClienteId = pokerClienteId;
	}

	public boolean getPokerClienteAssociado() {
		return this.pokerClienteAssociado == 0 ? false : true;
	}

	public void setPokerClienteAssociado(boolean pokerClienteAssociado) {
		this.pokerClienteAssociado = pokerClienteAssociado == false ? 0 : 1;
	}

	public boolean getPokerClienteAtivo() {
		return pokerClienteAtivo == 0 ? false : true;
	}

	public void setPokerClienteAtivo(boolean pokerClienteAtivo) {
		this.pokerClienteAtivo = pokerClienteAtivo == false ? 0 : 1;
	}

	public int getPokerClienteCodigo() {
		return this.pokerClienteCodigo;
	}

	public void setPokerClienteCodigo(int pokerClienteCodigo) {
		this.pokerClienteCodigo = pokerClienteCodigo;
	}

	public int getPokerClienteAuxiliar() {
		return pokerClienteAuxiliar;
	}

	public void setPokerClienteAuxiliar(int pokerClienteAuxiliar) {
		this.pokerClienteAuxiliar = pokerClienteAuxiliar;
	}

	public String getPokerClienteContato() {
		return this.pokerClienteContato;
	}

	public void setPokerClienteContato(String pokerClienteContato) {
		this.pokerClienteContato = pokerClienteContato;
	}

	public String getPokerClienteDocumento() {
		return this.pokerClienteDocumento;
	}

	public void setPokerClienteDocumento(String pokerClienteDocumento) {
		this.pokerClienteDocumento = pokerClienteDocumento;
	}

	public Date getPokerClienteData() {
		return this.pokerClienteData;
	}

	public void setPokerClienteData(Date pokerClienteData) {
		this.pokerClienteData = pokerClienteData;
	}

	public String getPokerClienteEmail() {
		return this.pokerClienteEmail;
	}

	public void setPokerClienteEmail(String pokerClienteEmail) {
		this.pokerClienteEmail = pokerClienteEmail;
	}

	public String getPokerClienteNome() {
		return this.pokerClienteNome;
	}

	public void setPokerClienteNome(String pokerClienteNome) {
		this.pokerClienteNome = pokerClienteNome;
	}

	public List<PokerParticipante> getPokerParticipantes() {
		return this.pokerParticipantes;
	}

	public void setPokerParticipantes(List<PokerParticipante> pokerParticipantes) {
		this.pokerParticipantes = pokerParticipantes;
	}

	public Number getId() {
		return pokerClienteId;
	}

	public void setId(Number id) {
		pokerClienteId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { pokerClienteId + "", pokerClienteCodigo + "", pokerClienteNome, pokerClienteAuxiliar + "", pokerClienteDocumento, pokerClienteContato, pokerClienteEmail,
				UtilClient.getDataGrid(pokerClienteData), getPokerClienteAssociado() + "", getPokerClienteAtivo() + "" };
	}

	public void anularDependencia() {
		pokerParticipantes = null;
	}
}