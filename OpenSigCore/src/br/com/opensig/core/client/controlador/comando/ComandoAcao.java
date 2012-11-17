package br.com.opensig.core.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.visao.PermitirSistema;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.sistema.SisAcao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisModulo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.TabPanel;

/**
 * Classe do comando ação padrão, para toda ação que não foi implementada.
 * 
 * @param <E>
 *            o tipo de dados do comando.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoAcao<E extends Dados> extends AComando<E> {

	/**
	 * Chamada de retorno assincrono.
	 */
	protected AsyncCallback async;
	/**
	 * Classe do objeto do comando.
	 */
	protected Class objeto;
	/**
	 * Chamada de retorno assincrono, que valida se o usuario tem permissao a
	 * executar a acao.
	 */
	protected AsyncCallback<ILogin> resp = new AsyncCallback<ILogin>() {

		public void onSuccess(ILogin result) {
			boolean permite = false;

			if (result != null) {
				for (SisModulo modulo : result.getModulos()) {
					if (modulo.equals(LISTA.getFuncao().getSisModulo())) {
						for (SisFuncao funcao : modulo.getSisFuncoes()) {
							if (funcao.equals(LISTA.getFuncao())) {
								SisAcao acao = UtilClient.getAcaoPermitida(funcao, objeto);
								if (acao != null && acao.isExecutar()) {
									permite = true;
								}
								break;
							}
						}
						break;
					}
				}
			}

			if (permite) {
				async.onSuccess(null);
			} else {
				onFailure(new IllegalArgumentException(OpenSigCore.i18n.msgPermissaoExecutar()));
			}
		}

		public void onFailure(Throwable caught) {
			MessageBox.hide();
			MessageBox.alert(OpenSigCore.i18n.txtPermissao(), OpenSigCore.i18n.msgPermissaoExecutar());
			async.onFailure(caught);
		}
	};

	/**
	 * @see AComando#AComando()
	 */
	public ComandoAcao() {
		this(null);
	}

	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoAcao(IComando proximo) {
		super(proximo);
		this.objeto = this.getClass();
	}

	@Override
	public void execute(Map contexto) {
		super.execute(contexto);

		try {
			TAB = (TabPanel) Ponte.getCentro().getActiveTab();
		} catch (Exception ex) {
			// nada
		}
	}

	/**
	 * Excucao do comando acao, verificando se o usuario tem permissao para a
	 * acao.
	 * 
	 * @param contexto
	 *            o map de valores atuais usados.
	 * @param async
	 *            a funcao de retorno a ser disparada apos confirmar a
	 *            permissao.
	 */
	protected void execute(Map contexto, AsyncCallback async) {
		super.execute(contexto);
		this.async = async;

		try {
			TAB = (TabPanel) Ponte.getCentro().getActiveTab();
		} catch (Exception ex) {
			// nada
		}

		// valida se pode executar a acao
		if (LISTA != null) {
			this.objeto = this.getClass();
			SisAcao acao = UtilClient.getAcaoPermitida(LISTA.getFuncao(), objeto);
			if (acao != null && acao.isExecutar()) {
				async.onSuccess(null);
			} else {
				PermitirSistema permitir = (PermitirSistema) GWT.create(PermitirSistema.class);
				permitir.setInfo(OpenSigCore.i18n.msgPermissaoExecutar());
				permitir.executar(resp);
			}
		}
	}

	// Gets e Seteres

	public AsyncCallback getAsync() {
		return async;
	}

	public void setAsync(AsyncCallback async) {
		this.async = async;
	}

	public Class getObjeto() {
		return objeto;
	}

	public void setObjeto(Class objeto) {
		this.objeto = objeto;
	}

	public AsyncCallback<ILogin> getResp() {
		return resp;
	}

	public void setResp(AsyncCallback<ILogin> resp) {
		this.resp = resp;
	}

}
