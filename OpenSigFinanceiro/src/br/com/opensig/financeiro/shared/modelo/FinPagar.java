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

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;

/**
 * Classe que representa um a pagar no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 18/11/2009
 */
@Entity
@Table(name = "fin_pagar")
public class FinPagar extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_pagar_id")
	private int finPagarId;

	@Column(name = "fin_pagar_valor")
	private Double finPagarValor;

	@Column(name = "fin_pagar_categoria")
	private String finPagarCategoria;

	@Column(name = "fin_pagar_cadastro")
	@Temporal(TemporalType.DATE)
	private Date finPagarCadastro;

	@Column(name = "fin_pagar_nfe")
	private int finPagarNfe;

	@Column(name = "fin_pagar_observacao")
	private String finPagarObservacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_conta_id")
	private FinConta finConta;

	@OneToMany(mappedBy = "finPagar", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<FinPagamento> finPagamentos;
	
	public FinPagar() {
		this(0);
	}

	public FinPagar(int finPagarId) {
		super("pu_financeiro", "FinPagar", "finPagarId", "finPagarCadastro", EDirecao.DESC);
		this.finPagarId = finPagarId;
	}

	public int getFinPagarId() {
		return finPagarId;
	}

	public void setFinPagarId(int finPagarId) {
		this.finPagarId = finPagarId;
	}

	public Double getFinPagarValor() {
		return finPagarValor;
	}

	public void setFinPagarValor(Double finPagarValor) {
		this.finPagarValor = finPagarValor;
	}

	public String getFinPagarCategoria() {
		return finPagarCategoria;
	}

	public void setFinPagarCategoria(String finPagarCategoria) {
		this.finPagarCategoria = finPagarCategoria;
	}

	public Date getFinPagarCadastro() {
		return finPagarCadastro;
	}

	public void setFinPagarCadastro(Date finPagarCadastro) {
		this.finPagarCadastro = finPagarCadastro;
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

	public List<FinPagamento> getFinPagamentos() {
		return finPagamentos;
	}

	public void setFinPagamentos(List<FinPagamento> finPagamentos) {
		this.finPagamentos = finPagamentos;
	}

	public int getFinPagarNfe() {
		return finPagarNfe;
	}

	public void setFinPagarNfe(int finPagarNfe) {
		this.finPagarNfe = finPagarNfe;
	}

	public String getFinPagarObservacao() {
		return finPagarObservacao;
	}

	public void setFinPagarObservacao(String finPagarObservacao) {
		this.finPagarObservacao = finPagarObservacao;
	}

	public Number getId() {
		return finPagarId;
	}

	public void setId(Number id) {
		finPagarId = id.intValue();
	}

	public String[] toArray() {

		return new String[] { finPagarId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), empEntidade.getEmpEntidadeId() + "",
				empEntidade.getEmpEntidadeNome1(), finConta.getFinContaId() + "", finConta.getFinContaNome(), finPagarValor.toString(), UtilClient.getDataGrid(finPagarCadastro),
				finPagarCategoria, finPagarNfe + "", finPagarObservacao };
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
		finPagamentos = null;
	}
}