package br.com.opensig.core.client.visao;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.EModo;
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
import com.google.gwt.user.client.ui.HTML;
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
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class JanelaExportar extends Window {

	protected IListagem lista;
	protected EModo modo;
	protected Record rec;
	protected AsyncCallback<String> async;
	
	protected int limite;
	protected Button btnOK;
	protected Panel panTipo;
	protected DataView dataView;
	protected Panel panForm;
	protected Radio chkListagem;
	protected Radio chkRegistro;
	protected Radio chkTudo;
	protected Radio chkAtual;
	protected Radio chkIntervalo;
	protected TextField txtIntervalo;

	protected Reader reader;
	protected XTemplate template;
	protected Store store;

	public JanelaExportar(final IListagem lista, EModo modo) {
		this(lista, modo, new AsyncCallback<String>() {
			public void onSuccess(String arg0) {
				lista.getPanel().getEl().unmask();
				UtilClient.exportar("ExportacaoService?id=" + arg0);
			}

			public void onFailure(Throwable arg0) {
				lista.getPanel().getEl().unmask();
				MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.errExportar());
			}
		});
	}

	public JanelaExportar(IListagem lista, EModo modo, AsyncCallback<String> async) {
		this.lista = lista;
		this.modo = modo;
		this.async = async;
		this.limite = UtilClient.CONF.get("listagem.registro") != null ? Integer.valueOf(UtilClient.CONF.get("listagem.registro")) : 500;

		inicializar();
		setDados();
	}

	public void inicializar() {
		// botao ok
		btnOK = new Button(OpenSigCore.i18n.txtOk());
		btnOK.setIconCls("icon-salvar");
		btnOK.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				exportar();
			}
		});

		setTitle(OpenSigCore.i18n.txtExportar());
		setSize(300, 280);
		setModal(true);
		setResizable(false);
		setButtonAlign(Position.CENTER);
		setIconCls("icon-exportar");
		add(getTipo());
		add(getOpcoes());
		addButton(btnOK);
		doLayout();
	}

	protected Panel getTipo() {
		reader = new ArrayReader(new RecordDef(new FieldDef[] { new StringFieldDef("classe"), new StringFieldDef("extensao"), new StringFieldDef("modelo"), new StringFieldDef("modo"),
				new StringFieldDef("nome"), new StringFieldDef("imagem"), new StringFieldDef("descricao") }));
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
		// pdf
		String[] pdf = new String[] { "br.com.opensig.core.server.exportar.Pdf", "pdf", "", modo.name(), "PDF", "pdf.png", "" };
		dados.add(pdf);
		// html
		String[] html = new String[] { "br.com.opensig.core.server.exportar.Html", "html", "", modo.name(), "HTML", "html.png", "" };
		dados.add(html);
		// xls
		String[] xls = new String[] { "br.com.opensig.core.server.exportar.Xls", "xls", "", modo.name(), "XLS", "xls.png", "" };
		dados.add(xls);

		// filtro
		FiltroTexto ft1 = new FiltroTexto("sisExpImpFuncao", ECompara.IGUAL, lista.getFuncao().getSisFuncaoClasse());
		FiltroTexto ft2 = new FiltroTexto("sisExpImpTipo", ECompara.IGUAL, "E");
		GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft1, ft2 });

		CoreProxy<SisExpImp> proxy = new CoreProxy<SisExpImp>(new SisExpImp());
		proxy.selecionar(0, 0, gf, false, new AsyncCallback<Lista<SisExpImp>>() {
			public void onSuccess(Lista<SisExpImp> result) {
				if (result != null) {
					// gera as exportacoes dinamicas
					for (SisExpImp expimp : result.getLista()) {
						String[] exp = new String[] { expimp.getSisExpImpClasse(), expimp.getSisExpImpExtensoes(), expimp.getSisExpImpModelo(), modo.name(), expimp.getSisExpImpNome(),
								expimp.getSisExpImpImagem(), expimp.getSisExpImpDescricao() };
						dados.add(exp);
					}
				}
				// carrega a store
				MemoryProxy dataProxy = new MemoryProxy(dados.toArray(new String[][] {}));
				store = new Store(dataProxy, reader);
				store.load();
				// atualiza a view
				dataView.setStore(store);
				dataView.refresh();
				dataView.select(0);
				rec = store.getAt(0);
				btnOK.focus();
			}

			public void onFailure(Throwable caught) {
				onSuccess(null);
			}
		});
	}

	protected Panel getOpcoes() {
		panForm = new Panel();
		panForm.setAutoScroll(true);
		panForm.setAutoWidth(true);
		panForm.setAutoHeight(true);
		panForm.setPaddings(5);
		panForm.setMargins(1);

		panForm.add(new HTML(OpenSigCore.i18n.msgExportarPagina()));
		panForm.add(new HTML("<br/>"));

		Panel panDados = new Panel();
		panDados.setLayout(new HorizontalLayout(5));
		
		chkRegistro = new Radio(OpenSigCore.i18n.txtRegistro(), "dados");
		chkRegistro.setHideLabel(true);
		chkRegistro.setChecked(modo == EModo.REGISTRO);
		panDados.add(chkRegistro);
		
		chkListagem = new Radio(OpenSigCore.i18n.txtListagem(),  "dados");
		chkListagem.setHideLabel(true);
		chkListagem.setChecked(modo == EModo.LISTAGEM);
		chkListagem.setDisabled(modo == EModo.REGISTRO);
		panDados.add(chkListagem);
		
		Panel panRange = new Panel();
		panRange.setLayout(new HorizontalLayout(5));

		chkTudo = new Radio(OpenSigCore.i18n.txtTudo(), "range");
		chkTudo.setHideLabel(true);
		chkTudo.setChecked(modo == EModo.LISTAGEM);
		chkTudo.setDisabled(modo == EModo.REGISTRO);
		panRange.add(chkTudo);

		chkAtual = new Radio(OpenSigCore.i18n.txtAtual(), "range");
		chkAtual.setHideLabel(true);
		chkAtual.setChecked(modo == EModo.REGISTRO);
		panRange.add(chkAtual);

		txtIntervalo = new TextField("", "intervalo", 60);
		txtIntervalo.setRegex("[0-9]+\\-[0-9]+");
		txtIntervalo.setLabelSeparator("");
		txtIntervalo.disable();

		chkIntervalo = new Radio(OpenSigCore.i18n.txtIntervalo(), "range");
		chkIntervalo.setHideLabel(true);
		chkIntervalo.setDisabled(modo == EModo.REGISTRO);
		chkIntervalo.addListener(new CheckboxListenerAdapter() {
			public void onCheck(Checkbox field, boolean checked) {
				txtIntervalo.setValue("");
				if (checked) {
					txtIntervalo.enable();
					txtIntervalo.focus();
				} else {
					txtIntervalo.disable();
				}
			}
		});
		panRange.add(chkIntervalo);
		panRange.add(txtIntervalo);

		panForm.add(panDados);
		panForm.add(panRange);
		return panForm;
	}

	public void exportar() {
		// intervalo
		int inicio = 0;
		int fim = 0;
		int tamanho = lista.getPaginador().getPageSize();

		if (chkAtual.getValue()) {
			inicio = (lista.getPaginador().getCurrentPage() - 1) * tamanho;
			fim = tamanho;
		} else if (chkIntervalo.getValue()) {
			try {
				String[] intervalo = txtIntervalo.getValueAsString().split("-");
				if (intervalo.length == 2) {
					int pi = Integer.valueOf(intervalo[0].trim());
					int pf = Integer.valueOf(intervalo[1].trim());
					inicio = (pi - 1) * tamanho;
					fim = (pf - pi) * tamanho + tamanho;
				}
			} catch (Exception e) {
				// pega tudo
			}

			// ajustando
			if (inicio < 0 || inicio > lista.getPanel().getStore().getTotalCount()) {
				inicio = 0;
			}
		}

		// setando opcoes
		if (dataView.getSelectionCount() == 0) {
			dataView.select(0);
			rec = store.getAt(0);
		}
		SisExpImp expimp = new SisExpImp();
		expimp.setSisExpImpClasse(rec.getAsString("classe"));
		expimp.setSisExpImpExtensoes(rec.getAsString("extensao"));
		expimp.setSisExpImpModelo(rec.getAsString("modelo"));
		expimp.setInicio(inicio);
		expimp.setLimite(fim);

		if ((fim == 0 && limite > 0 && lista.getPanel().getStore().getTotalCount() > limite) || (fim > limite && limite > 0)) {
			MessageBox.alert(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.msgExportar(limite + ""));
		} else if (lista.getPanel().getStore().getCount() > 0) {
			EModo modo2 = chkRegistro.getValue() ? EModo.REGISTRO : EModo.LISTAGEM;
			lista.setExportacao(expimp, modo, modo2, async);
			close();
		} else {
			new ToastWindow(OpenSigCore.i18n.txtExportar(), OpenSigCore.i18n.msgRegistro()).show();
			close();
		}
	}

	public IListagem getLista() {
		return lista;
	}
	
	public void setLista(IListagem lista) {
		this.lista = lista;
	}

	public EModo getModo() {
		return modo;
	}

	public void setModo(EModo modo) {
		this.modo = modo;
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

	public Panel getPanForm() {
		return panForm;
	}

	public void setPanForm(Panel panForm) {
		this.panForm = panForm;
	}

	public Radio getChkTudo() {
		return chkTudo;
	}

	public void setChkTudo(Radio chkTudo) {
		this.chkTudo = chkTudo;
	}

	public Radio getChkAtual() {
		return chkAtual;
	}

	public void setChkAtual(Radio chkAtual) {
		this.chkAtual = chkAtual;
	}

	public Radio getChkIntervalo() {
		return chkIntervalo;
	}

	public void setChkIntervalo(Radio chkIntervalo) {
		this.chkIntervalo = chkIntervalo;
	}

	public TextField getTxtIntervalo() {
		return txtIntervalo;
	}

	public void setTxtIntervalo(TextField txtIntervalo) {
		this.txtIntervalo = txtIntervalo;
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

	public int getLimite() {
		return limite;
	}

	public void setLimite(int limite) {
		this.limite = limite;
	}

}
