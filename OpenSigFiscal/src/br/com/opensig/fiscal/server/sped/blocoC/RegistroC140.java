package br.com.opensig.fiscal.server.sped.blocoC;

import java.util.Collections;
import java.util.Comparator;

import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;
import br.com.opensig.fiscal.server.sped.ARegistro;

public class RegistroC140<T extends Dados> extends ARegistro<DadosC140, T> {

	@Override
	public void executar() {
		super.executar();
		
		if (dados instanceof FinReceber) {
			FinReceber receber = (FinReceber) dados;
			RegistroC141<FinRecebimento> r141 = new RegistroC141<FinRecebimento>();
			r141.setEscritor(escritor);
			Collections.sort(receber.getFinRecebimentos(), new Comparator<FinRecebimento>() {
				public int compare(FinRecebimento o1, FinRecebimento o2) {
					return o1.getFinRecebimentoParcela().compareTo(o2.getFinRecebimentoParcela());
				}
			});
			for (FinRecebimento rec : receber.getFinRecebimentos()) {
				r141.setDados(rec);
				r141.executar();
				qtdLinhas += r141.getQtdLinhas();
			}
		} else if (dados instanceof FinPagar) {
			FinPagar pagar = (FinPagar) dados;
			RegistroC141<FinPagamento> r141 = new RegistroC141<FinPagamento>();
			r141.setEscritor(escritor);
			Collections.sort(pagar.getFinPagamentos(), new Comparator<FinPagamento>() {
				public int compare(FinPagamento o1, FinPagamento o2) {
					return o1.getFinPagamentoParcela().compareTo(o2.getFinPagamentoParcela());
				}
			});
			for (FinPagamento rec : pagar.getFinPagamentos()) {
				r141.setDados(rec);
				r141.executar();
				qtdLinhas += r141.getQtdLinhas();
			}
		}
	}

	@Override
	protected DadosC140 getDados(T dados) throws Exception {
		if (dados instanceof FinReceber) {
			return getReceber((FinReceber) dados);
		} else if (dados instanceof FinPagar) {
			return getPagar((FinPagar) dados);
		} else {
			return null;
		}
	}

	private DadosC140 getReceber(FinReceber receber) {
		DadosC140 d = new DadosC140();
		d.setInd_emit("0");
		d.setInd_tit("99");
		d.setDesc_tit(receber.getFinRecebimentos().get(0).getFinForma().getFinFormaDescricao());
		d.setNum_tit(receber.getFinRecebimentos().get(0).getFinRecebimentoDocumento());
		d.setQtd_parc(receber.getFinRecebimentos().size());
		d.setVl_tit(receber.getFinReceberValor());
		return d;
	}

	private DadosC140 getPagar(FinPagar pagar) {
		DadosC140 d = new DadosC140();
		d.setInd_emit("1");
		d.setInd_tit("99");
		d.setDesc_tit(pagar.getFinPagamentos().get(0).getFinForma().getFinFormaDescricao());
		d.setNum_tit(pagar.getFinPagamentos().get(0).getFinPagamentoDocumento());
		d.setQtd_parc(pagar.getFinPagamentos().size());
		d.setVl_tit(pagar.getFinPagarValor());
		return d;
	}
}
