package br.com.opensig.financeiro.client.controlador.comando.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.ComandoExecutar;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroData;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinRecebimento;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.PromptCallback;

public class ComandoQuitarRecebimento extends ComandoAcao<FinRecebimento> {

	private Record[] recs;
	private Date hoje;
	private String sha1;
	private IComando validaQuitar;

	public ComandoQuitarRecebimento() {
		// finaliza e libera
		AComando cmdFim = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				MessageBox.hide();

				for (Record rec : recs) {
					int row = LISTA.getPanel().getStore().indexOf(rec);
					LISTA.getPanel().getSelectionModel().deselectRow(row);
					rec.set("finRecebimentoStatus", OpenSigCore.i18n.txtRealizado().toUpperCase());
					rec.set("finRecebimentoRealizado", hoje);
					rec.set("finRecebimentoObservacao", sha1);
				}
			}
		};
		// executa o comando e atualiza todos relacionados
		ComandoExecutar cmdQuitar = new ComandoExecutar(cmdFim) {
			public void execute(Map contexto) {
				MessageBox.wait(OpenSigCore.i18n.txtQuitar(), OpenSigCore.i18n.txtAguarde());

				// variaveis usadas
				try {
					hoje = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).parse(contexto.get("data").toString());
				} catch (Exception e) {
					hoje = new Date();
				}
				sha1 = OpenSigCoreJS.sha1(UtilClient.getDataGrid(hoje)).toUpperCase();
				GrupoFiltro gf = new GrupoFiltro();
				Map<Integer, Double> contas = new HashMap<Integer, Double>();
				List<Sql> sqls = new ArrayList<Sql>();

				// gerando o filtro dos pagamentos e separando por conta
				for (Record rec : recs) {
					int id = rec.getAsInteger("finReceber.finConta.finContaId");
					double valor = contas.get(id) == null ? rec.getAsDouble("finRecebimentoValor") : contas.get(id) + rec.getAsDouble("finRecebimentoValor");
					contas.put(id, valor);

					FiltroNumero fn = new FiltroNumero("finRecebimentoId", ECompara.IGUAL, rec.getAsInteger("finRecebimentoId"));
					gf.add(fn, EJuncao.OU);
				}

				// atualizando as contas
				for (Entry<Integer, Double> conta : contas.entrySet()) {
					ParametroFormula pf = new ParametroFormula("finContaSaldo", conta.getValue());
					FiltroNumero fn = new FiltroNumero("finContaId", ECompara.IGUAL, conta.getKey());
					Sql sqlConta = new Sql(new FinConta(), EComando.ATUALIZAR, fn, pf);
					sqls.add(sqlConta);
				}

				// atualizando recebimento
				ParametroTexto pt = new ParametroTexto("finRecebimentoStatus", OpenSigCore.i18n.txtRealizado().toUpperCase());
				ParametroData pd = new ParametroData("finRecebimentoRealizado", hoje);
				ParametroTexto pt1 = new ParametroTexto("finRecebimentoObservacao", sha1);
				GrupoParametro gp = new GrupoParametro(new IParametro[] { pt, pd, pt1 });
				Sql sqlForma = new Sql(new FinRecebimento(), EComando.ATUALIZAR, gf, gp);
				sqls.add(sqlForma);

				setSqls(sqls.toArray(new Sql[] {}));
				super.execute(contexto);
			}
		};
		// validando quitar / estornar
		validaQuitar = new AComando(cmdQuitar) {
			public void execute(final Map contexto) {
				super.execute(contexto);
				recs = LISTA.getPanel().getSelectionModel().getSelections();

				// valida cada quitamento
				for (Record rec : recs) {
					if (!rec.getAsString("finRecebimentoStatus").equalsIgnoreCase(OpenSigCore.i18n.txtAberto())) {
						int row = LISTA.getPanel().getStore().indexOf(rec);
						LISTA.getPanel().getSelectionModel().deselectRow(row);
					}
				}

				recs = LISTA.getPanel().getSelectionModel().getSelections();
				// pede permissao ou mostra mensagem
				if (recs.length > 0) {
					MessageBox.prompt(OpenSigCore.i18n.txtQuitar(), OpenSigCore.i18n.msgConfirma() + "<br>" + OpenSigCore.i18n.txtData() + ": <b>DD/MM/AAAA</b>", new PromptCallback() {
						public void execute(String btnID, String text) {
							if (btnID.equalsIgnoreCase("ok")) {
								contexto.put("data", text);
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
				validaQuitar.execute(contexto);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
