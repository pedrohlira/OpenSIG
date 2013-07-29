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
import com.gwtext.client.widgets.form.TextField;
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
	private NumberField txtCfop;
	private TextField txtIcmsCst;
	private NumberField txtIcms;
	private TextField txtIpiCst;
	private NumberField txtIpi;
	private TextField txtPisCst;
	private NumberField txtPis;
	private TextField txtCofinsCst;
	private NumberField txtCofins;

	public ListagemVendaProdutos(boolean barraTarefa) {
		super(new ComVendaProduto(), barraTarefa);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comVendaProdutoId"), new IntegerFieldDef("comVenda.comVendaId"), new IntegerFieldDef("empEmpresaId"), new StringFieldDef("empEmpresa"),
				new StringFieldDef("empCliente"), new StringFieldDef("empFornecedor"), new IntegerFieldDef("prodProdutoId"), new StringFieldDef("comVendaProdutoBarra"),
				new StringFieldDef("prodProduto.prodProdutoDescricao"), new StringFieldDef("prodProduto.prodProdutoReferencia"), new DateFieldDef("comVenda.comVendaData"),
				new FloatFieldDef("comVendaProdutoQuantidade"), new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"),
				new FloatFieldDef("comVendaProdutoBruto"), new FloatFieldDef("comVendaProdutoDesconto"), new FloatFieldDef("comVendaProdutoLiquido"), new FloatFieldDef("comVendaProdutoTotalBruto"),
				new FloatFieldDef("comVendaProdutoTotalLiquido"), new IntegerFieldDef("comVendaProdutoEstoque"), new IntegerFieldDef("comVendaProdutoOrigem"),
				new IntegerFieldDef("comVendaProdutoCfop"), new StringFieldDef("comVendaProdutoIcmsCst"), new FloatFieldDef("comVendaProdutoIcms"), new StringFieldDef("comVendaProdutoIpiCst"),
				new FloatFieldDef("comVendaProdutoIpi"), new StringFieldDef("comVendaProdutoPisCst"), new FloatFieldDef("comVendaProdutoPis"), new StringFieldDef("comVendaProdutoCofinsCst"),
				new FloatFieldDef("comVendaProdutoCofins"), new IntegerFieldDef("comVendaProdutoOrdem") };
		campos = new RecordDef(fd);

		// editores
		txtQuantidade = new NumberField();
		txtQuantidade.setAllowBlank(false);
		txtQuantidade.setAllowNegative(false);
		txtQuantidade.setSelectOnFocus(true);
		txtQuantidade.setMaxLength(11);
		txtQuantidade.setDecimalPrecision(4);

		txtDesconto = new NumberField();
		txtDesconto.setAllowBlank(false);
		txtDesconto.setAllowNegative(false);
		txtDesconto.setSelectOnFocus(true);
		txtDesconto.setMaxLength(11);
		txtDesconto.setDecimalPrecision(2);

		txtLiquido = new NumberField();
		txtLiquido.setAllowBlank(false);
		txtLiquido.setAllowNegative(false);
		txtLiquido.setSelectOnFocus(true);
		txtLiquido.setMaxLength(11);
		txtLiquido.setDecimalPrecision(2);

		txtCfop = new NumberField();
		txtCfop.setAllowNegative(false);
		txtCfop.setAllowBlank(false);
		txtCfop.setAllowDecimals(false);
		txtCfop.setSelectOnFocus(true);
		txtCfop.setMinLength(4);
		txtCfop.setMaxLength(4);
		txtCfop.setMinValue(4000);

		txtIcmsCst = new TextField();
		txtIcmsCst.setRegex("^(\\d{2}|\\d{3})$");

		txtIcms = new NumberField();
		txtIcms.setAllowNegative(false);
		txtIcms.setAllowBlank(false);
		txtIcms.setSelectOnFocus(true);
		txtIcms.setDecimalPrecision(2);
		txtIcms.setMaxLength(5);

		txtIpiCst = new TextField();
		txtIpiCst.setRegex("^(\\d{2})$");

		txtIpi = new NumberField();
		txtIpi.setAllowNegative(false);
		txtIpi.setAllowBlank(false);
		txtIpi.setSelectOnFocus(true);
		txtIpi.setDecimalPrecision(2);
		txtIpi.setMaxLength(5);

		txtPisCst = new TextField();
		txtPisCst.setRegex("^(\\d{2})$");

		txtPis = new NumberField();
		txtPis.setAllowNegative(false);
		txtPis.setAllowBlank(false);
		txtPis.setSelectOnFocus(true);
		txtPis.setDecimalPrecision(2);
		txtPis.setMaxLength(5);

		txtCofinsCst = new TextField();
		txtCofinsCst.setRegex("^(\\d{2})$");

		txtCofins = new NumberField();
		txtCofins.setAllowNegative(false);
		txtCofins.setAllowBlank(false);
		txtCofins.setSelectOnFocus(true);
		txtCofins.setDecimalPrecision(2);
		txtCofins.setMaxLength(5);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "comVendaProdutoId", 10, true);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccVendaId = new ColumnConfig("", "comVendaId", 10, true);
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

		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "comVendaProdutoBarra", 100, true);

		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);

		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 75, true);

		ColumnConfig ccData = new ColumnConfig("", "comVenda.comVendaData", 10, true);
		ccData.setHidden(true);
		ccData.setFixed(true);

		ColumnConfig ccQuantidade = new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comVendaProdutoQuantidade", 75, true, IListagem.VALOR);
		ccQuantidade.setEditor(new GridEditor(txtQuantidade));

		ColumnConfig ccEmbalagemId = new ColumnConfig("", "prodEmbalagem.prodEmbalagemId", 10, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);

		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);

		ColumnConfig ccBruto = new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comVendaProdutoBruto", 75, true, IListagem.DINHEIRO);

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

		ColumnConfig ccCfop = new ColumnConfig(OpenSigCore.i18n.txtCfop(), "comVendaProdutoCfop", 75, true, IListagem.NUMERO);
		ccCfop.setEditor(new GridEditor(txtCfop));
		ccCfop.setHidden(true);

		ColumnConfig ccIcmsCst = new ColumnConfig(OpenSigCore.i18n.txtIcms() + " - " + OpenSigCore.i18n.txtCst(), "comVendaProdutoIcmsCst", 100, true);
		ccIcmsCst.setEditor(new GridEditor(txtIcmsCst));
		ccIcmsCst.setHidden(true);

		ColumnConfig ccIcms = new ColumnConfig(OpenSigCore.i18n.txtIcms(), "comVendaProdutoIcms", 75, true, IListagem.PORCENTAGEM);
		ccIcms.setEditor(new GridEditor(txtIcms));
		ccIcms.setHidden(true);

		ColumnConfig ccIpiCst = new ColumnConfig(OpenSigCore.i18n.txtIpi() + " - " + OpenSigCore.i18n.txtCst(), "comVendaProdutoIpiCst", 100, true);
		ccIpiCst.setEditor(new GridEditor(txtIpiCst));
		ccIpiCst.setHidden(true);

		ColumnConfig ccIpi = new ColumnConfig(OpenSigCore.i18n.txtIpi(), "comVendaProdutoIpi", 75, true, IListagem.PORCENTAGEM);
		ccIpi.setEditor(new GridEditor(txtIpi));
		ccIpi.setHidden(true);

		ColumnConfig ccPisCst = new ColumnConfig(OpenSigCore.i18n.txtPis() + " - " + OpenSigCore.i18n.txtCst(), "comVendaProdutoPisCst", 100, true);
		ccPisCst.setEditor(new GridEditor(txtPisCst));
		ccPisCst.setHidden(true);

		ColumnConfig ccPis = new ColumnConfig(OpenSigCore.i18n.txtPis(), "comVendaProdutoPis", 75, true, IListagem.PORCENTAGEM);
		ccPis.setEditor(new GridEditor(txtPis));
		ccPis.setHidden(true);

		ColumnConfig ccCofinsCst = new ColumnConfig(OpenSigCore.i18n.txtCofins() + " - " + OpenSigCore.i18n.txtCst(), "comVendaProdutoCofinsCst", 100, true);
		ccCofinsCst.setEditor(new GridEditor(txtCofinsCst));
		ccCofinsCst.setHidden(true);

		ColumnConfig ccCofins = new ColumnConfig(OpenSigCore.i18n.txtCofins(), "comVendaProdutoCofins", 75, true, IListagem.PORCENTAGEM);
		ccCofins.setEditor(new GridEditor(txtCofins));
		ccCofins.setHidden(true);

		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comVendaProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// sumarios
		ColumnConfig ccTotalBruto = new ColumnConfig(OpenSigCore.i18n.txtTotal() + " " + OpenSigCore.i18n.txtBruto(), "comVendaProdutoTotalBruto", 75, true, IListagem.DINHEIRO);
		SummaryColumnConfig sumBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccTotalBruto, IListagem.DINHEIRO);

		ColumnConfig ccTatalLiquido = new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comVendaProdutoTotalLiquido", 75, true, IListagem.DINHEIRO);
		SummaryColumnConfig sumLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccTatalLiquido, IListagem.DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccVendaId, ccEmpresaId, ccEmpresa, ccCliente, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccData, ccQuantidade,
				ccEmbalagemId, ccEmbalagem, ccBruto, ccDesconto, ccLiquido, sumBruto, sumLiquido, ccEstoque, ccOrigem, ccCfop, ccIcmsCst, ccIcms, ccIpiCst, ccIpi, ccPisCst, ccPis, ccCofinsCst,
				ccCofins, ccOrdem };
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
				String barra = rec.getAsString("comVendaProdutoBarra");
				double quantidade = rec.getAsDouble("comVendaProdutoQuantidade");
				int embalagemId = rec.getAsInteger("prodEmbalagem.prodEmbalagemId");
				int estoque = UtilClient.CONF.get("estoque.ativo").equalsIgnoreCase("sim") ? rec.getAsInteger("comVendaProdutoEstoque") : 0;
				double bruto = rec.getAsDouble("comVendaProdutoBruto");
				double desconto = rec.getAsDouble("comVendaProdutoDesconto");
				double liquido = rec.getAsDouble("comVendaProdutoLiquido");
				double totBruto = rec.getAsDouble("comVendaProdutoTotalBruto");
				double totLiquido = rec.getAsDouble("comVendaProdutoTotalLiquido");
				int cfop = rec.getAsInteger("comVendaProdutoCfop");
				String icmsCst = rec.getAsString("comVendaProdutoIcmsCst");
				double icms = rec.getAsDouble("comVendaProdutoIcms");
				String ipiCst = rec.getAsString("comVendaProdutoIpiCst");
				double ipi = rec.getAsDouble("comVendaProdutoIpi");
				String pisCst = rec.getAsString("comVendaProdutoPisCst");
				double pis = rec.getAsDouble("comVendaProdutoPis");
				String cofinsCst = rec.getAsString("comVendaProdutoCofinsCst");
				double cofins = rec.getAsDouble("comVendaProdutoCofins");

				if ((estoque != 0 && quantidade > estoque) || quantidade <= 0 || desconto > 100.00 || liquido <= 0) {
					throw new Exception();
				}

				ComVendaProduto vp = new ComVendaProduto();
				vp.setProdProduto(new ProdProduto(prodId));
				vp.setComVendaProdutoBarra(barra);
				vp.setComVendaProdutoQuantidade(quantidade);
				vp.setProdEmbalagem(new ProdEmbalagem(embalagemId));
				vp.setComVendaProdutoBruto(bruto);
				vp.setComVendaProdutoDesconto(desconto);
				vp.setComVendaProdutoLiquido(liquido);
				vp.setComVendaProdutoTotalBruto(totBruto);
				vp.setComVendaProdutoTotalLiquido(totLiquido);
				vp.setComVendaProdutoCfop(cfop);
				vp.setComVendaProdutoIcmsCst(icmsCst == null ? "" : icmsCst);
				vp.setComVendaProdutoIcms(icms);
				vp.setComVendaProdutoIpiCst(ipiCst == null ? "" : ipiCst);
				vp.setComVendaProdutoIpi(ipi);
				vp.setComVendaProdutoPisCst(pisCst == null ? "" : pisCst);
				vp.setComVendaProdutoPis(pis);
				vp.setComVendaProdutoCofinsCst(cofinsCst == null ? "" : cofinsCst);
				vp.setComVendaProdutoCofins(cofins);
				vp.setComVendaProdutoOrdem(ordem);
				lista.add(vp);
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

	public NumberField getTxtCfop() {
		return txtCfop;
	}

	public void setTxtCfop(NumberField txtCfop) {
		this.txtCfop = txtCfop;
	}

	public TextField getTxtIcmsCst() {
		return txtIcmsCst;
	}

	public void setTxtIcmsCst(TextField txtIcmsCst) {
		this.txtIcmsCst = txtIcmsCst;
	}

	public TextField getTxtIpiCst() {
		return txtIpiCst;
	}

	public void setTxtIpiCst(TextField txtIpiCst) {
		this.txtIpiCst = txtIpiCst;
	}

	public TextField getTxtPisCst() {
		return txtPisCst;
	}

	public void setTxtPisCst(TextField txtPisCst) {
		this.txtPisCst = txtPisCst;
	}

	public NumberField getTxtPis() {
		return txtPis;
	}

	public void setTxtPis(NumberField txtPis) {
		this.txtPis = txtPis;
	}

	public TextField getTxtCofinsCst() {
		return txtCofinsCst;
	}

	public void setTxtCofinsCst(TextField txtCofinsCst) {
		this.txtCofinsCst = txtCofinsCst;
	}

	public NumberField getTxtCofins() {
		return txtCofins;
	}

	public void setTxtCofins(NumberField txtCofins) {
		this.txtCofins = txtCofins;
	}

}
