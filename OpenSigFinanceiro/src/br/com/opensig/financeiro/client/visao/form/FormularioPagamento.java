package br.com.opensig.financeiro.client.visao.form;

import java.util.Date;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.shared.modelo.FinForma;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;
import br.com.opensig.financeiro.shared.modelo.FinPagar;

public class FormularioPagamento extends AFormularioFinanciado<FinPagamento> {

	public FormularioPagamento(SisFuncao funcao) {
		super(new FinPagamento(), funcao);
		nomes.put("id", "finPagamentoId");
		nomes.put("financeiroId", "finPagar.finPagarId");
		nomes.put("financeiroNome", "finPagar.empEntidade.empEntidadeNome1");
		nomes.put("financeiroConta", "finPagar.finConta.finContaId");
		nomes.put("financeiroEmpresa", "finPagar.empEmpresa");
		nomes.put("documento", "finPagamentoDocumento");
		nomes.put("valor", "finPagamentoValor");
		nomes.put("parcela", "finPagamentoParcela");
		nomes.put("cadastro", "finPagamentoCadastro");
		nomes.put("vencimento", "finPagamentoVencimento");
		nomes.put("status", "finPagamentoStatus");
		nomes.put("realizado", "finPagamentoRealizado");
		nomes.put("conciliado", "finPagamentoConciliado");
		nomes.put("financeiroNfe", "finPagar.finPagarNfe");
		nomes.put("observacao", "finPagamentoObservacao");
		inicializar();
	}

	/*
	 * @see br.com.sig.core.client.visao.lista.IFormulario#setDados()
	 */
	public boolean setDados() {
		classe.setFinForma(new FinForma(Integer.valueOf(cmbForma.getValue())));
		classe.setFinPagar(new FinPagar(Integer.valueOf(hdnFinanceiro.getValueAsString())));
		classe.setFinPagamentoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setFinPagamentoParcela(txtParcela.getValueAsString());
		classe.setFinPagamentoObservacao(txtObservacao.getValueAsString());
		classe.setFinPagamentoCadastro(new Date());
		classe.setFinPagamentoVencimento(dtVencimento.getValue());
		classe.setFinPagamentoStatus(OpenSigCore.i18n.txtAberto().toUpperCase());
		classe.setFinPagamentoRealizado(null);
		classe.setFinPagamentoConciliado(null);

		if (txtValor.getValue() != null) {
			classe.setFinPagamentoValor(txtValor.getValue().doubleValue());
		}
		classe.setFinPagamentoDocumento(txtDocumento.getValueAsString());

		return true;
	}

}
