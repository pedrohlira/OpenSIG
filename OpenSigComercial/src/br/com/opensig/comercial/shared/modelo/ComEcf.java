package br.com.opensig.comercial.shared.modelo;

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
import javax.xml.bind.annotation.XmlTransient;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

@Entity
@Table(name = "com_ecf")
@XmlRootElement(name = "EcfImpressora")
public class ComEcf extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_ecf_id")
	@XmlElement(name = "ecfImpressoraId")
	private int comEcfId;

	@Column(name = "com_ecf_caixa")
	@XmlElement(name = "ecfImpressoraCaixa")
	private int comEcfCaixa;

	@Column(name = "com_ecf_codigo")
	@XmlElement(name = "ecfImpressoraCodigo")
	private String comEcfCodigo;

	@Column(name = "com_ecf_mfadicional")
	@XmlElement(name = "ecfImpressoraMfAdicional")
	private String comEcfMfAdicional;

	@Column(name = "com_ecf_identificacao")
	@XmlElement(name = "ecfImpressoraIdentificacao")
	private String comEcfIdentificacao;

	@Column(name = "com_ecf_tipo")
	@XmlElement(name = "ecfImpressoraTipo")
	private String comEcfTipo;

	@Column(name = "com_ecf_marca")
	@XmlElement(name = "ecfImpressoraMarca")
	private String comEcfMarca;

	@Column(name = "com_ecf_modelo")
	@XmlElement(name = "ecfImpressoraModelo")
	private String comEcfModelo;

	@Column(name = "com_ecf_serie")
	@XmlElement(name = "ecfImpressoraSerie")
	private String comEcfSerie;

	@Column(name = "com_ecf_ativo")
	@XmlElement(name = "ecfImpressoraAtivo", type = Boolean.class)
	private int comEcfAtivo;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private EmpEmpresa empEmpresa;

	public ComEcf() {
		this(0);
	}

	public ComEcf(int comEcfId) {
		super("pu_comercial", "ComEcf", "comEcfId", "comEcfCaixa");
		this.comEcfId = comEcfId;
	}

	public int getComEcfId() {
		return this.comEcfId;
	}

	public void setComEcfId(int comEcfId) {
		this.comEcfId = comEcfId;
	}

	public String getComEcfMfAdicional() {
		return comEcfMfAdicional;
	}

	public void setComEcfMfAdicional(String comEcfMfAdicional) {
		this.comEcfMfAdicional = comEcfMfAdicional;
	}

	public String getComEcfIdentificacao() {
		return comEcfIdentificacao;
	}

	public void setComEcfIdentificacao(String comEcfIdentificacao) {
		this.comEcfIdentificacao = comEcfIdentificacao;
	}

	public String getComEcfTipo() {
		return comEcfTipo;
	}

	public void setComEcfTipo(String comEcfTipo) {
		this.comEcfTipo = comEcfTipo;
	}

	public String getComEcfMarca() {
		return comEcfMarca;
	}

	public void setComEcfMarca(String comEcfMarca) {
		this.comEcfMarca = comEcfMarca;
	}

	public int getComEcfCaixa() {
		return this.comEcfCaixa;
	}

	public void setComEcfCaixa(int comEcfCaixa) {
		this.comEcfCaixa = comEcfCaixa;
	}

	public String getComEcfCodigo() {
		return this.comEcfCodigo;
	}

	public void setComEcfCodigo(String comEcfCodigo) {
		this.comEcfCodigo = comEcfCodigo;
	}

	public String getComEcfModelo() {
		return this.comEcfModelo;
	}

	public void setComEcfModelo(String comEcfModelo) {
		this.comEcfModelo = comEcfModelo;
	}

	public String getComEcfSerie() {
		return this.comEcfSerie;
	}

	public void setComEcfSerie(String comEcfSerie) {
		this.comEcfSerie = comEcfSerie;
	}

	public boolean getComEcfAtivo() {
		return comEcfAtivo == 0 ? false : true;
	}

	public void setComEcfAtivo(boolean comEcfAtivo) {
		this.comEcfAtivo = comEcfAtivo == false ? 0 : 1;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return comEcfId;
	}

	public void setId(Number id) {
		comEcfId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { comEcfId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), comEcfCodigo, comEcfMfAdicional, comEcfIdentificacao, comEcfTipo,
				comEcfMarca, comEcfModelo, comEcfSerie, comEcfCaixa + "", getComEcfAtivo() + "" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empEmpresa = null;
	}
}