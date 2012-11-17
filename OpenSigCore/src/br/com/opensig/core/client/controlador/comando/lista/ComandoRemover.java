package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.shared.modelo.Dados;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;

/**
 * Classe do comando remover, usada nas sub-listagens para remover itens.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoRemover<E extends Dados> extends AComando<E> {
	private AListagemEditor<E> lista;

	/**
	 * Construtor padrao que recebe a sub-listagem para manipular.
	 * 
	 * @param lista
	 *            a listagem no modo edicao.
	 */
	public ComandoRemover(AListagemEditor<E> lista) {
		this.lista = lista;
	}

	@Override
	public void execute(Map contexto) {
		Record reg = lista.getSelectionModel().getSelected();
		if (reg != null) {
			MessageBox.hide();
			lista.getStore().remove(reg);
		}
	}
}
