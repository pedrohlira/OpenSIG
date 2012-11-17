package br.com.opensig.empresa.shared.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma entidade no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 09/06/2009
 */
@Entity
@Table(name = "emp_entidade")
public class EmpEntidade extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_entidade_id")
	private int empEntidadeId;

	@Column(name = "emp_entidade_ativo")
	private int empEntidadeAtivo;

	@Column(name = "emp_entidade_documento1")
	private String empEntidadeDocumento1;

	@Column(name = "emp_entidade_documento2")
	private String empEntidadeDocumento2;

	@Column(name = "emp_entidade_nome1")
	private String empEntidadeNome1;

	@Column(name = "emp_entidade_nome2")
	private String empEntidadeNome2;

	@Column(name = "emp_entidade_observacao")
	private String empEntidadeObservacao;

	@Column(name = "emp_entidade_pessoa")
	private String empEntidadePessoa;

	@OneToMany(mappedBy = "empEntidade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<EmpContato> empContatos;

	@OneToMany(mappedBy = "empEntidade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<EmpEndereco> empEnderecos;

	public EmpEntidade() {
		this(0);
	}

	public EmpEntidade(int empEntidadeId) {
		super("pu_empresa", "EmpEntidade", "empEntidadeId", "empEntidadeNome1");
		this.empEntidadeId = empEntidadeId;
	}

	public int getEmpEntidadeId() {
		return this.empEntidadeId;
	}

	public void setEmpEntidadeId(int empEntidadeId) {
		this.empEntidadeId = empEntidadeId;
	}

	public boolean getEmpEntidadeAtivo() {
		return empEntidadeAtivo == 0 ? false : true;
	}

	public void setEmpEntidadeAtivo(boolean empEntidadeAtivo) {
		this.empEntidadeAtivo = empEntidadeAtivo == false ? 0 : 1;
	}

	public String getEmpEntidadeDocumento1() {
		return this.empEntidadeDocumento1;
	}

	public void setEmpEntidadeDocumento1(String empEntidadeDocumento1) {
		this.empEntidadeDocumento1 = empEntidadeDocumento1;
	}

	public String getEmpEntidadeDocumento2() {
		return this.empEntidadeDocumento2;
	}

	public void setEmpEntidadeDocumento2(String empEntidadeDocumento2) {
		this.empEntidadeDocumento2 = empEntidadeDocumento2;
	}

	public String getEmpEntidadeNome1() {
		return this.empEntidadeNome1;
	}

	public void setEmpEntidadeNome1(String empEntidadeNome1) {
		this.empEntidadeNome1 = empEntidadeNome1;
	}

	public String getEmpEntidadeNome2() {
		return this.empEntidadeNome2;
	}

	public void setEmpEntidadeNome2(String empEntidadeNome2) {
		this.empEntidadeNome2 = empEntidadeNome2;
	}

	public String getEmpEntidadeObservacao() {
		return this.empEntidadeObservacao;
	}

	public void setEmpEntidadeObservacao(String empEntidadeObservacao) {
		this.empEntidadeObservacao = empEntidadeObservacao;
	}

	public String getEmpEntidadePessoa() {
		return this.empEntidadePessoa;
	}

	public void setEmpEntidadePessoa(String empEntidadePessoa) {
		this.empEntidadePessoa = empEntidadePessoa;
	}

	public List<EmpContato> getEmpContatos() {
		return this.empContatos;
	}

	public void setEmpContatos(List<EmpContato> empContatos) {
		this.empContatos = empContatos;
	}

	public List<EmpEndereco> getEmpEnderecos() {
		return this.empEnderecos;
	}

	public void setEmpEnderecos(List<EmpEndereco> empEnderecos) {
		this.empEnderecos = empEnderecos;
	}

	public boolean getAtivo() {
		return getEmpEntidadeAtivo();
	}

	public Number getId() {
		return empEntidadeId;
	}

	public void setId(Number id) {
		empEntidadeId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { empEntidadeId + "", empEntidadeNome1, empEntidadeNome2, empEntidadePessoa, empEntidadeDocumento1, empEntidadeDocumento2, getEmpEntidadeAtivo() + "",
				empEntidadeObservacao };
	}

	public void anularDependencia() {
		empContatos = null;
		empEnderecos = null;
	}
}