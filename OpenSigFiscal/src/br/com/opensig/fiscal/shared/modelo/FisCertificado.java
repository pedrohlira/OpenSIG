package br.com.opensig.fiscal.shared.modelo;

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
import br.com.opensig.core.shared.modelo.ELetra;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa o certificado da NFe no sistema
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 20/07/2010
 */
@Entity
@Table(name = "fis_certificado")
public class FisCertificado extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fis_certificado_id")
	private int fisCertificadoId;

	@Column(name = "fis_certificado_senha")
	private String fisCertificadoSenha;

	@Temporal(TemporalType.DATE)
	@Column(name = "fis_certificado_inicio")
	private Date fisCertificadoInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "fis_certificado_fim")
	private Date fisCertificadoFim;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	public FisCertificado() {
		this(0);
	}

	public FisCertificado(int fisCertificadoId) {
		super("pu_fiscal", "FisCertificado", "fisCertificadoId", "fisCertificadoId");
		this.fisCertificadoId = fisCertificadoId;
		setTipoLetra(ELetra.NORMAL);
	}

	public int getFisCertificadoId() {
		return fisCertificadoId;
	}

	public void setFisCertificadoId(int fisCertificadoId) {
		this.fisCertificadoId = fisCertificadoId;
	}

	public String getFisCertificadoSenha() {
		return fisCertificadoSenha;
	}

	public void setFisCertificadoSenha(String fisCertificadoSenha) {
		this.fisCertificadoSenha = fisCertificadoSenha;
	}

	public Date getFisCertificadoInicio() {
		return fisCertificadoInicio;
	}

	public void setFisCertificadoInicio(Date fisCertificadoInicio) {
		this.fisCertificadoInicio = fisCertificadoInicio;
	}

	public Date getFisCertificadoFim() {
		return fisCertificadoFim;
	}

	public void setFisCertificadoFim(Date fisCertificadoFim) {
		this.fisCertificadoFim = fisCertificadoFim;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public Number getId() {
		return fisCertificadoId;
	}

	public void setId(Number id) {
		fisCertificadoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { fisCertificadoId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), empEmpresa.getEmpEntidade().getEmpEntidadeDocumento1(),
				UtilClient.getDataGrid(fisCertificadoInicio), UtilClient.getDataGrid(fisCertificadoFim), "imagem" };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("empEmpresa")) {
			return new EmpEmpresa();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		empEmpresa = null;
	}
}