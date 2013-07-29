package br.com.opensig.financeiro.client.controlador.comando.boleto;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.financeiro.client.servico.FinanceiroProxy;
import br.com.opensig.financeiro.shared.modelo.FinConta;

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
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.layout.FitLayout;

public class ComandoGerar extends AComando {

	private AsyncCallback<String> asyncCallback;
	private AComando gerar;
	private String tipo;
	private boolean recibo;

	public ComandoGerar() {
		this("html", false);
	}

	public ComandoGerar(String tipo, boolean recibo) {
		this.tipo = tipo;
		this.recibo = recibo;

		asyncCallback = new AsyncCallback<String>() {

			public void onSuccess(String arg0) {
				MessageBox.hide();
				UtilClient.exportar("ExportacaoService?id=" + arg0);
				if (comando != null) {
					comando.execute(contexto);
				}
			}

			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtBoleto(), OpenSigCore.i18n.errCliente());
			}
		};
	}

	public void execute(Map contexto) {
		super.execute(contexto);
		gerar = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				final Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec.getAsDate("finRecebimentoRealizado") == null && !recibo) {
					final Window wnd = new Window("", 200, 130, true, false);
					final FormPanel frm = new FormPanel();

					frm.setLabelAlign(Position.TOP);
					frm.setPaddings(5);
					frm.setMargins(1);

					final ComboBox conta = getConta();
					frm.add(conta);

					Button btn = new Button(OpenSigCore.i18n.txtOk());
					btn.setIconCls("icon-salvar");
					btn.addListener(new ButtonListenerAdapter() {
						public void onClick(Button button, EventObject e) {
							if (frm.getForm().isValid()) {
								enviar(rec.getAsInteger("finRecebimentoId"), Integer.valueOf(conta.getValue()));
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
				} else if (recibo) {
					enviar(rec.getAsInteger("finRecebimentoId"), 0);
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtBoleto(), OpenSigCore.i18n.errBoleto());
				}
			}
		};
	}

	private void enviar(int boletoId, int contaId) {
		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtBoleto());
		FinanceiroProxy proxy = new FinanceiroProxy();
		proxy.gerar(boletoId, contaId, tipo, recibo, asyncCallback);
	}

	private ComboBox getConta() {
		FieldDef[] fdConta = new FieldDef[] { new IntegerFieldDef("finContaId"), new IntegerFieldDef("finBancoId"), new StringFieldDef("finBanco"), new StringFieldDef("finContaNome") };
		CoreProxy<FinConta> proxy = new CoreProxy<FinConta>(new FinConta());
		Store stConta = new Store(proxy, new ArrayReader(new RecordDef(fdConta)), false);
		stConta.load();

		ComboBox conta = new ComboBox(OpenSigCore.i18n.txtConta(), "conta", 150);
		conta.setListWidth(170);
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isRecibo() {
		return recibo;
	}

	public void setRecibo(boolean recibo) {
		this.recibo = recibo;
	}

	public AsyncCallback<String> getAsyncCallback() {
		return asyncCallback;
	}

	public void setAsyncCallback(AsyncCallback<String> asyncCallback) {
		this.asyncCallback = asyncCallback;
	}

	public AComando getGerar() {
		return gerar;
	}

	public void setGerar(AComando gerar) {
		this.gerar = gerar;
	}

}
