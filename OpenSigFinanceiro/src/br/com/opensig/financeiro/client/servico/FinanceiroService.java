package br.com.opensig.financeiro.client.servico;

import java.util.List;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;
import br.com.opensig.financeiro.shared.modelo.FinRemessa;
import br.com.opensig.financeiro.shared.modelo.FinRetorno;

public interface FinanceiroService extends CoreService {

	public String gerar(int boletoId, String tipo, boolean recibo) throws FinanceiroException;

	public Boolean remessa(FinRemessa rememessa) throws FinanceiroException;

	public String[][] retorno(FinRetorno retorno) throws FinanceiroException;
	
	public void excluirRetorno(FinRetorno retorno) throws FinanceiroException;

	public void excluirReceber(FinReceber receber) throws FinanceiroException;
	
	public void excluirPagar(FinPagar pagar) throws FinanceiroException;
	
	public FinRetorno salvarRetorno(FinRetorno retorno, List<FinRecebimento> recebimentos) throws FinanceiroException;
	
	public FinReceber salvarReceber(FinReceber receber, List<FinCategoria> categorias) throws FinanceiroException;
	
	public FinPagar salvarPagar(FinPagar pagar, List<FinCategoria> categorias) throws FinanceiroException;

}
