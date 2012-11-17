package br.com.opensig.fiscal.server.sped.blocoD;

import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroD190 extends ARegistro<DadosD190, ComFrete> {

	public static double ICMS_SAIDA = 0.00;
	public static double ICMS_ENTRADA = 0.00;

	@Override
	protected DadosD190 getDados(ComFrete dados) throws Exception {
		DadosD190 d = new DadosD190();
		d.setCst_icms("000");
		d.setCfop(dados.getComFreteCfop());
		d.setAliq_icms(dados.getComFreteAliquota());
		d.setVl_opr(dados.getComFreteValor());
		d.setVl_bc_icms(dados.getComFreteBase());
		d.setVl_icms(dados.getComFreteIcms());
		d.setVl_red_bc(0.00);
		d.setCod_obs("");

		// TODO quando fizer CTe e frete pra saida implementar o total
		if (d.getCfop() != 1605) {
			ICMS_ENTRADA += d.getVl_icms();
		}
		return d;
	}

}
