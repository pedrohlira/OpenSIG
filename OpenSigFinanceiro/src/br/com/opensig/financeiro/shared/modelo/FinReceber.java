package br.com.opensig.financeiro.shared.modelo;

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

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;

/**
 * Classe que representa um a receber no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "fin_receber")
@XmlRootElement(name = "EcfPagamento")
public class FinReceber extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_receber_id")
	@XmlElement(name = "ecfPagamentoId")
	private int finReceberId;

	@Column(name = "fin_receber_valor")
	@XmlElement(name = "ecfPagamentoValor")
	private Double finReceberValor;

	@Column(name = "fin_receber_categoria")
	@XmlElement(name = "ecfPagamentoNsu")
	private String finReceberCategoria;
	
	@Column(name = "fin_receber_nfe")
	@XmlElement(name = "ecfPagamentoGnf")
	private int finReceberNfe;

	@Column(name = "fin_receber_cadastro")
	@Temporal(TemporalType.DATE)
	@XmlElement(name = "ecfPagamentoData")
	private Date finReceberCadastro;

	@Column(name = "fin_receber_observacao")
	@XmlTransient
	private String finReceberObservacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	@XmlTransient
	private EmpEmpresa empEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_entidade_id")
	@XmlTransient
	private EmpEntidade empEntidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_conta_id")
	@XmlTransient
	private FinConta finConta;

	@OneToMany(mappedBy = "finReceber", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@XmlElement(name = "ecfPagamentoParcelas")
	private List<FinRecebimento> finRecebimentos;
	
	@Transient
	@XmlElement(name = "ecfPagamentoTipo")
	private FinForma finForma;
	
	public FinReceber() {
		this(0);
	}

	public FinReceber(int finReceberId) {
		super("pu_financeiro", "FinReceber", "finReceberId", "finReceberCadastro", EDirecao.DESC);
		this.finReceberId = finReceberId;
	}

	public int getFinReceberId() {
		return finReceberId;
	}

	public void setFinReceberId(int finReceberId) {
		this.finReceberId = finReceberId;
	}

	public Double getFinReceberValor() {
		return finReceberValor;
	}

	public void setFinReceberValor(Double finReceberValor) {
		this.finReceberValor = finReceberValor;
	}

	public String getFinReceberCategoria() {
		return finReceberCategoria;
	}

	public void setFinReceberCategoria(String finReceberCategoria) {
		this.finReceberCategoria = finReceberCategoria;
	}

	public int getFinReceberNfe() {
		return finReceberNfe;
	}

	public void setFinReceberNfe(int finReceberNfe) {
		this.finReceberNfe = finReceberNfe;
	}

	public Date getFinReceberCadastro() {
		return finReceberCadastro;
	}

	public void setFinReceberCadastro(Date finReceberCadastro) {
		this.finReceberCadastro = finReceberCadastro;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public EmpEntidade getEmpEntidade() {
		return empEntidade;
	}

	public void setEmpEntidade(EmpEntidade empEntidade) {
		this.empEntidade = empEntidade;
	}

	public FinConta getFinConta() {
		return finConta;
	}

	public void setFinConta(FinConta finConta) {
		this.finConta = finConta;
	}

	public List<FinRecebimento> getFinRecebimentos() {
		return finRecebimentos;
	}

	public void setFinRecebimentos(List<FinRecebimento> finRecebimentos) {
		this.finRecebimentos = finRecebimentos;
	}

	public String getFinReceberObservacao() {
		return finReceberObservacao;
	}

	public void setFinReceberObservacao(String finReceberObservacao) {
		this.finReceberObservacao = finReceberObservacao;
	}

	public FinForma getFinForma() {
		return finForma;
	}
	
	public void setFinForma(FinForma finForma) {
		this.finForma = finForma;
	}
	
	public Number getId() {
		return finReceberId;
	}

	public void setId(Number id) {
		finReceberId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { finReceberId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), empEntidade.getEmpEntidadeId() + "",
				empEntidade.getEmpEntidadeNome1(), finConta.getFinContaId() + "", finConta.getFinContaNome(), finReceberValor.toString(), UtilClient.getDataGrid(finReceberCadastro),
				finReceberCategoria, finReceberNfe + "", finReceberObservacao };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else if (campo.startsWith("finConta")) {
			return new FinConta();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empEmpresa = null;
		empEntidade = null;
		finConta = null;
		finRecebimentos = null;
	}
}