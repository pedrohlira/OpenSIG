package br.com.opensig.financeiro.client.visao.form;

import java.util.Date;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.shared.modelo.FinForma;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public class FormularioRecebimento extends AFormularioFinanciado<FinRecebimento> {

	public FormularioRecebimento(SisFuncao funcao) {
		super(new FinRecebimento(), funcao);
		nomes.put("id", "finRecebimentoId");
		nomes.put("financeiroId", "finReceber.finReceberId");
		nomes.put("financeiroNome", "finReceber.empEntidade.empEntidadeNome1");
		nomes.put("financeiroConta", "finReceber.finConta.finContaId");
		nomes.put("financeiroEmpresa", "finReceber.empEmpresa");
		nomes.put("documento", "finRecebimentoDocumento");
		nomes.put("valor", "finRecebimentoValor");
		nomes.put("parcela", "finRecebimentoParcela");
		nomes.put("cadastro", "finRecebimentoCadastro");
		nomes.put("vencimento", "finRecebimentoVencimento");
		nomes.put("status", "finRecebimentoStatus");
		nomes.put("realizado", "finRecebimentoRealizado");
		nomes.put("conciliado", "finRecebimentoConciliado");
		nomes.put("financeiroNfe", "finReceber.finReceberNfe");
		nomes.put("observacao", "finRecebimentoObservacao");
		inicializar();
	}

	/*
	 * @see br.com.sig.core.client.visao.lista.IFormulario#setDados()
	 */
	public boolean setDados() {
		classe.setFinForma(new FinForma(Integer.valueOf(cmbForma.getValue())));
		classe.setFinReceber(new FinReceber(Integer.valueOf(hdnFinanceiro.getValueAsString())));
		classe.setFinRecebimentoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setFinRecebimentoParcela(txtParcela.getValueAsString());
		classe.setFinRecebimentoObservacao(txtObservacao.getValueAsString());
		classe.setFinRecebimentoVencimento(dtVencimento.getValue());
		classe.setFinRecebimentoCadastro(new Date());
		classe.setFinRecebimentoStatus(OpenSigCore.i18n.txtAberto().toUpperCase());
		classe.setFinRecebimentoRealizado(null);
		classe.setFinRecebimentoConciliado(null);

		if (txtValor.getValue() != null) {
			classe.setFinRecebimentoValor(txtValor.getValue().doubleValue());
		}
		classe.setFinRecebimentoDocumento(txtDocumento.getValueAsString());
		return true;
	}

}
