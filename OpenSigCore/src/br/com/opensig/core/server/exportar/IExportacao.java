package br.com.opensig.core.server.exportar;

import br.com.opensig.core.client.servico.CoreService;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpRegistro;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

/**
 * Interface que generaliza a forma como o sistema trata as exportacoes.
 * 
 * @author Pedro H. Lira
 */
public interface IExportacao<E extends Dados> {

	/**
	 * Metodo que seta a autenticao atual do usuario.
	 * 
	 * @param auth
	 *            o objeto de autenticacao.
	 */
	public void setAuth(Autenticacao auth);

	/**
	 * Metodo que exporta a listagem.
	 * 
	 * @param service
	 *            objeto que permite selecionar os dados.
	 * @param modo
	 *            objeto que determina as configuracoes da exportacao.
	 * @param exp
	 *            a listagem que deve ser usada para exportar os dados.
	 * @param enderecos
	 *            os dados de endereco.
	 * @param contatos
	 *            os dados de contato.
	 * @return o local onde esta o arquivo gerado.
	 */
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpListagem<E> exp, String[][] enderecos, String[][] contatos);

	/**
	 * Metodo que exporta o registro.
	 * 
	 * @param service
	 *            objeto que permite selecionar os dados.
	 * @param modo
	 *            objeto que determina as configuracoes da exportacao.
	 * @param exp
	 *            o registro que deve ser usado para exportar os dados.
	 * @param enderecos
	 *            os dados de endereco.
	 * @param contatos
	 *            os dados de contato.
	 * @return o local onde esta o arquivo gerado.
	 */
	public String getArquivo(CoreService<E> service, SisExpImp modo, ExpRegistro<E> exp, String[][] enderecos, String[][] contatos);
}
