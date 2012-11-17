package br.com.opensig.fiscal.client.controlador.comando.acao;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.fiscal.client.servico.FiscalProxy;

public class ComandoBackup<E extends Dados> extends ComandoAcao<E> {

	AsyncCallback<String> donwload = new AsyncCallback<String>() {
		public void onSuccess(String result) {
			MessageBox.hide();
			UtilClient.exportar("ExportacaoService?id=" + result);
		}
		
		public void onFailure(Throwable caught) {
			MessageBox.hide();
			MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());			
		}
	};
	
	
	@Override
	public void execute(Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				MessageBox.wait(OpenSigCore.i18n.txtAguarde(), "Backup NFe");
				FiscalProxy proxy = new FiscalProxy();
				proxy.backup(LISTA.getClasse(), LISTA.getProxy().getFiltroTotal(), donwload);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}
}
