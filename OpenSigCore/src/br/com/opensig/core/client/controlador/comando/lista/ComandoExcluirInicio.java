package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.IComando;

import com.gwtext.client.widgets.MessageBox;

/**
 * Classe do comando excluir inicio, executa acoes antes da exclusao do registro.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoExcluirInicio extends ComandoAcao {

	/**
	 * @see AComando#AComando(IComando)
	 */
	public ComandoExcluirInicio(IComando proximo) {
		super(proximo);
		if (proximo == null) {
			throw new IllegalArgumentException("Deve passar o próximo comando!");
		}
	}

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto);
		MessageBox.confirm(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluir(), new MessageBox.ConfirmCallback() {
			public void execute(String btnID) {
				if (btnID.equalsIgnoreCase("yes")) {
					MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtExcluir());
					comando.execute(contexto);
				}
			}
		});
	}
}
