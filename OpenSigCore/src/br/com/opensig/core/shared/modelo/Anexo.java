package br.com.opensig.core.shared.modelo;

/**
 * Classe que representa um anexo de email no sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Anexo {

	private String nome;
	private String tipo;
	private byte[] dados;

	/**
	 * Construtor que recebe o nome, tipo e array de bytes.
	 * 
	 * @param nome
	 *            do arquivo.
	 * @param tipo
	 *            do arquivo.
	 * @param dados
	 *            em bytes do arquivo.
	 */
	public Anexo(String nome, String tipo, byte[] dados) {
		this.nome = nome;
		this.tipo = tipo;
		this.dados = dados;
	}

	// Gets e Seteres

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public byte[] getDados() {
		return dados;
	}

	public void setDados(byte[] dados) {
		this.dados = dados;
	}

}
