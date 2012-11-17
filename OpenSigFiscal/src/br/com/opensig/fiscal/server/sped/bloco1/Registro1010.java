package br.com.opensig.fiscal.server.sped.bloco1;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class Registro1010 extends ARegistro<Dados1010, Dados> {

	@Override
	protected Dados1010 getDados(Dados dados) throws Exception {
		Dados1010 d = new Dados1010();
		d.setInd_exp("N");
		d.setInd_ccrf("N");
		d.setInd_comb("N");
		d.setInd_usina("N");
		d.setInd_va("N");
		d.setInd_ee("N");
		d.setInd_cart("N");
		d.setInd_form("N");
		d.setInd_aer("N");
		return d;
	}

}
