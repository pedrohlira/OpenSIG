package br.com.opensig.financeiro.client.visao.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoAdicionar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoRemover;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.financeiro.client.visao.lista.ListagemFinanciados;
import br.com.opensig.financeiro.shared.modelo.FinCategoria;
import br.com.opensig.financeiro.shared.modelo.FinConta;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
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
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public abstract class AFormularioFinanceiro<E extends Dados, T extends Dados> extends AFormulario<E> {

	protected T financeiro;
	protected Hidden hdnCod;
	protected Hidden hdnEmpresa;
	protected Hidden hdnEntidade;
	protected NumberField txtValor;
	protected NumberField txtNfe;
	protected DateField dtCadastro;
	protected ComboBox cmbEntidade;
	protected ComboBox cmbConta;
	protected TextArea txtObservacao;
	protected Arvore<FinCategoria> treeCategoria;
	protected List<FinCategoria> categorias;
	protected String strCategorias;
	protected ListagemFinanciados<T> gridFormas;
	protected List<T> formas;
	protected ComboBox cmbForma;
	protected NumberField txtParcelas;
	protected Map<String, String> nomes;

	public AFormularioFinanceiro(E classe, T financeiro, SisFuncao funcao) {
		super(classe, funcao);
		nomes = new HashMap();
		this.financeiro = financeiro;
	}

	public void inicializar() {
		super.inicializar();

		Panel coluna1 = new Panel();
		coluna1.setBorder(false);
		coluna1.setLayout(new FormLayout());

		hdnCod = new Hidden(nomes.get("id"), "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);
		hdnEntidade = new Hidden("empEntidade.empEntidadeId", "0");
		add(hdnEntidade);

		txtNfe = new NumberField(OpenSigCore.i18n.txtNota(), nomes.get("nota"), 100);
		txtNfe.setAllowNegative(false);
		txtNfe.setAllowDecimals(false);
		txtNfe.setMaxLength(10);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getEntidade(), 350);
		linha1.addToRow(txtNfe, 110);
		coluna1.add(linha1);

		txtValor = new NumberField(OpenSigCore.i18n.txtValor(), nomes.get("valor"), 100);
		txtValor.setAllowBlank(false);
		txtValor.setAllowNegative(false);
		txtValor.setMaxLength(13);

		dtCadastro = new DateField(OpenSigCore.i18n.txtCadastro(), nomes.get("cadastro"), 100);
		dtCadastro.setAllowBlank(false);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(getConta(), 220);
		linha2.addToRow(txtValor, 120);
		linha2.addToRow(dtCadastro, 120);
		coluna1.add(linha2);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), nomes.get("observacao"));
		txtObservacao.setWidth("95%");
		txtObservacao.setMaxLength(255);
		coluna1.add(txtObservacao);

		Panel formColuna = new Panel();
		formColuna.setBorder(false);
		formColuna.setLayout(new ColumnLayout());
		formColuna.add(coluna1, new ColumnLayoutData(.7));
		formColuna.add(getCategoria(), new ColumnLayoutData(.3));
		add(formColuna);

		add(new HTML("<br/>"));

		gridFormas = new ListagemFinanciados<T>(financeiro, true) {
			public IComando AntesDoComando(IComando comando) {
				if (comando instanceof ComandoAdicionar) {
					IComando cmdAdicionar = new AComando() {
						public void execute(Map contexto) {
							super.execute(contexto);

							if (getForm().isValid()) {
								gridFormas.stopEditing();
								int parcelas = txtParcelas.getValue() != null ? txtParcelas.getValue().intValue() : 1;
								String forma = cmbForma.getValue();
								String valorAux = UtilClient.formataNumero(txtValor.getValue().doubleValue() / parcelas, 1, 2, false);
								valorAux = valorAux.replace(",", ".");

								for (int par = 1; par <= parcelas; par++) {
									String parcela = par < 10 ? "0" + par : "" + par;
									parcela += parcelas < 10 ? "/0" + parcelas : "/" + parcelas;

									if (par == parcelas) {
										double valor = Double.valueOf(valorAux);
										double resto = txtValor.getValue().doubleValue() - valor * parcelas;
										valorAux = UtilClient.formataNumero(valor + resto, 1, 2, false);
										valorAux = valorAux.replace(",", ".");
									}

									Record rec = gridFormas.getCampos().createRecord(new Object[gridFormas.getCampos().getFields().length]);
									rec.set("id", 0);
									rec.set("finFormaId", forma);
									rec.set("documento", txtNfe.getValueAsString());
									rec.set("valor", valorAux);
									rec.set("parcela", parcela);
									rec.set("status", OpenSigCore.i18n.txtAberto().toUpperCase());
									gridFormas.getStore().add(rec);
								}
								gridFormas.startEditing(0, 4);
							}
						}
					};
					return cmdAdicionar;
				} else if (comando instanceof ComandoRemover) {
					IComando cmdRemover = new AComando() {
						public void execute(Map contexto) {
							Record reg = gridFormas.getSelectionModel().getSelected();
							if (reg != null && reg.getAsString("status").equalsIgnoreCase(OpenSigCore.i18n.txtAberto())) {
								gridFormas.getStore().remove(reg);
							} else {
								MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
							}
						}
					};
					return cmdRemover;
				} else {
					return comando;
				}
			};
		};

		gridFormas.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (records.length > 0 && hdnCod.getValueAsString().equals("0")) {
					for (Record rec : records) {
						rec.set("status", OpenSigCore.i18n.txtAberto().toUpperCase());
						rec.set("realizado", (Date) null);
						rec.set("conciliado", (Date) null);
						rec.set("observacao", "");
					}
				}
			}
		});

		txtParcelas = new NumberField("", "parcelas", 50);
		txtParcelas.setAllowNegative(false);
		txtParcelas.setAllowDecimals(false);
		txtParcelas.setMaxLength(2);

		cmbForma = new ComboBox("", "tipo", 150);
		cmbForma.setListWidth(150);
		cmbForma.setStore(gridFormas.getStoreForma());
		cmbForma.setTriggerAction(ComboBox.ALL);
		cmbForma.setMode(ComboBox.LOCAL);
		cmbForma.setDisplayField("finFormaDescricao");
		cmbForma.setValueField("finFormaId");
		cmbForma.setForceSelection(true);
		cmbForma.setEditable(false);

		gridFormas.getTopToolbar().addText(OpenSigCore.i18n.txtParcela());
		gridFormas.getTopToolbar().addField(txtParcelas);
		gridFormas.getTopToolbar().addSpacer();
		gridFormas.getTopToolbar().addText(OpenSigCore.i18n.txtTipo());
		gridFormas.getTopToolbar().addField(cmbForma);

		add(gridFormas);
	}

	/*
	 * @see br.com.sig.core.client.visao.lista.IFormulario#setDados()
	 */
	public boolean setDados() {
		// valida as categorias
		categorias = new ArrayList<FinCategoria>();
		strCategorias = "";
		Collection<String[]> valores = new ArrayList<String[]>();
		boolean retorno = treeCategoria.validarCategoria(valores);

		for (String[] valor : valores) {
			strCategorias += valor[1] + "::";
			if (valor[0].equals("0")) {
				FinCategoria categoria = new FinCategoria();
				categoria.setFinCategoriaDescricao(valor[1]);
				categorias.add(categoria);
			}
		}

		// seta a observacao dos financiados igual a do financeiro
		for (Record rec : gridFormas.getStore().getRecords()) {
			rec.set("observacao", txtObservacao.getValueAsString());
		}

		// valida as parcelas
		formas = new ArrayList<T>();
		if (!gridFormas.validar(formas)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		// valida os valores
		double partes = 0.00;
		for (Record rec : gridFormas.getStore().getRecords()) {
			partes += rec.getAsDouble("valor");
		}

		String strTotal = UtilClient.formataNumero(txtValor.getValue().doubleValue(), 1, 2, true);
		String strParcial = UtilClient.formataNumero(partes, 1, 2, true);

		if (!strTotal.equals(strParcial)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.msgCampoInvalido(), OpenSigCore.i18n.txtParcial() + " = " + strParcial + " :: " + OpenSigCore.i18n.txtTotal() + " = " + strTotal).show();
		}

		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		txtParcelas.setValue("");
		dtCadastro.setRawValue(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(new Date()));
		FiltroNumero fn = new FiltroNumero(classe.getCampoId(), ECompara.IGUAL, 0);
		gridFormas.getProxy().setFiltroPadrao(fn);
		gridFormas.getStore().removeAll();
		treeCategoria.getLblValidacao().hide();
	}

	public void gerarListas() {
		// produtos
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridFormas.getModelos().getColumnCount(); i++) {
			if (gridFormas.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridFormas.getModelos().getColumnHeader(i), gridFormas.getModelos().getColumnWidth(i), null);
				if (gridFormas.getModelos().getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) gridFormas.getModelos().getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados.add(meta);
			}
		}

		// trocando campos visiveis
		metadados.set(5, metadados.get(4));
		metadados.set(4, null);

		SortState ordem = gridFormas.getStore().getSortState();
		financeiro.setCampoOrdem(ordem.getField());
		financeiro.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		int id = UtilClient.getSelecionado(lista.getPanel());
		classe.setId(id);
		FiltroObjeto filtro = new FiltroObjeto(nomes.get("classe"), ECompara.IGUAL, classe);

		ExpListagem<T> financiados = new ExpListagem<T>();
		financiados.setClasse(financeiro);
		financiados.setMetadados(metadados);
		financiados.setNome(gridFormas.getTitle());
		financiados.setFiltro(filtro);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(financiados);
	}

	protected ComboBox getEntidade() {
		FiltroTexto ft1 = new FiltroTexto("empEntidadeNome1", ECompara.CONTEM, null);
		FiltroTexto ft2 = new FiltroTexto("empEntidadeNome2", ECompara.CONTEM, null);
		GrupoFiltro gf = new GrupoFiltro(EJuncao.OU, new IFiltro[] { ft1, ft2 });

		FiltroBinario fb = new FiltroBinario("empEntidadeAtivo", ECompara.IGUAL, 1);
		GrupoFiltro filtros = new GrupoFiltro(EJuncao.E, new IFiltro[] { fb, gf });

		ComboEntidade persiste = new ComboEntidade<Dados>(new EmpEntidade());
		persiste.setFiltroPadrao(filtros);
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("empEntidadeId"), new StringFieldDef("empEntidadeNome1"), new StringFieldDef("empEntidadeNome2"),
				new StringFieldDef("empEntidadePessoa"), new StringFieldDef("empEntidadeDocumento1") };
		Store dados = new Store(persiste, new ArrayReader(new RecordDef(campos)), true);

		cmbEntidade = new ComboBox(OpenSigCore.i18n.txtEntidade(), "empEntidade.empEntidadeNome1", 330);
		cmbEntidade.setMinChars(1);
		cmbEntidade.setSelectOnFocus(true);
		cmbEntidade.setListWidth(500);
		cmbEntidade.setAllowBlank(false);
		cmbEntidade.setStore(dados);
		cmbEntidade.setTriggerAction(ComboBox.QUERY);
		cmbEntidade.setMode(ComboBox.REMOTE);
		cmbEntidade.setDisplayField("empEntidadeNome1");
		cmbEntidade.setTpl("<div class=\"x-combo-list-item\"><b>{empEntidadeNome1}</b> - <i>{empEntidadeNome2} [{empEntidadeDocumento1}] </i></div>");
		cmbEntidade.setValueField(persiste.getClasse().getCampoId());
		cmbEntidade.setForceSelection(true);
		cmbEntidade.addListener(new ComboBoxListenerAdapter() {

			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnEntidade.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (hdnEntidade.getRawValue().equals("")) {
					hdnEntidade.setValue("0");
				}
			}
		});

		return cmbEntidade;
	}

	protected ComboBox getConta() {
		FieldDef[] fdConta = new FieldDef[] { new IntegerFieldDef("finContaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("finBanco.finBancoId"), new StringFieldDef("finBanco.finBancoDescricao"), new StringFieldDef("finContaNome") };
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));

		CoreProxy<FinConta> proxy = new CoreProxy<FinConta>(new FinConta(), fo);
		final Store storeConta = new Store(proxy, new ArrayReader(new RecordDef(fdConta)), true);
		storeConta.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				treeCategoria.limpar();
				treeCategoria.carregar(null, new AsyncCallback<Lista<FinCategoria>>() {

					public void onSuccess(Lista<FinCategoria> result) {
						mostrar();
					}

					public void onFailure(Throwable caught) {
						new ToastWindow(OpenSigCore.i18n.txtCategoria(), OpenSigCore.i18n.errListagem());
					}
				});
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtEmbalagem(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeConta.load();
						}
					}
				});
			}
		});

		cmbConta = new ComboBox(OpenSigCore.i18n.txtConta(), "finConta.finContaId", 200);
		cmbConta.setAllowBlank(false);
		cmbConta.setStore(storeConta);
		cmbConta.setListWidth(200);
		cmbConta.setTriggerAction(ComboBox.ALL);
		cmbConta.setMode(ComboBox.LOCAL);
		cmbConta.setDisplayField("finContaNome");
		cmbConta.setValueField("finContaId");
		cmbConta.setForceSelection(true);
		cmbConta.setEditable(false);

		return cmbConta;
	}

	protected Arvore getCategoria() {
		treeCategoria = new Arvore(new FinCategoria(), OpenSigCore.i18n.txtCategoria());
		treeCategoria.setIconeNode("icon-categoria");
		treeCategoria.setFiltrar(true);
		treeCategoria.setEditar(true);
		treeCategoria.setWidth(200);
		treeCategoria.setHeight(200);
		treeCategoria.setBodyBorder(true);
		treeCategoria.inicializar();
		treeCategoria.getTxtFiltro().setMaxLength(20);

		return treeCategoria;
	}

	public void mostrarDados() {
		cmbConta.getStore().reload();
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = null;
		if (lista != null) {
			rec = lista.getPanel().getSelectionModel().getSelected();
		}

		if (rec != null) {
			getForm().loadRecord(rec);
			classe.setId(Integer.valueOf(hdnCod.getValueAsString()));
			FiltroObjeto fo = new FiltroObjeto(nomes.get("classe"), ECompara.IGUAL, classe);
			gridFormas.getProxy().setFiltroPadrao(fo);
			gridFormas.getStore().reload();
			String[] objs = rec.getAsString(nomes.get("categoria")).split("::");
			treeCategoria.selecionar(objs);
		} else {
			treeCategoria.selecionar(null);
			if (cmbConta.getStore().getRecords().length == 1) {
				cmbConta.setValue(cmbConta.getStore().getRecordAt(0).getAsString("finContaId"));
			}
		}
		cmbEntidade.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			duplicar = false;
		}
	}

	public T getFinanceiro() {
		return financeiro;
	}

	public void setFinanceiro(T financeiro) {
		this.financeiro = financeiro;
	}

	public NumberField getTxtParcelas() {
		return txtParcelas;
	}

	public void setTxtParcelas(NumberField txtParcelas) {
		this.txtParcelas = txtParcelas;
	}

	public Arvore<FinCategoria> getTreeCategoria() {
		return treeCategoria;
	}

	public void setTreeCategoria(Arvore<FinCategoria> treeCategoria) {
		this.treeCategoria = treeCategoria;
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

	public Hidden getHdnEntidade() {
		return hdnEntidade;
	}

	public void setHdnEntidade(Hidden hdnEntidade) {
		this.hdnEntidade = hdnEntidade;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public NumberField getTxtNfe() {
		return txtNfe;
	}

	public void setTxtNfe(NumberField txtNfe) {
		this.txtNfe = txtNfe;
	}

	public DateField getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(DateField dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public ComboBox getCmbEntidade() {
		return cmbEntidade;
	}

	public void setCmbEntidade(ComboBox cmbEntidade) {
		this.cmbEntidade = cmbEntidade;
	}

	public ComboBox getCmbConta() {
		return cmbConta;
	}

	public void setCmbConta(ComboBox cmbConta) {
		this.cmbConta = cmbConta;
	}

	public TextArea getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(TextArea txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public List<FinCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<FinCategoria> categorias) {
		this.categorias = categorias;
	}

	public String getStrCategorias() {
		return strCategorias;
	}

	public void setStrCategorias(String strCategorias) {
		this.strCategorias = strCategorias;
	}

	public ListagemFinanciados<T> getGridFormas() {
		return gridFormas;
	}

	public void setGridFormas(ListagemFinanciados<T> gridFormas) {
		this.gridFormas = gridFormas;
	}

	public List<T> getFormas() {
		return formas;
	}

	public void setFormas(List<T> formas) {
		this.formas = formas;
	}

	public Map<String, String> getNomes() {
		return nomes;
	}

	public void setNomes(Map<String, String> nomes) {
		this.nomes = nomes;
	}

}
