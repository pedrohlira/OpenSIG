package br.com.opensig.fiscal.server.sped.bloco9;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro9999 extends ARegistro<Dados9999, Dados> {

	@Override
	protected Dados9999 getDados(Dados dados) throws Exception {
		Dados9999 d = new Dados9999();
		d.setQtd_lin(qtdLinhas + 1);
		return d;
	}
}
