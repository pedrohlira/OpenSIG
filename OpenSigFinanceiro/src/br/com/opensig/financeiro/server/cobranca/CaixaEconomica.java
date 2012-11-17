package br.com.opensig.financeiro.server.cobranca;

import org.jboleto.Banco;

import br.com.opensig.financeiro.client.servico.FinanceiroException;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinRemessa;
import br.com.opensig.financeiro.shared.modelo.FinRetorno;

public class CaixaEconomica extends ACobranca {

	public CaixaEconomica(FinConta conta) {
		super(conta, Banco.CAIXA_ECONOMICA);
	}

	public Boolean remessa(FinRemessa rem) throws FinanceiroException {
		return false;
	}

	public String[][] retorno(FinRetorno ret) throws FinanceiroException {
		return null;
	}

}
