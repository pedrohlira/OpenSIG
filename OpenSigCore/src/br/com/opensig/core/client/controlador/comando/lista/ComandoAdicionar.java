package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.shared.modelo.Dados;

import com.gwtext.client.data.Record;

/**
 * Classe do comando adicionar, usada nas sub-listagens para inserir itens.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoAdicionar<E extends Dados> extends AComando<E> {

	private AListagemEditor<E> lista;

	/**
	 * Construtor padrao que recebe a sub-listagem para manipular.
	 * 
	 * @param lista
	 *            a listagem no modo edicao.
	 */
	public ComandoAdicionar(AListagemEditor<E> lista) {
		this.lista = lista;
	}

	@Override
	public void execute(Map contexto) {
		super.execute(contexto);

		lista.stopEditing();
		Record reg = lista.getCampos().createRecord(new Object[lista.getCampos().getFields().length]);
		if (contexto != null && contexto.get("resultado") != null) {
			String[][] resultado = (String[][]) contexto.get("resultado");
			for (String[] valores : resultado) {
				reg.set(valores[0], valores[1]);
			}
			lista.getStore().add(reg);
		} else {
			reg.set(reg.getFields()[0], 0);
			lista.getStore().add(reg);
			lista.startEditing(lista.getStore().getCount() - 1, 1);
		}
	}
}
