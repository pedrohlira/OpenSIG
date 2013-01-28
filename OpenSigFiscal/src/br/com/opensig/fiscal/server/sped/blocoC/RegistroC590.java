package br.com.opensig.fiscal.server.sped.blocoC;

import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC590 extends ARegistro<DadosC590, ComConsumo> {

	@Override
	protected DadosC590 getDados(ComConsumo dados) throws Exception {
		DadosC590 d = new DadosC590();
		d.setCfop(dados.getComConsumoCfop());
		d.setAliq_icms(dados.getComConsumoAliquota());
		d.setVl_opr(dados.getComConsumoValor());
		d.setVl_bc_icms(dados.getComConsumoBase());
		d.setVl_icms(dados.getComConsumoIcms());
		d.setCst_icms("000");
		d.setVl_bc_icms_st(0.00);
		d.setVl_icms_st(0.00);
		d.setVl_red_bc(0.00);
		d.setCod_obs("");
		return d;
	}

}
