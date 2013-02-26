package br.com.opensig.fiscal.server.sped.contribuicao.blocoA;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroA001 extends ARegistro<DadosA001, Dados> {

	@Override
	protected DadosA001 getDados(Dados dados) throws Exception {
		DadosA001 d = new DadosA001();
		d.setInd_mov(1);
		return d;
	}

}
