package br.com.opensig.fiscal.server.sped.contribuicao.blocoM;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroM990 extends ARegistro<DadosM990, Dados> {

	@Override
	protected DadosM990 getDados(Dados dados) throws Exception {
		DadosM990 d = new DadosM990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
