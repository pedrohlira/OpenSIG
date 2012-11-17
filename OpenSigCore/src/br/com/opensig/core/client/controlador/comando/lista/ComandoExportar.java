package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.visao.JanelaExportar;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Classe que define as opcoes padroes de exportacao.
 * 
 * @param <E>
 *            O tipo de dados de classe.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoExportar<E extends Dados> extends ComandoAcao<E> {

	/**
	 * Construtor padrao.
	 */
	public ComandoExportar() {
	}

	@Override
	public void execute(Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				execute();
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	public void execute() {
		EModo modo = contexto.get("acao") != null ? (EModo) contexto.get("acao") : EModo.LISTAGEM;
		JanelaExportar janela = new JanelaExportar(LISTA, modo);
		janela.show();
	}
}
