package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.List;

import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC190 extends ARegistro<DadosC190, List<DadosC170>> {

	@Override
	protected DadosC190 getDados(List<DadosC170> dados) throws Exception {
		DadosC190 d = new DadosC190();
		for (DadosC170 c170 : dados) {
			d.setCst_icms(c170.getCst_icms());
			d.setCfop(c170.getCfop());
			d.setAliq_icms(c170.getAliq_icms());
			d.setVl_opr(d.getVl_opr() + c170.getVl_item() + c170.getVl_ipi());
			d.setVl_bc_icms(d.getVl_bc_icms() + c170.getVl_bc_icms());
			d.setVl_icms(d.getVl_icms() + c170.getVl_icms());
			d.setVl_bc_icms_st(d.getVl_bc_icms_st() + c170.getVl_bc_icms_st());
			d.setVl_icms_st(d.getVl_icms_st() + c170.getVl_icms_st());
			d.setVl_ipi(d.getVl_ipi() + c170.getVl_ipi());
			d.setCod_obs("");
		}
		return d;
	}

}
