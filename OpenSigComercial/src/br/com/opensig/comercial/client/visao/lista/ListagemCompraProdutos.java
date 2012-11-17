package br.com.opensig.comercial.client.visao.lista;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

import com.gwtext.client.core.Ext;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemCompraProdutos extends AListagemEditor<ComCompraProduto> {

	private Store storeEmbalagem;
	private NumberField txtQuantidade;
	private NumberField txtValor;
	private NumberField txtCfop;
	private NumberField txtIcms;
	private NumberField txtIpi;
	private NumberField txtPreco;

	public ListagemCompraProdutos(boolean barraTarefa) {
		super(new ComCompraProduto(), barraTarefa);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comCompraProdutoId"), new IntegerFieldDef("comCompra.comCompraId"), new IntegerFieldDef("empEmpresaId"),
				new StringFieldDef("empEmpresa"), new StringFieldDef("empFornecedor"), new IntegerFieldDef("prodProdutoId"), new StringFieldDef("prodProduto.prodProdutoBarra"),
				new StringFieldDef("prodProduto.prodProdutoDescricao"), new StringFieldDef("prodProduto.prodProdutoReferencia"), new FloatFieldDef("prodTributacaoDentro"),
				new StringFieldDef("prodTributacaoCst"), new DateFieldDef("comCompra.comCompraRecebimento"), new FloatFieldDef("comCompraProdutoQuantidade"),
				new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"), new FloatFieldDef("comCompraProdutoValor"),
				new FloatFieldDef("comCompraProdutoTotal"), new IntegerFieldDef("comCompraProdutoCfop"), new FloatFieldDef("comCompraProdutoIcms"), new FloatFieldDef("comCompraProdutoIpi"),
				new FloatFieldDef("comCompraProdutoPreco"), new BooleanFieldDef("prodProduto.prodProdutoIncentivo"), new IntegerFieldDef("comCompraProdutoOrdem") };
		campos = new RecordDef(fd);

		FieldDef[] fdEmbalagem = new FieldDef[] { new IntegerFieldDef("prodEmbalagemId"), new StringFieldDef("prodEmbalagemNome"), new IntegerFieldDef("prodEmbalagemUnidade"),
				new StringFieldDef("prodEmbalagemDescricao") };
		CoreProxy<ProdEmbalagem> proxy = new CoreProxy<ProdEmbalagem>(new ProdEmbalagem());
		storeEmbalagem = new Store(proxy, new ArrayReader(new RecordDef(fdEmbalagem)), true);
		storeEmbalagem.load();

		// editores
		txtQuantidade = new NumberField();
		txtQuantidade.setAllowBlank(false);
		txtQuantidade.setAllowNegative(false);
		txtQuantidade.setSelectOnFocus(true);
		txtQuantidade.setMaxLength(11);

		txtValor = new NumberField();
		txtValor.setAllowBlank(false);
		txtValor.setAllowNegative(false);
		txtValor.setSelectOnFocus(true);
		txtValor.setDecimalPrecision(4);
		txtValor.setMaxLength(17);

		txtCfop = new NumberField();
		txtCfop.setAllowBlank(false);
		txtCfop.setAllowDecimals(false);
		txtCfop.setAllowNegative(false);
		txtCfop.setSelectOnFocus(true);
		txtCfop.setMinLength(4);
		txtCfop.setMaxLength(4);
		txtCfop.setMaxValue(4000);

		txtIcms = new NumberField();
		txtIcms.setAllowBlank(false);
		txtIcms.setAllowNegative(false);
		txtIcms.setSelectOnFocus(true);
		txtIcms.setDecimalPrecision(2);
		txtIcms.setMaxLength(5);

		txtIpi = new NumberField();
		txtIpi.setAllowBlank(false);
		txtIpi.setAllowNegative(false);
		txtIpi.setSelectOnFocus(true);
		txtIpi.setDecimalPrecision(2);
		txtIpi.setMaxLength(5);

		txtPreco = new NumberField();
		txtPreco.setAllowBlank(false);
		txtPreco.setAllowNegative(false);
		txtPreco.setSelectOnFocus(true);
		txtPreco.setMaxLength(11);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "comCompraProdutoId", 10, true);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccCompraId = new ColumnConfig("", "empCompraId", 10, true);
		ccCompraId.setHidden(true);
		ccCompraId.setFixed(true);

		ColumnConfig ccEmpresaId = new ColumnConfig("", "empEmpresaId", 10, true);
		ccEmpresaId.setHidden(true);
		ccEmpresaId.setFixed(true);

		ColumnConfig ccEmpresa = new ColumnConfig("", "empEmpresa", 10, true);
		ccEmpresa.setHidden(true);
		ccEmpresa.setFixed(true);

		ColumnConfig ccFornecedor = new ColumnConfig("", "empFornecedor", 10, true);
		ccFornecedor.setHidden(true);
		ccFornecedor.setFixed(true);

		ColumnConfig ccProdId = new ColumnConfig("", "prodProdutoId", 10, true);
		ccProdId.setHidden(true);
		ccProdId.setFixed(true);

		ColumnConfig ccRecebimento = new ColumnConfig("", "comCompra.comCompraRecebimento", 10, true);
		ccRecebimento.setHidden(true);
		ccRecebimento.setFixed(true);

		ColumnConfig ccTributacao = new ColumnConfig("", "prodTributacaoDentro", 10, true);
		ccTributacao.setHidden(true);
		ccTributacao.setFixed(true);

		ColumnConfig ccTributacaoCst = new ColumnConfig("", "prodTributacaoCst", 10, true);
		ccTributacaoCst.setHidden(true);
		ccTributacaoCst.setFixed(true);

		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodProduto.prodProdutoBarra", 100, true);

		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);

		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 75, true);

		ColumnConfig ccQuantidade = new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comCompraProdutoQuantidade", 50, true, IListagem.NUMERO);
		ccQuantidade.setEditor(new GridEditor(txtQuantidade));

		ColumnConfig ccEmbalagemId = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 75, true, new Renderer() {
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

		ColumnConfig ccEmbalagem = new ColumnConfig("", "prodEmbalagem.prodEmbalagemNome", 10, true);
		ccEmbalagem.setHidden(true);
		ccEmbalagem.setFixed(true);

		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "comCompraProdutoValor", 75, true, IListagem.VALOR);
		ccValor.setEditor(new GridEditor(txtValor));

		ColumnConfig ccCfop = new ColumnConfig(OpenSigCore.i18n.txtCfop(), "comCompraProdutoCfop", 50, true, IListagem.NUMERO);
		ccCfop.setEditor(new GridEditor(txtCfop));

		ColumnConfig ccIcms = new ColumnConfig(OpenSigCore.i18n.txtIcms(), "comCompraProdutoIcms", 50, true, IListagem.PORCENTAGEM);
		ccIcms.setEditor(new GridEditor(txtIcms));

		ColumnConfig ccIpi = new ColumnConfig(OpenSigCore.i18n.txtIpi(), "comCompraProdutoIpi", 50, true, IListagem.PORCENTAGEM);
		ccIpi.setEditor(new GridEditor(txtIpi));

		ColumnConfig ccPreco = new ColumnConfig(OpenSigCore.i18n.txtPreco(), "comCompraProdutoPreco", 75, true, IListagem.DINHEIRO);
		ccPreco.setEditor(new GridEditor(txtPreco));

		ColumnConfig ccIncentivo = new ColumnConfig(OpenSigCore.i18n.txtIncentivo(), "prodProduto.prodProdutoIncentivo", 75, true, IListagem.BOLEANO);

		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comCompraProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// sumarios
		ColumnConfig ccTotal = new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comCompraProdutoTotal", 75, true, IListagem.DINHEIRO);
		SummaryColumnConfig sumTotal = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccTotal, IListagem.DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccCompraId, ccEmpresaId, ccEmpresa, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccTributacao, ccTributacaoCst,
				ccRecebimento, ccQuantidade, ccEmbalagemId, ccEmbalagem, ccValor, sumTotal, ccCfop, ccIcms, ccIpi, ccPreco, ccIncentivo, ccOrdem };
		modelos = new ColumnModel(bcc);

		filtroPadrao = new FiltroNumero("comCompraProdutoId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtProduto(), "icon-produtocompra");
		setHeight(Ext.getBody().getHeight() - 420);
		super.inicializar();
	}

	public boolean validar(List<ComCompraProduto> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();
		int ordem = 1;

		for (Record rec : recs) {
			try {
				int prodId = rec.getAsInteger("prodProdutoId");
				double quantidade = rec.getAsDouble("comCompraProdutoQuantidade");
				int embalagemId = rec.getAsInteger("prodEmbalagem.prodEmbalagemId");
				double valor = rec.getAsDouble("comCompraProdutoValor");
				double total = rec.getAsDouble("comCompraProdutoTotal");
				int cfop = rec.getAsInteger("comCompraProdutoCfop");
				double icms = rec.getAsDouble("comCompraProdutoIcms");
				double ipi = rec.getAsDouble("comCompraProdutoIpi");
				double preco = rec.getAsDouble("comCompraProdutoPreco");
				double dentro = rec.getAsDouble("prodTributacaoDentro");
				String cst = rec.getAsString("prodTributacaoCst");
				String desc = rec.getAsString("prodProduto.prodProdutoDescricao");
				boolean incentivo = rec.getAsBoolean("prodProduto.prodProdutoIncentivo");

				if (quantidade < 0.01 || valor < 0.01 || total < 0.01) {
					throw new Exception();
				}

				ProdTributacao trib = new ProdTributacao();
				trib.setProdTributacaoDentro(dentro);
				trib.setProdTributacaoCst(cst);

				ProdProduto prod = new ProdProduto(prodId);
				prod.setProdTributacao(trib);
				prod.setProdProdutoCusto(valor);
				prod.setProdProdutoPreco(preco);
				prod.setProdProdutoDescricao(desc);
				prod.setProdProdutoIncentivo(incentivo);

				ComCompraProduto comProduto = new ComCompraProduto();
				comProduto.setProdProduto(prod);
				comProduto.setComCompraProdutoQuantidade(quantidade);
				comProduto.setProdEmbalagem(new ProdEmbalagem(embalagemId));
				comProduto.setComCompraProdutoValor(valor);
				comProduto.setComCompraProdutoTotal(total);
				comProduto.setComCompraProdutoCfop(cfop);
				comProduto.setComCompraProdutoIcms(icms);
				comProduto.setComCompraProdutoIpi(ipi);
				comProduto.setComCompraProdutoPreco(preco);
				comProduto.setComCompraProdutoOrdem(ordem);
				lista.add(comProduto);
				ordem++;
			} catch (Exception ex) {
				valida = false;
				int row = getStore().indexOf(rec);
				getView().getRow(row).getStyle().setColor("red");
			}
		}

		return getStore().getRecords().length > 0 && valida;
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

	public Store getStoreEmbalagem() {
		return storeEmbalagem;
	}

	public void setStoreEmbalagem(Store storeEmbalagem) {
		this.storeEmbalagem = storeEmbalagem;
	}

	public NumberField getTxtQuantidade() {
		return txtQuantidade;
	}

	public void setTxtQuantidade(NumberField txtQuantidade) {
		this.txtQuantidade = txtQuantidade;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public NumberField getTxtCfop() {
		return txtCfop;
	}

	public void setTxtCfop(NumberField txtCfop) {
		this.txtCfop = txtCfop;
	}

	public NumberField getTxtIcms() {
		return txtIcms;
	}

	public void setTxtIcms(NumberField txtIcms) {
		this.txtIcms = txtIcms;
	}

	public NumberField getTxtIpi() {
		return txtIpi;
	}

	public void setTxtIpi(NumberField txtIpi) {
		this.txtIpi = txtIpi;
	}

	public NumberField getTxtPreco() {
		return txtPreco;
	}

	public void setTxtPreco(NumberField txtPreco) {
		this.txtPreco = txtPreco;
	}
}
