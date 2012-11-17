package br.com.opensig.fiscal.server.sped.bloco9;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro9001 extends ARegistro<Dados9001, Dados> {

	@Override
	protected Dados9001 getDados(Dados dados) throws Exception {
		Dados9001 d = new Dados9001();
		d.setInd_mov(0);
		return d;
	}

}
