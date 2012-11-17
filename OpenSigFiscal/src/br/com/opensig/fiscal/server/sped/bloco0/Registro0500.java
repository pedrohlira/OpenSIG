package br.com.opensig.fiscal.server.sped.bloco0;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0500 extends ARegistro<Dados0500, Dados> {

	@Override
	public void executar() {
		if (estoque != null) {
			super.executar();
		}
	}

	@Override
	protected Dados0500 getDados(Dados dados) throws Exception {
		Dados0500 d = new Dados0500();
		d.setDt_alt(fim);
		d.setCod_nat_cc("01");
		d.setInd_cta("S");
		d.setNivel(1);
		d.setCod_cta("ESTOQUE");
		d.setNome_cta("CONTA DE CONTROLE DE ESTOQUE");

		return d;
	}
}
