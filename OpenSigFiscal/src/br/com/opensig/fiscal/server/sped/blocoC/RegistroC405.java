package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.List;
import java.util.Map.Entry;

import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComEcfZTotais;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC405 extends ARegistro<DadosC405, ComEcfZ> {

	@Override
	public void executar() {
		super.executar();

		// impostos federais
		RegistroC410 r410 = new RegistroC410();
		r410.setEscritor(escritor);
		r410.setDados(dados.getComEcfZTotais());
		r410.executar();
		qtdLinhas += r410.getQtdLinhas();

		// sub totais da leitura Z
		RegistroC420 r420 = new RegistroC420();
		r420.setEscritor(escritor);
		for (ComEcfZTotais tot : dados.getComEcfZTotais()) {
			if (tot.getComEcfZTotaisValor() > 0) {
				r420.setDados(tot);
				r420.executar();
				qtdLinhas += r420.getQtdLinhas();
			}
		}

		// vendas da leitura Z
		RegistroC460 r460 = new RegistroC460();
		r460.setEscritor(escritor);
		for (ComEcfVenda venda : dados.getComEcfVendas()) {
			if (venda.getComEcfVendaFechada() && venda.getComEcfVendaLiquido() > 0.00) {
				r460.setDados(venda);
				r460.executar();
				qtdLinhas += r460.getQtdLinhas();
			}
		}

		// analitico
		if (!r460.getAnalitico().isEmpty()) {
			RegistroC490 r490 = new RegistroC490();
			r490.setEscritor(escritor);
			for (Entry<String, List<DadosC470>> entry : r460.getAnalitico().entrySet()) {
				r490.setDados(entry.getValue());
				r490.executar();
				qtdLinhas += r490.getQtdLinhas();
			}
		}
	}

	@Override
	protected DadosC405 getDados(ComEcfZ dados) throws Exception {
		DadosC405 d = new DadosC405();
		d.setDt_doc(dados.getComEcfZMovimento());
		d.setCro(dados.getComEcfZCro());
		d.setCrz(dados.getComEcfZCrz());
		d.setNum_coo_fin(dados.getComEcfZCooFin());
		d.setGt_fin(dados.getComEcfZGt());
		d.setVl_brt(dados.getComEcfZBruto());
		return d;
	}

}
