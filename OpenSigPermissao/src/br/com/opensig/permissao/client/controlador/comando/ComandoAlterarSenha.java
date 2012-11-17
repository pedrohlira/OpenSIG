package br.com.opensig.permissao.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.permissao.client.visao.AlterarSenha;

import com.gwtext.client.widgets.MessageBox;

public class ComandoAlterarSenha extends AComando {

	public void execute(Map contexto) {
		// so pode mudar senha se nao for de sistema
		if (Ponte.getLogin().isSistema()) {
			MessageBox.alert(OpenSigCore.i18n.txtSenha(), OpenSigCore.i18n.txtAcessoNegado());
		} else {
			new AlterarSenha();
		}
	}
}
