package br.com.opensig.empresa.shared.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um pais no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 09/06/2009
 */
@Entity
@Table(name = "emp_pais")
public class EmpPais extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_pais_id")
	private int empPaisId;

	@Column(name = "emp_pais_ibge")
	private int empPaisIbge;
	
	@Column(name = "emp_pais_descricao")
	private String empPaisDescricao;

	@Column(name = "emp_pais_sigla")
	private String empPaisSigla;

	@OneToMany(mappedBy = "empPais")
	private List<EmpEstado> empEstados;

	public EmpPais() {
		this(0);
	}

	public EmpPais(int empPaisId) {
		super("pu_empresa", "EmpPais", "empPaisId", "empPaisDescricao");
		this.empPaisId = empPaisId;
	}

	public int getEmpPaisId() {
		return empPaisId;
	}

	public void setEmpPaisId(int empPaisId) {
		this.empPaisId = empPaisId;
	}

	public int getEmpPaisIbge() {
		return empPaisIbge;
	}

	public void setEmpPaisIbge(int empPaisIbge) {
		this.empPaisIbge = empPaisIbge;
	}

	public String getEmpPaisDescricao() {
		return empPaisDescricao;
	}

	public void setEmpPaisDescricao(String empPaisDescricao) {
		this.empPaisDescricao = empPaisDescricao;
	}

	public String getEmpPaisSigla() {
		return empPaisSigla;
	}

	public void setEmpPaisSigla(String empPaisSigla) {
		this.empPaisSigla = empPaisSigla;
	}

	public List<EmpEstado> getEmpEstados() {
		return empEstados;
	}

	public void setEmpEstados(List<EmpEstado> empEstados) {
		this.empEstados = empEstados;
	}

	public Number getId() {
		return empPaisId;
	}

	public void setId(Number id) {
		empPaisId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { empPaisId + "", empPaisIbge + "", empPaisDescricao, empPaisSigla };
	}

	public void anularDependencia() {
		empEstados = null;
	}
}