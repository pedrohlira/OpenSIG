package br.com.opensig.permissao.shared.modelo;

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

/**
 * Classe que representa um favorito do portal no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 27/08/2009
 */
@Entity
@Table(name = "sis_favorito_portal")
public class SisFavoritoPortal extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_favorito_portal_id")
	private int sisFavoritoPortalId;

	@Column(name = "sis_favorito_portal_coluna")
	private byte sisFavoritoPortalColuna;

	@Column(name = "sis_favorito_portal_ordem")
	private byte sisFavoritoPortalOrdem;

	@JoinColumn(name = "sis_usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisUsuario sisUsuario;

	@JoinColumn(name = "sis_favorito_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SisFavorito sisFavorito;

	public SisFavoritoPortal() {
		this(0);
	}

	public SisFavoritoPortal(int sisFavoritoPortalId) {
		super("pu_permissao", "SisFavoritoPortal", "sisFavoritoPortalId", "sisFavoritoPortalOrdem");
		this.sisFavoritoPortalId = sisFavoritoPortalId;
	}

	public int getSisFavoritoPortalId() {
		return sisFavoritoPortalId;
	}

	public void setSisFavoritoPortalId(int sisFavoritoPortalId) {
		this.sisFavoritoPortalId = sisFavoritoPortalId;
	}

	public byte getSisFavoritoPortalColuna() {
		return sisFavoritoPortalColuna;
	}

	public void setSisFavoritoPortalColuna(byte sisFavoritoPortalColuna) {
		this.sisFavoritoPortalColuna = sisFavoritoPortalColuna;
	}

	public byte getSisFavoritoPortalOrdem() {
		return sisFavoritoPortalOrdem;
	}

	public void setSisFavoritoPortalOrdem(byte sisFavoritoPortalOrdem) {
		this.sisFavoritoPortalOrdem = sisFavoritoPortalOrdem;
	}

	public SisFavorito getSisFavorito() {
		return this.sisFavorito;
	}

	public void setSisFavorito(SisFavorito sisFavorito) {
		this.sisFavorito = sisFavorito;
	}

	public SisUsuario getSisUsuario() {
		return sisUsuario;
	}

	public void setSisUsuario(SisUsuario sisUsuario) {
		this.sisUsuario = sisUsuario;
	}

	public Number getId() {
		return sisFavoritoPortalId;
	}

	public void setId(Number id) {
		sisFavoritoPortalId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { sisFavoritoPortalId + "", sisFavoritoPortalColuna + "", sisFavoritoPortalOrdem + "", sisFavorito.getSisFavoritoId() + "", sisFavorito.getSisFavoritoNome() };
	}

	public Dados getObjeto(String campo) {
		if (campo.startsWith("sisUsuario")) {
			return new SisUsuario();
		} else if (campo.startsWith("sisFavorito")) {
			return new SisFavorito();
		} else {
			return null;
		}
	}

	public void anularDependencia() {
		sisUsuario = null;
		sisFavorito = null;
	}
}