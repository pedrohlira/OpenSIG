package br.com.opensig.empresa.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um tipo de contato no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 09/06/2009
 */
@Entity
@Table(name = "emp_contato_tipo")
public class EmpContatoTipo extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_contato_tipo_id")
	private int empContatoTipoId;

	@Column(name = "emp_contato_tipo_descricao")
	private String empContatoTipoDescricao;

	public EmpContatoTipo() {
		this(0);
	}

	public EmpContatoTipo(int empContatoTipoId) {
		super("pu_empresa", "EmpContatoTipo", "empContatoTipoId", "empContatoTipoDescricao");
		this.empContatoTipoId = empContatoTipoId;
	}

	public int getEmpContatoTipoId() {
		return this.empContatoTipoId;
	}

	public void setEmpContatoTipoId(int empContatoTipoId) {
		this.empContatoTipoId = empContatoTipoId;
	}

	public String getEmpContatoTipoDescricao() {
		return this.empContatoTipoDescricao;
	}

	public void setEmpContatoTipoDescricao(String empContatoTipoDescricao) {
		this.empContatoTipoDescricao = empContatoTipoDescricao;
	}

	public Number getId() {
		return empContatoTipoId;
	}

	public void setId(Number id) {
		empContatoTipoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { empContatoTipoId + "", empContatoTipoDescricao };
	}
}