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
import br.com.opensig.core.shared.modelo.ELetra;
import br.com.opensig.core.shared.modelo.IFavoritoCampo;

/**
 * Classe que representa um campo de favorito no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 27/08/2009
 */
@Entity
@Table(name = "sis_favorito_campo")
public class SisFavoritoCampo extends Dados implements Serializable, IFavoritoCampo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_favorito_campo_id")
	private int sisFavoritoCampoId;

	@Column(name = "sis_favorito_campo_filtro1_compara")
	private String sisFavoritoCampoFiltro1Compara;

	@Column(name = "sis_favorito_campo_filtro1_valor")
	private String sisFavoritoCampoFiltro1Valor;

	@Column(name = "sis_favorito_campo_filtro2_compara")
	private String sisFavoritoCampoFiltro2Compara;

	@Column(name = "sis_favorito_campo_filtro2_valor")
	private String sisFavoritoCampoFiltro2Valor;

	@Column(name = "sis_favorito_campo_nome")
	private String sisFavoritoCampoNome;

	@Column(name = "sis_favorito_campo_tipo")
	private String sisFavoritoCampoTipo;

	@Column(name = "sis_favorito_campo_visivel")
	private boolean sisFavoritoCampoVisivel;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "sis_favorito_id")
	private SisFavorito sisFavorito;

	public SisFavoritoCampo() {
		this(0);
	}

	public SisFavoritoCampo(int sisFavoritoCampoId) {
		super("pu_permissao", "SisFavoritoCampo", "sisFavoritoCampoId", "sisFavoritoCampoNome");
		super.setTipoLetra(ELetra.NORMAL);
		this.sisFavoritoCampoId = sisFavoritoCampoId;
	}

	public int getSisFavoritoCampoId() {
		return this.sisFavoritoCampoId;
	}

	public void setSisFavoritoCampoId(int sisFavoritoCampoId) {
		this.sisFavoritoCampoId = sisFavoritoCampoId;
	}

	public String getSisFavoritoCampoFiltro1Compara() {
		return this.sisFavoritoCampoFiltro1Compara;
	}

	public void setSisFavoritoCampoFiltro1Compara(String sisFavoritoCampoFiltro1Compara) {
		this.sisFavoritoCampoFiltro1Compara = sisFavoritoCampoFiltro1Compara;
	}

	public String getSisFavoritoCampoFiltro1Valor() {
		return this.sisFavoritoCampoFiltro1Valor;
	}

	public void setSisFavoritoCampoFiltro1Valor(String sisFavoritoCampoFiltro1Valor) {
		this.sisFavoritoCampoFiltro1Valor = sisFavoritoCampoFiltro1Valor;
	}

	public String getSisFavoritoCampoFiltro2Compara() {
		return this.sisFavoritoCampoFiltro2Compara;
	}

	public void setSisFavoritoCampoFiltro2Compara(String sisFavoritoCampoFiltro2Compara) {
		this.sisFavoritoCampoFiltro2Compara = sisFavoritoCampoFiltro2Compara;
	}

	public String getSisFavoritoCampoFiltro2Valor() {
		return this.sisFavoritoCampoFiltro2Valor;
	}

	public void setSisFavoritoCampoFiltro2Valor(String sisFavoritoCampoFiltro2Valor) {
		this.sisFavoritoCampoFiltro2Valor = sisFavoritoCampoFiltro2Valor;
	}

	public String getSisFavoritoCampoNome() {
		return this.sisFavoritoCampoNome;
	}

	public void setSisFavoritoCampoNome(String sisFavoritoCampoNome) {
		this.sisFavoritoCampoNome = sisFavoritoCampoNome;
	}

	public String getSisFavoritoCampoTipo() {
		return this.sisFavoritoCampoTipo;
	}

	public void setSisFavoritoCampoTipo(String sisFavoritoCampoTipo) {
		this.sisFavoritoCampoTipo = sisFavoritoCampoTipo;
	}

	public boolean getSisFavoritoCampoVisivel() {
		return this.sisFavoritoCampoVisivel;
	}

	public void setSisFavoritoCampoVisivel(boolean sisFavoritoCampoVisivel) {
		this.sisFavoritoCampoVisivel = sisFavoritoCampoVisivel;
	}

	public SisFavorito getSisFavorito() {
		return this.sisFavorito;
	}

	public void setSisFavorito(SisFavorito sisFavorito) {
		this.sisFavorito = sisFavorito;
	}

	public Number getId() {
		return sisFavoritoCampoId;
	}

	public void setId(Number id) {
		sisFavoritoCampoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] {};
	}
}