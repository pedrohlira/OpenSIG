package br.com.opensig.core.client.controlador.comando.form;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.IComando;

import com.gwtext.client.widgets.MessageBox;

/**
 * Classe do comando salvar inicio, usada antes da acao de salvar.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoSalvarInicio extends ComandoAcao {

	/**
	 * @see AComando#AComando(IComando)
	 */
    public ComandoSalvarInicio(IComando proximo) {
        super(proximo);
        if (proximo == null) {
            throw new IllegalArgumentException("Deve passar o próximo comando!");
        }
    }

    @Override
    public void execute(Map contexto) {
        super.execute(contexto);
        if (FORM.getPanel().getForm().isValid()) {
            MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
            comando.execute(contexto);
        }
    }
}
