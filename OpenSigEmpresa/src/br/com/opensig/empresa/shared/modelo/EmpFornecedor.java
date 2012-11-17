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
 * Classe que representa um fornecedor no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 31/07/2009
 */
@Entity
@Table(name = "emp_fornecedor")
public class EmpFornecedor extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_fornecedor_id")
	private int empFornecedorId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;

	public EmpFornecedor() {
		this(0);
	}

	public EmpFornecedor(int empFornecedorId) {
		super("pu_empresa", "EmpFornecedor", "empFornecedorId", "empFornecedorId");
		this.empFornecedorId = empFornecedorId;
	}

	public int getEmpFornecedorId() {
		return this.empFornecedorId;
	}

	public void setEmpFornecedorId(int empFornecedorId) {
		this.empFornecedorId = empFornecedorId;
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
		return empFornecedorId;
	}

	public void setId(Number id) {
		empFornecedorId = id.intValue();
	}

	public String[] toArray() {
		String[] ent = empEntidade.toArray();
		String[] forn = new String[ent.length + 1];

		forn[0] = empFornecedorId + "";
		for (int i = 0; i < ent.length; i++) {
			forn[i + 1] = ent[i];
		}

		return forn;
	}

	public void anularDependencia() {
		empEntidade.anularDependencia();
	}
}