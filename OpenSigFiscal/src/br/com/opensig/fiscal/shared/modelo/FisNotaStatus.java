package br.com.opensig.fiscal.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

/**
 * Classe que representa o status da nfe no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 20/07/2010
 */
@Entity
@Table(name = "fis_nota_status")
public class FisNotaStatus extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fis_nota_status_id")
	private int fisNotaStatusId;

	@Column(name = "fis_nota_status_descricao")
	private String fisNotaStatusDescricao;

	public FisNotaStatus() {
		this(null);
	}

	public FisNotaStatus(ENotaStatus status) {
		super("pu_fiscal", "FisNotaStatus", "fisNotaStatusId", "fisNotaStatusDescricao");
		if (status != null) {
			this.fisNotaStatusId = status.getId();
			this.fisNotaStatusDescricao = status.name();
		}
	}

	public int getFisNotaStatusId() {
		return this.fisNotaStatusId;
	}

	public void setFisNotaStatusId(int fisNotaStatusId) {
		this.fisNotaStatusId = fisNotaStatusId;
	}

	public String getFisNotaStatusDescricao() {
		return this.fisNotaStatusDescricao;
	}

	public void setFisNotaStatusDescricao(String fisNotaStatusDescricao) {
		this.fisNotaStatusDescricao = fisNotaStatusDescricao;
	}

	public Number getId() {
		return fisNotaStatusId;
	}

	public void setId(Number id) {
		fisNotaStatusId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { fisNotaStatusId + "", fisNotaStatusDescricao };
	}

	public String toString() {
		return fisNotaStatusDescricao;
	}

}