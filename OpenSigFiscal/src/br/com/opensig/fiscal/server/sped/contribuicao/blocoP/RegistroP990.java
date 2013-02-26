package br.com.opensig.fiscal.server.sped.contribuicao.blocoP;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroP990 extends ARegistro<DadosP990, Dados> {

	@Override
	protected DadosP990 getDados(Dados dados) throws Exception {
		DadosP990 d = new DadosP990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
