package br.com.opensig.fiscal.server.sped.blocoH;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroH990 extends ARegistro<DadosH990, Dados> {

	@Override
	protected DadosH990 getDados(Dados dados) throws Exception {
		DadosH990 d = new DadosH990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
