package br.com.opensig.fiscal.server.sped.bloco1;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro1990 extends ARegistro<Dados1990, Dados> {

	@Override
	protected Dados1990 getDados(Dados dados) throws Exception {
		Dados1990 d = new Dados1990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
