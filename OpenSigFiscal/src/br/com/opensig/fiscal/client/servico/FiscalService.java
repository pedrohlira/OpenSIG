package br.com.opensig.fiscal.client.servico;

import java.util.Map;

import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.client.servico.ExportacaoException;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.shared.modelo.FisCertificado;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

public interface FiscalService<E extends Dados> extends CoreService<E> {

	public Map<String, String> analisarNFeSaida(FisNotaSaida saida) throws FiscalException;
	
	public Map<String, String> analisarNFeEntrada(FisNotaEntrada entrada) throws FiscalException;

	public String backup(E classe, IFiltro filtro) throws ExportacaoException;

	public String exportar(String arquivo, String nome, String tipo) throws ExportacaoException;
	
	public String status(int ambiente, int uf, int empresa) throws FiscalException;

	public String validar(int ambiente, IFiltro filtro, int empresa) throws FiscalException;

	public String situacao(int ambiente, String chave, int empresa) throws FiscalException;

	public String cadastro(int ambiente, int ibge, String uf, String tipo, String doc, int empresa) throws FiscalException;

	public String enviarNFe(String xml, int empresa) throws FiscalException;

	public String receberNFe(String xml, int empresa, String recibo) throws FiscalException;

	public String cancelar(String xml, int empresa) throws FiscalException;

	public String inutilizar(String xml, int empresa) throws FiscalException;

	public Map<String, String> salvarSaida(String xml, FisNotaStatus status, EmpEmpresa empresa) throws FiscalException;
	
	public Map<String, String> cancelarSaida(FisNotaSaida saida, String motivo) throws FiscalException;
	
	public Map<String, String> inutilizarSaida(FisNotaSaida saida, String motivo, int ini, int fim) throws FiscalException;

	public Map<String, String> salvarEntrada(String xml, FisNotaStatus status, EmpEmpresa empresa) throws FiscalException;
	
	public Map<String, String> cancelarEntrada(FisNotaEntrada entrada, String motivo) throws FiscalException;
	
	public Map<String, String> inutilizarEntrada(FisNotaEntrada entrada, String motivo, int ini, int fim) throws FiscalException;
	
	public void salvarCertificado(FisCertificado certificado) throws FiscalException;
}
