package br.com.opensig.fiscal.client.servico;

import java.util.Map;

import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreServiceAsync;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.shared.modelo.FisCertificado;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FiscalServiceAsync<E extends Dados> extends CoreServiceAsync<E> {

	public abstract void backup(E classe, IFiltro filtro, AsyncCallback<String> asyncCallback);

	public abstract void exportar(String arquivo, String nome, String tipo, AsyncCallback<String> asyncCallback);
	
	public abstract void status(int ambiente, int uf, int empresa, AsyncCallback<String> asyncallback);

	public abstract void validar(int ambiente, IFiltro filtro, int empresa, AsyncCallback<String> asyncallback);
	
	public abstract void situacao(int ambiente, String chave, int empresa, AsyncCallback<String> asyncallback);

	public abstract void cadastro(int ambiente, int ibge, String uf, String tipo, String doc, int empresa, AsyncCallback<String> asyncallback);

	public abstract void enviarNFe(String xml, int empresa, AsyncCallback<String> asyncallback);

	public abstract void receberNFe(String xml, int empresa, String recibo, AsyncCallback<String> asyncallback);

	public abstract void analisarNFeSaida(FisNotaSaida saida, AsyncCallback<Map<String, String>> asyncCallback);
	
	public abstract void analisarNFeEntrada(FisNotaEntrada entrada, AsyncCallback<Map<String, String>> asyncCallback);

	public abstract void cancelar(String xml, int empresa, AsyncCallback<String> asyncallback);

	public abstract void cancelarSaida(FisNotaSaida saida, String motivo, AsyncCallback<Map<String, String>> asyncCallback);
	
	public abstract void cancelarEntrada(FisNotaEntrada entrada, String motivo, AsyncCallback<Map<String, String>> asyncCallback);

	public abstract void inutilizar(String xml, int empresa, AsyncCallback<String> asyncallback);

	public abstract void inutilizarSaida(FisNotaSaida saida, String motivo, int ini, int fim, AsyncCallback<Map<String, String>> asyncCallback);
	
	public abstract void inutilizarEntrada(FisNotaEntrada entrada, String motivo, int ini, int fim, AsyncCallback<Map<String, String>> asyncCallback);

	public abstract void salvarSaida(String xml, FisNotaStatus status, EmpEmpresa empresa, AsyncCallback<Map<String, String>> asyncCallback);
	
	public abstract void salvarEntrada(String xml, FisNotaStatus status, EmpEmpresa empresa, AsyncCallback<Map<String, String>> asyncCallback);
	
	public abstract void salvarCertificado(FisCertificado certificado, AsyncCallback asyncCallback);
}
