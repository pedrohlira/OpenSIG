package br.com.opensig.fiscal.server.sped.contribuicao.blocoP;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroP001 extends ARegistro<DadosP001, Dados> {

	@Override
	protected DadosP001 getDados(Dados dados) throws Exception {
		DadosP001 d = new DadosP001();
		d.setInd_mov(1);
		return d;
	}

}
