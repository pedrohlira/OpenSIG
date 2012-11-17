package br.com.opensig.financeiro.shared.modelo;

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

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa uma conta no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 18/11/2009
 */
@Entity
@Table(name = "fin_conta")
public class FinConta extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_conta_id")
	private int finContaId;

	@Column(name = "fin_conta_nome")
	private String finContaNome;

	@Column(name = "fin_conta_numero")
	private String finContaNumero;

	@Column(name = "fin_conta_agencia")
	private String finContaAgencia;

	@Column(name = "fin_conta_saldo")
	private Double finContaSaldo;

	@Column(name = "fin_conta_carteira")
	private String finContaCarteira;

	@Column(name = "fin_conta_convenio")
	private String finContaConvenio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_banco_id")
	private FinBanco finBanco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	public FinConta() {
		this(0);
	}

	public FinConta(int finContaId) {
		super("pu_financeiro", "FinConta", "finContaId", "finContaNome");
		this.finContaId = finContaId;
	}

	public int getFinContaId() {
		return finContaId;
	}

	public void setFinContaId(int finContaId) {
		this.finContaId = finContaId;
	}

	public String getFinContaNumero() {
		return finContaNumero;
	}

	public void setFinContaNumero(String finContaNumero) {
		this.finContaNumero = finContaNumero;
	}

	public String getFinContaCarteira() {
		return finContaCarteira;
	}

	public void setFinContaCarteira(String finContaCarteira) {
		this.finContaCarteira = finContaCarteira;
	}

	public String getFinContaConvenio() {
		return finContaConvenio;
	}

	public void setFinContaConvenio(String finContaConvenio) {
		this.finContaConvenio = finContaConvenio;
	}

	public Double getFinContaSaldo() {
		return finContaSaldo;
	}

	public void setFinContaSaldo(Double finContaSaldo) {
		this.finContaSaldo = finContaSaldo;
	}

	public String getFinContaNome() {
		return finContaNome;
	}

	public void setFinContaNome(String finContaNome) {
		this.finContaNome = finContaNome;
	}

	public String getFinContaAgencia() {
		return finContaAgencia;
	}

	public void setFinContaAgencia(String finContaAgencia) {
		this.finContaAgencia = finContaAgencia;
	}

	public FinBanco getFinBanco() {
		return finBanco;
	}

	public void setFinBanco(FinBanco finBanco) {
		this.finBanco = finBanco;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return finContaId;
	}

	public void setId(Number id) {
		finContaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { finContaId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), finBanco.getFinBancoId() + "", finBanco.getFinBancoDescricao(),
				finContaNome, finContaNumero, finContaAgencia, finContaCarteira, finContaConvenio, finContaSaldo.toString() };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("finBanco")) {
			return new FinBanco();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		finBanco = null;
		empEmpresa = null;
	}
}
