package br.com.opensig.produto.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.client.visao.form.FormularioPesquisa;
import br.com.opensig.produto.shared.modelo.ProdProduto;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class ComandoPesquisa extends ComandoFuncao {
	private static Window wnd;
	private static FormularioPesquisa formPesquisa;
	private AsyncCallback<Record> async;

	public ComandoPesquisa() {
		this(null);
	}

	public ComandoPesquisa(AsyncCallback<Record> async) {
		this.async = async;
	}

	public void execute(Map contexto) {
		if (wnd == null) {
			wnd = new Window();
			wnd.setLayout(new FitLayout());
			wnd.setModal(true);
			wnd.setClosable(false);
			wnd.setTitle(OpenSigCore.i18n.txtPesquisar());
			wnd.setIconCls("icon-pesquisa");
			wnd.setButtonAlign(Position.CENTER);
			wnd.addListener(new WindowListenerAdapter(){
				public void onShow(Component component) {
					formPesquisa.getTxtBusca().focus(true, 10);
				}
			});

			DADOS = (SisFuncao) contexto.get("dados");
			formPesquisa = new FormularioPesquisa(new ProdProduto(), DADOS);
			formPesquisa.setWnd(wnd);
			wnd.add(formPesquisa);
		}

		formPesquisa.setAsyncResultado(async);
		formPesquisa.mostrarDados();
		wnd.setWidth(Ext.getBody().getWidth() - 20);
		wnd.setHeight(Ext.getBody().getHeight() - 20);
		wnd.doLayout();
		wnd.show();
	}
}
