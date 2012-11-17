package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.fiscal.server.sped.Bean;

public class DadosC310 extends Bean {

	private int num_doc_canc;

	public DadosC310() {
		reg = "C310";
	}

	public int getNum_doc_canc() {
		return num_doc_canc;
	}

	public void setNum_doc_canc(int num_doc_canc) {
		this.num_doc_canc = num_doc_canc;
	}
}
