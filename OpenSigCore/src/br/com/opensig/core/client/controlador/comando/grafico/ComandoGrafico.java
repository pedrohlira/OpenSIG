package br.com.opensig.core.client.controlador.comando.grafico;

import java.util.Collection;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtextux.client.widgets.window.ToastWindow;

/**
 * Classe de comando de graficos.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoGrafico<E extends Dados> extends AComando<E> {

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto);
		GRAFICO.getPanel().getEl().mask(OpenSigCore.i18n.txtAguarde());
		CoreProxy<E> persistencia = new CoreProxy<E>(DADOS);

		persistencia.buscar(GRAFICO.getCmbCampoX().getValue(), GRAFICO.getCmbCampoSubX().getValue(), GRAFICO.getEData().toString(), GRAFICO.getCmbCampoY().getValue(), GRAFICO.getEValor(),
				GRAFICO.getEOrdem(), LISTA.getProxy().getFiltroTotal(), new AsyncCallback<Collection<String[]>>() {

					public void onFailure(Throwable caught) {
						GRAFICO.getPanel().getEl().unmask();
						MessageBox.alert(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errListagem());
					}

					public void onSuccess(Collection<String[]> result) {
						GRAFICO.getPanel().getEl().unmask();
						GRAFICO.mostrarGrafico(result);
						if (result.size() == 0) {
							new ToastWindow(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errRegistro()).show();
						}

						if (comando != null) {
							comando.execute(contexto);
						}
					}
				});
	}
}
