package br.com.opensig.fiscal.server.sped.blocoD;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroD190 extends ARegistro<DadosD190, List<ComFrete>> {

	@Override
	protected DadosD190 getDados(List<ComFrete> dados) throws Exception {
		DadosD190 d = new DadosD190();
		for (ComFrete frete : dados) {
			d.setCfop(frete.getComFreteCfop());
			d.setAliq_icms(frete.getComFreteAliquota());
			d.setVl_opr(somarDoubles(d.getVl_opr(), frete.getComFreteValor()));
			d.setVl_bc_icms(somarDoubles(d.getVl_bc_icms(), frete.getComFreteBase()));
			d.setVl_icms(somarDoubles(d.getVl_icms(), frete.getComFreteIcms()));
		}
		d.setCst_icms("000");
		d.setVl_red_bc(0.00);
		d.setCod_obs("");
		return d;
	}

}
