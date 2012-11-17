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
import br.com.opensig.core.shared.modelo.IFavoritoGrafico;

/**
 * Classe que representa um grafico no favorito no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 27/08/2009
 */
@Entity
@Table(name="sis_favorito_grafico")
public class SisFavoritoGrafico extends Dados implements Serializable, IFavoritoGrafico {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sis_favorito_grafico_id")
	private int sisFavoritoGraficoId;

	@Column(name="sis_favorito_grafico_tipo")
	private String sisFavoritoGraficoTipo;
	
	@Column(name="sis_favorito_grafico_busca")
	private String sisFavoritoGraficoBusca;

	@Column(name="sis_favorito_grafico_data")
	private String sisFavoritoGraficoData;

	@Column(name="sis_favorito_grafico_ordem")
	private String sisFavoritoGraficoOrdem;

	@Column(name="sis_favorito_grafico_subx")
	private String sisFavoritoGraficoSubx;

	@Column(name="sis_favorito_grafico_x")
	private String sisFavoritoGraficoX;

	@Column(name="sis_favorito_grafico_y")
	private String sisFavoritoGraficoY;
	
	@Column(name="sis_favorito_grafico_limite")
	private int sisFavoritoGraficoLimite;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "sis_favorito_id")
	private SisFavorito sisFavorito;

    public SisFavoritoGrafico() {
    	this(0);
    }

    public SisFavoritoGrafico(int sisFavoritoGraficoId) {
		super("pu_permissao", "SisFavoritoGrafico", "sisFavoritoGraficoId", "sisFavoritoGraficoId");
		super.setTipoLetra(ELetra.NORMAL);
		this.sisFavoritoGraficoId = sisFavoritoGraficoId;
    }
    
	public int getSisFavoritoGraficoId() {
		return this.sisFavoritoGraficoId;
	}

	public void setSisFavoritoGraficoId(int sisFavoritoGraficoId) {
		this.sisFavoritoGraficoId = sisFavoritoGraficoId;
	}

	public String getSisFavoritoGraficoBusca() {
		return this.sisFavoritoGraficoBusca;
	}

	public void setSisFavoritoGraficoBusca(String sisFavoritoGraficoBusca) {
		this.sisFavoritoGraficoBusca = sisFavoritoGraficoBusca;
	}

	public String getSisFavoritoGraficoData() {
		return this.sisFavoritoGraficoData;
	}

	public void setSisFavoritoGraficoData(String sisFavoritoGraficoData) {
		this.sisFavoritoGraficoData = sisFavoritoGraficoData;
	}

	public String getSisFavoritoGraficoOrdem() {
		return this.sisFavoritoGraficoOrdem;
	}

	public void setSisFavoritoGraficoOrdem(String sisFavoritoGraficoOrdem) {
		this.sisFavoritoGraficoOrdem = sisFavoritoGraficoOrdem;
	}

	public String getSisFavoritoGraficoSubx() {
		return this.sisFavoritoGraficoSubx;
	}

	public void setSisFavoritoGraficoSubx(String sisFavoritoGraficoSubx) {
		this.sisFavoritoGraficoSubx = sisFavoritoGraficoSubx == null ? "" : sisFavoritoGraficoSubx;
	}

	public String getSisFavoritoGraficoX() {
		return this.sisFavoritoGraficoX;
	}

	public void setSisFavoritoGraficoX(String sisFavoritoGraficoX) {
		this.sisFavoritoGraficoX = sisFavoritoGraficoX;
	}

	public String getSisFavoritoGraficoY() {
		return this.sisFavoritoGraficoY;
	}

	public void setSisFavoritoGraficoY(String sisFavoritoGraficoY) {
		this.sisFavoritoGraficoY = sisFavoritoGraficoY;
	}

	public String getSisFavoritoGraficoTipo() {
		return sisFavoritoGraficoTipo;
	}

	public void setSisFavoritoGraficoTipo(String sisFavoritoGraficoTipo) {
		this.sisFavoritoGraficoTipo = sisFavoritoGraficoTipo;
	}

	public int getSisFavoritoGraficoLimite() {
		return sisFavoritoGraficoLimite;
	}

	public void setSisFavoritoGraficoLimite(int sisFavoritoGraficoLimite) {
		this.sisFavoritoGraficoLimite = sisFavoritoGraficoLimite;
	}

	public SisFavorito getSisFavorito() {
		return sisFavorito;
	}

	public void setSisFavorito(SisFavorito sisFavorito) {
		this.sisFavorito = sisFavorito;
	}

	public Number getId() {
		return sisFavoritoGraficoId;
	}

	public void setId(Number id) {
		sisFavoritoGraficoId = id.intValue();
	}

	public String[] toArray() {
		return new String[] {};
	}
}