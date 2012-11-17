package br.com.opensig.comercial.client.controlador.comando.acao;

import java.util.Map;

import br.com.opensig.comercial.shared.modelo.ComVenda;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.ComandoExecutar;
import br.com.opensig.core.client.controlador.comando.ComandoFormulario;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.parametro.GrupoParametro;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroObjeto;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.financeiro.client.visao.form.FormularioReceber;
import br.com.opensig.financeiro.shared.modelo.FinReceber;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;

public class ComandoReceber extends ComandoAcao<ComVenda> {

	private int vendaId;
	private AsyncCallback async;
	private FinReceber receber;
	private IComando validaReceber;

	public ComandoReceber() {
		// finalizando
		AComando cmdFim = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				Record rec = LISTA.getPanel().getSelectionModel().getSelected();
				rec.set("comVendaRecebida", true);
				rec.set("finReceber.finReceberId", receber.getFinReceberId());
			}
		};
		// salvando a venda com o status fechada
		final ComandoExecutar<ComVenda> cmdVenda = new ComandoExecutar<ComVenda>(cmdFim) {
			public void execute(Map contexto) {
				FiltroNumero fn = new FiltroNumero("comVendaId", ECompara.IGUAL, vendaId);
				ParametroObjeto po = new ParametroObjeto("finReceber", receber);
				ParametroBinario pb = new ParametroBinario("comVendaRecebida", 1);
				GrupoParametro gp = new GrupoParametro(new IParametro[] { po, pb });

				Sql sql = new Sql(new ComVenda(vendaId), EComando.ATUALIZAR, fn, gp);
				setSqls(new Sql[] { sql });
				super.execute(contexto);
			}
		};
		async = new AsyncCallback() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errSalvar());
			}

			public void onSuccess(Object arg0) {
				try {
					receber = (FinReceber) arg0;
					if (receber.getFinReceberId() > 0) {
						cmdVenda.execute(contexto);
					} else {
						onFailure(null);
					}
				} catch (Exception e) {
					onFailure(e);
				}
			}
		};
		// validando receber
	    validaReceber = new AComando() {
			public void execute(final Map contexto) {
				super.execute(contexto);
				final Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec.getAsBoolean("comVendaFechada") && !rec.getAsBoolean("comVendaRecebida") && !rec.getAsBoolean("comVendaCancelada")) {
					vendaId = rec.getAsInteger("comVendaId");
					abrirFinanceiro();
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
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
				validaReceber.execute(contexto);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
	
	private void abrirFinanceiro() {
		final Record rec = LISTA.getPanel().getSelectionModel().getSelected();
		final FormularioReceber formReceber = new FormularioReceber(FORM.getFuncao());
		formReceber.setTitle(OpenSigCore.i18n.txtFinanceiro());

		formReceber.addListener(new FormPanelListenerAdapter() {
			public void onRender(Component component) {
				int empresaId = rec.getAsInteger("empEmpresa.empEmpresaId");
				formReceber.getHdnEmpresa().setValue(empresaId + "");
				formReceber.getHdnEntidade().setValue(rec.getAsString("empCliente.empEntidade.empEntidadeId"));
				formReceber.getCmbEntidade().setValue(rec.getAsString("empCliente.empEntidade.empEntidadeNome1"));
				formReceber.getCmbEntidade().disable();
				formReceber.getTxtValor().setValue(rec.getAsDouble("comVendaValorLiquido"));
				String data = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(rec.getAsDate("comVendaData"));
				formReceber.getDtCadastro().setValue(data);
				formReceber.getDtCadastro().disable();
				formReceber.mostrarDados();
				formReceber.getGridFormas().setHeight(260);

				formReceber.getCmbConta().getStore().addStoreListener(new StoreListenerAdapter() {
					public void onLoad(Store store, Record[] records) {
						formReceber.getCmbConta().setValue(UtilClient.CONF.get("conta.padrao"));
					}
				});

				Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
					public boolean execute() {
						if (formReceber.getTreeCategoria().getRoot().getChildNodes().length > 0) {
							formReceber.getTreeCategoria().selecionar(new String[] { UtilClient.CONF.get("categoria.venda") });
							formReceber.getTreeCategoria().disable();
							return false;
						} else {
							return true;
						}

					}
				}, 1000);
			}
		});

		new ComandoFormulario(formReceber, async).execute(formReceber.getContexto());
	}
}
