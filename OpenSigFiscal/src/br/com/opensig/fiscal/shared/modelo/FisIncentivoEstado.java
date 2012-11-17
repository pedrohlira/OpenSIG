package br.com.opensig.fiscal.shared.modelo;

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
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEstado;

/**
 * Classe que representa o incentivo do estado no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 08/09/2010
 */
@Entity
@Table(name = "fis_incentivo_estado")
public class FisIncentivoEstado extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fis_incentivo_estado_id")
	private int fisIncentivoEstadoId;

	@Column(name = "fis_incentivo_estado_icms1")
	private Double fisIncentivoEstadoIcms1;

	@Column(name = "fis_incentivo_estado_icms2")
	private Double fisIncentivoEstadoIcms2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_estado_id")
	private EmpEstado empEstado;

	@JoinColumn(name = "emp_empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private EmpEmpresa empEmpresa;

	public FisIncentivoEstado() {
		this(0);
	}

	public FisIncentivoEstado(int fisIncentivoEstadoId) {
		super("pu_fiscal", "FisIncentivoEstado", "fisIncentivoEstadoId");
		this.fisIncentivoEstadoId = fisIncentivoEstadoId;
	}

	public int getFisIncentivoEstadoId() {
		return this.fisIncentivoEstadoId;
	}

	public void setFisIncentivoEstadoId(int fisIncentivoEstadoId) {
		this.fisIncentivoEstadoId = fisIncentivoEstadoId;
	}

	public Double getFisIncentivoEstadoIcms1() {
		return this.fisIncentivoEstadoIcms1;
	}

	public void setFisIncentivoEstadoIcms1(Double fisIncentivoEstadoIcms1) {
		this.fisIncentivoEstadoIcms1 = fisIncentivoEstadoIcms1;
	}

	public Double getFisIncentivoEstadoIcms2() {
		return this.fisIncentivoEstadoIcms2;
	}

	public void setFisIncentivoEstadoIcms2(Double fisIncentivoEstadoIcms2) {
		this.fisIncentivoEstadoIcms2 = fisIncentivoEstadoIcms2;
	}

	public EmpEstado getEmpEstado() {
		return empEstado;
	}

	public void setEmpEstado(EmpEstado empEstado) {
		this.empEstado = empEstado;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return fisIncentivoEstadoId;
	}

	public void setId(Number id) {
		fisIncentivoEstadoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { fisIncentivoEstadoId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), empEstado.getEmpEstadoId() + "",
				empEstado.getEmpEstadoDescricao(), fisIncentivoEstadoIcms1.toString(), fisIncentivoEstadoIcms2.toString() };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEstado")) {
			return new EmpEstado();
		} else if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empEstado = null;
		empEmpresa = null;
	}

}