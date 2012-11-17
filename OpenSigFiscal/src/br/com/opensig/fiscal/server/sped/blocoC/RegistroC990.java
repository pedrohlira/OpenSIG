package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC990 extends ARegistro<DadosC990, Dados> {

	@Override
	protected DadosC990 getDados(Dados dados) throws Exception {
		DadosC990 d = new DadosC990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
