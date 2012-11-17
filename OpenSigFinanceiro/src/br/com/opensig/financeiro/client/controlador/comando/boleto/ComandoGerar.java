package br.com.opensig.financeiro.client.controlador.comando.boleto;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.financeiro.client.servico.FinanceiroProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;

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
				Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec.getAsInteger("finForma.finFormaId") != 4 && !recibo) {
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				} else if ((rec.getAsInteger("finForma.finFormaId") == 4 && rec.getAsDate("finRecebimentoRealizado") == null) || recibo) {
					MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtBoleto());

					int boletoId = rec.getAsInteger("finRecebimentoId");
					FinanceiroProxy proxy = new FinanceiroProxy();
					proxy.gerar(boletoId, tipo, recibo, asyncCallback);
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtBoleto(), OpenSigCore.i18n.errBoleto());
				}
			}
		};
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
