package br.com.opensig.poker.client.controlador.comando;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.ComandoExecutar;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.poker.shared.modelo.PokerParticipante;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;

public class ComandoAddOn extends ComandoAcao {
	
	private Record[] recs;
	private IComando validaAddOn;

	public ComandoAddOn() {
		// finaliza e libera
		AComando cmdFim = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				MessageBox.hide();

				for (Record rec : recs) {
					int row = LISTA.getPanel().getStore().indexOf(rec);
					LISTA.getPanel().getSelectionModel().deselectRow(row);
					rec.set("pokerParticipanteAdicional", true);
				}
			}
		};
		// executa o comando e atualiza todos relacionados
		ComandoExecutar cmdAddOn = new ComandoExecutar(cmdFim) {
			public void execute(Map contexto) {
				MessageBox.wait(OpenSigCore.i18n.txtAddon(), OpenSigCore.i18n.txtAguarde());

				// variaveis usadas
				GrupoFiltro gf = new GrupoFiltro();
				List<Sql> sqls = new ArrayList<Sql>();

				// gerando o filtro dos participantes
				for (Record rec : recs) {
					FiltroNumero fn = new FiltroNumero("pokerParticipanteId", ECompara.IGUAL, rec.getAsInteger("pokerParticipanteId"));
					gf.add(fn, EJuncao.OU);
				}

				// atualizando participantes
				ParametroBinario pb = new ParametroBinario("pokerParticipanteAdicional", 1);
				Sql sqlForma = new Sql(new PokerParticipante(), EComando.ATUALIZAR, gf, pb);
				sqls.add(sqlForma);

				setSqls(sqls.toArray(new Sql[] {}));
				super.execute(contexto);
			}
		};
		// validando addon
		validaAddOn = new AComando(cmdAddOn) {
			public void execute(final Map contexto) {
				super.execute(contexto);
				recs = LISTA.getPanel().getSelectionModel().getSelections();

				// valida cada addon
				for (Record rec : recs) {
					if (!rec.getAsBoolean("pokerParticipanteAtivo") || rec.getAsBoolean("pokerParticipanteAdicional")) {
						int row = LISTA.getPanel().getStore().indexOf(rec);
						LISTA.getPanel().getSelectionModel().deselectRow(row);
					}
				}

				recs = LISTA.getPanel().getSelectionModel().getSelections();
				// pede permissao ou mostra mensagem
				if (recs.length > 0) {
					MessageBox.confirm(OpenSigCore.i18n.txtRebuy(), OpenSigCore.i18n.msgConfirma(), new ConfirmCallback(){
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
				validaAddOn.execute(contexto);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
