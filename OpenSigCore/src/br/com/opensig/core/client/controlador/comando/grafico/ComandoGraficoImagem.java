package br.com.opensig.core.client.controlador.comando.grafico;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.servico.ExportacaoProxy;
import br.com.opensig.core.shared.modelo.Dados;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

/**
 * Classe de comando de exportacao da imagem do grafico.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoGraficoImagem<E extends Dados> extends AComando<E> {

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto);
		String imagem = contexto.get("imagem").toString();
		ExportacaoProxy proxy = new ExportacaoProxy();
		proxy.exportar(imagem, "grafico.png", new AsyncCallback<String>() {
			public void onSuccess(String result) {
				UtilClient.exportar("ExportacaoService?id=" + result);
			}

			public void onFailure(Throwable caught) {
				MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());
			}
		});
	}
}
