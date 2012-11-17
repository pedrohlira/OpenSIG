package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0990 extends ARegistro<Dados0990, Dados> {

	@Override
	protected Dados0990 getDados(Dados dados) throws Exception {
		Dados0990 d = new Dados0990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
