package br.com.opensig.comercial.client.visao.lista;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemVendaProdutos extends AListagemEditor<ComVendaProduto> {

	private NumberField txtQuantidade;
	private NumberField txtDesconto;
	private NumberField txtLiquido;
	private NumberField txtIcms;
	private NumberField txtIpi;

	public ListagemVendaProdutos(boolean barraTarefa) {
		super(new ComVendaProduto(), barraTarefa);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comVendaProdutoId"), new IntegerFieldDef("comVenda.comVendaId"), new IntegerFieldDef("empEmpresaId"), new StringFieldDef("empEmpresa"),
				new StringFieldDef("empCliente"), new StringFieldDef("empFornecedor"), new IntegerFieldDef("prodProdutoId"), new StringFieldDef("prodProduto.prodProdutoBarra"),
				new StringFieldDef("prodProduto.prodProdutoDescricao"), new StringFieldDef("prodProduto.prodProdutoReferencia"), new DateFieldDef("comVenda.comVendaData"),
				new FloatFieldDef("comVendaProdutoQuantidade"), new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"),
				new FloatFieldDef("comVendaProdutoBruto"), new FloatFieldDef("comVendaProdutoDesconto"), new FloatFieldDef("comVendaProdutoLiquido"), new FloatFieldDef("comVendaProdutoTotalBruto"),
				new FloatFieldDef("comVendaProdutoTotalLiquido"), new IntegerFieldDef("comVendaProdutoEstoque"), new IntegerFieldDef("comVendaProdutoOrigem"),
				new FloatFieldDef("comVendaProdutoIcms"), new FloatFieldDef("comVendaProdutoIpi"), new IntegerFieldDef("comVendaProdutoOrdem") };
		campos = new RecordDef(fd);

		// editores
		txtQuantidade = new NumberField();
		txtQuantidade.setAllowBlank(false);
		txtQuantidade.setAllowNegative(false);
		txtQuantidade.setSelectOnFocus(true);
		txtQuantidade.setMaxLength(11);

		txtDesconto = new NumberField();
		txtDesconto.setAllowBlank(false);
		txtDesconto.setAllowNegative(false);
		txtDesconto.setSelectOnFocus(true);
		txtDesconto.setMaxLength(13);

		txtLiquido = new NumberField();
		txtLiquido.setAllowBlank(false);
		txtLiquido.setAllowNegative(false);
		txtLiquido.setSelectOnFocus(true);
		txtLiquido.setMaxLength(13);

		txtIcms = new NumberField();
		txtIcms.setAllowNegative(false);
		txtIcms.setAllowBlank(false);
		txtIcms.setSelectOnFocus(true);
		txtIcms.setDecimalPrecision(2);
		txtIcms.setMaxLength(5);

		txtIpi = new NumberField();
		txtIpi.setAllowNegative(false);
		txtIpi.setAllowBlank(false);
		txtIpi.setSelectOnFocus(true);
		txtIpi.setDecimalPrecision(2);
		txtIpi.setMaxLength(5);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "comVendaProdutoId", 10, true);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccVendaId = new ColumnConfig("", "empVendaId", 10, true);
		ccVendaId.setHidden(true);
		ccVendaId.setFixed(true);

		ColumnConfig ccEmpresaId = new ColumnConfig("", "empEmpresaId", 10, true);
		ccEmpresaId.setHidden(true);
		ccEmpresaId.setFixed(true);

		ColumnConfig ccEmpresa = new ColumnConfig("", "empEmpresa", 10, true);
		ccEmpresa.setHidden(true);
		ccEmpresa.setFixed(true);

		ColumnConfig ccCliente = new ColumnConfig("", "empCliente", 10, true);
		ccCliente.setHidden(true);
		ccCliente.setFixed(true);

		ColumnConfig ccFornecedor = new ColumnConfig("", "empFornecedor", 10, true);
		ccFornecedor.setHidden(true);
		ccFornecedor.setFixed(true);

		ColumnConfig ccProdId = new ColumnConfig("", "prodProdutoId", 10, true);
		ccProdId.setHidden(true);
		ccProdId.setFixed(true);

		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodProduto.prodProdutoBarra", 100, true);

		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);

		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 75, true);

		ColumnConfig ccData = new ColumnConfig("", "comVenda.comVendaData", 10, true);
		ccData.setHidden(true);
		ccData.setFixed(true);

		ColumnConfig ccQuantidade = new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comVendaProdutoQuantidade", 50, true, IListagem.NUMERO);
		ccQuantidade.setEditor(new GridEditor(txtQuantidade));

		ColumnConfig ccEmbalagemId = new ColumnConfig("", "prodEmbalagem.prodEmbalagemId", 10, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);

		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);

		ColumnConfig ccBruto = new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comVendaProdutoBruto", 75, true, IListagem.DINHEIRO);
		ccBruto.setHidden(true);

		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto(), "comVendaProdutoDesconto", 50, true, IListagem.PORCENTAGEM);
		ccDesconto.setEditor(new GridEditor(txtDesconto));

		ColumnConfig ccLiquido = new ColumnConfig(OpenSigCore.i18n.txtLiquido(), "comVendaProdutoLiquido", 75, true, IListagem.DINHEIRO);
		ccLiquido.setEditor(new GridEditor(txtLiquido));

		ColumnConfig ccEstoque = new ColumnConfig("", "comVendaProdutoEstoque", 0, false);
		ccEstoque.setHidden(true);
		ccEstoque.setFixed(true);

		ColumnConfig ccOrigem = new ColumnConfig("", "comVendaProdutoOrigem", 0, false);
		ccOrigem.setHidden(true);
		ccOrigem.setFixed(true);

		ColumnConfig ccIcms = new ColumnConfig(OpenSigCore.i18n.txtIcms(), "comVendaProdutoIcms", 75, true, IListagem.PORCENTAGEM);
		ccIcms.setEditor(new GridEditor(txtIcms));
		ccIcms.setHidden(true);

		ColumnConfig ccIpi = new ColumnConfig(OpenSigCore.i18n.txtIpi(), "comVendaProdutoIpi", 75, true, IListagem.PORCENTAGEM);
		ccIpi.setEditor(new GridEditor(txtIpi));
		ccIpi.setHidden(true);

		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comVendaProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// sumarios
		ColumnConfig ccTotalBruto = new ColumnConfig(OpenSigCore.i18n.txtTotal() + " " + OpenSigCore.i18n.txtBruto(), "comVendaProdutoTotalBruto", 75, true, IListagem.DINHEIRO);
		ccTotalBruto.setHidden(true);
		SummaryColumnConfig sumBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccTotalBruto, IListagem.DINHEIRO);

		ColumnConfig ccTatalLiquido = new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comVendaProdutoTotalLiquido", 75, true, IListagem.DINHEIRO);
		SummaryColumnConfig sumLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccTatalLiquido, IListagem.DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccVendaId, ccEmpresaId, ccEmpresa, ccCliente, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccData, ccQuantidade,
				ccEmbalagemId, ccEmbalagem, ccBruto, ccDesconto, ccLiquido, sumBruto, sumLiquido, ccEstoque, ccOrigem, ccIcms, ccIpi, ccOrdem };
		modelos = new ColumnModel(bcc);

		// configurações padrão e carrega dados
		addEditorGridListener(new EditorGridListenerAdapter() {
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				double qtd = record.getAsDouble("comVendaProdutoQuantidade");
				double bruto = record.getAsDouble("comVendaProdutoBruto");
				double desc = record.getAsDouble("comVendaProdutoDesconto");
				double liquido = record.getAsDouble("comVendaProdutoLiquido");

				if (field.equals("comVendaProdutoDesconto")) {
					liquido = bruto - (bruto * desc / 100);
					String strValor = NumberFormat.getFormat("0.##").format(liquido);
					liquido = Double.valueOf(strValor.replace(",", "."));
				} else if (field.equals("comVendaProdutoLiquido")) {
					desc = 100 - (liquido / bruto * 100);
					String strDesc = NumberFormat.getFormat("0.##").format(desc);
					desc = Double.valueOf(strDesc.replace(",", "."));
				}

				if (desc > 100) {
					desc = 100;
					liquido = 0;
				}

				double totBruto = qtd * bruto;
				double totLiquido = qtd * liquido;

				record.set("comVendaProdutoQuantidade", qtd);
				record.set("comVendaProdutoDesconto", desc);
				record.set("comVendaProdutoLiquido", liquido);
				record.set("comVendaProdutoTotalBruto", totBruto);
				record.set("comVendaProdutoTotalLiquido", totLiquido);
			}
		});

		filtroPadrao = new FiltroNumero("comVendaProdutoId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtProduto(), "icon-produtovenda");
		setHeight(Ext.getBody().getHeight() - 320);
		super.inicializar();
	}

	public boolean validar(List<ComVendaProduto> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();
		int ordem = 1;

		for (Record rec : recs) {
			try {
				int prodId = rec.getAsInteger("prodProdutoId");
				double quantidade = rec.getAsDouble("comVendaProdutoQuantidade");
				int embalagemId = rec.getAsInteger("prodEmbalagem.prodEmbalagemId");
				int estoque = UtilClient.CONF.get("estoque.ativo").equalsIgnoreCase("nao") ? 0 : rec.getAsInteger("comVendaProdutoEstoque");
				double bruto = rec.getAsDouble("comVendaProdutoBruto");
				double desconto = rec.getAsDouble("comVendaProdutoDesconto");
				double liquido = rec.getAsDouble("comVendaProdutoLiquido");
				double totBruto = rec.getAsDouble("comVendaProdutoTotalBruto");
				double totLiquido = rec.getAsDouble("comVendaProdutoTotalLiquido");
				double icms = rec.getAsDouble("comVendaProdutoIcms");
				double ipi = rec.getAsDouble("comVendaProdutoIpi");

				if ((estoque != 0 && quantidade > estoque) || quantidade < 1 || desconto > 100.00 || liquido < 0.01) {
					throw new Exception();
				}

				ComVendaProduto venProduto = new ComVendaProduto();
				venProduto.setProdProduto(new ProdProduto(prodId));
				venProduto.setComVendaProdutoQuantidade(quantidade);
				venProduto.setProdEmbalagem(new ProdEmbalagem(embalagemId));
				venProduto.setComVendaProdutoBruto(bruto);
				venProduto.setComVendaProdutoDesconto(desconto);
				venProduto.setComVendaProdutoLiquido(liquido);
				venProduto.setComVendaProdutoTotalBruto(totBruto);
				venProduto.setComVendaProdutoTotalLiquido(totLiquido);
				venProduto.setComVendaProdutoIcms(icms);
				venProduto.setComVendaProdutoIpi(ipi);
				venProduto.setComVendaProdutoOrdem(ordem);
				lista.add(venProduto);
				ordem++;
			} catch (Exception ex) {
				valida = false;
				int row = getStore().indexOf(rec);
				getView().getRow(row).getStyle().setColor("red");
			}
		}

		return getStore().getRecords().length > 0 && valida;
	}

	public NumberField getTxtQuantidade() {
		return txtQuantidade;
	}

	public void setTxtQuantidade(NumberField txtQuantidade) {
		this.txtQuantidade = txtQuantidade;
	}

	public NumberField getTxtDesconto() {
		return txtDesconto;
	}

	public void setTxtDesconto(NumberField txtDesconto) {
		this.txtDesconto = txtDesconto;
	}

	public NumberField getTxtLiquido() {
		return txtLiquido;
	}

	public void setTxtLiquido(NumberField txtLiquido) {
		this.txtLiquido = txtLiquido;
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
}
