package br.com.opensig.financeiro.client.servico;

import java.util.List;

import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.financeiro.shared.modelo.FinReceber;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class FinanceiroProxy<E extends Dados> extends CoreProxy<E> implements FinanceiroServiceAsync<E> {

	private static final FinanceiroServiceAsync async = (FinanceiroServiceAsync) GWT.create(FinanceiroService.class);
	private static final ServiceDefTarget sdf = (ServiceDefTarget) async;

	public FinanceiroProxy() {
		this(null);
	}

	public FinanceiroProxy(E classe) {
		super.classe = classe;
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "FinanceiroService");
	}
	
	public void gerar(int boletoId, int contaId, String tipo, boolean recibo, AsyncCallback<String> asyncCallback) {
		async.gerar(boletoId, contaId, tipo, recibo, asyncCallback);
	}

	public void salvarReceber(FinReceber receber, List<FinCategoria> categorias, AsyncCallback<FinReceber> asyncCallback) {
		async.salvarReceber(receber, categorias, asyncCallback);
	}

	public void salvarPagar(FinPagar paagr, List<FinCategoria> categorias, AsyncCallback<FinPagar> asyncCallback) {
		async.salvarPagar(paagr, categorias, asyncCallback);
	}

}
