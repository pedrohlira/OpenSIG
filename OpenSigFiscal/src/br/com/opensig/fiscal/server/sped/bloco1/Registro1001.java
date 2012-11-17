package br.com.opensig.fiscal.server.sped.bloco1;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro1001 extends ARegistro<Dados1001, Dados> {

	@Override
	protected Dados1001 getDados(Dados dados) throws Exception {
		Dados1001 d = new Dados1001();
		d.setInd_mov(0);
		return d;
	}

}
