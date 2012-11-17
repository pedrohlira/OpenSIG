package br.com.opensig.core.shared.modelo.sistema;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ELetra;

/**
 * Classe que representa uma tipo de exportacao ou importacao de dados.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
@Entity
@Table(name = "sis_exp_imp")
public class SisExpImp extends Dados implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sis_exp_imp_id")
	private int sisExpImpId;

	@Column(name = "sis_exp_imp_classe")
	private String sisExpImpClasse;

	@Column(name = "sis_exp_imp_descricao")
	private String sisExpImpDescricao;

	@Column(name = "sis_exp_imp_extensoes")
	private String sisExpImpExtensoes;

	@Column(name = "sis_exp_imp_funcao")
	private String sisExpImpFuncao;

	@Column(name = "sis_exp_imp_imagem")
	private String sisExpImpImagem;

	@Lob()
	@Column(name = "sis_exp_imp_modelo")
	private String sisExpImpModelo;

	@Column(name = "sis_exp_imp_nome")
	private String sisExpImpNome;

	@Column(name = "sis_exp_imp_tipo")
	private String sisExpImpTipo;

	@Transient
	private int inicio;

	@Transient
	private int limite;

	@Transient
	private String[] outros;

	public SisExpImp() {
		this(0);
	}

	public SisExpImp(int sisExpImpId) {
		super("pu_core", "SisExpImp", "sisExpImpId", "sisExpImpNome");
		this.sisExpImpId = sisExpImpId;
		setTipoLetra(ELetra.NORMAL);
	}

	public int getSisExpImpId() {
		return this.sisExpImpId;
	}

	public void setSisExpImpId(int sisExpImpId) {
		this.sisExpImpId = sisExpImpId;
	}

	public String getSisExpImpClasse() {
		return this.sisExpImpClasse;
	}

	public void setSisExpImpClasse(String sisExpImpClasse) {
		this.sisExpImpClasse = sisExpImpClasse;
	}

	public String getSisExpImpDescricao() {
		return this.sisExpImpDescricao;
	}

	public void setSisExpImpDescricao(String sisExpImpDescricao) {
		this.sisExpImpDescricao = sisExpImpDescricao;
	}

	public String getSisExpImpExtensoes() {
		return this.sisExpImpExtensoes;
	}

	public void setSisExpImpExtensoes(String sisExpImpExtensoes) {
		this.sisExpImpExtensoes = sisExpImpExtensoes;
	}

	public String getSisExpImpFuncao() {
		return this.sisExpImpFuncao;
	}

	public void setSisExpImpFuncao(String sisExpImpFuncao) {
		this.sisExpImpFuncao = sisExpImpFuncao;
	}

	public String getSisExpImpImagem() {
		return this.sisExpImpImagem;
	}

	public void setSisExpImpImagem(String sisExpImpImagem) {
		this.sisExpImpImagem = sisExpImpImagem;
	}

	public String getSisExpImpModelo() {
		return this.sisExpImpModelo;
	}

	public void setSisExpImpModelo(String sisExpImpModelo) {
		this.sisExpImpModelo = sisExpImpModelo;
	}

	public String getSisExpImpNome() {
		return this.sisExpImpNome;
	}

	public void setSisExpImpNome(String sisExpImpNome) {
		this.sisExpImpNome = sisExpImpNome;
	}

	public String getSisExpImpTipo() {
		return this.sisExpImpTipo;
	}

	public void setSisExpImpTipo(String sisExpImpTipo) {
		this.sisExpImpTipo = sisExpImpTipo;
	}

	public int getInicio() {
		return inicio;
	}

	public void setInicio(int inicio) {
		this.inicio = inicio;
	}

	public int getLimite() {
		return limite;
	}

	public void setLimite(int limite) {
		this.limite = limite;
	}

	public String[] getOutros() {
		return outros;
	}

	public void setOutros(String[] outros) {
		this.outros = outros;
	}

	public Number getId() {
		return sisExpImpId;
	}

	public void setId(Number id) {
		sisExpImpId = id.intValue();
	}

	public String[] toArray() {
		return new String[] { sisExpImpId + "", sisExpImpNome, sisExpImpExtensoes, sisExpImpDescricao, sisExpImpImagem, sisExpImpClasse, sisExpImpFuncao, sisExpImpTipo, sisExpImpModelo };
	}

}
