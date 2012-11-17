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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um municipio no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "emp_municipio")
@XmlRootElement
public class EmpMunicipio extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_municipio_id")
	@XmlElement(name = "sisMunicipioId")
	private int empMunicipioId;

	@Column(name = "emp_municipio_ibge")
	@XmlElement(name = "sisMunicipioIbge")
	private int empMunicipioIbge;

	@Column(name = "emp_municipio_descricao")
	@XmlElement(name = "sisMunicipioDescricao")
	private String empMunicipioDescricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_estado_id")
	@XmlElement(name = "sisEstado")
	private EmpEstado empEstado;

	public EmpMunicipio() {
		this(0);
	}

	public EmpMunicipio(int empMunicipioId) {
		super("pu_empresa", "EmpMunicipio", "empMunicipioId", "empMunicipioDescricao");
		this.empMunicipioId = empMunicipioId;
	}

	public int getEmpMunicipioId() {
		return empMunicipioId;
	}

	public void setEmpMunicipioId(int empMunicipioId) {
		this.empMunicipioId = empMunicipioId;
	}

	public int getEmpMunicipioIbge() {
		return empMunicipioIbge;
	}

	public void setEmpMunicipioIbge(int empMunicipioIbge) {
		this.empMunicipioIbge = empMunicipioIbge;
	}

	public String getEmpMunicipioDescricao() {
		return empMunicipioDescricao;
	}

	public void setEmpMunicipioDescricao(String empMunicipioDescricao) {
		this.empMunicipioDescricao = empMunicipioDescricao;
	}

	public EmpEstado getEmpEstado() {
		return empEstado;
	}

	public void setEmpEstado(EmpEstado empEstado) {
		this.empEstado = empEstado;
	}

	public Number getId() {
		return empMunicipioId;
	}

	public void setId(Number id) {
		empMunicipioId = id.intValue();
	}

	public void anularDependencia() {
		empEstado = null;
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEstado")) {
			return new EmpEstado();
		} else {
			return null;
		}
	}

	public String[] toArray() {
		return new String[] { empMunicipioId + "", empMunicipioIbge + "", empMunicipioDescricao, empEstado.getEmpEstadoId() + "", empEstado.getEmpEstadoDescricao(),
				empEstado.getEmpPais().getEmpPaisId() + "", empEstado.getEmpPais().getEmpPaisDescricao() };
	}
}