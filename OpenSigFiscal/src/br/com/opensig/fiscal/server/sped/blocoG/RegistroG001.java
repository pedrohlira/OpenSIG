package br.com.opensig.fiscal.server.sped.blocoG;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroG001 extends ARegistro<DadosG001, Dados> {

	@Override
	protected DadosG001 getDados(Dados dados) throws Exception {
		DadosG001 d = new DadosG001();
		d.setInd_mov(1);
		return d;
	}

}
