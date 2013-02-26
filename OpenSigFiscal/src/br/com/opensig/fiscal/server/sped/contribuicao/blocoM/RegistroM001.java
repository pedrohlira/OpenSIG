package br.com.opensig.fiscal.server.sped.contribuicao.blocoM;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroM001 extends ARegistro<DadosM001, Dados> {

	@Override
	protected DadosM001 getDados(Dados dados) throws Exception {
		DadosM001 d = new DadosM001();
		d.setInd_mov(1);
		return d;
	}

}
