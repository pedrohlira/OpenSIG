package br.com.opensig.fiscal.server.sped.blocoD;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroD590 extends ARegistro<DadosD590, List<ComConsumo>> {

	@Override
	protected DadosD590 getDados(List<ComConsumo> dados) throws Exception {
		DadosD590 d = new DadosD590();
		for (ComConsumo consumo : dados) {
			d.setCfop(consumo.getComConsumoCfop());
			d.setAliq_icms(consumo.getComConsumoAliquota());
			d.setVl_opr(somarDoubles(d.getVl_opr(), consumo.getComConsumoValor()));
			d.setVl_bc_icms(somarDoubles(d.getVl_bc_icms(), consumo.getComConsumoBase()));
			d.setVl_icms(somarDoubles(d.getVl_icms(), consumo.getComConsumoIcms()));
		}
		d.setCst_icms("000");
		d.setVl_bc_icms_uf(0.00);
		d.setVl_icms_uf(0.00);
		d.setVl_red_bc(0.00);
		d.setCod_obs("");
		return d;
	}

}
