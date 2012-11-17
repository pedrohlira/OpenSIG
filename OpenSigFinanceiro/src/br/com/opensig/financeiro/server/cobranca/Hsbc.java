package br.com.opensig.financeiro.server.cobranca;

import org.jboleto.Banco;

import br.com.opensig.financeiro.client.servico.FinanceiroException;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinRemessa;
import br.com.opensig.financeiro.shared.modelo.FinRetorno;

public class Hsbc extends ACobranca {

	public Hsbc(FinConta conta) {
		super(conta, Banco.HSBC);
	}

	public Boolean remessa(FinRemessa rem) throws FinanceiroException {
		return false;
	}

	public String[][] retorno(FinRetorno ret) throws FinanceiroException {
		return null;
	}

}
