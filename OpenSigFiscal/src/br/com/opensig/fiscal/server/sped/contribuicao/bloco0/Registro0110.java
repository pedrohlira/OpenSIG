package br.com.opensig.fiscal.server.sped.contribuicao.bloco0;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro0110 extends ARegistro<Dados0110, Dados> {

	@Override
	protected Dados0110 getDados(Dados dados) throws Exception {
		Dados0110 d = new Dados0110();
		d.setCod_inc_trib(2);
		d.setCod_tipo_cont(1);
		d.setInd_reg_cum((Integer.valueOf(auth.getConf().get("sped.contribuicao.0110.ind_reg_cum"))));
		return d;
	}
}
