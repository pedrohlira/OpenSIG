package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.List;

import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC490 extends ARegistro<DadosC490, List<DadosC470>> {

	@Override
	protected DadosC490 getDados(List<DadosC470> dados) throws Exception {
		DadosC490 d = new DadosC490();
		for (DadosC470 c470 : dados) {
			d.setCst_icms(c470.getCst_icms());
			d.setCfop(c470.getCfop());
			d.setAliq_icms(c470.getAliq_icms());
			d.setVl_opr(d.getVl_opr() + c470.getVl_item());
			d.setCod_obs("");
		}
		if (d.getAliq_icms() > 0) {
			d.setVl_bc_icms(d.getVl_opr());
			d.setVl_icms(d.getVl_bc_icms() * d.getAliq_icms() / 100);
		}

		return d;
	}

}
