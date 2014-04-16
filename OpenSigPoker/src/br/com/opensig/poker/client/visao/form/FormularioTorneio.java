package br.com.opensig.poker.client.visao.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.client.servico.PokerProxy;
import br.com.opensig.poker.client.visao.lista.ListagemMesa;
import br.com.opensig.poker.client.visao.lista.ListagemNivel;
import br.com.opensig.poker.client.visao.lista.ListagemPremiacao;
import br.com.opensig.poker.shared.modelo.PokerMesa;
import br.com.opensig.poker.shared.modelo.PokerNivel;
import br.com.opensig.poker.shared.modelo.PokerPremiacao;
import br.com.opensig.poker.shared.modelo.PokerTorneio;
import br.com.opensig.poker.shared.modelo.PokerTorneioTipo;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioTorneio extends AFormulario<PokerTorneio> {

	private Hidden hdnCod;
	private Hidden hdnTorneioTipo;
	private ComboBox cmbTorneioTipo;
	private TextField txtCodigo;
	private TextField txtNome;
	private NumberField txtPonto;
	private NumberField txtPremio;
	private NumberField txtTaxa;
	private DateField dtData;
	private NumberField txtEntrada;
	private NumberField txtEntradaFicha;
	private NumberField txtReentrada;
	private NumberField txtReentradaFicha;
	private NumberField txtAdicional;
	private NumberField txtAdicionalFicha;
	private NumberField txtDealer;
	private NumberField txtDealerFicha;
	private Checkbox chkAtivo;
	private TabPanel tabDados;
	private ListagemNivel gridNiveis;
	private ListagemMesa gridMesas;
	private ListagemPremiacao gridPremios;
	private List<PokerNivel> niveis;
	private List<PokerMesa> mesas;
	private List<PokerPremiacao> premios;

	public FormularioTorneio(SisFuncao funcao) {
		super(new PokerTorneio(), funcao);
		inicializar();
	}

	public void inicializar() {
		hdnCod = new Hidden("pokerTorneioId", "0");
		add(hdnCod);

		hdnTorneioTipo = new Hidden("pokerTorneioTipo.pokerTorneioTipoId", "0");
		add(hdnTorneioTipo);

		txtCodigo = new TextField(OpenSigCore.i18n.txtCod(), "pokerTorneioCodigo", 110);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "pokerTorneioNome", 180);
		txtNome.setMaxLength(20);
		txtNome.setAllowBlank(false);

		txtPonto = new NumberField(OpenSigCore.i18n.txtPontos(), "pokerTorneioPonto", 60, 0);
		txtPonto.setAllowBlank(false);
		txtPonto.setAllowNegative(false);
		txtPonto.setAllowDecimals(false);

		txtPremio = new NumberField(OpenSigCore.i18n.txtPremio(), "pokerTorneioPremio", 60, 0);
		txtPremio.setAllowBlank(false);
		txtPremio.setAllowNegative(false);

		txtTaxa = new NumberField(OpenSigCore.i18n.txtTaxa() + "%", "pokerTorneioTaxa", 60, 0);
		txtTaxa.setAllowBlank(false);

		dtData = new DateField(OpenSigCore.i18n.txtData(), "pokerTorneioData", 80);
		dtData.setAllowBlank(false);
		
		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getTorneioTipo(), 150);
		linha1.addToRow(txtCodigo, 130);
		linha1.addToRow(txtNome, 200);
		linha1.addToRow(txtPonto, 80);
		linha1.addToRow(txtPremio, 80);
		linha1.addToRow(txtTaxa, 80);
		linha1.addToRow(dtData, 100);
		add(linha1);

		txtEntrada = new NumberField(OpenSigCore.i18n.txtValor(), "pokerTorneioEntrada", 70);
		txtEntrada.setAllowBlank(false);
		txtEntrada.setAllowNegative(false);

		txtEntradaFicha = new NumberField(OpenSigCore.i18n.txtFichas(), "pokerTorneioEntradaFicha", 50);
		txtEntradaFicha.setAllowBlank(false);
		txtEntradaFicha.setAllowNegative(false);
		txtEntradaFicha.setAllowDecimals(false);

		MultiFieldPanel box1 = new MultiFieldPanel();
		box1.addToRow(txtEntrada, 90);
		box1.addToRow(txtEntradaFicha, 70);
		box1.setBorder(false);
		FieldSet entrada = new FieldSet(OpenSigCore.i18n.txtBuyin());
		entrada.add(box1);

		txtReentrada = new NumberField(OpenSigCore.i18n.txtValor(), "pokerTorneioReentrada", 70);
		txtReentrada.setAllowBlank(false);
		txtReentrada.setAllowNegative(false);

		txtReentradaFicha = new NumberField(OpenSigCore.i18n.txtFichas(), "pokerTorneioReentradaFicha", 50);
		txtReentradaFicha.setAllowBlank(false);
		txtReentradaFicha.setAllowNegative(false);
		txtReentradaFicha.setAllowDecimals(false);

		MultiFieldPanel box2 = new MultiFieldPanel();
		box2.addToRow(txtReentrada, 90);
		box2.addToRow(txtReentradaFicha, 70);
		box2.setBorder(false);
		FieldSet reentrada = new FieldSet(OpenSigCore.i18n.txtRebuy());
		reentrada.add(box2);

		txtAdicional = new NumberField(OpenSigCore.i18n.txtValor(), "pokerTorneioAdicional", 70);
		txtAdicional.setAllowBlank(false);
		txtAdicional.setAllowNegative(false);

		txtAdicionalFicha = new NumberField(OpenSigCore.i18n.txtFichas(), "pokerTorneioAdicionalFicha", 50);
		txtAdicionalFicha.setAllowBlank(false);
		txtAdicionalFicha.setAllowNegative(false);
		txtAdicionalFicha.setAllowDecimals(false);

		MultiFieldPanel box3 = new MultiFieldPanel();
		box3.addToRow(txtAdicional, 90);
		box3.addToRow(txtAdicionalFicha, 70);
		box3.setBorder(false);
		FieldSet adicional = new FieldSet(OpenSigCore.i18n.txtAddon());
		adicional.add(box3);
		
		txtDealer = new NumberField(OpenSigCore.i18n.txtValor(), "pokerTorneioDealer", 70);
		txtDealer.setAllowBlank(false);
		txtDealer.setAllowNegative(false);

		txtDealerFicha = new NumberField(OpenSigCore.i18n.txtFichas(), "pokerTorneioAdicionalFicha", 50);
		txtDealerFicha.setAllowBlank(false);
		txtDealerFicha.setAllowNegative(false);
		txtDealerFicha.setAllowDecimals(false);

		MultiFieldPanel box4 = new MultiFieldPanel();
		box4.addToRow(txtDealer, 90);
		box4.addToRow(txtDealerFicha, 70);
		box4.setBorder(false);
		FieldSet dealer = new FieldSet(OpenSigCore.i18n.txtDealer());
		dealer.add(box4);

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "pokerTorneioAtivo");
		chkAtivo.setValue(true);
		
		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.addToRow(entrada, 190);
		linha2.addToRow(reentrada, 190);
		linha2.addToRow(adicional, 190);
		linha2.addToRow(dealer, 190);
		linha2.addToRow(chkAtivo, 60);
		linha2.setBorder(false);
		add(linha2);

		gridNiveis = new ListagemNivel(true);
		gridMesas = new ListagemMesa(true);
		gridPremios = new ListagemPremiacao(true);

		tabDados = new TabPanel();
		tabDados.setPlain(true);
		tabDados.setHeight(300);
		tabDados.setActiveTab(0);
		tabDados.add(gridNiveis);
		tabDados.add(gridMesas);
		tabDados.add(gridPremios);
		add(tabDados);

		super.inicializar();
	}

	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					PokerProxy proxy = new PokerProxy();
					proxy.salvarTorneio(classe, ASYNC);
				}
			};
		}

		return comando;
	}

	public boolean setDados() {
		boolean retorno = true;
		// valida os niveis
		niveis = new ArrayList<PokerNivel>();
		if (!gridNiveis.validar(niveis)) {
			tabDados.setActiveTab(0);
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}
		// valida as mesas
		mesas = new ArrayList<PokerMesa>();
		if (!gridMesas.validar(mesas)) {
			tabDados.setActiveTab(1);
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}
		// valida os premios
		premios = new ArrayList<PokerPremiacao>();
		if (!gridPremios.validar(premios)) {
			tabDados.setActiveTab(2);
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		classe.setPokerNiveis(niveis);
		classe.setPokerMesas(mesas);
		classe.setPokerPremiacoes(premios);
		classe.setPokerTorneioId(Integer.valueOf(hdnCod.getValueAsString()));
		if (!hdnTorneioTipo.getValueAsString().equals("0")) {
			PokerTorneioTipo tipo = new PokerTorneioTipo(Integer.valueOf(hdnTorneioTipo.getValueAsString()));
			classe.setPokerTorneioTipo(tipo);
		}
		classe.setPokerTorneioCodigo(txtCodigo.getValueAsString());
		classe.setPokerTorneioNome(txtNome.getValueAsString());
		if (txtPonto.getValue() != null) {
			classe.setPokerTorneioPonto(txtPonto.getValue().intValue());
		}
		if (txtPremio.getValue() != null) {
			classe.setPokerTorneioPremio(txtPremio.getValue().doubleValue());
		}
		if (txtTaxa.getValue() != null) {
			classe.setPokerTorneioTaxa(txtTaxa.getValue().doubleValue());
		}
		classe.setPokerTorneioData(dtData.getValue());
		if (txtEntrada.getValue() != null) {
			classe.setPokerTorneioEntrada(txtEntrada.getValue().doubleValue());
		}
		if (txtEntradaFicha.getValue() != null) {
			classe.setPokerTorneioEntradaFicha(txtEntradaFicha.getValue().intValue());
		}
		if (txtReentrada.getValue() != null) {
			classe.setPokerTorneioReentrada(txtReentrada.getValue().doubleValue());
		}
		if (txtReentradaFicha.getValue() != null) {
			classe.setPokerTorneioReentradaFicha(txtReentradaFicha.getValue().intValue());
		}
		if (txtAdicional.getValue() != null) {
			classe.setPokerTorneioAdicional(txtAdicional.getValue().doubleValue());
		}
		if (txtAdicionalFicha.getValue() != null) {
			classe.setPokerTorneioAdicionalFicha(txtAdicionalFicha.getValue().intValue());
		}
		if (txtDealer.getValue() != null) {
			classe.setPokerTorneioDealer(txtDealer.getValue().doubleValue());
		}
		if (txtDealerFicha.getValue() != null) {
			classe.setPokerTorneioDealerFicha(txtDealerFicha.getValue().intValue());
		}
		classe.setPokerTorneioAtivo(chkAtivo.getValue());

		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		FiltroNumero fn = new FiltroNumero("pokerNivelId", ECompara.IGUAL, 0);
		gridNiveis.getProxy().setFiltroPadrao(fn);
		gridNiveis.getStore().removeAll();

		FiltroNumero fn1 = new FiltroNumero("pokerMesaId", ECompara.IGUAL, 0);
		gridMesas.getProxy().setFiltroPadrao(fn1);
		gridMesas.getStore().removeAll();

		FiltroNumero fn2 = new FiltroNumero("pokerPremiacaoId", ECompara.IGUAL, 0);
		gridPremios.getProxy().setFiltroPadrao(fn2);
		gridPremios.getStore().removeAll();
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
			classe.setPokerTorneioId(rec.getAsInteger("pokerTorneioId"));
			classe.setPokerTorneioArrecadado(rec.getAsDouble("pokerTorneioArrecadado"));
			classe.setPokerTorneioComissao(rec.getAsDouble("pokerTorneioComissao"));
			classe.setPokerTorneioFechado(rec.getAsBoolean("pokerTorneioFechado"));

			FiltroObjeto fo = new FiltroObjeto("pokerTorneio", ECompara.IGUAL, classe);
			gridNiveis.getProxy().setFiltroPadrao(fo);
			gridNiveis.getStore().reload();

			gridMesas.getProxy().setFiltroPadrao(fo);
			gridMesas.getStore().reload();

			gridPremios.getProxy().setFiltroPadrao(fo);
			gridPremios.getStore().reload();
		} else {
			txtCodigo.setValue("T" + new Date().getTime());
		}
		txtCodigo.setDisabled(true);

		if (duplicar) {
			hdnCod.setValue("0");
			txtCodigo.setValue("T" + new Date().getTime());
			duplicar = false;
			classe.setPokerTorneioArrecadado(0.00);
			classe.setPokerTorneioComissao(0.00);
			classe.setPokerTorneioFechado(false);
		}
	}

	public void gerarListas() {
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("pokerTorneio", ECompara.IGUAL, new PokerTorneio(id));

		// niveis
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridNiveis.getModelos().getColumnCount(); i++) {
			if (gridNiveis.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridNiveis.getModelos().getColumnHeader(i), gridNiveis.getModelos().getColumnWidth(i), null);
				metadados.add(meta);
			}
		}

		SortState ordem = gridNiveis.getStore().getSortState();
		PokerNivel nivel = new PokerNivel();
		nivel.setCampoOrdem(ordem.getField());
		nivel.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));

		ExpListagem<PokerNivel> niveis = new ExpListagem<PokerNivel>();
		niveis.setClasse(nivel);
		niveis.setMetadados(metadados);
		niveis.setNome(gridNiveis.getTitle());
		niveis.setFiltro(filtro);

		// mesas
		List<ExpMeta> metadados1 = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridMesas.getModelos().getColumnCount(); i++) {
			if (gridMesas.getModelos().isHidden(i)) {
				metadados1.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridMesas.getModelos().getColumnHeader(i), gridMesas.getModelos().getColumnWidth(i), null);
				metadados1.add(meta);
			}
		}

		SortState ordem1 = gridMesas.getStore().getSortState();
		PokerMesa mesa = new PokerMesa();
		mesa.setCampoOrdem(ordem1.getField());
		mesa.setOrdemDirecao(EDirecao.valueOf(ordem1.getDirection().getDirection()));

		ExpListagem<PokerMesa> mesas = new ExpListagem<PokerMesa>();
		mesas.setClasse(mesa);
		mesas.setMetadados(metadados1);
		mesas.setNome(gridMesas.getTitle());
		mesas.setFiltro(filtro);

		// mesas
		List<ExpMeta> metadados2 = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridPremios.getModelos().getColumnCount(); i++) {
			if (gridPremios.getModelos().isHidden(i)) {
				metadados2.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridPremios.getModelos().getColumnHeader(i), gridPremios.getModelos().getColumnWidth(i), null);
				metadados2.add(meta);
			}
		}

		SortState ordem2 = gridPremios.getStore().getSortState();
		PokerPremiacao premio = new PokerPremiacao();
		premio.setCampoOrdem(ordem2.getField());
		premio.setOrdemDirecao(EDirecao.valueOf(ordem2.getDirection().getDirection()));

		ExpListagem<PokerPremiacao> premios = new ExpListagem<PokerPremiacao>();
		premios.setClasse(premio);
		premios.setMetadados(metadados2);
		premios.setNome(gridPremios.getTitle());
		premios.setFiltro(filtro);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(niveis);
		expLista.add(mesas);
		expLista.add(premios);
	}

	private ComboBox getTorneioTipo() {
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("pokerTorneioTipoId"), new StringFieldDef("pokerTorneioTipoNome") };
		CoreProxy<PokerTorneioTipo> proxy = new CoreProxy<PokerTorneioTipo>(new PokerTorneioTipo());
		Store store = new Store(proxy, new ArrayReader(new RecordDef(campos)), false);

		cmbTorneioTipo = new ComboBox(OpenSigCore.i18n.txtTipo(), "pokerTorneioTipo.pokerTorneioTipoNome", 130);
		cmbTorneioTipo.setAllowBlank(false);
		cmbTorneioTipo.setStore(store);
		cmbTorneioTipo.setTriggerAction(ComboBox.ALL);
		cmbTorneioTipo.setMode(ComboBox.REMOTE);
		cmbTorneioTipo.setMinChars(1);
		cmbTorneioTipo.setDisplayField("pokerTorneioTipoNome");
		cmbTorneioTipo.setValueField("pokerTorneioTipoId");
		cmbTorneioTipo.setForceSelection(true);
		cmbTorneioTipo.setEditable(false);
		cmbTorneioTipo.setListWidth(130);
		cmbTorneioTipo.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnTorneioTipo.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbTorneioTipo.getRawValue().equals("")) {
					hdnTorneioTipo.setValue("0");
				}
			}
		});

		return cmbTorneioTipo;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnTorneioTipo() {
		return hdnTorneioTipo;
	}

	public void setHdnTorneioTipo(Hidden hdnTorneioTipo) {
		this.hdnTorneioTipo = hdnTorneioTipo;
	}

	public ComboBox getCmbTorneioTipo() {
		return cmbTorneioTipo;
	}

	public void setCmbTorneioTipo(ComboBox cmbTorneioTipo) {
		this.cmbTorneioTipo = cmbTorneioTipo;
	}

	public TextField getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(TextField txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

	public TextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(TextField txtNome) {
		this.txtNome = txtNome;
	}

	public NumberField getTxtPremio() {
		return txtPremio;
	}

	public void setTxtPremio(NumberField txtPremio) {
		this.txtPremio = txtPremio;
	}

	public NumberField getTxtTaxa() {
		return txtTaxa;
	}

	public void setTxtTaxa(NumberField txtTaxa) {
		this.txtTaxa = txtTaxa;
	}

	public DateField getDtData() {
		return dtData;
	}

	public void setDtData(DateField dtData) {
		this.dtData = dtData;
	}

	public NumberField getTxtEntrada() {
		return txtEntrada;
	}

	public void setTxtEntrada(NumberField txtEntrada) {
		this.txtEntrada = txtEntrada;
	}

	public NumberField getTxtEntradaFicha() {
		return txtEntradaFicha;
	}

	public void setTxtEntradaFicha(NumberField txtEntradaFicha) {
		this.txtEntradaFicha = txtEntradaFicha;
	}

	public NumberField getTxtReentrada() {
		return txtReentrada;
	}

	public void setTxtReentrada(NumberField txtReentrada) {
		this.txtReentrada = txtReentrada;
	}

	public NumberField getTxtReentradaFicha() {
		return txtReentradaFicha;
	}

	public void setTxtReentradaFicha(NumberField txtReentradaFicha) {
		this.txtReentradaFicha = txtReentradaFicha;
	}

	public NumberField getTxtAdicional() {
		return txtAdicional;
	}

	public void setTxtAdicional(NumberField txtAdicional) {
		this.txtAdicional = txtAdicional;
	}

	public NumberField getTxtAdicionalFicha() {
		return txtAdicionalFicha;
	}

	public void setTxtAdicionalFicha(NumberField txtAdicionalFicha) {
		this.txtAdicionalFicha = txtAdicionalFicha;
	}

	public NumberField getTxtDealer() {
		return txtDealer;
	}

	public void setTxtDealer(NumberField txtDealer) {
		this.txtDealer = txtDealer;
	}

	public NumberField getTxtDealerFicha() {
		return txtDealerFicha;
	}

	public void setTxtDealerFicha(NumberField txtDealerFicha) {
		this.txtDealerFicha = txtDealerFicha;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

	public TabPanel getTabDados() {
		return tabDados;
	}

	public void setTabDados(TabPanel tabDados) {
		this.tabDados = tabDados;
	}

	public ListagemNivel getGridNiveis() {
		return gridNiveis;
	}

	public void setGridNiveis(ListagemNivel gridNiveis) {
		this.gridNiveis = gridNiveis;
	}

	public ListagemMesa getGridMesas() {
		return gridMesas;
	}

	public void setGridMesas(ListagemMesa gridMesas) {
		this.gridMesas = gridMesas;
	}

	public NumberField getTxtPonto() {
		return txtPonto;
	}

	public void setTxtPonto(NumberField txtPonto) {
		this.txtPonto = txtPonto;
	}

	public ListagemPremiacao getGridPremios() {
		return gridPremios;
	}

	public void setGridPremios(ListagemPremiacao gridPremios) {
		this.gridPremios = gridPremios;
	}

	public List<PokerNivel> getNiveis() {
		return niveis;
	}

	public void setNiveis(List<PokerNivel> niveis) {
		this.niveis = niveis;
	}

	public List<PokerMesa> getMesas() {
		return mesas;
	}

	public void setMesas(List<PokerMesa> mesas) {
		this.mesas = mesas;
	}

	public List<PokerPremiacao> getPremios() {
		return premios;
	}

	public void setPremios(List<PokerPremiacao> premios) {
		this.premios = premios;
	}

}
