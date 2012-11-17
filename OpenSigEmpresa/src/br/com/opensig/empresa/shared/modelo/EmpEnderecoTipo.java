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
 * Classe que representa um tipo de endereço no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 09/06/2009
 */
@Entity
@Table(name = "emp_endereco_tipo")
public class EmpEnderecoTipo extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_endereco_tipo_id")
	private int empEnderecoTipoId;

	@Column(name = "emp_endereco_tipo_descricao")
	private String empEnderecoTipoDescricao;

	public EmpEnderecoTipo() {
		this(0);
	}

	public EmpEnderecoTipo(int empEnderecoTipoId) {
		super("pu_empresa", "EmpEnderecoTipo", "empEnderecoTipoId", "empEnderecoTipoDescricao");
		this.empEnderecoTipoId = empEnderecoTipoId;
	}

	public int getEmpEnderecoTipoId() {
		return this.empEnderecoTipoId;
	}

	public void setEmpEnderecoTipoId(int empEnderecoTipoId) {
		this.empEnderecoTipoId = empEnderecoTipoId;
	}

	public String getEmpEnderecoTipoDescricao() {
		return this.empEnderecoTipoDescricao;
	}

	public void setEmpEnderecoTipoDescricao(String empEnderecoTipoDescricao) {
		this.empEnderecoTipoDescricao = empEnderecoTipoDescricao;
	}

	public Number getId() {
		return empEnderecoTipoId;
	}

	public void setId(Number id) {
		empEnderecoTipoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { empEnderecoTipoId + "", empEnderecoTipoDescricao };
	}
}