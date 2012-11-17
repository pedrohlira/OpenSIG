package br.com.opensig.fiscal.server.sped.blocoE;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroE001 extends ARegistro<DadosE001, Dados> {

	@Override
	protected DadosE001 getDados(Dados dados) throws Exception {
		DadosE001 d = new DadosE001();
		d.setInd_mov(1);
		return d;
	}

}
