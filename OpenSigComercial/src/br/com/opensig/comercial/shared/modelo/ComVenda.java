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

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

/**
 * Classe que representa uma venda no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 07/08/2009
 */
@Entity
@Table(name = "com_venda")
public class ComVenda extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "com_venda_id")
	private int comVendaId;

	@Column(name = "com_venda_valor_bruto")
	private Double comVendaValorBruto;

	@Column(name = "com_venda_valor_liquido")
	private Double comVendaValorLiquido;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "com_venda_data")
	private Date comVendaData;

	@Column(name = "com_venda_fechada")
	private int comVendaFechada;

	@Column(name = "com_venda_recebida")
	private int comVendaRecebida;

	@Column(name = "com_venda_nfe")
	private int comVendaNfe;

	@Column(name = "com_venda_cancelada")
	private int comVendaCancelada;
	
	@Column(name = "com_venda_observacao")
	private String comVendaObservacao;

	@JoinColumn(name = "sis_usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisUsuario sisUsuario;

	@JoinColumn(name = "emp_cliente_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpCliente empCliente;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	@JoinColumn(name = "com_natureza_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ComNatureza comNatureza;

	@JoinColumn(name = "fin_receber_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private FinReceber finReceber;

	@JoinColumn(name = "fis_nota_saida_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private FisNotaSaida fisNotaSaida;

	@OneToMany(mappedBy = "comVenda", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<ComVendaProduto> comVendaProdutos;

	public ComVenda() {
		this(0);
	}

	public ComVenda(int comVendaId) {
		super("pu_comercial", "ComVenda", "comVendaId", "comVendaData", EDirecao.DESC);
		this.comVendaId = comVendaId;
	}

	public int getComVendaId() {
		return comVendaId;
	}

	public void setComVendaId(int comVendaId) {
		this.comVendaId = comVendaId;
	}

	public Double getComVendaValorBruto() {
		return comVendaValorBruto;
	}

	public void setComVendaValorBruto(Double comVendaValorBruto) {
		this.comVendaValorBruto = comVendaValorBruto;
	}

	public Double getComVendaValorLiquido() {
		return comVendaValorLiquido;
	}

	public void setComVendaValorLiquido(Double comVendaValorLiquido) {
		this.comVendaValorLiquido = comVendaValorLiquido;
	}

	public Date getComVendaData() {
		return comVendaData;
	}

	public void setComVendaData(Date comVendaData) {
		this.comVendaData = comVendaData;
	}

	public boolean getComVendaFechada() {
		return comVendaFechada == 0 ? false : true;
	}

	public void setComVendaFechada(boolean comVendaFechada) {
		this.comVendaFechada = comVendaFechada == false ? 0 : 1;
	}

	public boolean getComVendaRecebida() {
		return comVendaRecebida == 0 ? false : true;
	}

	public void setComVendaRecebida(boolean comVendaRecebida) {
		this.comVendaRecebida = comVendaRecebida == false ? 0 : 1;
	}

	public boolean getComVendaNfe() {
		return comVendaNfe == 0 ? false : true;
	}

	public void setComVendaNfe(boolean comVendaNfe) {
		this.comVendaNfe = comVendaNfe == false ? 0 : 1;
	}

	public String getComVendaObservacao() {
		return comVendaObservacao;
	}

	public void setComVendaObservacao(String comVendaObservacao) {
		this.comVendaObservacao = comVendaObservacao;
	}

	public boolean getComVendaCancelada() {
		return comVendaCancelada == 0 ? false : true;
	}

	public void setComVendaCancelada(boolean comVendaCancelada) {
		this.comVendaCancelada = comVendaCancelada == false ? 0 : 1;
	}
	
	public SisUsuario getSisUsuario() {
		return sisUsuario;
	}

	public void setSisUsuario(SisUsuario sisUsuario) {
		this.sisUsuario = sisUsuario;
	}

	public EmpCliente getEmpCliente() {
		return empCliente;
	}

	public void setEmpCliente(EmpCliente empCliente) {
		this.empCliente = empCliente;
	}

	public List<ComVendaProduto> getComVendaProdutos() {
		return comVendaProdutos;
	}

	public void setComVendaProdutos(List<ComVendaProduto> comVendaProdutos) {
		this.comVendaProdutos = comVendaProdutos;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public FinReceber getFinReceber() {
		return finReceber;
	}

	public void setFinReceber(FinReceber finReceber) {
		this.finReceber = finReceber;
	}

	public FisNotaSaida getFisNotaSaida() {
		return fisNotaSaida;
	}

	public void setFisNotaSaida(FisNotaSaida fisNotaSaida) {
		this.fisNotaSaida = fisNotaSaida;
	}

	public ComNatureza getComNatureza() {
		return comNatureza;
	}

	public void setComNatureza(ComNatureza comNatureza) {
		this.comNatureza = comNatureza;
	}

	public Number getId() {
		return comVendaId;
	}

	public void setId(Number id) {
		comVendaId = id.intValue();
	}

	public String[] toArray() {
		int receberId = finReceber == null ? 0 : finReceber.getFinReceberId();
		int nfeId = fisNotaSaida == null ? 0 : fisNotaSaida.getFisNotaSaidaId();
		int contaId = finReceber == null ? 0 : finReceber.getFinConta().getFinContaId();

		return new String[] { comVendaId + "", empCliente.getEmpClienteId() + "", empCliente.getEmpEntidade().getEmpEntidadeId() + "", empCliente.getEmpEntidade().getEmpEntidadeNome1(),
				empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), sisUsuario.getSisUsuarioId() + "", sisUsuario.getSisUsuarioLogin(),
				UtilClient.getDataHoraGrid(comVendaData), comVendaValorBruto.toString(), comVendaValorLiquido.toString(), comNatureza.getComNaturezaId() + "", comNatureza.getComNaturezaNome(),
				getComVendaFechada() + "", contaId + "", receberId + "", getComVendaRecebida() + "", nfeId + "", getComVendaNfe() + "", getComVendaCancelada() + "", comVendaObservacao };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("sisUsuario")) {
			return new SisUsuario();
		} else if (campo.startsWith("empCliente")) {
			return new EmpCliente();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else if (campo.startsWith("comNatureza")) {
			return new ComNatureza();
		} else if (campo.startsWith("finReceber")) {
			return new FinReceber();
		} else if (campo.startsWith("fisNotaSaida")) {
			return new FisNotaSaida();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		sisUsuario = null;
		empCliente = null;
		empEmpresa = null;
		comNatureza = null;
		comVendaProdutos = null;
		finReceber = null;
		fisNotaSaida = null;
	}
}
