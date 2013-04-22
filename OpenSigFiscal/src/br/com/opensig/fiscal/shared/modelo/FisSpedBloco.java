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
 * Classe que representa os Fiscal usados no SPED.
 * 
 * @author Pedro H. Lira
 */
@Entity
@Table(name = "fis_sped_bloco")
public class FisSpedBloco extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fis_sped_bloco_id")
	private int fisSpedBlocoId;

	@Column(name = "fis_sped_bloco_tipo")
	private String fisSpedBlocoTipo;

	@Column(name = "fis_sped_bloco_classe")
	private String fisSpedBlocoClasse;

	@Column(name = "fis_sped_bloco_letra")
	private String fisSpedBlocoLetra;

	@Column(name = "fis_sped_bloco_ordem")
	private int fisSpedBlocoOrdem;

	@Column(name = "fis_sped_bloco_nivel")
	private int fisSpedBlocoNivel;

	@Column(name = "fis_sped_bloco_registro")
	private String fisSpedBlocoRegistro;

	public FisSpedBloco() {
		this(0);
	}

	public FisSpedBloco(int fisSpedBlocoId) {
		super("pu_fiscal", "FisSpedBloco", "fisSpedBlocoId", "fisSpedBlocoOrdem");
		this.fisSpedBlocoId = fisSpedBlocoId;
	}

	public int getFisSpedBlocoId() {
		return fisSpedBlocoId;
	}

	public void setFisSpedBlocoId(int fisSpedBlocoId) {
		this.fisSpedBlocoId = fisSpedBlocoId;
	}

	public String getFisSpedBlocoTipo() {
		return fisSpedBlocoTipo;
	}

	public void setFisSpedBlocoTipo(String fisSpedBlocoTipo) {
		this.fisSpedBlocoTipo = fisSpedBlocoTipo;
	}

	public String getFisSpedBlocoClasse() {
		return fisSpedBlocoClasse;
	}

	public void setFisSpedBlocoClasse(String fisSpedBlocoClasse) {
		this.fisSpedBlocoClasse = fisSpedBlocoClasse;
	}

	public String getFisSpedBlocoLetra() {
		return fisSpedBlocoLetra;
	}

	public void setFisSpedBlocoLetra(String fisSpedBlocoLetra) {
		this.fisSpedBlocoLetra = fisSpedBlocoLetra;
	}

	public int getFisSpedBlocoOrdem() {
		return fisSpedBlocoOrdem;
	}

	public void setFisSpedBlocoOrdem(int fisSpedBlocoOrdem) {
		this.fisSpedBlocoOrdem = fisSpedBlocoOrdem;
	}

	public int getFisSpedBlocoNivel() {
		return fisSpedBlocoNivel;
	}

	public void setFisSpedBlocoNivel(int fisSpedBlocoNivel) {
		this.fisSpedBlocoNivel = fisSpedBlocoNivel;
	}

	public String getFisSpedBlocoRegistro() {
		return fisSpedBlocoRegistro;
	}

	public void setFisSpedBlocoRegistro(String fisSpedBlocoRegistro) {
		this.fisSpedBlocoRegistro = fisSpedBlocoRegistro;
	}

	@Override
	public Number getId() {
		return this.fisSpedBlocoId;
	}

	@Override
	public void setId(Number id) {
		this.fisSpedBlocoId = id.intValue();
	}

	@Override
	public String[] toArray() {
		return null;
	}

}