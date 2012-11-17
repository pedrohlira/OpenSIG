package br.com.opensig.comercial.client.servico;

import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComValorProduto;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.servico.CoreServiceAsync;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ComercialServiceAsync<E extends Dados> extends CoreServiceAsync<E> {

	public abstract void gerarNfe(ComVenda venda, ComFrete frete, AsyncCallback<FisNotaSaida> asyncCallback);
	
	public abstract void gerarNfe(ComCompra compra, ComFrete frete, AsyncCallback<FisNotaEntrada> asyncCallback);

	public abstract void fecharCompra(ComCompra compra, AsyncCallback asyncCallback);

	public abstract void fecharVenda(ComVenda venda, AsyncCallback<String[][]> asyncCallback);

	public abstract void fecharFrete(ComFrete frete, AsyncCallback asyncCallback);

	public abstract void fecharEcfVenda(ComEcfVenda venda, AsyncCallback<String[][]> asyncCallback);
	
	public abstract void salvarCompra(ComCompra compra, AsyncCallback<ComCompra> asyncCallback);

	public abstract void salvarVenda(ComVenda venda, AsyncCallback<ComVenda> asyncCallback);

	public abstract void salvarValor(ComValorProduto valor, AsyncCallback<ComValorProduto> asyncCallback);

	public abstract void salvarEcfVenda(ComEcfVenda venda, AsyncCallback<ComEcfVenda> asyncCallback);
	
	public abstract void salvarEcfZ(ComEcfZ z, AsyncCallback<ComEcfZ> asyncCallback);
	
	public abstract void excluirCompra(ComCompra compra, AsyncCallback asyncCallback);

	public abstract void excluirVenda(ComVenda venda, AsyncCallback asyncCallback);

	public abstract void cancelarVenda(ComVenda venda, AsyncCallback asyncCallback);

	public abstract void excluirFrete(ComFrete frete, AsyncCallback asyncCallback);
	
	public abstract void excluirEcfVenda(ComEcfVenda venda, AsyncCallback asyncCallback);

	public abstract void importarEcfVenda(List<String> nomesArquivos, AsyncCallback<Map<String, Integer>> asyncCallback);
	
}
