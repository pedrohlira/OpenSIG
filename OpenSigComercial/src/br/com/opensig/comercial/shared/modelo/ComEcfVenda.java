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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.opensig.comercial.server.rest.BooleanToInteger;
import br.com.opensig.comercial.shared.rest.SisCliente;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

/**
 * Classe que representa a venda pelo ECF.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "com_ecf_venda")
@XmlRootElement(name = "EcfVenda")
public class ComEcfVenda extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_ecf_venda_id")
	@XmlElement(name = "ecfVendaId")
	private int comEcfVendaId;

	@Column(name = "com_ecf_venda_bruto")
	@XmlElement(name = "ecfVendaBruto")
	private Double comEcfVendaBruto;

	@Column(name = "com_ecf_venda_fechada")
	@XmlElement(name = "ecfVendaFechada")
	@XmlJavaTypeAdapter(BooleanToInteger.class)
	private int comEcfVendaFechada;

	@Column(name = "com_ecf_venda_cancelada")
	@XmlElement(name = "ecfVendaCancelada")
	@XmlJavaTypeAdapter(BooleanToInteger.class)
	private int comEcfVendaCancelada;

	@Column(name = "com_ecf_venda_ccf")
	@XmlElement(name = "ecfVendaCcf")
	private int comEcfVendaCcf;

	@Column(name = "com_ecf_venda_coo")
	@XmlElement(name = "ecfVendaCoo")
	private int comEcfVendaCoo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "com_ecf_venda_data")
	@XmlElement(name = "ecfVendaData")
	private Date comEcfVendaData;

	@Column(name = "com_ecf_venda_desconto")
	@XmlElement(name = "ecfVendaDesconto")
	private Double comEcfVendaDesconto;

	@Column(name = "com_ecf_venda_acrescimo")
	@XmlElement(name = "ecfVendaAcrescimo")
	private Double comEcfVendaAcrescimo;

	@Column(name = "com_ecf_venda_liquido")
	@XmlElement(name = "ecfVendaLiquido")
	private Double comEcfVendaLiquido;

	@Column(name = "com_ecf_venda_observacao")
	@XmlElement(name = "ecfVendaObservacao")
	private String comEcfVendaObservacao;

	@JoinColumn(name = "sis_usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisUsuario sisUsuario;

	@JoinColumn(name = "sis_vendedor_id", referencedColumnName = "sis_usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisUsuario sisVendedor;

	@JoinColumn(name = "sis_gerente_id", referencedColumnName = "sis_usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisUsuario sisGerente;

	@JoinColumn(name = "emp_cliente_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private EmpCliente empCliente;

	@Transient
	private SisCliente sisCliente;

	@JoinColumn(name = "fin_receber_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@XmlTransient
	private FinReceber finReceber;

	@JoinColumn(name = "com_ecf_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private ComEcf comEcf;

	@JoinColumn(name = "com_ecf_z_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@XmlTransient
	private ComEcfZ comEcfZ;

	@OneToMany(mappedBy = "comEcfVenda", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@XmlElement(name = "ecfVendaProdutos")
	private List<ComEcfVendaProduto> comEcfVendaProdutos;

	@Transient
	private List<FinReceber> ecfPagamentos;

	private transient String cancelada;

	private transient String descIndicador;

	private transient String acresIndicador;

	public ComEcfVenda() {
		this(0);
	}

	public ComEcfVenda(int comEcfVendaId) {
		super("pu_comercial", "ComEcfVenda", "comEcfVendaId", "comEcfVendaData", EDirecao.DESC);
		this.comEcfVendaId = comEcfVendaId;
	}

	public int getComEcfVendaId() {
		return this.comEcfVendaId;
	}

	public void setComEcfVendaId(int comEcfVendaId) {
		this.comEcfVendaId = comEcfVendaId;
	}

	public Double getComEcfVendaBruto() {
		return this.comEcfVendaBruto;
	}

	public void setComEcfVendaBruto(Double comEcfVendaBruto) {
		this.comEcfVendaBruto = comEcfVendaBruto;
	}

	public boolean getComEcfVendaFechada() {
		return comEcfVendaFechada == 0 ? false : true;
	}

	public void setComEcfVendaFechada(boolean comEcfVendaFechada) {
		this.comEcfVendaFechada = comEcfVendaFechada == false ? 0 : 1;
	}

	public boolean getComEcfVendaCancelada() {
		return comEcfVendaCancelada == 0 ? false : true;
	}

	public void setComEcfVendaCancelada(boolean comEcfVendaCancelada) {
		this.comEcfVendaCancelada = comEcfVendaCancelada == false ? 0 : 1;
	}

	public int getComEcfVendaCcf() {
		return comEcfVendaCcf;
	}

	public void setComEcfVendaCcf(int comEcfVendaCcf) {
		this.comEcfVendaCcf = comEcfVendaCcf;
	}

	public int getComEcfVendaCoo() {
		return this.comEcfVendaCoo;
	}

	public void setComEcfVendaCoo(int comEcfVendaCoo) {
		this.comEcfVendaCoo = comEcfVendaCoo;
	}

	public Date getComEcfVendaData() {
		return this.comEcfVendaData;
	}

	public void setComEcfVendaData(Date comEcfVendaData) {
		this.comEcfVendaData = comEcfVendaData;
	}

	public Double getComEcfVendaDesconto() {
		return this.comEcfVendaDesconto;
	}

	public void setComEcfVendaDesconto(Double comEcfVendaDesconto) {
		this.comEcfVendaDesconto = comEcfVendaDesconto;
	}

	public Double getComEcfVendaAcrescimo() {
		return comEcfVendaAcrescimo;
	}

	public void setComEcfVendaAcrescimo(Double comEcfVendaAcrescimo) {
		this.comEcfVendaAcrescimo = comEcfVendaAcrescimo;
	}

	public Double getComEcfVendaLiquido() {
		return this.comEcfVendaLiquido;
	}

	public void setComEcfVendaLiquido(Double comEcfVendaLiquido) {
		this.comEcfVendaLiquido = comEcfVendaLiquido;
	}

	public EmpCliente getEmpCliente() {
		return empCliente;
	}

	public void setEmpCliente(EmpCliente empCliente) {
		this.empCliente = empCliente;
	}

	public SisCliente getSisCliente() {
		return sisCliente;
	}

	public void setSisCliente(SisCliente sisCliente) {
		this.sisCliente = sisCliente;
	}

	public FinReceber getFinReceber() {
		return finReceber;
	}

	public void setFinReceber(FinReceber finReceber) {
		this.finReceber = finReceber;
	}

	public SisUsuario getSisUsuario() {
		return sisUsuario;
	}

	public void setSisUsuario(SisUsuario sisUsuario) {
		this.sisUsuario = sisUsuario;
	}

	public SisUsuario getSisVendedor() {
		return sisVendedor;
	}

	public void setSisVendedor(SisUsuario sisVendedor) {
		this.sisVendedor = sisVendedor;
	}

	public SisUsuario getSisGerente() {
		return sisGerente;
	}

	public void setSisGerente(SisUsuario sisGerente) {
		this.sisGerente = sisGerente;
	}

	public ComEcf getComEcf() {
		return this.comEcf;
	}

	public void setComEcf(ComEcf comEcf) {
		this.comEcf = comEcf;
	}

	public ComEcfZ getComEcfZ() {
		return comEcfZ;
	}

	public void setComEcfZ(ComEcfZ comEcfZ) {
		this.comEcfZ = comEcfZ;
	}

	public String getCancelada() {
		return cancelada;
	}

	public void setCancelada(String cancelada) {
		this.cancelada = cancelada;
	}

	public String getDescIndicador() {
		return descIndicador;
	}

	public void setDescIndicador(String descIndicador) {
		this.descIndicador = descIndicador;
	}

	public String getAcresIndicador() {
		return acresIndicador;
	}

	public void setAcresIndicador(String acresIndicador) {
		this.acresIndicador = acresIndicador;
	}

	public String getComEcfVendaObservacao() {
		return comEcfVendaObservacao;
	}

	public void setComEcfVendaObservacao(String comEcfVendaObservacao) {
		this.comEcfVendaObservacao = comEcfVendaObservacao;
	}

	public List<ComEcfVendaProduto> getComEcfVendaProdutos() {
		return this.comEcfVendaProdutos;
	}

	public void setComEcfVendaProdutos(List<ComEcfVendaProduto> comEcfVendaProdutos) {
		this.comEcfVendaProdutos = comEcfVendaProdutos;
	}

	public List<FinReceber> getEcfPagamentos() {
		return ecfPagamentos;
	}

	public void setEcfPagamentos(List<FinReceber> ecfPagamentos) {
		this.ecfPagamentos = ecfPagamentos;
	}

	public Number getId() {
		return comEcfVendaId;
	}

	public void setId(Number id) {
		comEcfVendaId = id.intValue();
	}

	public String[] toArray() {
		int vendedorId = sisVendedor == null ? 0 : sisVendedor.getSisUsuarioId();
		String vendedorNome = sisVendedor == null ? "" : sisVendedor.getSisUsuarioLogin();
		int gerenteId = sisGerente == null ? 0 : sisGerente.getSisUsuarioId();
		String gerenteNome = sisGerente == null ? "" : sisGerente.getSisUsuarioLogin();
		int clienteId = empCliente == null ? 0 : empCliente.getEmpClienteId();
		String clienteNome = empCliente == null ? "" : empCliente.getEmpEntidade().getEmpEntidadeNome1();
		int receberId = finReceber == null ? 0 : finReceber.getFinReceberId();

		return new String[] { comEcfVendaId + "", comEcfZ.getComEcfZId() + "", comEcf.getEmpEmpresa().getEmpEmpresaId() + "", comEcf.getEmpEmpresa().getEmpEntidade().getEmpEntidadeNome1(),
				sisUsuario.getSisUsuarioId() + "", sisUsuario.getSisUsuarioLogin(), vendedorId + "", vendedorNome, gerenteId + "", gerenteNome, clienteId + "", clienteNome, comEcf.getComEcfId() + "",
				comEcf.getComEcfSerie(), comEcfVendaCcf + "", comEcfVendaCoo + "", UtilClient.getDataGrid(comEcfVendaData), comEcfVendaBruto.toString(), comEcfVendaDesconto.toString(),
				comEcfVendaAcrescimo.toString(), comEcfVendaLiquido.toString(), getComEcfVendaFechada() + "", receberId + "", getComEcfVendaCancelada() + "", comEcfVendaObservacao };
	}

	public void anularDependencia() {
		sisUsuario = null;
		sisVendedor = null;
		sisGerente = null;
		empCliente = null;
		finReceber = null;
		comEcf = null;
		comEcfZ = null;
		comEcfVendaProdutos = null;
	}
}