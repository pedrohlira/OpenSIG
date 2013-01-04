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
import javax.persistence.Transient;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

/**
 * Classe que representa o sped no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "fis_sped")
public class FisSped extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fis_sped_id")
	private int fisSpedId;

	@Column(name = "fis_sped_ano")
	private int fisSpedAno;

	@Column(name = "fis_sped_protocolo")
	private String fisSpedProtocolo;

	@Temporal(TemporalType.DATE)
	@Column(name = "fis_sped_data")
	private Date fisSpedData;

	@Column(name = "fis_sped_mes")
	private int fisSpedMes;

	@Column(name = "fis_sped_tipo")
	private String fisSpedTipo;

	@Column(name = "fis_sped_ativo")
	private int fisSpedAtivo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_empresa_id")
	private EmpEmpresa empEmpresa;

	@Transient
	private int finalidade;

	public FisSped() {
		this(0);
	}

	public FisSped(int fisSpedId) {
		super("pu_fiscal", "FisSped", "fisSpedId", "fisSpedData", EDirecao.DESC);
		this.fisSpedId = fisSpedId;
	}

	public int getFisSpedId() {
		return this.fisSpedId;
	}

	public void setFisSpedId(int fisSpedId) {
		this.fisSpedId = fisSpedId;
	}

	public int getFisSpedAno() {
		return this.fisSpedAno;
	}

	public void setFisSpedAno(int fisSpedAno) {
		this.fisSpedAno = fisSpedAno;
	}

	public Date getFisSpedData() {
		return this.fisSpedData;
	}

	public void setFisSpedData(Date fisSpedData) {
		this.fisSpedData = fisSpedData;
	}

	public int getFisSpedMes() {
		return this.fisSpedMes;
	}

	public void setFisSpedMes(int fisSpedMes) {
		this.fisSpedMes = fisSpedMes;
	}

	public String getFisSpedTipo() {
		return this.fisSpedTipo;
	}

	public void setFisSpedTipo(String fisSpedTipo) {
		this.fisSpedTipo = fisSpedTipo;
	}

	public boolean getFisSpedAtivo() {
		return this.fisSpedAtivo == 0 ? false : true;
	}

	public void setFisSpedAtivo(boolean fisSpedAtivo) {
		this.fisSpedAtivo = fisSpedAtivo == false ? 0 : 1;
	}

	public String getFisSpedProtocolo() {
		return fisSpedProtocolo;
	}

	public void setFisSpedProtocolo(String fisSpedProtocolo) {
		this.fisSpedProtocolo = fisSpedProtocolo;
	}

	public EmpEmpresa getEmpEmpresa() {
		return empEmpresa;
	}

	public void setEmpEmpresa(EmpEmpresa empEmpresa) {
		this.empEmpresa = empEmpresa;
	}

	public int getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(int finalidade) {
		this.finalidade = finalidade;
	}

	public Number getId() {
		return fisSpedId;
	}

	public void setId(Number id) {
		fisSpedId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { fisSpedId + "", empEmpresa.getEmpEmpresaId() + "", empEmpresa.getEmpEntidade().getEmpEntidadeNome1(), fisSpedAno + "", fisSpedMes + "",
				fisSpedTipo, UtilClient.getDataGrid(fisSpedData), getFisSpedAtivo() + "", fisSpedProtocolo };
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