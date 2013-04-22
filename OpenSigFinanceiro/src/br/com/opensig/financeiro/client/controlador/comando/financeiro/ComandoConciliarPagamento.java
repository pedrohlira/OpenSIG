package br.com.opensig.financeiro.client.controlador.comando.financeiro;

import java.util.ArrayList;
import java.util.Date;
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
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroData;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.controlador.parametro.ParametroObjeto;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.financeiro.shared.modelo.FinConta;
import br.com.opensig.financeiro.shared.modelo.FinPagamento;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.layout.FitLayout;

public class ComandoConciliarPagamento extends ComandoAcao<FinPagamento> {

	private Record[] recs;
	private IComando validaConciliar;

	public ComandoConciliarPagamento() {
		// finaliza e libera
		AComando cmdFim = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				MessageBox.hide();

				for (Record rec : recs) {
					int row = LISTA.getPanel().getStore().indexOf(rec);
					LISTA.getPanel().getSelectionModel().deselectRow(row);
					rec.set("finPagamentoStatus", OpenSigCore.i18n.txtConciliado().toUpperCase());
					rec.set("finPagamentoConciliado", (Date) contexto.get("data"));
				}
			}
		};
		// executa o comando e atualiza todos relacionados
		ComandoExecutar cmbConciliar = new ComandoExecutar(cmdFim) {
			public void execute(Map contexto) {
				MessageBox.wait(OpenSigCore.i18n.txtConciliar(), OpenSigCore.i18n.txtAguarde());

				// variaveis usadas
				GrupoFiltro gf = new GrupoFiltro();
				List<Sql> sqls = new ArrayList<Sql>();
				FinConta conta = new FinConta(Integer.valueOf(contexto.get("conta").toString()));
				double valor = 0.00;
				
				// gerando o filtro dos pagamentos e separando por conta
				for (Record rec : recs) {
					valor += rec.getAsDouble("finPagamentoValor");
					FiltroNumero fn = new FiltroNumero("finPagamentoId", ECompara.IGUAL, rec.getAsInteger("finPagamentoId"));
					gf.add(fn, EJuncao.OU);
				}

				// atualizando pagamento
				ParametroTexto pt = new ParametroTexto("finPagamentoStatus", OpenSigCore.i18n.txtConciliado().toUpperCase());
				ParametroData pd = new ParametroData("finPagamentoConciliado", (Date) contexto.get("data"));
				ParametroObjeto po = new ParametroObjeto("finConta", conta);
				GrupoParametro gp = new GrupoParametro(new IParametro[] { pt, pd, po });
				Sql sqlForma = new Sql(new FinPagamento(), EComando.ATUALIZAR, gf, gp);
				sqls.add(sqlForma);

				// atualizando a conta
				ParametroFormula pf = new ParametroFormula("finContaSaldo", valor * -1);
				FiltroNumero fn = new FiltroNumero("finContaId", ECompara.IGUAL, conta.getFinContaId());
				Sql sqlConta = new Sql(conta, EComando.ATUALIZAR, fn, pf);
				sqls.add(sqlConta);
				
				setSqls(sqls.toArray(new Sql[] {}));
				super.execute(contexto);
			}
		};
		// validando quitar / estornar
		validaConciliar = new AComando(cmbConciliar) {
			public void execute(final Map contexto) {
				super.execute(contexto);
				recs = LISTA.getPanel().getSelectionModel().getSelections();

				// valida cada quitamento
				for (Record rec : recs) {
					if (!rec.getAsString("finPagamentoStatus").equalsIgnoreCase(OpenSigCore.i18n.txtRealizado())) {
						int row = LISTA.getPanel().getStore().indexOf(rec);
						LISTA.getPanel().getSelectionModel().deselectRow(row);
					}
				}

				recs = LISTA.getPanel().getSelectionModel().getSelections();
				// pede permissao ou mostra mensagem
				if (recs.length > 0) {
					final Window wnd = new Window("", 300, 130, true, false);
					final FormPanel frm = new FormPanel();

					frm.setLabelAlign(Position.TOP);
					frm.setPaddings(5);
					frm.setMargins(1);

					final ComboBox conta = getConta();
					final DateField data = new DateField(OpenSigCore.i18n.txtData(), "data", 100);
					data.setAllowBlank(false);
					
					MultiFieldPanel linha1 = new MultiFieldPanel();
					linha1.setBorder(false);
					linha1.addToRow(conta, 150);
					linha1.addToRow(data, 120);
					frm.add(linha1);

					Button btn = new Button(OpenSigCore.i18n.txtOk());
					btn.setIconCls("icon-salvar");
					btn.addListener(new ButtonListenerAdapter() {
						public void onClick(Button button, EventObject e) {
							if (frm.getForm().isValid()) {
								contexto.put("data", data.getValue());
								contexto.put("conta", conta.getValue());
								comando.execute(contexto);
								wnd.close();
							}
						}
					});

					wnd.setTitle(OpenSigCore.i18n.txtRecebimento(), "icon-preco");
					wnd.setLayout(new FitLayout());
					wnd.add(frm);
					wnd.addButton(btn);
					wnd.setButtonAlign(Position.CENTER);
					wnd.show();
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
				validaConciliar.execute(contexto);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	private ComboBox getConta() {
		FieldDef[] fdConta = new FieldDef[] { new IntegerFieldDef("finContaId"), new IntegerFieldDef("finBancoId"), new StringFieldDef("finBanco"), new StringFieldDef("finContaNome") };
		CoreProxy<FinConta> proxy = new CoreProxy<FinConta>(new FinConta());
		Store stConta = new Store(proxy, new ArrayReader(new RecordDef(fdConta)), false);
		stConta.load();

		ComboBox conta = new ComboBox(OpenSigCore.i18n.txtConta(), "conta", 130);
		conta.setListWidth(200);
		conta.setAllowBlank(false);
		conta.setStore(stConta);
		conta.setTriggerAction(ComboBox.ALL);
		conta.setMode(ComboBox.LOCAL);
		conta.setDisplayField("finContaNome");
		conta.setValueField("finContaId");
		conta.setForceSelection(true);
		conta.setEditable(false);
		
		return conta;
	}
}
