package br.com.opensig.comercial.client.servico;

import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComValorProduto;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ComercialProxy<E extends Dados> extends CoreProxy<E> implements ComercialServiceAsync<E> {

	private ComercialServiceAsync async = (ComercialServiceAsync) GWT.create(ComercialService.class);
	private ServiceDefTarget sdf = (ServiceDefTarget) async;

	public ComercialProxy() {
		this(null);
	}

	public ComercialProxy(E classe) {
		super.classe = classe;
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "ComercialService");
	}

	@Override
	public void gerarNfe(ComVenda venda, ComFrete frete, AsyncCallback<FisNotaSaida> asyncCallback) {
		async.gerarNfe(venda, frete, asyncCallback);
	}

	@Override
	public void gerarNfe(ComCompra compra, ComFrete frete, AsyncCallback<FisNotaEntrada> asyncCallback) {
		async.gerarNfe(compra, frete, asyncCallback);
	}
	
	@Override
	public void fecharCompra(ComCompra compra, AsyncCallback asyncCallback) {
		async.fecharCompra(compra, asyncCallback);
	}

	@Override
	public void fecharVenda(ComVenda venda, AsyncCallback<String[][]> asyncCallback) {
		async.fecharVenda(venda, asyncCallback);
	}

	@Override
	public void fecharFrete(ComFrete frete, AsyncCallback asyncCallback) {
		async.fecharFrete(frete, asyncCallback);
	}

	@Override
	public void salvarCompra(ComCompra compra, AsyncCallback<ComCompra> asyncCallback) {
		async.salvarCompra(compra, asyncCallback);
	}

	@Override
	public void salvarVenda(ComVenda venda, AsyncCallback<ComVenda> asyncCallback) {
		async.salvarVenda(venda, asyncCallback);
	}

	@Override
	public void salvarValor(ComValorProduto valor, AsyncCallback<ComValorProduto> asyncCallback) {
		async.salvarValor(valor, asyncCallback);
	}

	@Override
	public void salvarEcfZ(ComEcfZ z, AsyncCallback<ComEcfZ> asyncCallback) {
		async.salvarEcfZ(z, asyncCallback);
	}
	
	@Override
	public void excluirCompra(ComCompra compra, AsyncCallback asyncCallback) {
		async.excluirCompra(compra, asyncCallback);
	}

	@Override
	public void excluirVenda(ComVenda venda, AsyncCallback asyncCallback) {
		async.excluirVenda(venda, asyncCallback);
	}

	@Override
	public void cancelarVenda(ComVenda venda, AsyncCallback asyncCallback) {
		async.cancelarVenda(venda, asyncCallback);
	}

	@Override
	public void excluirFrete(ComFrete frete, AsyncCallback asyncCallback) {
		async.excluirFrete(frete, asyncCallback);
	}

	@Override
	public void importarEcfVenda(List<String> nomesArquivos, AsyncCallback<Map<String, Integer>> asyncCallback) {
		async.importarEcfVenda(nomesArquivos, asyncCallback);
	}

	@Override
	public void salvarEcfVenda(ComEcfVenda venda, AsyncCallback<ComEcfVenda> asyncCallback) {
		async.salvarEcfVenda(venda, asyncCallback);
	}

	@Override
	public void fecharEcfVenda(ComEcfVenda venda, AsyncCallback<String[][]> asyncCallback) {
		async.fecharEcfVenda(venda, asyncCallback);
	}
	
	@Override
	public void excluirEcfVenda(ComEcfVenda venda, AsyncCallback asyncCallback) {
		async.excluirEcfVenda(venda, asyncCallback);
	}
}
