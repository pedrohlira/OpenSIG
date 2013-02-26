package br.com.opensig.fiscal.server.sped.contribuicao.blocoD;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroD001 extends ARegistro<DadosD001, Dados> {

	@Override
	protected DadosD001 getDados(Dados dados) throws Exception {
		DadosD001 d = new DadosD001();
		d.setInd_mov(1);
		return d;
	}

}
