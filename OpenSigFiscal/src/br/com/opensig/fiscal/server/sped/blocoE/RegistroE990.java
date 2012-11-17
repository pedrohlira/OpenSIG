package br.com.opensig.fiscal.server.sped.blocoE;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroE990 extends ARegistro<DadosE990, Dados> {

	@Override
	protected DadosE990 getDados(Dados dados) throws Exception {
		DadosE990 d = new DadosE990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
