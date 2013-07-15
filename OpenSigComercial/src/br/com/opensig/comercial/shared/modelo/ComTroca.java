package br.com.opensig.comercial.shared.modelo;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.opensig.comercial.server.rest.BooleanToInteger;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa uma troca no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "com_troca")
@XmlRootElement(name = "EcfTroca")
public class ComTroca extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_troca_id")
	@XmlElement(name = "ecfTrocaId")
	private Integer comTrocaId;

	@Column(name = "com_troca_data")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlElement(name = "ecfTrocaData")
	private Date comTrocaData;

	@Column(name = "com_troca_valor")
	@XmlElement(name = "ecfTrocaValor")
	private Double comTrocaValor;

	@Column(name = "com_troca_ativo")
	@XmlElement(name = "ecfTrocaAtivo")
	@XmlJavaTypeAdapter(BooleanToInteger.class)
	private int comTrocaAtivo;

	@Column(name = "com_troca_cliente")
	@XmlElement(name = "ecfTrocaCliente")
	private String comTrocaCliente;

	@Column(name = "com_troca_ecf")
	@XmlElement(name = "ecfTrocaEcf")
	private int comTrocaEcf;

	@Column(name = "com_troca_coo")
	@XmlElement(name = "ecfTrocaCoo")
	private int comTrocaCoo;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private EmpEmpresa empEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_compra_id")
	@XmlTransient
	private ComCompra comCompra;

	@OneToMany(mappedBy = "comTroca", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@XmlElement(name = "ecfTrocaProdutos")
	private List<ComTrocaProduto> comTrocaProdutos;

	public ComTroca() {
		this(0);
	}

	public ComTroca(int comTrocaId) {
		super("pu_comercial", "ComTroca", "comTrocaId", "comTrocaData", EDirecao.DESC);
		this.comTrocaId = comTrocaId;
	}

	public Integer getComTrocaId() {
		return comTrocaId;
	}

	public void setComTrocaId(Integer comTrocaId) {
		this.comTrocaId = comTrocaId;
	}

	public Date getComTrocaData() {
		return comTrocaData;
	}

	public void setComTrocaData(Date comTrocaData) {
		this.comTrocaData = comTrocaData;
	}

	public Double getComTrocaValor() {
		return comTrocaValor;
	}

	public void setComTrocaValor(Double comTrocaValor) {
		this.comTrocaValor = comTrocaValor;
	}

	public boolean getComTrocaAtivo() {
		return comTrocaAtivo == 0 ? false : true;
	}

	public void setComTrocaAtivo(boolean comTrocaAtivo) {
		this.comTrocaAtivo = comTrocaAtivo == false ? 0 : 1;
	}

	public String getComTrocaCliente() {
		return comTrocaCliente;
	}

	public void setComTrocaCliente(String comTrocaCliente) {
		this.comTrocaCliente = comTrocaCliente;
	}

	public int getComTrocaEcf() {
		return comTrocaEcf;
	}

	public void setComTrocaEcf(int comTrocaEcf) {
		this.comTrocaEcf = comTrocaEcf;
	}

	public int getComTrocaCoo() {
		return comTrocaCoo;
	}

	public void setComTrocaCoo(int comTrocaCoo) {
		this.comTrocaCoo = comTrocaCoo;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public ComCompra getComCompra() {
		return comCompra;
	}

	public void setComCompra(ComCompra comCompra) {
		this.comCompra = comCompra;
	}

	public List<ComTrocaProduto> getComTrocaProdutos() {
		return comTrocaProdutos;
	}

	public void setComTrocaProdutos(List<ComTrocaProduto> comTrocaProdutos) {
		this.comTrocaProdutos = comTrocaProdutos;
	}

	public Number getId() {
		return comTrocaId;
	}

	public void setId(Number id) {
		comTrocaId = id.intValue();
	}

	public String[] toArray() {
		int compraId = comCompra == null ? 0 : comCompra.getComCompraId();

		return new String[] { comTrocaId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), compraId + "", UtilClient.getDataHoraGrid(comTrocaData),
				comTrocaCliente, comTrocaValor.toString(), comTrocaEcf + "", comTrocaCoo + "", getComTrocaAtivo() + "" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else if (campo.startsWith("comCompra")) {
			return new ComCompra();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empEmpresa = null;
		comCompra = null;
		comTrocaProdutos = null;
	}
}
