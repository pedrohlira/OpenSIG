package br.com.opensig.fiscal.server.sped.bloco9;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro9990 extends ARegistro<Dados9990, Dados> {

	@Override
	protected Dados9990 getDados(Dados dados) throws Exception {
		Dados9990 d = new Dados9990();
		d.setQtd_lin(qtdLinhas + 2);
		fimBloco = true;
		return d;
	}
}
