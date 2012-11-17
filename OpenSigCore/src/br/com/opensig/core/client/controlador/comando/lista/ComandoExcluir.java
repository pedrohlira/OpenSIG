package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * Classe do comando excluir, comando padronizado para excluir um registro
 * existente.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoExcluir<E extends Dados> extends ComandoAcao<E> {

	private boolean perguntar = true;

	/**
	 * @see AComando#AComando()
	 */
	public ComandoExcluir() {
		this(new ComandoExcluirFinal());
	}

	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoExcluir(IComando proximo) {
		super(proximo);
		if (proximo == null) {
			throw new IllegalArgumentException("Deve passar o próximo comando!");
		}
	}

	@Override
	public void execute(final Map contexto) {
		if (perguntar) {
			MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
				public void execute(String btnID) {
					if (btnID.equalsIgnoreCase("yes")) {
						MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
						executar(contexto);
					}
				}
			});
		} else {
			executar(contexto);
		}
	}

	// continuacao do comando acima, para evitar problemas ao gerar javascript
	private void executar(final Map contexto) {
		int id = UtilClient.getSelecionado((GridPanel) contexto.get("lista"));

		if (id > 0) {
			contexto.put("selecionado", id);
			super.execute(contexto, new AsyncCallback() {
				public void onSuccess(Object result) {
					DADOS.setId(Integer.valueOf(contexto.get("selecionado").toString()));
					CoreProxy<E> proxy = new CoreProxy<E>(DADOS);
					proxy.deletar(ASYNC);
				}

				public void onFailure(Throwable caught) {
				}
			});
		}
	}

	// Gets e Seteres
	
	public boolean isPerguntar() {
		return perguntar;
	}

	public void setPerguntar(boolean perguntar) {
		this.perguntar = perguntar;
	}

}
