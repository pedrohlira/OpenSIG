package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroF001 extends ARegistro<DadosF001, Dados> {

	@Override
	protected DadosF001 getDados(Dados dados) throws Exception {
		DadosF001 d = new DadosF001();
		d.setInd_mov(vendas.size() > 0 || zs.size() > 0 || notas.size() > 0 ? 0 : 1);
		return d;
	}

}
