package br.com.opensig.comercial.client.visao.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.comercial.client.GerarPreco;
import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.client.visao.lista.ListagemValorArredonda;
import br.com.opensig.comercial.shared.modelo.ComValorArredonda;
import br.com.opensig.comercial.shared.modelo.ComValorProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.produto.shared.modelo.ProdProduto;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioValorProduto extends AFormulario<ComValorProduto> {

	private Label lblVariaveis;
	private Label lblValidar;
	private Label variaveis;
	private Label teste;
	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private Hidden hdnFornecedor;
	private Hidden hdnProduto;
	private NumberField txtDespesa;
	private NumberField txtMarkup;
	private ComboBox cmbFornecedor;
	private ComboBox cmbProduto;
	private TextArea txtFormula;
	private ListagemValorArredonda gridArredonda;
	private ToolbarButton btnFormula;

	public FormularioValorProduto(SisFuncao funcao) {
		super(new ComValorProduto(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comValorProdutoId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);
		hdnFornecedor = new Hidden("empFornecedor.empFornecedorId", "0");
		add(hdnFornecedor);
		hdnProduto = new Hidden("prodProduto.prodProdutoId", "0");
		add(hdnProduto);

		txtDespesa = new NumberField(OpenSigCore.i18n.txtDespesa() + " %", "comValorProdutoDespesa", 50);
		txtDespesa.setAllowBlank(false);
		txtDespesa.setAllowDecimals(false);
		txtDespesa.setAllowNegative(false);
		txtDespesa.setMaxLength(3);

		txtMarkup = new NumberField(OpenSigCore.i18n.txtLucro() + " %", "comValorProdutoMarkup", 50);
		txtMarkup.setAllowBlank(false);
		txtMarkup.setAllowDecimals(false);
		txtMarkup.setAllowNegative(false);
		txtMarkup.setMaxLength(3);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtDespesa, 70);
		linha1.addToRow(txtMarkup, 70);
		linha1.addToRow(getFornecedor(), 330);
		linha1.addToRow(getProduto(), 220);
		add(linha1);

		txtFormula = new TextArea(OpenSigCore.i18n.txtFormula(), "comValorProdutoFormula");
		txtFormula.setTitle(OpenSigCore.i18n.msgFormula());
		txtFormula.setMaxLength(1000);
		txtFormula.setAllowBlank(false);
		txtFormula.setWidth("95%");
		add(txtFormula);

		final Map<String, String> vars = new HashMap<String, String>();
		{
			vars.put("BRUTO", "10.00");
			vars.put("IPI", "05");
			vars.put("ICMS", "07");
			vars.put("FRETE", "03");
			vars.put("SEGURO", "00");
			vars.put("OUTROS", "00");
			vars.put("DESCONTO", "00");
		}
		btnFormula = new ToolbarButton(OpenSigCore.i18n.txtValidar());
		btnFormula.setIconCls("icon-analisar");
		btnFormula.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (getForm().isValid() && gridArredonda.validar(new ArrayList<ComValorArredonda>())) {
					String despesa = txtDespesa.getValueAsString().length() == 1 ? "0" + txtDespesa.getValueAsString() : txtDespesa.getValueAsString();
					String markup = txtMarkup.getValueAsString().length() == 1 ? "0" + txtMarkup.getValueAsString() : txtMarkup.getValueAsString();

					vars.put("DESPESA", despesa);
					vars.put("MARKUP", markup);

					try {
						double resposta = GerarPreco.executarFormula(txtFormula.getValueAsString(), vars);
						double arredondado = GerarPreco.arredondar(resposta, gridArredonda.getStore());
						String msg1 = OpenSigCore.i18n.txtFormula() + "=" + NumberFormat.getCurrencyFormat().format(resposta);
						String msg2 = OpenSigCore.i18n.txtArredondamento() + "=" + NumberFormat.getCurrencyFormat().format(arredondado);
						MessageBox.alert(OpenSigCore.i18n.txtFormula(), msg1 + "<br/>" + msg2);
					} catch (Exception ex) {
						MessageBox.alert(OpenSigCore.i18n.txtFormula(), OpenSigCore.i18n.errInvalido());
					}
				}
			}
		});

		lblVariaveis = new Label(OpenSigCore.i18n.txtVariaveis() + ": ");
		lblVariaveis.setStyle("font-weight: bold;");
		add(lblVariaveis);

		variaveis = new Label("BRUTO - IPI - ICMS - FRETE - SEGURO - OUTROS - DESCONTO - DESPESA - MARKUP");
		variaveis.setStyle("font-style: italic; font-size: 12px;");
		add(variaveis);
		add(new HTML("<br />"));

		lblValidar = new Label(OpenSigCore.i18n.txtExemplo() + ": ");
		lblValidar.setStyle("font-weight: bold;");
		add(lblValidar);

		teste = new Label("BRUTO = R$ 10,00 - IPI = 5% - ICMS = 7% - FRETE = 3% - SEGURO = 0% - OUTROS = 0% - DESCONTO = 0%");
		teste.setStyle("font-style: italic; font-size: 12px;");
		add(teste);
		add(new HTML("<br />"));

		gridArredonda = new ListagemValorArredonda(true);
		add(gridArredonda);

		addListener(new FormPanelListenerAdapter() {
			public void onRender(Component component) {
				super.onRender(component);
				getTopToolbar().addSeparator();
				getTopToolbar().addButton(btnFormula);
			}
		});
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					ComercialProxy proxy = new ComercialProxy();
					proxy.salvarValor(classe, ASYNC);
				}
			};
		}

		return comando;
	}

	public boolean setDados() {
		boolean retorno = true;
		List<ComValorArredonda> arredondados = new ArrayList<ComValorArredonda>();

		if (!gridArredonda.validar(arredondados)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		classe.setComValorArredondas(arredondados);
		classe.setComValorProdutoId(Integer.valueOf(hdnCod.getValueAsString()));
		if (txtDespesa.getValue() != null) {
			classe.setComValorProdutoDespesa(txtDespesa.getValue().intValue());
		}
		if (txtMarkup.getValue() != null) {
			classe.setComValorProdutoMarkup(txtMarkup.getValue().intValue());
		}
		classe.setComValorProdutoFormula(txtFormula.getValueAsString());

		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}

		if (!hdnFornecedor.getValueAsString().equals("0")) {
			EmpFornecedor fornecedor = new EmpFornecedor(Integer.valueOf(hdnFornecedor.getValueAsString()));
			classe.setEmpFornecedor(fornecedor);
		} else {
			classe.setEmpFornecedor(null);
		}

		if (!hdnProduto.getValueAsString().equals("0")) {
			ProdProduto produto = new ProdProduto(Integer.valueOf(hdnProduto.getValueAsString()));
			classe.setProdProduto(produto);
		} else {
			classe.setProdProduto(null);
		}
		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		FiltroNumero fn = new FiltroNumero("comValorProdutoId", ECompara.IGUAL, 0);
		gridArredonda.getProxy().setFiltroPadrao(fn);
		gridArredonda.getStore().removeAll();
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
			classe.setComValorProdutoId(Integer.valueOf(hdnCod.getValueAsString()));
			FiltroObjeto fo = new FiltroObjeto("comValorProduto", ECompara.IGUAL, classe);
			gridArredonda.getProxy().setFiltroPadrao(fo);
			gridArredonda.getStore().reload();
		}
		txtDespesa.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			duplicar = false;
		}
	}

	private ComboBox getFornecedor() {
		cmbFornecedor = UtilClient.getComboEntidade(new ComboEntidade(new EmpFornecedor()));
		cmbFornecedor.setName("empFornecedor.empEntidade.empEntidadeNome1");
		cmbFornecedor.setLabel(OpenSigCore.i18n.txtFornecedor());
		cmbFornecedor.setAllowBlank(true);
		cmbFornecedor.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnFornecedor.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbFornecedor.getRawValue().equals("")) {
					hdnFornecedor.setValue("0");
				}
			}
		});

		return cmbFornecedor;
	}

	private ComboBox getProduto() {
		FieldDef[] fdProduto = new FieldDef[] { new IntegerFieldDef("prodProdutoId"), new StringFieldDef("prodProdutoNcm"), new StringFieldDef("prodProdutoBarra"),
				new StringFieldDef("prodProdutoDescricao") };

		FiltroBinario fb = new FiltroBinario("prodProdutoAtivo", ECompara.IGUAL, 1);
		CoreProxy<ProdProduto> proxy = new CoreProxy<ProdProduto>(new ProdProduto(), fb);
		Store storeProduto = new Store(proxy, new ArrayReader(new RecordDef(fdProduto)), true);

		cmbProduto = new ComboBox(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 200);
		cmbProduto.setMinChars(3);
		cmbProduto.setSelectOnFocus(true);
		cmbProduto.setListWidth(300);
		cmbProduto.setStore(storeProduto);
		cmbProduto.setTriggerAction(ComboBox.QUERY);
		cmbProduto.setMode(ComboBox.REMOTE);
		cmbProduto.setDisplayField("prodProdutoDescricao");
		cmbProduto.setValueField("prodProdutoId");
		cmbProduto.setForceSelection(true);
		cmbProduto.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnProduto.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbProduto.getRawValue().equals("")) {
					hdnProduto.setValue("0");
				}
			}
		});

		return cmbProduto;
	}

	public void gerarListas() {
		// produtos
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridArredonda.getModelos().getColumnCount(); i++) {
			if (gridArredonda.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridArredonda.getModelos().getColumnHeader(i), gridArredonda.getModelos().getColumnWidth(i), null);
				if (gridArredonda.getModelos().getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) gridArredonda.getModelos().getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados.add(meta);
			}
		}

		SortState ordem = gridArredonda.getStore().getSortState();
		ComValorArredonda comArr = new ComValorArredonda();
		comArr.setCampoOrdem(ordem.getField());
		comArr.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("comValorProduto", ECompara.IGUAL, new ComValorProduto(id));
		
		ExpListagem<ComValorArredonda> arredonda = new ExpListagem<ComValorArredonda>();
		arredonda.setClasse(comArr);
		arredonda.setMetadados(metadados);
		arredonda.setNome(gridArredonda.getTitle());
		arredonda.setFiltro(filtro);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(arredonda);
	}

	public Label getLblVariaveis() {
		return lblVariaveis;
	}

	public void setLblVariaveis(Label lblVariaveis) {
		this.lblVariaveis = lblVariaveis;
	}

	public Label getLblValidar() {
		return lblValidar;
	}

	public void setLblValidar(Label lblValidar) {
		this.lblValidar = lblValidar;
	}

	public Label getVariaveis() {
		return variaveis;
	}

	public void setVariaveis(Label variaveis) {
		this.variaveis = variaveis;
	}

	public Label getTeste() {
		return teste;
	}

	public void setTeste(Label teste) {
		this.teste = teste;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
	}

	public Hidden getHdnFornecedor() {
		return hdnFornecedor;
	}

	public void setHdnFornecedor(Hidden hdnFornecedor) {
		this.hdnFornecedor = hdnFornecedor;
	}

	public Hidden getHdnProduto() {
		return hdnProduto;
	}

	public void setHdnProduto(Hidden hdnProduto) {
		this.hdnProduto = hdnProduto;
	}

	public NumberField getTxtDespesa() {
		return txtDespesa;
	}

	public void setTxtDespesa(NumberField txtDespesa) {
		this.txtDespesa = txtDespesa;
	}

	public NumberField getTxtMarkup() {
		return txtMarkup;
	}

	public void setTxtMarkup(NumberField txtMarkup) {
		this.txtMarkup = txtMarkup;
	}

	public ComboBox getCmbFornecedor() {
		return cmbFornecedor;
	}

	public void setCmbFornecedor(ComboBox cmbFornecedor) {
		this.cmbFornecedor = cmbFornecedor;
	}

	public ComboBox getCmbProduto() {
		return cmbProduto;
	}

	public void setCmbProduto(ComboBox cmbProduto) {
		this.cmbProduto = cmbProduto;
	}

	public TextArea getTxtFormula() {
		return txtFormula;
	}

	public void setTxtFormula(TextArea txtFormula) {
		this.txtFormula = txtFormula;
	}

	public ListagemValorArredonda getGridArredonda() {
		return gridArredonda;
	}

	public void setGridArredonda(ListagemValorArredonda gridArredonda) {
		this.gridArredonda = gridArredonda;
	}

	public ToolbarButton getBtnFormula() {
		return btnFormula;
	}

	public void setBtnFormula(ToolbarButton btnFormula) {
		this.btnFormula = btnFormula;
	}

}
