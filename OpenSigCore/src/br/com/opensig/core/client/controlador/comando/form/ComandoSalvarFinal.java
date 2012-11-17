package br.com.opensig.core.client.controlador.comando.form;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;

import com.gwtext.client.widgets.MessageBox;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe do comando salvar final, usada apos termino da acao de salvar.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoSalvarFinal extends ComandoAcao {

	@Override
	public void execute(Map contexto) {
		super.execute(contexto);
		MessageBox.hide();
		Object erro = contexto.get("erro");
		contexto.put("erro", null);

		if (erro == null) {
			if (LISTA != null) {
				LISTA.getPanel().getStore().reload();
			}

			new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.msgSalvarOK()).show();
			if (comando == null) {
				comando = new ComandoCancelar();
			}
			comando.execute(contexto);
		} else {
			if (erro.toString().toUpperCase().contains("DUPLICATE")) {
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errExiste());
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errSalvar());
			}
			new ToastWindow(OpenSigCore.i18n.txtSalvar(), erro.toString()).show();
		}
	}
}
