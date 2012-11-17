package br.com.opensig.empresa.shared.modelo;

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

/**
 * Classe que representa um contato no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 09/06/2009
 */
@Entity
@Table(name = "emp_contato")
public class EmpContato extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_contato_id")
	private int empContatoId;

	@Column(name = "emp_contato_descricao")
	private String empContatoDescricao;

	@Column(name = "emp_contato_pessoa")
	private String empContatoPessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_contato_tipo_id")
	private EmpContatoTipo empContatoTipo;

	public EmpContato() {
		this(0);
	}

	public EmpContato(int empContatoId) {
		super("pu_empresa", "EmpContato", "empContatoId", "empContatoDescricao");
		this.empContatoId = empContatoId;
	}

	public int getEmpContatoId() {
		return this.empContatoId;
	}

	public void setEmpContatoId(int empContatoId) {
		this.empContatoId = empContatoId;
	}

	public String getEmpContatoDescricao() {
		return this.empContatoDescricao;
	}

	public void setEmpContatoDescricao(String empContatoDescricao) {
		this.empContatoDescricao = empContatoDescricao;
	}

	public String getEmpContatoPessoa() {
		return this.empContatoPessoa;
	}

	public void setEmpContatoPessoa(String empContatoPessoa) {
		this.empContatoPessoa = empContatoPessoa;
	}

	public EmpEntidade getEmpEntidade() {
		return this.empEntidade;
	}

	public void setEmpEntidade(EmpEntidade empEntidade) {
		this.empEntidade = empEntidade;
	}

	public EmpContatoTipo getEmpContatoTipo() {
		return this.empContatoTipo;
	}

	public void setEmpContatoTipo(EmpContatoTipo empContatoTipo) {
		this.empContatoTipo = empContatoTipo;
	}

	public Number getId() {
		return empContatoId;
	}

	public void setId(Number id) {
		empContatoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { empContatoId + "", empContatoTipo.getId().toString(), empContatoTipo.getEmpContatoTipoDescricao(), empContatoDescricao, empContatoPessoa };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empContatoTipo")) {
			return new EmpContatoTipo();
		} else {
			return null;
		}
	}
	
	public void anularDependencia() {
		empContatoTipo = null;
		empEntidade = null;
	}
}