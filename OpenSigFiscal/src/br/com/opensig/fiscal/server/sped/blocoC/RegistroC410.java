package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC410 extends ARegistro<DadosC410, List<ComEcfZTotais>> {

	@Override
	protected DadosC410 getDados(List<ComEcfZTotais> dados) throws Exception {
		// recupera o valor liquido total vendido no dia
		double liquido = 0.00;
		for (ComEcfZTotais t : dados) {
			if (!t.getComEcfZTotaisCodigo().equals("OPNF") && !t.getComEcfZTotaisCodigo().startsWith("D") && !t.getComEcfZTotaisCodigo().startsWith("C")) {
				liquido += t.getComEcfZTotaisValor();
			}
		}

		DadosC410 d = new DadosC410();
		d.setVl_pis(liquido * pis / 100);
		d.setVl_cofins(liquido * cofins / 100);
		return d;
	}
}
