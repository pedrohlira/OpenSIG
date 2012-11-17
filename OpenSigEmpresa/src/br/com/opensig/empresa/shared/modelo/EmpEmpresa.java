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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa uma empresa no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "emp_empresa")
public class EmpEmpresa extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_empresa_id")
	private int empEmpresaId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;
	
	@OneToMany(mappedBy = "empEmpresa", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<EmpPlano> empPlanos;

	public EmpEmpresa() {
		this(0);
	}

	public EmpEmpresa(int empEmpresaId) {
		super("pu_empresa", "EmpEmpresa", "empEmpresaId", "empEmpresaId");
		this.empEmpresaId = empEmpresaId;
	}

	public int getEmpEmpresaId() {
		return this.empEmpresaId;
	}

	public void setEmpEmpresaId(int empEmpresaId) {
		this.empEmpresaId = empEmpresaId;
	}

	public EmpEntidade getEmpEntidade() {
		return this.empEntidade;
	}

	public void setEmpEntidade(EmpEntidade empEntidade) {
		this.empEntidade = empEntidade;
	}

	public boolean getAtivo() {
		return empEntidade.getAtivo();
	}

	public Number getId() {
		return empEmpresaId;
	}

	public void setId(Number id) {
		empEmpresaId = id.intValue();
	}

	public List<EmpPlano> getEmPlanos() {
		return empPlanos;
	}

	public void setEmPlano(List<EmpPlano> emPlanos) {
		this.empPlanos = emPlanos;
	}

	public String[] toArray() {
		String[] ent = empEntidade.toArray();
		String[] emp = new String[ent.length + 1];

		emp[0] = empEmpresaId + "";
		for (int i = 0; i < ent.length; i++) {
			emp[i + 1] = ent[i];
		}

		return emp;
	}

	public void anularDependencia() {
		empEntidade.anularDependencia();
		empPlanos = null;
	}

	public String toString() {
		return empEntidade.getEmpEntidadeNome1();
	}
}