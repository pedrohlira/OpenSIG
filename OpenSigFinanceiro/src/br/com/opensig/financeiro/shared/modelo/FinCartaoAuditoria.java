package br.com.opensig.financeiro.shared.modelo;

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
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

/**
 * Classe que representa a auditoria do cartao presente no sistema.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "fin_cartao_auditoria")
public class FinCartaoAuditoria extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fin_cartao_auditoria_id")
	private int finCartaoAuditoriaId;

	@Column(name = "fin_cartao_auditoria_acao")
	private String finCartaoAuditoriaAcao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fin_cartao_auditoria_data")
	private Date finCartaoAuditoriaData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fin_cartao_presente_id")
	private FinCartaoPresente finCartaoPresente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sis_usuario_id")
	private SisUsuario sisUsuario;

	public FinCartaoAuditoria() {
		this(0);
	}

	public FinCartaoAuditoria(int finCartaoAuditoriaId) {
		super("pu_financeiro", "FinCartaoAuditoria", "finCartaoAuditoriaId", "finCartaoAuditoriaData", EDirecao.DESC);
		this.finCartaoAuditoriaId = finCartaoAuditoriaId;
	}

	public int getFinCartaoAuditoriaId() {
		return this.finCartaoAuditoriaId;
	}

	public void setFinCartaoAuditoriaId(int finCartaoAuditoriaId) {
		this.finCartaoAuditoriaId = finCartaoAuditoriaId;
	}

	public String getFinCartaoAuditoriaAcao() {
		return this.finCartaoAuditoriaAcao;
	}

	public void setFinCartaoAuditoriaAcao(String finCartaoAuditoriaAcao) {
		this.finCartaoAuditoriaAcao = finCartaoAuditoriaAcao;
	}

	public Date getFinCartaoAuditoriaData() {
		return this.finCartaoAuditoriaData;
	}

	public void setFinCartaoAuditoriaData(Date finCartaoAuditoriaData) {
		this.finCartaoAuditoriaData = finCartaoAuditoriaData;
	}

	public FinCartaoPresente getFinCartaoPresente() {
		return this.finCartaoPresente;
	}

	public void setFinCartaoPresente(FinCartaoPresente finCartaoPresente) {
		this.finCartaoPresente = finCartaoPresente;
	}

	public SisUsuario getSisUsuario() {
		return sisUsuario;
	}

	public void setSisUsuario(SisUsuario sisUsuario) {
		this.sisUsuario = sisUsuario;
	}

	public Number getId() {
		return finCartaoAuditoriaId;
	}

	public void setId(Number id) {
		finCartaoAuditoriaId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { finCartaoAuditoriaId + "", finCartaoPresente.getFinCartaoPresenteNumero(), sisUsuario.getSisUsuarioLogin(), UtilClient.getDataHoraGrid(finCartaoAuditoriaData),
				finCartaoAuditoriaAcao };
	}

	public void anularDependencia() {
		finCartaoPresente = null;
		sisUsuario = null;
	}
}