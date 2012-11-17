package br.com.opensig.comercial.client.visao.lista;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.parametro.ParametroNumero;
import br.com.opensig.core.client.visao.PermitirSistema;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.produto.shared.modelo.ProdEstoque;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.layout.FitLayout;

public class ListagemValidarEstoque {

	private AsyncCallback async;

	public ListagemValidarEstoque(final String[][] dados, final IComando comando, final Map contexto) {
		// janela
		final Window wnd = new Window();
		wnd.setLayout(new FitLayout());
		wnd.setModal(true);
		wnd.setWidth(600);
		wnd.setHeight(300);
		wnd.setClosable(false);
		wnd.setButtonAlign(Position.CENTER);
		wnd.setTitle(OpenSigCore.i18n.txtEstoque() + " -> " + OpenSigCore.i18n.txtFechar(), "icon-estoque");

		// dados
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("id"), new StringFieldDef("descricao"), new StringFieldDef("referencia"), new FloatFieldDef("estoque"), new FloatFieldDef("qtd") };
		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "cod", 10, true);
		ccId.setHidden(true);
		ccId.setFixed(true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "descricao", 300, true);
		ColumnConfig ccEstoque = new ColumnConfig(OpenSigCore.i18n.txtEstoque(), "estoque", 75, true, new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				return "<span style='color:blue;'>" + value + "</span>";
			}
		});
		ColumnConfig ccQtd = new ColumnConfig(OpenSigCore.i18n.txtQtd(), "qtd", 75, true, new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				return "<span style='color:red;'>" + value + "</span>";
			}
		});
		ColumnConfig ccRef = new ColumnConfig(OpenSigCore.i18n.txtRef(), "referencia", 100, true);
		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccDescricao, ccRef, ccEstoque, ccQtd };

		MemoryProxy proxy = new MemoryProxy(dados);
		ArrayReader reader = new ArrayReader(new RecordDef(fd));
		final Store store = new Store(proxy, reader);
		store.load();

		// listagem
		GridPanel grid = new GridPanel();
		grid.setAutoScroll(true);
		grid.setStripeRows(true);
		grid.setStore(store);
		grid.setColumnModel(new ColumnModel(bcc));
		grid.setSelectionModel(new RowSelectionModel(true));

		// apos confirmado re-executa a acao
		async = new AsyncCallback() {
			public void onSuccess(Object result) {
				comando.execute(contexto);
				wnd.close();
			}

			public void onFailure(Throwable caught) {
				MessageBox.alert(OpenSigCore.i18n.txtFechar(), OpenSigCore.i18n.errSalvar());
			}
		};

		// permissao de gerencia
		final AsyncCallback<ILogin> asyncLogin = new AsyncCallback<ILogin>() {
			public void onSuccess(ILogin result) {
				if (result != null && result.getDesconto() == 100) {
					atualizar(store.getRecords());
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				}
			}

			public void onFailure(Throwable caught) {
				MessageBox.alert(OpenSigCore.i18n.txtFechar(), OpenSigCore.i18n.errSalvar());
			}
		};

		// botao
		Button btnCancelar = new Button();
		btnCancelar.setText(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				wnd.close();
			}
		});
		Button btnConfirmar = new Button();
		btnConfirmar.setText(OpenSigCore.i18n.txtConfirma());
		btnConfirmar.setIconCls("icon-salvar");
		btnConfirmar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (Ponte.getLogin().getDesconto() == 100) {
					atualizar(store.getRecords());
				} else {
					PermitirSistema permitir = (PermitirSistema) GWT.create(PermitirSistema.class);
					permitir.setInfo(OpenSigCore.i18n.msgPermissaoExecutar());
					permitir.executar(asyncLogin);
				}
			}
		});

		wnd.add(grid);
		wnd.addButton(btnCancelar);
		wnd.addButton(btnConfirmar);
		wnd.show();
	}

	private void atualizar(Record[] recs) {
		List<Sql> sqls = new ArrayList<Sql>();
		for (Record rec : recs) {
			FiltroNumero fn = new FiltroNumero("prodEstoqueId", ECompara.IGUAL, rec.getAsInteger("id"));
			ParametroNumero pn = new ParametroNumero("prodEstoqueQuantidade", rec.getAsDouble("qtd"));
			Sql sql = new Sql(new ProdEstoque(), EComando.ATUALIZAR, fn, pn);
			sqls.add(sql);
		}

		ComercialProxy proxy = new ComercialProxy();
		proxy.executar(sqls.toArray(new Sql[] {}), async);
	}
}
