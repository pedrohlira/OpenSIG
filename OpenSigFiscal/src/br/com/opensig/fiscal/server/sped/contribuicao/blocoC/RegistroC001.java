package br.com.opensig.fiscal.server.sped.contribuicao.blocoC;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC001 extends ARegistro<DadosC001, Dados> {

	@Override
	protected DadosC001 getDados(Dados dados) throws Exception {
		DadosC001 d = new DadosC001();
		d.setInd_mov(1);
		return d;
	}

}
