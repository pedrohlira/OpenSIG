package br.com.opensig.empresa.shared.modelo;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

/**
 * Classe que representa um funcionario no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "emp_funcionario")
public class EmpFuncionario extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_funcionario_id")
	private int empFuncionarioId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	public EmpFuncionario() {
		this(0);
	}

	public EmpFuncionario(int empFuncionarioId) {
		super("pu_empresa", "EmpFuncionario", "empFuncionarioId", "empFuncionarioId");
		this.empFuncionarioId = empFuncionarioId;
	}

	public int getEmpFuncionarioId() {
		return this.empFuncionarioId;
	}

	public void setEmpFuncionarioId(int empFuncionarioId) {
		this.empFuncionarioId = empFuncionarioId;
	}

	public EmpEntidade getEmpEntidade() {
		return this.empEntidade;
	}

	public void setEmpEntidade(EmpEntidade empEntidade) {
		this.empEntidade = empEntidade;
	}

	public EmpEmpresa getEmpEmpresa() {
		return this.empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public boolean getAtivo() {
		return empEntidade.getAtivo();
	}

	public Number getId() {
		return empFuncionarioId;
	}

	public void setId(Number id) {
		empFuncionarioId = id.intValue();
	}

	public String[] toArray() {
		String[] ent = empEntidade.toArray();
		String[] fun = new String[ent.length + 3];

		fun[0] = empFuncionarioId + "";
		for (int i = 0; i < ent.length; i++) {
			fun[i + 1] = ent[i];
		}
		fun[ent.length + 1] = empEmpresa.getEmpEmpresaId() + "";
		fun[ent.length + 2] = empEmpresa.getEmpEntidade().getEmpEntidadeNome1();

		return fun;
	}

	public void anularDependencia() {
		empEntidade.anularDependencia();
	}
}