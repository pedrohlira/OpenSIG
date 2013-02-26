package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosF010 extends Bean {

	private String cnpj;

	public DadosF010() {
		reg = "F010";
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

}
