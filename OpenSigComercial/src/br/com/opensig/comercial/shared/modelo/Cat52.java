package br.com.opensig.comercial.shared.modelo;

import br.com.opensig.core.shared.modelo.Dados;

public class Cat52 extends Dados {

	private String arquivo;
	private int vendas;
	private int vendaNfechadas;
	private int prodNachados;
	private String erro;
	
	public Cat52() {
		super("", "", "");
	}
	
	@Override
	public Number getId() {
		return null;
	}

	@Override
	public void setId(Number id) {
	}

	@Override
	public String[] toArray() {
		return null;
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public int getVendas() {
		return vendas;
	}

	public void setVendas(int vendas) {
		this.vendas = vendas;
	}

	public int getVendaNfechadas() {
		return vendaNfechadas;
	}

	public void setVendaNfechadas(int vendaNfechadas) {
		this.vendaNfechadas = vendaNfechadas;
	}

	public int getProdNachados() {
		return prodNachados;
	}

	public void setProdNachados(int prodNachados) {
		this.prodNachados = prodNachados;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

}
