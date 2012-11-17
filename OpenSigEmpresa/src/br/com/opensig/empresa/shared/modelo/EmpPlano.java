package br.com.opensig.empresa.shared.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa um plano no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 20/08/2011
 */
@Entity
@Table(name = "emp_plano")
public class EmpPlano extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_plano_id")
	private int empPlanoId;

	@Temporal(TemporalType.DATE)
	@Column(name = "emp_plano_fim")
	private Date empPlanoFim;

	@Temporal(TemporalType.DATE)
	@Column(name = "emp_plano_inicio")
	private Date empPlanoInicio;

	@Column(name = "emp_plano_limite")
	private int empPlanoLimite;

	@Column(name = "emp_plano_excedente")
	private int empPlanoExcedente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	public EmpPlano() {
		this(0);
	}

	public EmpPlano(int empPlanoId) {
		super("pu_empresa", "EmpPlano", "empPlanoId", "empPlanoId");
		this.empPlanoId = empPlanoId;
	}

	public int getEmpPlanoId() {
		return this.empPlanoId;
	}

	public void setEmpPlanoId(int empPlanoId) {
		this.empPlanoId = empPlanoId;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Date getEmpPlanoFim() {
		return this.empPlanoFim;
	}

	public void setEmpPlanoFim(Date empPlanoFim) {
		this.empPlanoFim = empPlanoFim;
	}

	public Date getEmpPlanoInicio() {
		return this.empPlanoInicio;
	}

	public void setEmpPlanoInicio(Date empPlanoInicio) {
		this.empPlanoInicio = empPlanoInicio;
	}

	public int getEmpPlanoLimite() {
		return this.empPlanoLimite;
	}

	public void setEmpPlanoLimite(int empPlanoLimite) {
		this.empPlanoLimite = empPlanoLimite;
	}

	public boolean getEmpPlanoExcedente() {
		return empPlanoExcedente == 0 ? false : true;
	}

	public void setEmpPlanoExcedente(boolean empPlanoExcedente) {
		this.empPlanoExcedente = empPlanoExcedente == false ? 0 : 1;
	}

	public Number getId() {
		return empPlanoId;
	}

	public void setId(Number id) {
		empPlanoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { empPlanoId + "", empEmpresa.getEmpEmpresaId() + "", UtilClient.getDataGrid(empPlanoInicio), UtilClient.getDataGrid(empPlanoFim), empPlanoLimite + "",
				getEmpPlanoExcedente() + "" };
	}

	public void anularDependencia() {
		empEmpresa = null;
	}
}