package br.com.opensig.comercial.client.controlador.comando.acao;

import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoGerarVenda extends ComandoAcao {

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				final Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec != null) {
					MessageBox.confirm(OpenSigCore.i18n.txtVenda(), OpenSigCore.i18n.msgConfirma(), new MessageBox.ConfirmCallback() {
						public void execute(String btnID) {
							if (btnID.equalsIgnoreCase("yes")) {
								MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtVenda());
								
								// gerando o filtro
								FiltroNumero fn = new FiltroNumero("comCompraId", ECompara.IGUAL, rec.getAsInteger("comCompraId"));
								ComercialProxy proxy = new ComercialProxy();
								proxy.gerarVenda(fn, new AsyncCallback<Object>() {
									public void onSuccess(Object result) {
										new ComandoSalvarFinal().execute(contexto);
									};
									
									public void onFailure(Throwable caught) {
										MessageBox.hide();
										new ToastWindow(OpenSigCore.i18n.txtSalvar(), caught.getMessage()).show();
									}
								});
							}
						}
					});
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtVenda(), OpenSigCore.i18n.errSelecionar());
				}
			}
			public void onFailure(Throwable caught) {
			}
		});
	}
}
