package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import com.gwtext.client.widgets.MessageBox;

import br.com.opensig.core.client.controlador.comando.ComandoFuncao;

public class ComandoTributacao extends ComandoFuncao {

	public void execute(Map contexto) {
		MessageBox.alert("Tributação", "Selecione um dos sub-menus!");
	}
}
