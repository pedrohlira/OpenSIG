package br.com.opensig.financeiro.client.servico;

import java.util.List;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;

public interface FinanceiroService extends CoreService {

	public String gerar(int boletoId, String tipo, boolean recibo) throws FinanceiroException;

	public FinReceber salvarReceber(FinReceber receber, List<FinCategoria> categorias) throws FinanceiroException;
	
	public FinPagar salvarPagar(FinPagar pagar, List<FinCategoria> categorias) throws FinanceiroException;

}
