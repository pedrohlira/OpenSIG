package br.com.opensig.fiscal.server.sped.contribuicao.blocoF;

import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroF500 extends ARegistro<DadosF500, EmpEmpresa> {

	@Override
	protected DadosF500 getDados(EmpEmpresa dados) throws Exception {
		// calculando o valor vendido
		double bruto = 0.00;
		double desc = 0.00;

		// das vendas
		for (ComVenda venda : vendas) {
			if (venda.getEmpEmpresa().getEmpEmpresaId() == dados.getEmpEmpresaId()) {
				bruto += venda.getComVendaValorLiquido();
			}
		}
		// das z
		for (ComEcfZ z : zs) {
			if (z.getComEcf().getEmpEmpresa().getEmpEmpresaId() == dados.getEmpEmpresaId()) {
				bruto += z.getComEcfZBruto();
				// pegando as canceladas
				for (ComEcfZTotais zt : z.getComEcfZTotais()) {
					if (zt.getComEcfZTotaisCodigo().startsWith("D")) {
						desc += zt.getComEcfZTotaisValor();
					}
				}
			}
		}
		// das notas consumidor
		for (ComEcfNota nota : notas) {
			if (nota.getEmpEmpresa().getEmpEmpresaId() == dados.getEmpEmpresaId()) {
				bruto += nota.getComEcfNotaBruto();
				desc += nota.getComEcfNotaDesconto();
			}
		}
		// outros valores
		double base = bruto - desc;
		double val_pis = base * pis / 100;
		double val_cofins = base * cofins / 100;

		DadosF500 d = new DadosF500();
		d.setVl_rec_caixa(bruto);
		d.setCst_pis("01");
		d.setVl_desc_pis(desc);
		d.setVl_bc_pis(base);
		d.setAliq_pis(pis);
		d.setVl_pis(val_pis);
		d.setCst_cofins("01");
		d.setVl_desc_cofins(desc);
		d.setVl_bc_cofins(base);
		d.setAliq_cofins(cofins);
		d.setVl_cofins(val_cofins);
		return d;
	}
}
