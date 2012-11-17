package br.com.opensig.financeiro.server.boleto;

import br.com.opensig.core.server.exportar.IExportacao;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

public interface IRecibo extends IExportacao {

	public byte[] getRecibo(FinRecebimento boleto);
	
}
