package br.com.opensig.permissao.client.servico;

import java.util.List;

import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Classe que é utilizada para entrar no sistema e ter acesso à permissões no
 * sistema.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 * @since 15/10/2008
 */
public class PermissaoProxy extends CoreProxy implements PermissaoServiceAsync, ILogin {

	private static final long serialVersionUID = 4522735665179835950L;
	private static final PermissaoServiceAsync async = (PermissaoServiceAsync) GWT.create(PermissaoService.class);
	private static final ServiceDefTarget sdf = (ServiceDefTarget) async;

	protected Autenticacao autenticacao;

	/**
	 * Construtor padrão
	 */
	public PermissaoProxy() {
		sdf.setServiceEntryPoint(GWT.getHostPageBaseURL() + "PermissaoService");
	}

	/**
	 * @see ILogin#getId()
	 */
	public int getId() {
		return Integer.valueOf(autenticacao.getUsuario()[0]);
	}

	/**
	 * @see ILogin#getUsuario()
	 */
	public String getUsuario() {
		return autenticacao.getUsuario()[1];
	}

	/**
	 * @see ILogin#getSenha()
	 */
	public String getSenha() {
		return autenticacao.getUsuario()[2];
	}

	/**
	 * @see ILogin#getEmpresa()
	 */
	public String[] getEmpresa() {
		return autenticacao.getEmpresa();
	}

	/**
	 * @see ILogin#getEmpresaId()
	 */
	public int getEmpresaId() {
		return Integer.valueOf(autenticacao.getEmpresa()[0]);
	}

	/**
	 * @see ILogin#getEmpresaNome()
	 */
	public String getEmpresaNome() {
		return autenticacao.getEmpresa()[2];
	}

	/**
	 * @see ILogin#getDesconto()
	 */
	public int getDesconto() {
		return Integer.valueOf(autenticacao.getUsuario()[3]);
	}

	/**
	 * @see ILogin#atualizarSenha(String)
	 */
	public void atualizarSenha(String novaSenha) {
		autenticacao.getUsuario()[2] = novaSenha;
	}

	/**
	 * @see ILogin#isBloqueado()
	 */
	public boolean isBloqueado() {
		return autenticacao.isBloqueado();
	}

	/**
	 * @see ILogin#isSistema()
	 */
	public boolean isSistema() {
		return Boolean.valueOf(autenticacao.getUsuario()[8]);
	}

	/**
	 * @see ILogin#getModulos()
	 */
	public List<SisModulo> getModulos() {
		return autenticacao.getModulos();
	}

	/**
	 * @see PermissaoService#entrar(String, String, String, int, boolean)
	 */
	@Override
	public void entrar(String usuario, String senha, String captcha, int empresa, boolean permissao, final AsyncCallback<Autenticacao> asyncCallback) {
		async.entrar(usuario, senha, captcha, empresa, permissao, new AsyncCallback<Autenticacao>() {

			public void onFailure(Throwable caught) {
				asyncCallback.onFailure(caught);
			}

			public void onSuccess(Autenticacao result) {
				autenticacao = result;
				UtilClient.CONF = autenticacao.getConf();
				asyncCallback.onSuccess(result);
			}
		});
	}

	/**
	 * @see PermissaoService#sair()
	 */
	public void sair(AsyncCallback asyncCallback) {
		async.sair(asyncCallback);
	}
	
	public void isLogado(AsyncCallback<Boolean> asyncCallback) {
		async.isLogado(asyncCallback);
	}
	
	public void bloquear(boolean bloqueio, AsyncCallback asyncCallback) {
		async.bloquear(bloqueio, asyncCallback);
	}

	public void recuperarSenha(String email, AsyncCallback asyncCallback) {
		async.recuperarSenha(email, asyncCallback);
	}

	/**
	 * @see ILogin#getFuncoes(SisModulo)
	 */
	public List<SisFuncao> getFuncoes(int moduloId) {
		List<SisFuncao> funcoes = null;
		List<SisModulo> modulos = getModulos();

		if (modulos != null) {
			for (SisModulo sisModulo : modulos) {
				if (sisModulo.getSisModuloId() == moduloId) {
					funcoes = sisModulo.getSisFuncoes();
					break;
				}
			}
		}

		return funcoes;
	}

	/**
	 * @see ILogin#getFuncoes(SisModulo)
	 */
	public List<SisFuncao> getFuncoes(String muduloClasse) {
		List<SisFuncao> funcoes = null;
		List<SisModulo> modulos = getModulos();

		if (modulos != null) {
			for (SisModulo sisModulo : modulos) {
				if (sisModulo.getSisModuloClasse().equalsIgnoreCase(muduloClasse)) {
					funcoes = sisModulo.getSisFuncoes();
					break;
				}
			}
		}

		return funcoes;
	}

	/**
	 * @see ILogin#getAcoes(SisFuncao)
	 */
	public List<SisAcao> getAcoes(int moduloId, int funcaoId) {
		List<SisAcao> acoes = null;
		List<SisFuncao> funcoes = getFuncoes(moduloId);

		if (funcoes != null) {
			for (SisFuncao sisFuncao : funcoes) {
				if (sisFuncao.getSisFuncaoId() == funcaoId) {
					acoes = sisFuncao.getSisAcoes();
					break;
				}
			}
		}

		return acoes;
	}

	/**
	 * @see ILogin#getAcoes(SisFuncao)
	 */
	public List<SisAcao> getAcoes(String moduloClasse, String funcaoClasse) {
		List<SisAcao> acoes = null;
		List<SisFuncao> funcoes = getFuncoes(moduloClasse);

		if (funcoes != null) {
			for (SisFuncao sisFuncao : funcoes) {
				if (sisFuncao.getSisFuncaoClasse().equalsIgnoreCase(funcaoClasse)) {
					acoes = sisFuncao.getSisAcoes();
					break;
				}
			}
		}

		return acoes;
	}

}
