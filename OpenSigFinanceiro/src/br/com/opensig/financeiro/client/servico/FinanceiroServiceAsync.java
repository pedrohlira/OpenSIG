package br.com.opensig.financeiro.client.servico;

import java.util.List;

import br.com.opensig.core.client.servico.CoreServiceAsync;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FinanceiroServiceAsync<E extends Dados> extends CoreServiceAsync<E> {

	public abstract void gerar(int boletoId, String tipo, boolean recibo, AsyncCallback<String> asyncCallback);

	public abstract void salvarReceber(FinReceber receber, List<FinCategoria> categorias, AsyncCallback<FinReceber> asyncCallback);
	
	public abstract void salvarPagar(FinPagar paagr, List<FinCategoria> categorias, AsyncCallback<FinPagar> asyncCallback);
}
