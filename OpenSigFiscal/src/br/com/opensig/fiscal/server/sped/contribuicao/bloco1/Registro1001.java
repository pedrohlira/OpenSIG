package br.com.opensig.fiscal.server.sped.contribuicao.bloco1;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro1001 extends ARegistro<Dados1001, Dados> {

	@Override
	protected Dados1001 getDados(Dados dados) throws Exception {
		Dados1001 d = new Dados1001();
		d.setInd_mov(vendas.size() > 0 || zs.size() > 0 || notas.size() > 0 ? 0 : 1);
		return d;
	}

}
