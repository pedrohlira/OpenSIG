package br.com.opensig.fiscal.client.servico;

import java.util.Map;

import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.shared.modelo.FisCertificado;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class FiscalProxy<E extends Dados> extends CoreProxy<E> implements FiscalServiceAsync<E> {

	private static final FiscalServiceAsync async = (FiscalServiceAsync) GWT.create(FiscalService.class);
	private static final ServiceDefTarget sdf = (ServiceDefTarget) async;

	public FiscalProxy() {
		this(null);
	}

	public FiscalProxy(E classe) {
		super.classe = classe;
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "FiscalService");
	}

	@Override
	public void analisarNFeSaida(FisNotaSaida saida, AsyncCallback<Map<String, String>> asyncCallback) {
		async.analisarNFeSaida(saida, asyncCallback);
	}

	@Override
	public void analisarNFeEntrada(FisNotaEntrada entrada, AsyncCallback<Map<String, String>> asyncCallback) {
		async.analisarNFeEntrada(entrada, asyncCallback);
	}

	@Override
	public void exportar(String arquivo, String nome, String tipo, AsyncCallback<String> asyncCallback) {
		async.exportar(arquivo, nome, tipo, asyncCallback);
	}

	@Override
	public void backup(E classe, IFiltro filtro, AsyncCallback<String> asyncCallback) {
		async.backup(classe, filtro, asyncCallback);
	}

	@Override
	public void status(int ambiente, int uf, AsyncCallback<String> asyncallback) {
		async.status(ambiente, uf, asyncallback);
	}

	@Override
	public void validar(int ambiente, IFiltro filtro, boolean auto, AsyncCallback<String> asyncallback) {
		async.validar(ambiente, filtro, auto, asyncallback);
	}

	@Override
	public void situacao(int ambiente, String chave, AsyncCallback<String> asyncallback) {
		async.situacao(ambiente, chave, asyncallback);
	}

	@Override
	public void cadastro(int ambiente, int ibge, String uf, String tipo, String doc, AsyncCallback<String> asyncallback) {
		async.cadastro(ambiente, ibge, uf, tipo, doc, asyncallback);
	}

	@Override
	public void enviarNFe(String xml, AsyncCallback<String> asyncallback) {
		async.enviarNFe(xml, asyncallback);
	}

	@Override
	public void receberNFe(String xml, String recibo, AsyncCallback<String> asyncallback) {
		async.receberNFe(xml, recibo, asyncallback);
	}

	@Override
	public void cancelarSaida(FisNotaSaida saida, String motivo, AsyncCallback<Map<String, String>> asyncCallback) {
		async.cancelarSaida(saida, motivo, asyncCallback);
	};

	@Override
	public void cancelarEntrada(FisNotaEntrada entrada, String motivo, AsyncCallback<Map<String, String>> asyncCallback) {
		async.cancelarEntrada(entrada, motivo, asyncCallback);
	};

	@Override
	public void cancelar(String xml, AsyncCallback<String> asyncallback) {
		async.cancelar(xml, asyncallback);
	}

	@Override
	public void inutilizar(String xml, AsyncCallback<String> asyncallback) {
		async.inutilizar(xml, asyncallback);
	}

	@Override
	public void inutilizarSaida(FisNotaSaida saida, String motivo, int ini, int fim, AsyncCallback<Map<String, String>> asyncCallback) {
		async.inutilizarSaida(saida, motivo, ini, fim, asyncCallback);
	};

	@Override
	public void inutilizarEntrada(FisNotaEntrada entrada, String motivo, int ini, int fim, AsyncCallback<Map<String, String>> asyncCallback) {
		async.inutilizarEntrada(entrada, motivo, ini, fim, asyncCallback);
	};

	@Override
	public void salvarSaida(String xml, FisNotaStatus status, AsyncCallback<Map<String, String>> asyncCallback) {
		async.salvarSaida(xml, status, asyncCallback);
	}

	@Override
	public void salvarEntrada(String xml, FisNotaStatus status, AsyncCallback<Map<String, String>> asyncCallback) {
		async.salvarEntrada(xml, status, asyncCallback);
	}

	@Override
	public void salvarCertificado(FisCertificado certificado, AsyncCallback asyncCallback) {
		async.salvarCertificado(certificado, asyncCallback);
	}

	@Override
	public void salvar(AsyncCallback<E> asyncCallback) {
		this.salvar(classe, asyncCallback);
	}

	@Override
	public void salvar(E unidade, AsyncCallback<E> asyncCallback) {
		async.salvar(unidade, asyncCallback);
	}

	@Override
	public void deletar(AsyncCallback<E> asyncCallback) {
		this.deletar(classe, asyncCallback);
	}

	@Override
	public void deletar(E unidade, AsyncCallback<E> asyncCallback) {
		async.deletar(unidade, asyncCallback);
	}
}
