package br.com.opensig.comercial.client.visao.form;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.client.visao.lista.ListagemEcfVendaProdutos;
import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.comercial.shared.modelo.ComEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.comando.lista.ComandoAdicionar;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.visao.PermitirSistema;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.EBusca;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpCliente;
import br.com.opensig.financeiro.shared.modelo.FinReceber;
import br.com.opensig.permissao.shared.modelo.SisUsuario;
import br.com.opensig.produto.client.controlador.comando.ComandoPesquisa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioEcfVenda extends AFormulario<ComEcfVenda> {

	private Hidden hdnCod;
	private Hidden hdnCliente;
	private Hidden hdnUsuario;
	private Hidden hdnReceber;
	private Hidden hdnEcf;
	private Hidden hdnEcfZ;
	private DateField dtData;
	private NumberField txtCcf;
	private NumberField txtCoo;
	private NumberField txtBruto;
	private NumberField txtDesc;
	private NumberField txtAcres;
	private NumberField txtLiquido;
	private Label lblRegistros;
	private ListagemEcfVendaProdutos gridProdutos;
	private List<ComEcfVendaProduto> produtos;

	private boolean autorizado;
	private boolean autosave;
	private boolean importada;
	private double max;
	private AsyncCallback asyncSalvar;
	private AsyncCallback<ILogin> asyncLogin;
	private ComandoPesquisa cmdPesquisa;

	public FormularioEcfVenda(SisFuncao funcao) {
		super(new ComEcfVenda(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comEcfVendaId", "0");
		add(hdnCod);
		hdnEcf = new Hidden("comEcf.comEcfId", "0");
		add(hdnEcf);
		hdnEcfZ = new Hidden("comEcfZ.comEcfZId", "0");
		add(hdnEcfZ);
		hdnUsuario = new Hidden("sisUsuario.sisUsuarioId", "0");
		add(hdnUsuario);
		hdnCliente = new Hidden("empCliente.empClienteId", "0");
		add(hdnCliente);
		hdnReceber = new Hidden("finReceber.finReceberId", "0");
		add(hdnReceber);

		dtData = new DateField(OpenSigCore.i18n.txtData(), "comEcfVendaData", 80);
		dtData.setVisible(false);
		dtData.setHideLabel(true);
		add(dtData);

		txtCcf = new NumberField(OpenSigCore.i18n.txtCcf(), "comEcfVendaCcf", 60);
		txtCcf.setDecimalPrecision(0);
		txtCcf.setReadOnly(true);

		txtCoo = new NumberField(OpenSigCore.i18n.txtCoo(), "comEcfVendaCoo", 60);
		txtCoo.setDecimalPrecision(0);
		txtCoo.setReadOnly(true);

		txtBruto = new NumberField(OpenSigCore.i18n.txtBruto(), "comEcfVendaBruto", 100, 0);
		txtBruto.setReadOnly(true);

		txtDesc = new NumberField(OpenSigCore.i18n.txtDesconto() + "%", "comEcfVendaDesconto", 50, 0);
		txtDesc.setReadOnly(true);

		txtAcres = new NumberField(OpenSigCore.i18n.txtAcrescimo() + "%", "comEcfVendaAcrescimo", 50, 0);
		txtAcres.setReadOnly(true);

		txtLiquido = new NumberField(OpenSigCore.i18n.txtLiquido(), "comEcfVendaLiquido", 100, 0);
		txtLiquido.setReadOnly(true);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtCcf, 80);
		linha1.addToRow(txtCoo, 80);
		linha1.addToRow(txtBruto, 110);
		linha1.addToRow(txtDesc, 60);
		linha1.addToRow(txtAcres, 60);
		linha1.addToRow(txtLiquido, 110);
		add(linha1);
		lblRegistros = new Label();

		final AsyncCallback<Record> asyncPesquisa = new AsyncCallback<Record>() {

			public void onFailure(Throwable arg0) {
				// nada
			}

			public void onSuccess(Record result) {
				if (importada) {
					Record reg = gridProdutos.getSelectionModel().getSelected();
					reg.set("prodProdutoId", result.getAsInteger("prodProdutoId"));
					reg.set("prodProduto.prodProdutoBarra", result.getAsString("prodProdutoBarra"));
					reg.set("prodProduto.prodProdutoDescricao", result.getAsString("prodProdutoDescricao"));
					reg.set("prodProduto.prodProdutoReferencia", result.getAsString("prodProdutoReferencia"));
				} else {
					gridProdutos.stopEditing();
					int pos;
					for (pos = 0; pos < gridProdutos.getStore().getCount(); pos++) {
						if (gridProdutos.getStore().getAt(pos).getAsInteger("prodProdutoId") == result.getAsInteger("prodProdutoId")) {
							break;
						}
					}

					if (pos == gridProdutos.getStore().getCount()) {
						double bruto = result.getAsDouble("prodProdutoPreco");

						Record reg = gridProdutos.getCampos().createRecord(new Object[gridProdutos.getCampos().getFields().length]);
						reg.set("comEcfVendaProdutoId", 0);
						reg.set("prodProdutoId", result.getAsInteger("prodProdutoId"));
						reg.set("prodProduto.prodProdutoBarra", result.getAsString("prodProdutoBarra"));
						reg.set("prodProduto.prodProdutoDescricao", result.getAsString("prodProdutoDescricao"));
						reg.set("prodProduto.prodProdutoReferencia", result.getAsString("prodProdutoReferencia"));
						reg.set("comEcfVendaProdutoQuantidade", 0);
						reg.set("prodEmbalagem.prodEmbalagemId", result.getAsInteger("prodEmbalagem.prodEmbalagemId"));
						reg.set("prodEmbalagem.prodEmbalagemNome", result.getAsString("prodEmbalagem.prodEmbalagemNome"));
						reg.set("comEcfVendaProdutoBruto", bruto);
						reg.set("comEcfVendaProdutoDesconto", 0);
						reg.set("comEcfVendaProdutoAcrescimo", 0);
						reg.set("comEcfVendaProdutoLiquido", bruto);
						reg.set("comEcfVendaProdutoTotal", 0);
						gridProdutos.getStore().add(reg);
					} else {
						new ToastWindow(getTitle(), OpenSigCore.i18n.errExiste()).show();
					}

					for (int col = 0; col < gridProdutos.getModelos().getColumnCount(); col++) {
						if (gridProdutos.getModelos().getDataIndex(col).equals("comEcfVendaProdutoQuantidade")) {
							gridProdutos.startEditing(pos, col);
							break;
						}
					}

					totalizar("");
				}
			}
		};
		cmdPesquisa = new ComandoPesquisa(asyncPesquisa);

		gridProdutos = new ListagemEcfVendaProdutos(true) {
			public IComando AntesDoComando(IComando comando) {
				if (comando instanceof ComandoAdicionar) {
					int registros = gridProdutos.getStore().getRecords().length;
					int minimo = UtilClient.CONF.get("listagem.autosave") != null ? Integer.valueOf(UtilClient.CONF.get("listagem.autosave")) : 0;

					if (registros > 0 && minimo > 0 && registros % minimo == 0 && getForm().isValid() && setDados()) {
						autoSave();
						return null;
					} else {
						return cmdPesquisa;
					}
				} else {
					return comando;
				}
			};
		};
		gridProdutos.getContexto().put("dados", funcao);
		gridProdutos.getTopToolbar().addText(OpenSigCore.i18n.txtRegistro() + ": ");
		gridProdutos.getTopToolbar().addElement(lblRegistros.getElement());
		gridProdutos.getTopToolbar().addSpacer();
		add(gridProdutos);

		gridProdutos.addEditorGridListener(new EditorGridListenerAdapter() {
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				totalizar("");
			}
		});

		gridProdutos.addGridRowListener(new GridRowListenerAdapter() {
			public void onRowContextMenu(GridPanel grid, int rowIndex, EventObject e) {
				cmdPesquisa.execute(gridProdutos.getContexto());
			}
		});

		gridProdutos.addEditorGridListener(new EditorGridListenerAdapter() {
			public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex, int colIndex) {
				return importada == false;
			}
		});

		gridProdutos.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onRemove(Store store, Record record, int index) {
				totalizar("");
			}

			public void onLoad(Store store, Record[] records) {
				totalizar("");
				importada = false;
				for (Record rec : records) {
					if (hdnCod.getValueAsString().equals("0")) {
						rec.set(rec.getFields()[0], 0);
					}
					importada |= rec.getAsInteger("prodProdutoId") == 0;
				}

				if (importada) {
					gridProdutos.getTopToolbar().hide();
				} else {
					gridProdutos.getTopToolbar().show();
				}
			}
		});

		ToolTip tip = new ToolTip(OpenSigCore.i18n.msgCompraProduto());
		tip.applyTo(gridProdutos);

		asyncSalvar = new AsyncCallback<ComEcfVenda>() {
			public void onFailure(Throwable caught) {
				contexto.put("erro", caught);
				ComandoSalvarFinal fim = new ComandoSalvarFinal();
				if (autosave) {
					fim.setProximo(cmdPesquisa);
				}
				fim.execute(contexto);
				autosave = false;
			};

			public void onSuccess(ComEcfVenda result) {
				contexto.put("resultado", result);
				hdnCod.setValue(result.getComEcfVendaId() + "");
				ComandoSalvarFinal fim = new ComandoSalvarFinal();
				if (autosave) {
					fim.setProximo(cmdPesquisa);
				}
				fim.execute(contexto);
				autosave = false;
			};
		};

		asyncLogin = new AsyncCallback<ILogin>() {
			public void onSuccess(ILogin result) {
				if (result != null && result.getDesconto() > max) {
					autorizado = true;
					ComercialProxy proxy = new ComercialProxy();
					proxy.salvarEcfVenda(classe, asyncSalvar);
				} else {
					autorizado = false;
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				}
			}

			public void onFailure(Throwable caught) {
				autorizado = false;
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.errSalvar());
			}
		};
	}

	private void autoSave() {
		autosave = true;
		AntesDaAcao(new ComandoSalvar());
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salavando
		if (comando instanceof ComandoSalvar) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
			max = 0.00;
			for (Record rec : gridProdutos.getStore().getRecords()) {
				max = rec.getAsDouble("comEcfVendaProdutoDesconto") > max ? rec.getAsDouble("comEcfVendaProdutoDesconto") : max;
			}

			if (max <= Ponte.getLogin().getDesconto() || autorizado) {
				ComercialProxy proxy = new ComercialProxy();
				proxy.salvarEcfVenda(classe, asyncSalvar);
			} else {
				PermitirSistema permitir = (PermitirSistema) GWT.create(PermitirSistema.class);
				permitir.setInfo(OpenSigCore.i18n.msgPermissaoDesconto(Ponte.getLogin().getDesconto() + ""));
				permitir.executar(asyncLogin);
			}
			comando = null;
		}

		return comando;
	}

	/*
	 * @see br.com.sig.core.client.visao.lista.IFormulario#setDados()
	 */
	public boolean setDados() {
		boolean retorno = true;
		produtos = new ArrayList<ComEcfVendaProduto>();

		if (!gridProdutos.validar(produtos)) {
			retorno = false;
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		classe.setComEcfVendaProdutos(produtos);
		classe.setComEcfVendaId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setComEcf(new ComEcf(Integer.valueOf(hdnEcf.getValueAsString())));
		classe.setComEcfZ(new ComEcfZ(Integer.valueOf(hdnEcfZ.getValueAsString())));
		classe.setSisUsuario(new SisUsuario(Integer.valueOf(hdnUsuario.getValueAsString())));
		classe.setComEcfVendaCcf(txtCcf.getValue().intValue());
		classe.setComEcfVendaCoo(txtCoo.getValue().intValue());
		classe.setComEcfVendaData(dtData.getValue());
		classe.setComEcfVendaBruto(txtBruto.getValue().doubleValue());
		classe.setComEcfVendaDesconto(txtDesc.getValue().doubleValue());
		classe.setComEcfVendaAcrescimo(txtAcres.getValue().doubleValue());
		classe.setComEcfVendaLiquido(txtLiquido.getValue().doubleValue());
		if (hdnCliente.getValueAsString().equals("0")) {
			classe.setEmpCliente(null);
		} else {
			classe.setEmpCliente(new EmpCliente(Integer.valueOf(hdnCliente.getValueAsString())));
		}
		if (hdnReceber.getValueAsString().equals("0")) {
			classe.setFinReceber(null);
		} else {
			classe.setFinReceber(new FinReceber(Integer.valueOf(hdnReceber.getValueAsString())));
		}
		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		FiltroNumero fn = new FiltroNumero("comEcfVendaProdutoId", ECompara.IGUAL, 0);
		gridProdutos.getProxy().setFiltroPadrao(fn);
		gridProdutos.getStore().removeAll();
		lblRegistros.setText("0");
		autorizado = false;
	}

	public void mostrarDados() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
			classe.setComEcfVendaId(Integer.valueOf(hdnCod.getValueAsString()));
			FiltroObjeto fo = new FiltroObjeto("comEcfVenda", ECompara.IGUAL, classe);
			gridProdutos.getProxy().setFiltroPadrao(fo);
			gridProdutos.getStore().reload();
		}
	}

	public void gerarListas() {
		// produtos
		List<ExpMeta> metadados = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridProdutos.getModelos().getColumnCount(); i++) {
			if (gridProdutos.getModelos().isHidden(i)) {
				metadados.add(null);
			} else {
				ExpMeta meta = new ExpMeta(gridProdutos.getModelos().getColumnHeader(i), gridProdutos.getModelos().getColumnWidth(i), null);
				if (gridProdutos.getModelos().getColumnConfigs()[i] instanceof SummaryColumnConfig) {
					SummaryColumnConfig col = (SummaryColumnConfig) gridProdutos.getModelos().getColumnConfigs()[i];
					String tp = col.getSummaryType().equals("average") ? "AVG" : col.getSummaryType().toUpperCase();
					meta.setGrupo(EBusca.getBusca(tp));
				}
				metadados.add(meta);
			}
		}

		SortState ordem = gridProdutos.getStore().getSortState();
		ComEcfVendaProduto venProd = new ComEcfVendaProduto();
		venProd.setCampoOrdem(ordem.getField());
		venProd.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		int id = UtilClient.getSelecionado(lista.getPanel());
		FiltroObjeto filtro = new FiltroObjeto("comEcfVenda", ECompara.IGUAL, new ComEcfVenda(id));

		ExpListagem<ComEcfVendaProduto> produtos = new ExpListagem<ComEcfVendaProduto>();
		produtos.setClasse(venProd);
		produtos.setMetadados(metadados);
		produtos.setNome(gridProdutos.getTitle());
		produtos.setFiltro(filtro);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(produtos);
	}

	private void totalizar(String field) {
		double bruto = 0;
		double liquido = 0;

		lblRegistros.setText(gridProdutos.getStore().getRecords().length + "");
		for (Record rec : gridProdutos.getStore().getRecords()) {
			int qtd = rec.getAsInteger("comEcfVendaProdutoQuantidade");
			bruto += qtd * rec.getAsDouble("comEcfVendaProdutoBruto");
			liquido += qtd * rec.getAsDouble("comEcfVendaProdutoLiquido");
		}

		double desc = 0;
		double acres = 0;
		if (bruto > liquido) {
			desc = 100 - (liquido / bruto * 100);
		} else if (bruto < liquido) {
			acres = (liquido / bruto * 100) - 100;
		}

		txtBruto.setValue(bruto);
		txtDesc.setValue(desc);
		txtAcres.setValue(acres);
		txtLiquido.setValue(liquido);
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnCliente() {
		return hdnCliente;
	}

	public void setHdnCliente(Hidden hdnCliente) {
		this.hdnCliente = hdnCliente;
	}

	public Hidden getHdnUsuario() {
		return hdnUsuario;
	}

	public void setHdnUsuario(Hidden hdnUsuario) {
		this.hdnUsuario = hdnUsuario;
	}

	public Label getLblRegistros() {
		return lblRegistros;
	}

	public void setLblRegistros(Label lblRegistros) {
		this.lblRegistros = lblRegistros;
	}

	public ListagemEcfVendaProdutos getGridProdutos() {
		return gridProdutos;
	}

	public void setGridProdutos(ListagemEcfVendaProdutos gridProdutos) {
		this.gridProdutos = gridProdutos;
	}

	public List<ComEcfVendaProduto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ComEcfVendaProduto> produtos) {
		this.produtos = produtos;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public Hidden getHdnEcf() {
		return hdnEcf;
	}

	public void setHdnEcf(Hidden hdnEcf) {
		this.hdnEcf = hdnEcf;
	}

	public DateField getDtData() {
		return dtData;
	}

	public void setDtData(DateField dtData) {
		this.dtData = dtData;
	}

	public NumberField getTxtCoo() {
		return txtCoo;
	}

	public void setTxtCoo(NumberField txtCoo) {
		this.txtCoo = txtCoo;
	}

	public NumberField getTxtBruto() {
		return txtBruto;
	}

	public void setTxtBruto(NumberField txtBruto) {
		this.txtBruto = txtBruto;
	}

	public NumberField getTxtDesc() {
		return txtDesc;
	}

	public void setTxtDesc(NumberField txtDesc) {
		this.txtDesc = txtDesc;
	}

	public NumberField getTxtLiquido() {
		return txtLiquido;
	}

	public void setTxtLiquido(NumberField txtLiquido) {
		this.txtLiquido = txtLiquido;
	}

	public Hidden getHdnReceber() {
		return hdnReceber;
	}

	public void setHdnReceber(Hidden hdnReceber) {
		this.hdnReceber = hdnReceber;
	}

	public Hidden getHdnEcfZ() {
		return hdnEcfZ;
	}

	public void setHdnEcfZ(Hidden hdnEcfZ) {
		this.hdnEcfZ = hdnEcfZ;
	}

	public NumberField getTxtCcf() {
		return txtCcf;
	}

	public void setTxtCcf(NumberField txtCcf) {
		this.txtCcf = txtCcf;
	}

	public NumberField getTxtAcres() {
		return txtAcres;
	}

	public void setTxtAcres(NumberField txtAcres) {
		this.txtAcres = txtAcres;
	}

}
