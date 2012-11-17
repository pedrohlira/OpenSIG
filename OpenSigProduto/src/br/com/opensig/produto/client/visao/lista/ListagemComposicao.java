package br.com.opensig.produto.client.visao.lista;

import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdComposicao;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;

public class ListagemComposicao extends AListagemEditor<ProdComposicao> {

	private Store storeEmbalagem;
	private ComboBox cmbProduto;
	private NumberField txtQtd;
	private NumberField txtValor;
	private RecordDef camposTabela;
	private boolean mudouProduto;

	public ListagemComposicao(boolean barraTarefa) {
		super(new ProdComposicao(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodComposicaoId"), new IntegerFieldDef("prodProduto.prodProdutoId"), new StringFieldDef("prodProduto.prodProdutoDescricao"),
				new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"), new FloatFieldDef("prodComposicaoQuantidade"),
				new FloatFieldDef("prodComposicaoValor") };
		campos = new RecordDef(fd);

		// editores
		FieldDef[] fdEmbalagem = new FieldDef[] { new IntegerFieldDef("prodEmbalagemId"), new StringFieldDef("prodEmbalagemNome"), new IntegerFieldDef("prodEmbalagemUnidade"),
				new StringFieldDef("prodEmbalagemDescricao") };
		CoreProxy<ProdEmbalagem> proxy1 = new CoreProxy<ProdEmbalagem>(new ProdEmbalagem());
		storeEmbalagem = new Store(proxy1, new ArrayReader(new RecordDef(fdEmbalagem)), true);
		storeEmbalagem.load();

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "prodComposicaoId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccProdutoId = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoId", 400, false, new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (mudouProduto) {
					record.set("prodProduto.prodProdutoId", cmbProduto.getValue());
					record.set("prodProduto.prodProdutoDescricao", cmbProduto.getRawValue());
					mudouProduto = false;
				}
				return value == null ? "" : record.getAsString("prodProduto.prodProdutoDescricao");
			}
		});
		ccProdutoId.setEditor(new GridEditor(getProduto()));

		ColumnConfig ccProduto = new ColumnConfig("", "prodProduto.prodProdutoDescricao", 10, false);
		ccProduto.setHidden(true);
		ccProduto.setFixed(true);

		ColumnConfig ccEmbalagemId = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 100, false, new Renderer() {
			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (value != null) {
					Record reg = UtilClient.getRegistro(storeEmbalagem, "prodEmbalagemId", value.toString());
					return reg.getAsString("prodEmbalagemNome");
				} else {
					return "";
				}
			}
		});
		ccEmbalagemId.setEditor(new GridEditor(getEmbalagem()));

		ColumnConfig ccEmbalagem = new ColumnConfig("", "prodEmbalagem.prodEmbalagemNome", 10, false);
		ccEmbalagem.setHidden(true);
		ccEmbalagem.setFixed(true);

		txtQtd = new NumberField();
		txtQtd.setAllowBlank(false);
		txtQtd.setAllowDecimals(true);
		txtQtd.setAllowNegative(false);
		txtQtd.setDecimalPrecision(4);
		txtQtd.setMaxLength(15);
		txtQtd.setSelectOnFocus(true);
		ColumnConfig ccQtd = new ColumnConfig(OpenSigCore.i18n.txtQtd(), "prodComposicaoQuantidade", 100, false, IListagem.VALOR);
		ccQtd.setEditor(new GridEditor(txtQtd));

		txtValor = new NumberField();
		txtValor.setAllowBlank(false);
		txtValor.setAllowDecimals(true);
		txtValor.setAllowNegative(false);
		txtValor.setDecimalPrecision(2);
		txtValor.setMaxLength(13);
		txtValor.setSelectOnFocus(true);
		ColumnConfig ccPreco = new ColumnConfig(OpenSigCore.i18n.txtPreco(), "prodComposicaoValor", 100, false, IListagem.DINHEIRO);
		ccPreco.setEditor(new GridEditor(txtValor));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccProdutoId, ccProduto, ccEmbalagemId, ccEmbalagem, ccQtd, ccPreco };
		modelos = new ColumnModel(bcc);

		// barra de menu
		filtroPadrao = new FiltroNumero("prodComposicaoId", ECompara.IGUAL, 0);

		// configurações padrão e carrega dados
		setTitle(OpenSigCore.i18n.txtItens(), "icon-padrao");
		super.inicializar();
	}

	public boolean validar(List<ProdComposicao> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				int produtoId = rec.getAsInteger("prodProduto.prodProdutoId");
				int embalagemId = rec.getAsInteger("prodEmbalagem.prodEmbalagemId");
				double qtd = rec.getAsDouble("prodComposicaoQuantidade");
				double valor = rec.getAsDouble("prodComposicaoValor");

				if (produtoId < 1 || embalagemId < 1 || qtd < 0.0001 || valor < 0.01) {
					throw new Exception();
				}

				ProdComposicao composicao = new ProdComposicao();
				composicao.setProdProduto(new ProdProduto(produtoId));
				composicao.setProdEmbalagem(new ProdEmbalagem(embalagemId));
				composicao.setProdComposicaoQuantidade(qtd);
				composicao.setProdComposicaoValor(valor);
				lista.add(composicao);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida;
	}

	private ComboBox getProduto() {
		FieldDef[] fdProduto = new FieldDef[] { new IntegerFieldDef("prodProdutoId"), new StringFieldDef("prodProdutoNcm"), new StringFieldDef("prodProdutoBarra"),
				new StringFieldDef("prodProdutoDescricao") };
		final CoreProxy<ProdProduto> proxyProduto = new CoreProxy<ProdProduto>(new ProdProduto());
		Store storeProduto = new Store(proxyProduto, new ArrayReader(new RecordDef(fdProduto)), false);

		cmbProduto = new ComboBox(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoId", 60);
		cmbProduto.setMinChars(1);
		cmbProduto.setWidth(250);
		cmbProduto.setAllowBlank(false);
		cmbProduto.setStore(storeProduto);
		cmbProduto.setTriggerAction(ComboBox.ALL);
		cmbProduto.setMode(ComboBox.REMOTE);
		cmbProduto.setDisplayField("prodProdutoDescricao");
		cmbProduto.setValueField("prodProdutoId");
		cmbProduto.setLoadingText(OpenSigCore.i18n.txtAguarde());
		cmbProduto.setForceSelection(true);
		cmbProduto.setHideTrigger(true);
		cmbProduto.setPageSize(20);
		cmbProduto.setSelectOnFocus(true);
		cmbProduto.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				mudouProduto = true;
			}
		});

		return cmbProduto;
	}

	private ComboBox getEmbalagem() {
		ComboBox cmbEmbalagem = new ComboBox(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 60);
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

	public ComboBox getCmbProduto() {
		return cmbProduto;
	}

	public void setCmbProduto(ComboBox cmbProduto) {
		this.cmbProduto = cmbProduto;
	}

	public Store getStoreEmbalagem() {
		return storeEmbalagem;
	}

	public void setStoreEmbalagem(Store storeEmbalagem) {
		this.storeEmbalagem = storeEmbalagem;
	}

	public NumberField getTxtQtd() {
		return txtQtd;
	}

	public void setTxtQtd(NumberField txtQtd) {
		this.txtQtd = txtQtd;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public RecordDef getCamposTabela() {
		return camposTabela;
	}

	public void setCamposTabela(RecordDef camposTabela) {
		this.camposTabela = camposTabela;
	}

	public boolean isMudouProduto() {
		return mudouProduto;
	}

	public void setMudouProduto(boolean mudouProduto) {
		this.mudouProduto = mudouProduto;
	}

}
