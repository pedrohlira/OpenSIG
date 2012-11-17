package br.com.opensig.fiscal.server.sped.blocoG;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroG990 extends ARegistro<DadosG990, Dados> {

	@Override
	protected DadosG990 getDados(Dados dados) throws Exception {
		DadosG990 d = new DadosG990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
