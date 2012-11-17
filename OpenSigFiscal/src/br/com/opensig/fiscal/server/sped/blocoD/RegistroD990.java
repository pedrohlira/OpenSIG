package br.com.opensig.fiscal.server.sped.blocoD;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroD990 extends ARegistro<DadosD990, Dados> {

	@Override
	protected DadosD990 getDados(Dados dados) throws Exception {
		DadosD990 d = new DadosD990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
