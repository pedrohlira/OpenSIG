package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.ComandoExecutar;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

/**
 * Classe do comando excluir filtrados, executa a exclusao em lote.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoExcluirFiltrados<E extends Dados> extends ComandoAcao<E> {

	/**
	 * @see AComando#AComando()
	 */
	public ComandoExcluirFiltrados() {
		this(new ComandoExcluirFinal());
	}

	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoExcluirFiltrados(IComando proximo) {
		super(proximo);
		if (proximo == null) {
			throw new IllegalArgumentException("Deve passar o próximo comando!");
		}
	}

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				LISTA.getPanel().getSelectionModel().clearSelections();
				MessageBox.confirm(OpenSigCore.i18n.txtExcluirFiltrados(), OpenSigCore.i18n.msgExcluirFiltrados(), new MessageBox.ConfirmCallback() {

					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluirFiltrados());
							IFiltro filtro = LISTA.getProxy().getFiltroTotal();
							Sql sql = new Sql(DADOS, EComando.EXCLUIR, filtro);
							ComandoExecutar<E> cmdExecutar = new ComandoExecutar<E>(comando, new Sql[] { sql });
							cmdExecutar.execute(contexto);
						}
					}
				});
			}

			public void onFailure(Throwable caught) {
				// nao tem permissao
			}
		});
	}
}
