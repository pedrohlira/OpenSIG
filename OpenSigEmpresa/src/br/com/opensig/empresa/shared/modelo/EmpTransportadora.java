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
 * Classe que representa uma transportadora no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 31/07/2009
 */
@Entity
@Table(name = "emp_transportadora")
public class EmpTransportadora extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_transportadora_id")
	private int empTransportadoraId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "emp_entidade_id")
	private EmpEntidade empEntidade;

	public EmpTransportadora() {
		this(0);
	}

	public EmpTransportadora(int empTransportadoraId) {
		super("pu_empresa", "EmpTransportadora", "empTransportadoraId", "empTransportadoraId");
		this.empTransportadoraId = empTransportadoraId;
	}

	public int getEmpTransportadoraId() {
		return this.empTransportadoraId;
	}

	public void setEmpTransportadoraId(int empTransportadoraId) {
		this.empTransportadoraId = empTransportadoraId;
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
		return empTransportadoraId;
	}

	public void setId(Number id) {
		empTransportadoraId = id.intValue();
	}

	public String[] toArray() {
		String[] ent = empEntidade.toArray();
		String[] trans = new String[ent.length + 1];

		trans[0] = empTransportadoraId + "";
		for (int i = 0; i < ent.length; i++) {
			trans[i + 1] = ent[i];
		}

		return trans;
	}

	public void anularDependencia() {
		empEntidade.anularDependencia();
	}
}