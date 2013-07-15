package br.com.opensig.comercial.client.controlador.comando.acao;

import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoGerarCompra extends ComandoAcao {

	private Record[] recs;
	private IComando validaTroca;

	public ComandoGerarCompra() {
		// executa o comando e atualiza todos relacionados
		AComando cmdGerar = new AComando() {
			public void execute(final Map contexto) {
				MessageBox.wait(OpenSigCore.i18n.txtCompra(), OpenSigCore.i18n.txtAguarde());

				// gerando o filtro das trocas
				GrupoFiltro gf = new GrupoFiltro();
				for (Record rec : recs) {
					FiltroNumero fn = new FiltroNumero("comTrocaId", ECompara.IGUAL, rec.getAsInteger("comTrocaId"));
					gf.add(fn, EJuncao.OU);
				}

				ComercialProxy proxy = new ComercialProxy();
				proxy.gerarCompra(gf, new AsyncCallback<Object>() {
					public void onSuccess(Object result) {
						new ComandoSalvarFinal().execute(contexto);
					};
					
					public void onFailure(Throwable caught) {
						MessageBox.hide();
						new ToastWindow(OpenSigCore.i18n.txtSalvar(), caught.getMessage()).show();
					}
				});
			}
		};
		// validando se pode usar a troca
		validaTroca = new AComando(cmdGerar) {
			public void execute(final Map contexto) {
				super.execute(contexto);
				recs = LISTA.getPanel().getSelectionModel().getSelections();

				// valida cada troca
				for (Record rec : recs) {
					if (!rec.getAsBoolean("comTrocaAtivo")) {
						int row = LISTA.getPanel().getStore().indexOf(rec);
						LISTA.getPanel().getSelectionModel().deselectRow(row);
					}
				}

				recs = LISTA.getPanel().getSelectionModel().getSelections();
				// pede permissao ou mostra mensagem
				if (recs.length > 0) {
					MessageBox.confirm(OpenSigCore.i18n.txtCompra(), OpenSigCore.i18n.msgConfirma(), new ConfirmCallback() {
						public void execute(String btnID) {
							if (btnID.equalsIgnoreCase("yes")) {
								comando.execute(contexto);
							}
						}
					});
				} else {
					MessageBox.alert(OpenSigCore.i18n.errInvalido(), OpenSigCore.i18n.errSelecionar());
				}
			}
		};
	}

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				validaTroca.execute(contexto);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
