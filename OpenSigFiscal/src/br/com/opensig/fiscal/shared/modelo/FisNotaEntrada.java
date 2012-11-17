package br.com.opensig.fiscal.shared.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ELetra;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa uma nfe de entrada no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 20/07/2010
 */
@Entity
@Table(name = "fis_nota_entrada")
public class FisNotaEntrada extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fis_nota_entrada_id")
	private int fisNotaEntradaId;

	@Column(name = "fis_nota_entrada_chave")
	private String fisNotaEntradaChave;

	@Temporal(TemporalType.DATE)
	@Column(name = "fis_nota_entrada_cadastro")
	private Date fisNotaEntradaCadastro;

	@Temporal(TemporalType.DATE)
	@Column(name = "fis_nota_entrada_data")
	private Date fisNotaEntradaData;

	@Column(name = "fis_nota_entrada_numero")
	private int fisNotaEntradaNumero;

	@Column(name = "fis_nota_entrada_protocolo")
	private String fisNotaEntradaProtocolo;

	@Column(name = "fis_nota_entrada_protocolo_cancelado")
	private String fisNotaEntradaProtocoloCancelado;

	@Column(name = "fis_nota_entrada_recibo")
	private String fisNotaEntradaRecibo;

	@Column(name = "fis_nota_entrada_valor")
	private Double fisNotaEntradaValor;

	@Column(name = "fis_nota_entrada_icms")
	private Double fisNotaEntradaIcms;

	@Column(name = "fis_nota_entrada_ipi")
	private Double fisNotaEntradaIpi;

	@Column(name = "fis_nota_entrada_pis")
	private Double fisNotaEntradaPis;

	@Column(name = "fis_nota_entrada_cofins")
	private Double fisNotaEntradaCofins;

	@Lob()
	@Column(name = "fis_nota_entrada_xml")
	private String fisNotaEntradaXml;

	@Lob()
	@Column(name = "fis_nota_entrada_xml_cancelado")
	private String fisNotaEntradaXmlCancelado;

	@Lob()
	@Column(name = "fis_nota_entrada_erro")
	private String fisNotaEntradaErro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fis_nota_status_id")
	private FisNotaStatus fisNotaStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	public FisNotaEntrada() {
		this(0);
	}

	public FisNotaEntrada(int fisNotaEntradaId) {
		super("pu_fiscal", "FisNotaEntrada", "fisNotaEntradaId", "fisNotaEntradaData", EDirecao.DESC);
		this.fisNotaEntradaId = fisNotaEntradaId;
		setTipoLetra(ELetra.NORMAL);
	}

	public int getFisNotaEntradaId() {
		return this.fisNotaEntradaId;
	}

	public void setFisNotaEntradaId(int fisNotaEntradaId) {
		this.fisNotaEntradaId = fisNotaEntradaId;
	}

	public String getFisNotaEntradaChave() {
		return this.fisNotaEntradaChave;
	}

	public void setFisNotaEntradaChave(String fisNotaEntradaChave) {
		this.fisNotaEntradaChave = fisNotaEntradaChave;
	}

	public Date getFisNotaEntradaCadastro() {
		return fisNotaEntradaCadastro;
	}

	public void setFisNotaEntradaCadastro(Date fisNotaEntradaCadastro) {
		this.fisNotaEntradaCadastro = fisNotaEntradaCadastro;
	}

	public Date getFisNotaEntradaData() {
		return this.fisNotaEntradaData;
	}

	public void setFisNotaEntradaData(Date fisNotaEntradaData) {
		this.fisNotaEntradaData = fisNotaEntradaData;
	}

	public int getFisNotaEntradaNumero() {
		return this.fisNotaEntradaNumero;
	}

	public void setFisNotaEntradaNumero(int fisNotaEntradaNumero) {
		this.fisNotaEntradaNumero = fisNotaEntradaNumero;
	}

	public String getFisNotaEntradaProtocolo() {
		return fisNotaEntradaProtocolo;
	}

	public void setFisNotaEntradaProtocolo(String fisNotaEntradaProtocolo) {
		this.fisNotaEntradaProtocolo = fisNotaEntradaProtocolo;
	}

	public String getFisNotaEntradaRecibo() {
		return fisNotaEntradaRecibo;
	}

	public void setFisNotaEntradaRecibo(String fisNotaEntradaRecibo) {
		this.fisNotaEntradaRecibo = fisNotaEntradaRecibo;
	}

	public Double getFisNotaEntradaValor() {
		return this.fisNotaEntradaValor;
	}

	public void setFisNotaEntradaValor(Double fisNotaEntradaValor) {
		this.fisNotaEntradaValor = fisNotaEntradaValor;
	}

	public String getFisNotaEntradaXml() {
		return this.fisNotaEntradaXml;
	}

	public void setFisNotaEntradaXml(String fisNotaEntradaXml) {
		this.fisNotaEntradaXml = fisNotaEntradaXml;
	}

	public FisNotaStatus getFisNotaStatus() {
		return this.fisNotaStatus;
	}

	public void setFisNotaStatus(FisNotaStatus fisNotaStatus) {
		this.fisNotaStatus = fisNotaStatus;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return fisNotaEntradaId;
	}

	public void setId(Number id) {
		fisNotaEntradaId = id.intValue();
	}

	public Double getFisNotaEntradaIcms() {
		return fisNotaEntradaIcms;
	}

	public void setFisNotaEntradaIcms(Double fisNotaEntradaIcms) {
		this.fisNotaEntradaIcms = fisNotaEntradaIcms;
	}

	public Double getFisNotaEntradaIpi() {
		return fisNotaEntradaIpi;
	}

	public void setFisNotaEntradaIpi(Double fisNotaEntradaIpi) {
		this.fisNotaEntradaIpi = fisNotaEntradaIpi;
	}

	public String getFisNotaEntradaErro() {
		return fisNotaEntradaErro;
	}

	public void setFisNotaEntradaErro(String fisNotaEntradaErro) {
		this.fisNotaEntradaErro = fisNotaEntradaErro;
	}

	public Double getFisNotaEntradaPis() {
		return fisNotaEntradaPis;
	}

	public void setFisNotaEntradaPis(Double fisNotaEntradaPis) {
		this.fisNotaEntradaPis = fisNotaEntradaPis;
	}

	public Double getFisNotaEntradaCofins() {
		return fisNotaEntradaCofins;
	}

	public void setFisNotaEntradaCofins(Double fisNotaEntradaCofins) {
		this.fisNotaEntradaCofins = fisNotaEntradaCofins;
	}

	public String getFisNotaEntradaProtocoloCancelado() {
		return fisNotaEntradaProtocoloCancelado;
	}

	public void setFisNotaEntradaProtocoloCancelado(String fisNotaEntradaProtocoloCancelado) {
		this.fisNotaEntradaProtocoloCancelado = fisNotaEntradaProtocoloCancelado;
	}

	public String getFisNotaEntradaXmlCancelado() {
		return fisNotaEntradaXmlCancelado;
	}

	public void setFisNotaEntradaXmlCancelado(String fisNotaEntradaXmlCancelado) {
		this.fisNotaEntradaXmlCancelado = fisNotaEntradaXmlCancelado;
	}

	public String[] toArray() {
		return new String[] { fisNotaEntradaId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), fisNotaStatus.getFisNotaStatusId() + " ",
				fisNotaStatus.getFisNotaStatusDescricao(), UtilClient.getDataGrid(fisNotaEntradaCadastro), fisNotaEntradaNumero + "", UtilClient.getDataGrid(fisNotaEntradaData),
				fisNotaEntradaValor.toString(), fisNotaEntradaChave, fisNotaEntradaIcms.toString(), fisNotaEntradaIpi.toString(), fisNotaEntradaPis.toString(), fisNotaEntradaCofins.toString(),
				fisNotaEntradaProtocolo, "xml", "danfe", fisNotaEntradaProtocoloCancelado, "cancelada", fisNotaEntradaRecibo, "*" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("fisNotaStatus")) {
			return new FisNotaStatus();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		fisNotaStatus = null;
		empEmpresa = null;
	}
}