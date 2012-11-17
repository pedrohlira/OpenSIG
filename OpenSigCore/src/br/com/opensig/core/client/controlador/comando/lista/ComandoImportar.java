package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.visao.JanelaImportar;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Classe abstrata que define as opcoes padroes de importacao.
 * 
 * @param <E>
 *            O tipo de dados de classe.
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoImportar<E extends Dados> extends ComandoAcao<E> {

	/**
	 * Construtor padrao.
	 */
	public ComandoImportar() {
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
	
	private void execute(){
		JanelaImportar janela = new JanelaImportar(LISTA);
		janela.show();
	}
}
