package br.com.opensig.fiscal.server.sped.contribuicao.blocoA;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroA990 extends ARegistro<DadosA990, Dados> {

	@Override
	protected DadosA990 getDados(Dados dados) throws Exception {
		DadosA990 d = new DadosA990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
