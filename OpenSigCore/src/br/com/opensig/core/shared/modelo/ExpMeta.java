package br.com.opensig.core.shared.modelo;

import java.io.Serializable;

/**
 * Classe que representa o meta dado da listagem da exportacao.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ExpMeta implements Serializable {

	private String rotulo;
	private Integer tamanho;
	private EBusca grupo;

	/**
	 * Construtor padrao.
	 */
	public ExpMeta() {

	}

	/**
	 * Construtor com os dados inicializados.
	 * 
	 * @param rotulo
	 *            o texto descritivo do campo.
	 * @param tamanho
	 *            o tamanho do campo dentro da listagem.
	 * @param grupo
	 *            o tipo de agrupamento caso o campo tenha.
	 */
	public ExpMeta(String rotulo, Integer tamanho, EBusca grupo) {
		this.rotulo = rotulo;
		this.tamanho = tamanho;
		this.grupo = grupo;
	}

	// Gets e Seteres

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public EBusca getGrupo() {
		return grupo;
	}

	public void setGrupo(EBusca grupo) {
		this.grupo = grupo;
	}

}
