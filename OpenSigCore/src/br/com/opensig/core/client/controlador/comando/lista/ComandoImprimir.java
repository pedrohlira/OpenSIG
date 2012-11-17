package br.com.opensig.core.client.controlador.comando.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.controlador.comando.EModo;
import br.com.opensig.core.client.visao.JanelaExportar;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

/**
 * Classe de exportacao do tipo impressao em tela.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class ComandoImprimir extends ComandoAcao {

	private AsyncCallback<String> async;

	/**
	 * Construtor padrao.
	 */
	public ComandoImprimir() {
		async = new AsyncCallback<String>() {
			public void onSuccess(String arg0) {
				LISTA.getPanel().getEl().unmask();
				UtilClient.exportar("ExportacaoService?imp=true&id=" + arg0);
			}

			public void onFailure(Throwable arg0) {
				LISTA.getPanel().getEl().unmask();
				MessageBox.alert(OpenSigCore.i18n.txtImprimir(), OpenSigCore.i18n.errImprimir());
			}
		};
	}

	@Override
	public void execute(final Map contexto) {
		super.execute(contexto);
		final EModo modo = contexto.get("acao") != null ? (EModo) contexto.get("acao") : EModo.LISTAGEM;
		final JanelaExportar janela = new JanelaExportar(LISTA, modo, async);

		janela.getBtnOK().purgeListeners();
		janela.getBtnOK().addListener(new ButtonListenerAdapter() {
			public void onClick(com.gwtext.client.widgets.Button button, com.gwtext.client.core.EventObject e) {
				janela.getDataView().select(1);
				janela.setRec(janela.getStore().getAt(1));
				janela.exportar();
			};
		});
		janela.getPanTipo().setVisible(false);
		janela.setIconCls("icon-imprimir");
		janela.setTitle(OpenSigCore.i18n.txtImprimir());
		janela.setHeight(160);
		janela.show();
	}
}
