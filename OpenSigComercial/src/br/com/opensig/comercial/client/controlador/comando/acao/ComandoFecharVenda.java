package br.com.opensig.comercial.client.controlador.comando.acao;

import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.client.visao.lista.ListagemValidarEstoque;
import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.visao.Ponte;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoFecharVenda extends ComandoAcao {

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				final Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec != null && !rec.getAsBoolean("comVendaFechada")) {
					MessageBox.confirm(Ponte.getCentro().getActiveTab().getTitle(), OpenSigCore.i18n.msgConfirma(), new MessageBox.ConfirmCallback() {

						public void execute(String btnID) {
							if (btnID.equalsIgnoreCase("yes")) {
								MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtFechar());
								ComVenda venda = new ComVenda(rec.getAsInteger("comVendaId"));

								ComercialProxy proxy = new ComercialProxy();
								proxy.fecharVenda(venda, new AsyncCallback<String[][]>() {
									public void onFailure(Throwable caught) {
										MessageBox.hide();
										MessageBox.alert(OpenSigCore.i18n.txtFechar(), OpenSigCore.i18n.errSalvar());
									};

									public void onSuccess(String[][] result) {
										MessageBox.hide();

										if (result.length == 0) {
											rec.set("comVendaFechada", true);
											new ToastWindow(OpenSigCore.i18n.txtFechar(), OpenSigCore.i18n.msgSalvarOK()).show();
										} else {
											validarEstoque(result);
										}
									};
								});
							}
						}
					});
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtFechar(), OpenSigCore.i18n.txtAcessoNegado());
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
	
	private void validarEstoque(String[][] result) {
		new ListagemValidarEstoque(result, this, contexto);
	}
}
