package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe do comando excluir final, executa acoes apos a exclusao do registro.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoExcluirFinal extends ComandoAcao {

	@Override
	public void execute(Map contexto) {
		super.execute(contexto);
		MessageBox.hide();
		Object erro = contexto.get("erro");
		contexto.put("erro", null);

		if (erro == null) {
			Record rec = LISTA.getPanel().getSelectionModel().getSelected();
			if (rec != null) {
				LISTA.getPanel().getStore().remove(rec);
			} else {
				LISTA.getPanel().getStore().reload();
			}
			new ToastWindow(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.msgExcluirOK()).show();
		} else {
			MessageBox.alert(OpenSigCore.i18n.txtExcluir(), OpenSigCore.i18n.errExcluir());
		}

		if (comando != null) {
			comando.execute(contexto);
		}
	}
}
