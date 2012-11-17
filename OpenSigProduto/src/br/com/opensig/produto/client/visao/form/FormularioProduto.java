package br.com.opensig.produto.client.visao.form;

import java.util.ArrayList;
import java.util.Collection;
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
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.produto.client.servico.ProdutoProxy;
import br.com.opensig.produto.client.visao.lista.ListagemComposicao;
import br.com.opensig.produto.client.visao.lista.ListagemPreco;
import br.com.opensig.produto.shared.modelo.ProdCategoria;
import br.com.opensig.produto.shared.modelo.ProdComposicao;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdEstoque;
import br.com.opensig.produto.shared.modelo.ProdIpi;
import br.com.opensig.produto.shared.modelo.ProdOrigem;
import br.com.opensig.produto.shared.modelo.ProdPreco;
import br.com.opensig.produto.shared.modelo.ProdProduto;
import br.com.opensig.produto.shared.modelo.ProdTipo;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioProduto extends AFormulario<ProdProduto> {

	private Hidden hdnFornecedor;
	private Hidden hdnFabricante;
	private Hidden hdnCod;
	private Hidden hdnSinc;
	private TextField txtDescricao;
	private TextField txtReferencia;
	private NumberField txtCusto;
	private NumberField txtPreco;
	private NumberField txtVolume;
	private NumberField txtEstoque;
	private TextField txtNcm;
	private TextField txtBarra;
	private ComboBox cmbFornecedor;
	private ComboBox cmbFabricante;
	private ComboBox cmbTributacao;
	private ComboBox cmbIpi;
	private ComboBox cmbTipo;
	private ComboBox cmbOrigem;
	private ComboBox cmbEmbalagem;
	private Checkbox chkAtivo;
	private Checkbox chkIncentivo;
	private Arvore<ProdCategoria> treeCategoria;
	private TextArea txtObservacao;
	private Date dtCadastro;
	private TabPanel tabDados;
	private ListagemPreco gridPrecos;
	private ListagemComposicao gridComposicoes;
	private List<ProdPreco> precos;
	private List<ProdComposicao> composicoes;
	private List<ProdCategoria> categorias;

	public FormularioProduto(SisFuncao funcao) {
		super(new ProdProduto(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();
		Panel coluna1 = new Panel();
		coluna1.setBorder(false);
		coluna1.setLayout(new FormLayout());

		hdnFornecedor = new Hidden("empFornecedor.empFornecedorId", "0");
		add(hdnFornecedor);

		hdnFabricante = new Hidden("empFabricante.empFornecedorId", "0");
		add(hdnFabricante);

		hdnCod = new Hidden("prodProdutoId", "0");
		add(hdnCod);

		hdnSinc = new Hidden("prodProdutoSinc", "0");
		add(hdnSinc);

		txtNcm = new TextField(OpenSigCore.i18n.txtNcm(), "prodProdutoNcm", 80);
		txtNcm.setAllowBlank(false);
		txtNcm.setMinLength(2);
		txtNcm.setMaxLength(8);
		txtNcm.setRegex("^(\\d{2}|\\d{8})$");

		txtBarra = new TextField(OpenSigCore.i18n.txtBarra(), "prodProdutoBarra", 110);
		txtBarra.setMinLength(8);
		txtBarra.setMaxLength(14);
		txtBarra.setRegex("^(\\d{8}|\\d{12}|\\d{13}|\\d{14})$");

		txtVolume = new NumberField(OpenSigCore.i18n.txtQtdCx(), "prodProdutoVolume", 60);
		txtVolume.setAllowBlank(false);
		txtVolume.setAllowDecimals(false);
		txtVolume.setAllowNegative(false);
		txtVolume.setMaxLength(10);
		txtVolume.setValue(1);
		txtVolume.setMinValue(1);

		txtCusto = new NumberField(OpenSigCore.i18n.txtCusto(), "prodProdutoCusto", 80);
		txtCusto.setAllowBlank(false);
		txtCusto.setAllowDecimals(true);
		txtCusto.setAllowNegative(false);
		txtCusto.setDecimalPrecision(2);
		txtCusto.setMaxLength(13);
		txtCusto.setValue(0);

		txtPreco = new NumberField(OpenSigCore.i18n.txtPreco(), "prodProdutoPreco", 80);
		txtPreco.setAllowBlank(false);
		txtPreco.setAllowDecimals(true);
		txtPreco.setAllowNegative(false);
		txtPreco.setDecimalPrecision(2);
		txtPreco.setMaxLength(13);
		txtPreco.setValue(0);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNcm, 100);
		linha1.addToRow(txtBarra, 130);
		linha1.addToRow(getEmbalagem(), 80);
		linha1.addToRow(txtVolume, 80);
		linha1.addToRow(txtCusto, 100);
		linha1.addToRow(txtPreco, 100);
		coluna1.add(linha1);

		txtReferencia = new TextField(OpenSigCore.i18n.txtRef(), "prodProdutoReferencia", 80);
		txtReferencia.setRegex("^\\w*$");
		txtReferencia.setMaxLength(20);

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "prodProdutoDescricao", 320);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(100);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtReferencia, 100);
		linha2.addToRow(txtDescricao, 340);
		linha2.addToRow(getOrigem(), 150);
		coluna1.add(linha2);

		MultiFieldPanel linha3 = new MultiFieldPanel();
		linha3.setBorder(false);
		linha3.addToRow(getFornecedor(), 310);
		linha3.addToRow(getTributacao(), 140);
		linha3.addToRow(getIpi(), 140);
		coluna1.add(linha3);

		txtEstoque = new NumberField(OpenSigCore.i18n.txtEstoque(), "t1.prodEstoqueQuantidade", 60);
		txtEstoque.setAllowBlank(false);
		txtEstoque.setAllowNegative(false);
		txtEstoque.setMaxLength(10);
		txtEstoque.setValue(0);

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "prodProdutoAtivo");
		chkAtivo.setChecked(true);

		chkIncentivo = new Checkbox(OpenSigCore.i18n.txtIncentivo(), "prodProdutoIncentivo");

		MultiFieldPanel linha4 = new MultiFieldPanel();
		linha4.setBorder(false);
		linha4.addToRow(getFabricante(), 310);
		linha4.addToRow(getTipo(), 60);
		linha4.addToRow(txtEstoque, 80);
		linha4.addToRow(chkAtivo, 50);
		linha4.addToRow(chkIncentivo, 70);
		coluna1.add(linha4);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), "prodProdutoObservacao");
		txtObservacao.setMaxLength(255);
		txtObservacao.setWidth("95%");
		coluna1.add(txtObservacao);

		Panel formColuna = new Panel();
		formColuna.setBorder(false);
		formColuna.setLayout(new ColumnLayout());
		formColuna.add(coluna1, new ColumnLayoutData(.75));
		formColuna.add(getCategoria(), new ColumnLayoutData(.25));
		add(formColuna);

		tabDados = new TabPanel();
		tabDados.setPlain(true);
		tabDados.setHeight(150);
		tabDados.setActiveTab(0);

		gridPrecos = new ListagemPreco(true);
		gridComposicoes = new ListagemComposicao(true);
		tabDados.add(gridPrecos);
		tabDados.add(gridComposicoes);
		add(tabDados);

		super.inicializar();
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					ProdutoProxy proxy = new ProdutoProxy();
					proxy.salvarProduto(classe, categorias, ASYNC);
				}
			};
		}

		return comando;
	}

	public boolean setDados() {
		// valida as categorias
		categorias = new ArrayList<ProdCategoria>();
		Collection<String[]> valores = new ArrayList<String[]>();
		boolean retorno = treeCategoria.validarCategoria(valores);
		String strCategorias = "";

		for (String[] valor : valores) {
			strCategorias += valor[1] + "::";
			if (valor[0].equals("0")) {
				ProdCategoria categoria = new ProdCategoria();
				categoria.setProdCategoriaDescricao(valor[1]);
				categorias.add(categoria);
			}
		}

		// valida os precos
		precos = new ArrayList<ProdPreco>();
		if (!gridPrecos.validar(precos)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}
		
		// valida a composicao
		composicoes = new ArrayList<ProdComposicao>();
		if (!gridComposicoes.validar(composicoes)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}
		
		// valida os valores
		double parcial = 0.00;
		for (ProdComposicao comp : composicoes) {
			parcial += comp.getProdComposicaoValor();
		}

		String strTotal = UtilClient.formataNumero(txtPreco.getValue().doubleValue(), 1, 2, true);
		String strParcial = UtilClient.formataNumero(parcial, 1, 2, true);

		if (parcial > 0.00 && !strTotal.equals(strParcial)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.msgCampoInvalido(), OpenSigCore.i18n.txtParcial() + " = " + strParcial + " :: " + OpenSigCore.i18n.txtTotal() + " = " + strTotal).show();
		}
		
		classe.setProdPrecos(precos);
		classe.setProdComposicoes(composicoes);
		classe.setProdProdutoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setProdProdutoNcm(txtNcm.getValueAsString());
		classe.setProdProdutoBarra(txtBarra.getValueAsString().equals("") ? null : txtBarra.getValueAsString());
		classe.setProdProdutoReferencia(txtReferencia.getValueAsString());
		if (txtCusto.getValue() != null) {
			classe.setProdProdutoCusto(txtCusto.getValue().doubleValue());
		}
		if (txtPreco.getValue() != null) {
			classe.setProdProdutoPreco(txtPreco.getValue().doubleValue());
		}
		if (txtVolume.getValue() != null) {
			classe.setProdProdutoVolume(txtVolume.getValue().intValue());
		}
		classe.setProdProdutoDescricao(txtDescricao.getValueAsString());
		classe.setProdProdutoAtivo(chkAtivo.getValue());
		classe.setProdProdutoIncentivo(chkIncentivo.getValue());
		classe.setProdProdutoSinc(Integer.valueOf(hdnSinc.getValueAsString()));
		if (!hdnFornecedor.getValueAsString().equals("0")) {
			EmpFornecedor fornecedor = new EmpFornecedor(Integer.valueOf(hdnFornecedor.getValueAsString()));
			classe.setEmpFornecedor(fornecedor);
		} else {
			classe.setEmpFornecedor(null);
		}
		if (!hdnFabricante.getValueAsString().equals("0")) {
			EmpFornecedor fabricante = new EmpFornecedor(Integer.valueOf(hdnFabricante.getValueAsString()));
			classe.setEmpFabricante(fabricante);
		} else {
			classe.setEmpFabricante(null);
		}
		ProdTributacao tributacao = new ProdTributacao(Integer.valueOf(cmbTributacao.getValue()));
		classe.setProdTributacao(tributacao);
		ProdIpi ipi = new ProdIpi(Integer.valueOf(cmbIpi.getValue()));
		classe.setProdIpi(ipi);
		classe.setProdProdutoCategoria(strCategorias);
		if (classe.getProdProdutoId() == 0) {
			classe.setProdProdutoCadastrado(new Date());
			classe.setProdProdutoAlterado(new Date());
		} else {
			classe.setProdProdutoCadastrado(dtCadastro);
			classe.setProdProdutoAlterado(new Date());
		}
		ProdOrigem origem = new ProdOrigem(Integer.valueOf(cmbOrigem.getValue()));
		classe.setProdOrigem(origem);
		ProdEmbalagem volume = new ProdEmbalagem(Integer.valueOf(cmbEmbalagem.getValue()));
		classe.setProdEmbalagem(volume);
		ProdTipo tipo = new ProdTipo(Integer.valueOf(cmbTipo.getValue()));
		classe.setProdTipo(tipo);
		if (txtEstoque.getValue() != null) {
			ProdEstoque estoque = new ProdEstoque();
			estoque.setProdEstoqueQuantidade(txtEstoque.getValue().doubleValue());
			estoque.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));

			List<ProdEstoque> estoques = new ArrayList<ProdEstoque>();
			estoques.add(estoque);
			classe.setProdEstoques(estoques);
		}
		classe.setProdProdutoObservacao(txtObservacao.getValueAsString() == null ? "" : txtObservacao.getValueAsString());

		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		FiltroNumero fn = new FiltroNumero("prodPrecoId", ECompara.IGUAL, 0);
		gridPrecos.getProxy().setFiltroPadrao(fn);
		gridPrecos.getStore().removeAll();
		FiltroNumero fn1 = new FiltroNumero("prodComposicaoId", ECompara.IGUAL, 0);
		gridComposicoes.getProxy().setFiltroPadrao(fn1);
		gridComposicoes.getStore().removeAll();
		treeCategoria.getLblValidacao().hide();
	}

	public void mostrarDados() {
		if (cmbOrigem.getStore().getRecords().length == 0) {
			cmbOrigem.getStore().load();
		} else {
			mostrar();
		}
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
			classe.setProdProdutoId(rec.getAsInteger("prodProdutoId"));

			dtCadastro = rec.getAsDate("prodProdutoCadastrado");
			txtCusto.setValue(txtCusto.getValue().doubleValue());
			txtPreco.setValue(txtPreco.getValue().doubleValue());

			FiltroObjeto fo = new FiltroObjeto("prodProduto", ECompara.IGUAL, classe);
			gridPrecos.getProxy().setFiltroPadrao(fo);
			gridPrecos.getStore().reload();

			FiltroObjeto fo1 = new FiltroObjeto("prodProdutoPrincipal", ECompara.IGUAL, classe);
			gridComposicoes.getProxy().setFiltroPadrao(fo1);
			gridComposicoes.getStore().reload();

			String[] objs = rec.getAsString("prodProdutoCategoria").split("::");
			treeCategoria.selecionar(objs);
		} else {
			cmbOrigem.setValue("1");
			cmbTributacao.setValue("1");
			cmbIpi.setValue("7");
			cmbEmbalagem.setValue("1");
			cmbTipo.setValue("1");
			treeCategoria.limpar();
			treeCategoria.carregar(null, null);
		}
		txtNcm.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnSinc.setValue("0");
			duplicar = false;
		}
	}

	private ComboBox getFornecedor() {
		cmbFornecedor = UtilClient.getComboEntidade(new ComboEntidade(new EmpFornecedor()));
		cmbFornecedor.setWidth(280);
		cmbFornecedor.setName("empFornecedor.empEntidade.empEntidadeNome1");
		cmbFornecedor.setLabel(OpenSigCore.i18n.txtFornecedor());
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

	private ComboBox getFabricante() {
		cmbFabricante = UtilClient.getComboEntidade(new ComboEntidade(new EmpFornecedor()));
		cmbFabricante.setWidth(280);
		cmbFabricante.setName("empFabricante.empEntidade.empEntidadeNome1");
		cmbFabricante.setLabel(OpenSigCore.i18n.txtFabricante());
		cmbFabricante.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnFabricante.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbFabricante.getRawValue().equals("")) {
					hdnFabricante.setValue("0");
				}
			}
		});

		return cmbFabricante;
	}

	private ComboBox getOrigem() {
		FieldDef[] fdOrigem = new FieldDef[] { new IntegerFieldDef("prodOrigemId"), new StringFieldDef("prodOrigemDescricao") };
		CoreProxy<ProdOrigem> proxy = new CoreProxy<ProdOrigem>(new ProdOrigem());
		final Store storeOrigem = new Store(proxy, new ArrayReader(new RecordDef(fdOrigem)), true);
		storeOrigem.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				cmbEmbalagem.getStore().load();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtOrigem(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeOrigem.load();
						}
					}
				});
			}
		});

		cmbOrigem = new ComboBox(OpenSigCore.i18n.txtOrigem(), "prodOrigem.prodOrigemId", 130);
		cmbOrigem.setAllowBlank(false);
		cmbOrigem.setStore(storeOrigem);
		cmbOrigem.setTriggerAction(ComboBox.ALL);
		cmbOrigem.setMode(ComboBox.LOCAL);
		cmbOrigem.setDisplayField("prodOrigemDescricao");
		cmbOrigem.setValueField("prodOrigemId");
		cmbOrigem.setForceSelection(true);
		cmbOrigem.setListWidth(250);
		cmbOrigem.setEditable(false);

		return cmbOrigem;
	}

	private ComboBox getEmbalagem() {
		FieldDef[] fdEmbalagem = new FieldDef[] { new IntegerFieldDef("prodEmbalagemId"), new StringFieldDef("prodEmbalagemNome"), new IntegerFieldDef("prodEmbalagemUnidade"),
				new StringFieldDef("prodEmbalagemDescricao") };
		CoreProxy<ProdEmbalagem> proxy = new CoreProxy<ProdEmbalagem>(new ProdEmbalagem());
		final Store storeEmbalagem = new Store(proxy, new ArrayReader(new RecordDef(fdEmbalagem)), true);
		storeEmbalagem.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				cmbTributacao.getStore().load();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtEmbalagem(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeEmbalagem.load();
						}
					}
				});
			}
		});

		cmbEmbalagem = new ComboBox(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 60);
		cmbEmbalagem.setAllowBlank(false);
		cmbEmbalagem.setStore(storeEmbalagem);
		cmbEmbalagem.setTriggerAction(ComboBox.ALL);
		cmbEmbalagem.setMode(ComboBox.LOCAL);
		cmbEmbalagem.setDisplayField("prodEmbalagemNome");
		cmbEmbalagem.setValueField("prodEmbalagemId");
		cmbEmbalagem.setTpl("<div class=\"x-combo-list-item\"><b>{prodEmbalagemNome}</b> - <i>" + OpenSigCore.i18n.txtUnidade() + " [{prodEmbalagemUnidade}]</i></div>");
		cmbEmbalagem.setForceSelection(true);
		cmbEmbalagem.setListWidth(150);
		cmbEmbalagem.setEditable(false);

		return cmbEmbalagem;
	}

	private ComboBox getTributacao() {
		FieldDef[] fdTributacao = new FieldDef[] { new IntegerFieldDef("prodTributacaoId"), new StringFieldDef("prodTributacaoNome"), new StringFieldDef("prodTributacaoCst"),
				new StringFieldDef("prodTributacaoCson"), new IntegerFieldDef("prodTributacaoCfop") };
		CoreProxy<ProdTributacao> proxy = new CoreProxy<ProdTributacao>(new ProdTributacao());
		final Store storeTributacao = new Store(proxy, new ArrayReader(new RecordDef(fdTributacao)), false);
		storeTributacao.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				cmbIpi.getStore().load();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtTributacao(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeTributacao.load();
						}
					}
				});
			}
		});

		cmbTributacao = new ComboBox(OpenSigCore.i18n.txtTributacao(), "prodTributacao.prodTributacaoId", 110);
		cmbTributacao.setAllowBlank(false);
		cmbTributacao.setStore(storeTributacao);
		cmbTributacao.setTriggerAction(ComboBox.ALL);
		cmbTributacao.setMode(ComboBox.LOCAL);
		cmbTributacao.setDisplayField("prodTributacaoNome");
		cmbTributacao.setValueField("prodTributacaoId");
		cmbTributacao.setTpl("<div class=\"x-combo-list-item\"><b>{prodTributacaoNome}</b> - <i>" + OpenSigCore.i18n.txtCfop() + " [{prodTributacaoCfop}], " + OpenSigCore.i18n.txtCst()
				+ " [{prodTributacaoCst}]</i></div>");
		cmbTributacao.setForceSelection(true);
		cmbTributacao.setListWidth(400);
		cmbTributacao.setEditable(false);

		return cmbTributacao;
	}

	private ComboBox getIpi() {
		FieldDef[] fdIpi = new FieldDef[] { new IntegerFieldDef("prodIpiId"), new StringFieldDef("prodIpiNome"), new StringFieldDef("prodIpiCstEntrada"), new StringFieldDef("prodIpiCstSaida"),
				new IntegerFieldDef("prodIpiAliquota") };
		CoreProxy<ProdIpi> proxy = new CoreProxy<ProdIpi>(new ProdIpi());
		final Store storeIpi = new Store(proxy, new ArrayReader(new RecordDef(fdIpi)), false);
		storeIpi.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				cmbTipo.getStore().load();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtIpi(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeIpi.load();
						}
					}
				});
			}
		});

		cmbIpi = new ComboBox(OpenSigCore.i18n.txtIpi(), "prodIpi.prodIpiId", 110);
		cmbIpi.setAllowBlank(false);
		cmbIpi.setStore(storeIpi);
		cmbIpi.setTriggerAction(ComboBox.ALL);
		cmbIpi.setMode(ComboBox.LOCAL);
		cmbIpi.setDisplayField("prodIpiNome");
		cmbIpi.setValueField("prodIpiId");
		cmbIpi.setTpl("<div class=\"x-combo-list-item\"><b>{prodIpiNome}</b> - <i>" + OpenSigCore.i18n.txtAliquota() + " [{prodIpiAliquota}], " + OpenSigCore.i18n.txtCst()
				+ " [{prodIpiCstEntrada} / {prodIpiCstSaida}]</i></div>");
		cmbIpi.setForceSelection(true);
		cmbIpi.setListWidth(500);
		cmbIpi.setEditable(false);

		return cmbIpi;
	}

	private ComboBox getTipo() {
		FieldDef[] fdTipo = new FieldDef[] { new IntegerFieldDef("prodTipoId"), new StringFieldDef("prodTipoValor"), new StringFieldDef("prodTipoDescricao") };
		CoreProxy<ProdTipo> proxy = new CoreProxy<ProdTipo>(new ProdTipo());
		final Store storeTipo = new Store(proxy, new ArrayReader(new RecordDef(fdTipo)), false);
		storeTipo.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				treeCategoria.carregar(null, new AsyncCallback<Lista<ProdCategoria>>() {

					public void onSuccess(Lista<ProdCategoria> result) {
						mostrar();
					}

					public void onFailure(Throwable caught) {
						new ToastWindow(OpenSigCore.i18n.txtCategoria(), OpenSigCore.i18n.errListagem());
					}
				});
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtIpi(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeTipo.load();
						}
					}
				});
			}
		});

		cmbTipo = new ComboBox(OpenSigCore.i18n.txtTipo(), "prodTipo.prodTipoId", 30);
		cmbTipo.setAllowBlank(false);
		cmbTipo.setStore(storeTipo);
		cmbTipo.setTriggerAction(ComboBox.ALL);
		cmbTipo.setMode(ComboBox.LOCAL);
		cmbTipo.setDisplayField("prodTipoValor");
		cmbTipo.setValueField("prodTipoId");
		cmbTipo.setTpl("<div class=\"x-combo-list-item\"><b>{prodTipoValor}</b> - <i>[{prodTipoDescricao}]</i></div>");
		cmbTipo.setForceSelection(true);
		cmbTipo.setListWidth(300);
		cmbTipo.setEditable(false);

		return cmbTipo;
	}

	private Arvore getCategoria() {
		treeCategoria = new Arvore(new ProdCategoria(), OpenSigCore.i18n.txtCategoria());
		treeCategoria.setTitle(OpenSigCore.i18n.txtCategoria());
		treeCategoria.setIconeNode("icon-categoria");
		treeCategoria.setFiltrar(true);
		treeCategoria.setEditar(true);
		treeCategoria.setWidth(200);
		treeCategoria.setHeight(230);
		treeCategoria.setBodyBorder(true);
		treeCategoria.inicializar();
		treeCategoria.getTxtFiltro().setMaxLength(20);

		return treeCategoria;
	}

	public void gerarListas() {
		// precos
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridPrecos.getModelos().getColumnCount(); i++) {
			if (gridPrecos.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridPrecos.getModelos().getColumnHeader(i), gridPrecos.getModelos().getColumnWidth(i), null);
				if (gridPrecos.getModelos().getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) gridPrecos.getModelos().getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados.add(meta);
			}
		}

		// alterando campos visiveis
		metadados.set(2, metadados.get(1));
		metadados.set(1, null);

		SortState ordem = gridPrecos.getStore().getSortState();
		ProdPreco preco = new ProdPreco();
		preco.setCampoOrdem(ordem.getField());
		preco.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("prodProduto", ECompara.IGUAL, new ProdProduto(id));

		ExpListagem<ProdPreco> precos = new ExpListagem<ProdPreco>();
		precos.setClasse(preco);
		precos.setMetadados(metadados);
		precos.setNome(gridPrecos.getTitle());
		precos.setFiltro(filtro);

		// composicoes
		List<ExpMeta> metadados1 = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridComposicoes.getModelos().getColumnCount(); i++) {
			if (gridComposicoes.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridComposicoes.getModelos().getColumnHeader(i), gridComposicoes.getModelos().getColumnWidth(i), null);
				if (gridComposicoes.getModelos().getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) gridComposicoes.getModelos().getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados1.add(meta);
			}
		}

		// alterando campos visiveis
		metadados.set(2, metadados1.get(1));
		metadados.set(1, null);
		metadados.set(4, metadados1.get(3));
		metadados.set(3, null);

		SortState ordem1 = gridComposicoes.getStore().getSortState();
		ProdComposicao composicao = new ProdComposicao();
		composicao.setCampoOrdem(ordem1.getField());
		composicao.setOrdemDirecao(EDirecao.valueOf(ordem1.getDirection().getDirection()));
		// filtro
		FiltroObjeto filtro1 = new FiltroObjeto("prodProdutoPrincipal", ECompara.IGUAL, new ProdProduto(id));

		ExpListagem<ProdComposicao> composicoes = new ExpListagem<ProdComposicao>();
		composicoes.setClasse(composicao);
		composicoes.setMetadados(metadados1);
		composicoes.setNome(gridComposicoes.getTitle());
		composicoes.setFiltro(filtro1);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(precos);
		expLista.add(composicoes);
	}

	public Hidden getHdnFornecedor() {
		return hdnFornecedor;
	}

	public void setHdnFornecedor(Hidden hdnFornecedor) {
		this.hdnFornecedor = hdnFornecedor;
	}

	public Hidden getHdnFabricante() {
		return hdnFabricante;
	}

	public void setHdnFabricante(Hidden hdnFabricante) {
		this.hdnFabricante = hdnFabricante;
	}

	public Hidden getHdnSinc() {
		return hdnSinc;
	}

	public void setHdnSinc(Hidden hdnSinc) {
		this.hdnSinc = hdnSinc;
	}

	public NumberField getTxtEstoque() {
		return txtEstoque;
	}

	public void setTxtEstoque(NumberField txtEstoque) {
		this.txtEstoque = txtEstoque;
	}

	public ComboBox getCmbFabricante() {
		return cmbFabricante;
	}

	public void setCmbFabricante(ComboBox cmbFabricante) {
		this.cmbFabricante = cmbFabricante;
	}

	public TextField getTxtNcm() {
		return txtNcm;
	}

	public void setTxtNcm(TextField txtNcm) {
		this.txtNcm = txtNcm;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public TextField getTxtReferencia() {
		return txtReferencia;
	}

	public void setTxtReferencia(TextField txtReferencia) {
		this.txtReferencia = txtReferencia;
	}

	public NumberField getTxtCusto() {
		return txtCusto;
	}

	public void setTxtCusto(NumberField txtCusto) {
		this.txtCusto = txtCusto;
	}

	public NumberField getTxtPreco() {
		return txtPreco;
	}

	public void setTxtPreco(NumberField txtPreco) {
		this.txtPreco = txtPreco;
	}

	public TextField getTxtBarra() {
		return txtBarra;
	}

	public void setTxtBarra(TextField txtBarra) {
		this.txtBarra = txtBarra;
	}

	public ComboBox getCmbFornecedor() {
		return cmbFornecedor;
	}

	public void setCmbFornecedor(ComboBox cmbFornecedor) {
		this.cmbFornecedor = cmbFornecedor;
	}

	public ComboBox getCmbTributacao() {
		return cmbTributacao;
	}

	public void setCmbTributacao(ComboBox cmbTributacao) {
		this.cmbTributacao = cmbTributacao;
	}

	public ComboBox getCmbIpi() {
		return cmbIpi;
	}

	public void setCmbIpi(ComboBox cmbIpi) {
		this.cmbIpi = cmbIpi;
	}

	public ComboBox getCmbTipo() {
		return cmbTipo;
	}

	public void setCmbTipo(ComboBox cmbTipo) {
		this.cmbTipo = cmbTipo;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

	public Arvore getTreeCategoria() {
		return treeCategoria;
	}

	public Date getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public ListagemPreco getGridPrecos() {
		return gridPrecos;
	}

	public void setGridPrecos(ListagemPreco gridPrecos) {
		this.gridPrecos = gridPrecos;
	}

	public List<ProdPreco> getPrecos() {
		return precos;
	}

	public void setPrecos(List<ProdPreco> precos) {
		this.precos = precos;
	}

	public ListagemComposicao getGridComposicoes() {
		return gridComposicoes;
	}

	public void setGridComposicoes(ListagemComposicao gridComposicoes) {
		this.gridComposicoes = gridComposicoes;
	}

	public List<ProdComposicao> getComposicoes() {
		return composicoes;
	}

	public void setComposicoes(List<ProdComposicao> composicoes) {
		this.composicoes = composicoes;
	}

	public NumberField getTxtVolume() {
		return txtVolume;
	}

	public void setTxtVolume(NumberField txtVolume) {
		this.txtVolume = txtVolume;
	}

	public ComboBox getCmbOrigem() {
		return cmbOrigem;
	}

	public void setCmbOrigem(ComboBox cmbOrigem) {
		this.cmbOrigem = cmbOrigem;
	}

	public ComboBox getCmbEmbalagem() {
		return cmbEmbalagem;
	}

	public void setCmbEmbalagem(ComboBox cmbEmbalagem) {
		this.cmbEmbalagem = cmbEmbalagem;
	}

	public Checkbox getChkIncentivo() {
		return chkIncentivo;
	}

	public void setChkIncentivo(Checkbox chkIncentivo) {
		this.chkIncentivo = chkIncentivo;
	}

	public List<ProdCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<ProdCategoria> categorias) {
		this.categorias = categorias;
	}

	public void setTreeCategoria(Arvore<ProdCategoria> treeCategoria) {
		this.treeCategoria = treeCategoria;
	}

	public TextArea getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(TextArea txtObservacao) {
		this.txtObservacao = txtObservacao;
	}
}
