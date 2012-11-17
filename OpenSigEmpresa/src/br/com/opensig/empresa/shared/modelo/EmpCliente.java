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
 * Classe que representa um cliente no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 31/07/2009
 */
@Entity
@Table(name = "emp_cliente")
public class EmpCliente extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_cliente_id")
	private int empClienteId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;

	public EmpCliente() {
		this(0);
	}

	public EmpCliente(int empClienteId) {
		super("pu_empresa", "EmpCliente", "empClienteId", "empClienteId");
		this.empClienteId = empClienteId;
	}

	public int getEmpClienteId() {
		return this.empClienteId;
	}

	public void setEmpClienteId(int empClienteId) {
		this.empClienteId = empClienteId;
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
		return empClienteId;
	}

	public void setId(Number id) {
		empClienteId = id.intValue();
	}

	public String[] toArray() {
		String[] ent = empEntidade.toArray();
		String[] cli = new String[ent.length + 1];

		cli[0] = empClienteId + "";
		for (int i = 0; i < ent.length; i++) {
			cli[i + 1] = ent[i];
		}

		return cli;
	}

	public void anularDependencia() {
		empEntidade.anularDependencia();
	}
}