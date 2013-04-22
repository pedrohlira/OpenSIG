package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroF990 extends ARegistro<DadosF990, Dados> {

	@Override
	protected DadosF990 getDados(Dados dados) throws Exception {
		DadosF990 d = new DadosF990();
		d.setQtd_lin(qtdLinhas + 1);
		fimBloco = true;
		return d;
	}
}
