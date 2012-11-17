package br.com.opensig.core.client.visao;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.XTemplate;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Reader;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DataView;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DataViewListenerAdapter;

public class JanelaImportar extends Window {

	protected Record rec;
	protected IListagem lista;

	protected Button btnOK;
	protected Panel panTipo;
	protected DataView dataView;

	protected Reader reader;
	protected XTemplate template;
	protected Store store;

	public JanelaImportar(IListagem lista) {
		this.lista = lista;
		inicializar();
		setDados();
	}

	public void inicializar() {
		// botao ok
		btnOK = new Button(OpenSigCore.i18n.txtOk());
		btnOK.setIconCls("icon-salvar");
		btnOK.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				importar();
			}
		});

		setTitle(OpenSigCore.i18n.txtImportar());
		setSize(300, 180);
		setModal(true);
		setResizable(false);
		setButtonAlign(Position.CENTER);
		setIconCls("icon-importar");
		add(getTipo());
		addButton(btnOK);
		doLayout();
	}

	protected Panel getTipo() {
		reader = new ArrayReader(new RecordDef(new FieldDef[] { new StringFieldDef("classe"), new StringFieldDef("extensao"), new StringFieldDef("modelo"), new StringFieldDef("nome"),
				new StringFieldDef("imagem"), new StringFieldDef("descricao") }));
		template = new XTemplate(new String[] { "<tpl for='.'>", "<div class='thumb-wrap'>",
				"<div class='thumb'><img src='" + GWT.getModuleName() + "/img/expimp/{imagem}' ext:qtip='{descricao}'/></div>", "<span class='x-editable' ext:qtip='{descricao}'>{nome}</span></div>",
				"</tpl>", "<div class='x-clear'></div>" });

		// tipo
		panTipo = new Panel();
		panTipo.setAutoWidth(true);
		panTipo.setHeight(115);
		panTipo.setAutoScroll(true);
		panTipo.setPaddings(5);
		// visao
		dataView = new DataView("div.thumb-wrap");
		dataView.setTpl(template);
		dataView.setSingleSelect(true);
		dataView.setOverCls("x-view-over");
		dataView.addListener(new DataViewListenerAdapter() {
			public void onClick(DataView source, int index, Element node, EventObject e) {
				rec = source.getRecord(node);
			}
		});

		panTipo.add(dataView);
		return panTipo;
	}

	protected void setDados() {
		final List<String[]> dados = new ArrayList<String[]>();
		// filtro
		FiltroTexto ft1 = new FiltroTexto("sisExpImpFuncao", ECompara.IGUAL, lista.getFuncao().getSisFuncaoClasse());
		FiltroTexto ft2 = new FiltroTexto("sisExpImpTipo", ECompara.IGUAL, "I");
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft1, ft2 });

		CoreProxy<SisExpImp> proxy = new CoreProxy<SisExpImp>(new SisExpImp());
		proxy.selecionar(0, 0, gf, false, new AsyncCallback<Lista<SisExpImp>>() {
			public void onSuccess(Lista<SisExpImp> result) {
				if (result.getLista().isEmpty()) {
					MessageBox.alert(OpenSigCore.i18n.txtImportar(), OpenSigCore.i18n.errRegistro());
					close();
				} else {
					// gera as importacoes dinamicas
					for (SisExpImp expimp : result.getLista()) {
						String[] imp = new String[] { expimp.getSisExpImpClasse(), expimp.getSisExpImpExtensoes(), expimp.getSisExpImpModelo(), expimp.getSisExpImpNome(), expimp.getSisExpImpImagem(),
								expimp.getSisExpImpDescricao() };
						dados.add(imp);
					}
					// carrega a store
					MemoryProxy dataProxy = new MemoryProxy(dados.toArray(new String[][] {}));
					store = new Store(dataProxy, reader);
					store.load();
					dataView.setStore(store);
					dataView.refresh();
					dataView.select(0);
					rec = store.getAt(0);
					btnOK.focus();
				}
			}

			public void onFailure(Throwable caught) {
				onSuccess(null);
			}
		});
	}

	public void importar() {
		if (dataView.getSelectionCount() == 0) {
			dataView.select(0);
			rec = store.getAt(0);
		}

		SisExpImp expimp = new SisExpImp();
		expimp.setSisExpImpNome(rec.getAsString("nome"));
		expimp.setSisExpImpClasse(rec.getAsString("classe"));
		expimp.setSisExpImpExtensoes(rec.getAsString("extensao"));
		expimp.setSisExpImpModelo(rec.getAsString("modelo"));
		lista.setImportacao(expimp);
		close();
	}

	public Record getRec() {
		return rec;
	}

	public void setRec(Record rec) {
		this.rec = rec;
	}

	public Button getBtnOK() {
		return btnOK;
	}

	public void setBtnOK(Button btnOK) {
		this.btnOK = btnOK;
	}

	public Panel getPanTipo() {
		return panTipo;
	}

	public void setPanTipo(Panel panTipo) {
		this.panTipo = panTipo;
	}

	public DataView getDataView() {
		return dataView;
	}

	public void setDataView(DataView dataView) {
		this.dataView = dataView;
	}

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}

	public XTemplate getTemplate() {
		return template;
	}

	public void setTemplate(XTemplate template) {
		this.template = template;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public IListagem getLista() {
		return lista;
	}

	public void setLista(IListagem lista) {
		this.lista = lista;
	}

}
