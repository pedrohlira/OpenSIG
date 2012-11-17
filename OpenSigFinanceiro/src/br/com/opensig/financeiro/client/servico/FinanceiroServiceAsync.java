package br.com.opensig.financeiro.client.servico;

import java.util.List;

import br.com.opensig.core.client.servico.CoreServiceAsync;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;
import br.com.opensig.financeiro.shared.modelo.FinRemessa;
import br.com.opensig.financeiro.shared.modelo.FinRetorno;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FinanceiroServiceAsync<E extends Dados> extends CoreServiceAsync<E> {

	public abstract void gerar(int boletoId, String tipo, boolean recibo, AsyncCallback<String> asyncCallback);

	public abstract void remessa(FinRemessa remessa, AsyncCallback<Boolean> asyncCallback);

	public abstract void retorno(FinRetorno retorno, AsyncCallback<String[][]> asyncCallback);
	
	public abstract void excluirRetorno(FinRetorno retorno, AsyncCallback asyncCallback);
	
	public abstract void excluirReceber(FinReceber receber, AsyncCallback asyncCallback);
	
	public abstract void excluirPagar(FinPagar paagr, AsyncCallback asyncCallback);
	
	public abstract void salvarRetorno(FinRetorno retorno, List<FinRecebimento> recebimentos, AsyncCallback<FinRetorno> asyncCallback);
	
	public abstract void salvarReceber(FinReceber receber, List<FinCategoria> categorias, AsyncCallback<FinReceber> asyncCallback);
	
	public abstract void salvarPagar(FinPagar paagr, List<FinCategoria> categorias, AsyncCallback<FinPagar> asyncCallback);
}
