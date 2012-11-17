package br.com.opensig.fiscal.shared.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.opensig.core.shared.modelo.Dados;

@Entity
@Table(name = "fis_sped_bloco")
public class FisSpedBloco extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fis_sped_bloco_id")
	private int fisSpedBlocoId;

	@Column(name = "fis_sped_bloco_classe")
	private String fisSpedBlocoClasse;

	@Column(name = "fis_sped_bloco_descricao")
	private String fisSpedBlocoDescricao;

	@Column(name = "fis_sped_bloco_icms_ipi")
	private int fisSpedBlocoIcmsIpi;

	@Column(name = "fis_sped_bloco_letra")
	private String fisSpedBlocoLetra;

	@Column(name = "fis_sped_bloco_obrigatorio")
	private int fisSpedBlocoObrigatorio;

	@Column(name = "fis_sped_bloco_ordem")
	private int fisSpedBlocoOrdem;

	@Column(name = "fis_sped_bloco_nivel")
	private int fisSpedBlocoNivel;
	
	@Column(name = "fis_sped_bloco_pis_cofins")
	private int fisSpedBlocoPisCofins;

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
		return this.fisSpedBlocoId;
	}

	public void setFisSpedBlocoId(int fisSpedBlocoId) {
		this.fisSpedBlocoId = fisSpedBlocoId;
	}

	public String getFisSpedBlocoClasse() {
		return this.fisSpedBlocoClasse;
	}

	public void setFisSpedBlocoClasse(String fisSpedBlocoClasse) {
		this.fisSpedBlocoClasse = fisSpedBlocoClasse;
	}

	public String getFisSpedBlocoDescricao() {
		return this.fisSpedBlocoDescricao;
	}

	public void setFisSpedBlocoDescricao(String fisSpedBlocoDescricao) {
		this.fisSpedBlocoDescricao = fisSpedBlocoDescricao;
	}

	public boolean getFisSpedBlocoIcmsIpi() {
		return this.fisSpedBlocoIcmsIpi == 0 ? false : true;
	}

	public void setFisSpedBlocoIcmsIpi(boolean fisSpedBlocoIcmsIpi) {
		this.fisSpedBlocoIcmsIpi = fisSpedBlocoIcmsIpi == false ? 0 : 1;
	}

	public String getFisSpedBlocoLetra() {
		return this.fisSpedBlocoLetra;
	}

	public void setFisSpedBlocoLetra(String fisSpedBlocoLetra) {
		this.fisSpedBlocoLetra = fisSpedBlocoLetra;
	}

	public boolean getFisSpedBlocoObrigatorio() {
		return this.fisSpedBlocoObrigatorio == 0 ? false : true;
	}

	public void setFisSpedBlocoObrigatorio(boolean fisSpedBlocoObrigatorio) {
		this.fisSpedBlocoObrigatorio = fisSpedBlocoObrigatorio == false ? 0 : 1;
	}

	public int getFisSpedBlocoOrdem() {
		return this.fisSpedBlocoOrdem;
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
	
	public boolean getFisSpedBlocoPisCofins() {
		return this.fisSpedBlocoPisCofins == 0 ? false : true;
	}

	public void setFisSpedBlocoPisCofins(boolean fisSpedBlocoPisCofins) {
		this.fisSpedBlocoPisCofins = fisSpedBlocoPisCofins == false ? 0 : 1;
	}

	public String getFisSpedBlocoRegistro() {
		return this.fisSpedBlocoRegistro;
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
		return new String[] { fisSpedBlocoId + "", getFisSpedBlocoIcmsIpi() + "", getFisSpedBlocoPisCofins() + "", fisSpedBlocoLetra, fisSpedBlocoDescricao, fisSpedBlocoRegistro,
				getFisSpedBlocoObrigatorio() + "", fisSpedBlocoClasse, fisSpedBlocoOrdem + "", fisSpedBlocoNivel + "" };
	}

}