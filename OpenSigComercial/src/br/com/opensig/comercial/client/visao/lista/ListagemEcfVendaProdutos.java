package br.com.opensig.comercial.client.visao.lista;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

import com.google.gwt.i18n.client.NumberFormat;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemEcfVendaProdutos extends AListagemEditor<ComEcfVendaProduto> {

	private NumberField txtQuantidade;
	private NumberField txtDesconto;
	private NumberField txtAcrescimo;
	private NumberField txtLiquido;

	public ListagemEcfVendaProdutos(boolean barraTarefa) {
		super(new ComEcfVendaProduto(), barraTarefa);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comEcfVendaProdutoId"), new IntegerFieldDef("comEcfVenda.comEcfVendaId"), new IntegerFieldDef("comEcfVenda.comEcf.comEcfId"),
				new StringFieldDef("comEcfVenda.comEcf.comEcfSerie"), new IntegerFieldDef("empEmpresaId"), new StringFieldDef("empEmpresa"), new StringFieldDef("empFornecedor"),
				new IntegerFieldDef("prodProdutoId"), new StringFieldDef("prodProduto.prodProdutoBarra"), new StringFieldDef("prodProduto.prodProdutoDescricao"),
				new StringFieldDef("prodProduto.prodProdutoReferencia"), new DateFieldDef("comEcfVenda.comEcfVendaData"), new FloatFieldDef("comEcfVendaProdutoQuantidade"),
				new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"), new FloatFieldDef("comEcfVendaProdutoBruto"),
				new FloatFieldDef("comEcfVendaProdutoDesconto"), new FloatFieldDef("comEcfVendaProdutoAcrescimo"), new FloatFieldDef("comEcfVendaProdutoLiquido"),
				new FloatFieldDef("comEcfVendaProdutoTotal"), new BooleanFieldDef("comEcfVendaProdutoCancelado"), new IntegerFieldDef("comEcfVendaProdutoOrdem") };
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

		txtAcrescimo = new NumberField();
		txtAcrescimo.setAllowBlank(false);
		txtAcrescimo.setAllowNegative(false);
		txtAcrescimo.setSelectOnFocus(true);
		txtAcrescimo.setMaxLength(13);

		txtLiquido = new NumberField();
		txtLiquido.setAllowBlank(false);
		txtLiquido.setAllowNegative(false);
		txtLiquido.setSelectOnFocus(true);
		txtLiquido.setMaxLength(13);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "comVendaProdutoId", 10, true);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccVendaId = new ColumnConfig("", "empVendaId", 10, true);
		ccVendaId.setHidden(true);
		ccVendaId.setFixed(true);

		ColumnConfig ccEcfId = new ColumnConfig("", "comEcfId", 10, true);
		ccEcfId.setHidden(true);
		ccEcfId.setFixed(true);

		ColumnConfig ccEcfSerie = new ColumnConfig("", "comEcfSerie", 10, true);
		ccEcfSerie.setHidden(true);
		ccEcfSerie.setFixed(true);

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

		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodProduto.prodProdutoBarra", 100, true);

		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);

		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 75, true);

		ColumnConfig ccData = new ColumnConfig("", "comEcfVenda.comEcfVendaData", 10, true);
		ccData.setHidden(true);
		ccData.setFixed(true);

		ColumnConfig ccQuantidade = new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comEcfVendaProdutoQuantidade", 50, true, IListagem.NUMERO);
		ccQuantidade.setEditor(new GridEditor(txtQuantidade));

		ColumnConfig ccEmbalagemId = new ColumnConfig("", "prodEmbalagem.prodEmbalagemId", 10, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);

		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);

		ColumnConfig ccBruto = new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comEcfVendaProdutoBruto", 75, true, IListagem.DINHEIRO);
		ccBruto.setHidden(true);

		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto(), "comEcfVendaProdutoDesconto", 50, true, IListagem.PORCENTAGEM);
		ccDesconto.setEditor(new GridEditor(txtDesconto));

		ColumnConfig ccAcrescimo = new ColumnConfig(OpenSigCore.i18n.txtAcrescimo(), "comEcfVendaProdutoAcrescimo", 50, true, IListagem.PORCENTAGEM);
		ccAcrescimo.setEditor(new GridEditor(txtAcrescimo));

		ColumnConfig ccLiquido = new ColumnConfig(OpenSigCore.i18n.txtLiquido(), "comEcfVendaProdutoLiquido", 75, true, IListagem.DINHEIRO);
		ccLiquido.setEditor(new GridEditor(txtLiquido));

		ColumnConfig ccCancelado = new ColumnConfig(OpenSigCore.i18n.txtCancelada(), "comEcfVendaProdutoCancelado", 75, true, IListagem.BOLEANO);

		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comEcfVendaProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// sumarios
		ColumnConfig ccTotal = new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comEcfVendaProdutoTotal", 75, true, IListagem.DINHEIRO);
		SummaryColumnConfig sumTotal = new SummaryColumnConfig(SummaryColumnConfig.SUM, ccTotal, IListagem.DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccVendaId, ccEcfId, ccEcfSerie, ccEmpresaId, ccEmpresa, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccData,
				ccQuantidade, ccEmbalagemId, ccEmbalagem, ccBruto, ccDesconto, ccAcrescimo, ccLiquido, sumTotal, ccCancelado, ccOrdem };
		modelos = new ColumnModel(bcc);

		// configurações padrão e carrega dados
		addEditorGridListener(new EditorGridListenerAdapter() {
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				double qtd = record.getAsDouble("comEcfVendaProdutoQuantidade");
				double bruto = record.getAsDouble("comEcfVendaProdutoBruto");
				double desc = record.getAsDouble("comEcfVendaProdutoDesconto");
				double acres = record.getAsDouble("comEcfVendaProdutoAcrescimo");
				double liquido = record.getAsDouble("comEcfVendaProdutoLiquido");

				if (field.equals("comEcfVendaProdutoDesconto")) {
					liquido = bruto - (bruto * desc / 100);
					String strValor = NumberFormat.getFormat("0.##").format(liquido);
					liquido = Double.valueOf(strValor.replace(",", "."));
					acres = 0;
				} else if (field.equals("comEcfVendaProdutoAcrescimo")) {
					liquido = bruto + (bruto * acres / 100);
					String strValor = NumberFormat.getFormat("0.##").format(liquido);
					liquido = Double.valueOf(strValor.replace(",", "."));
					desc = 0;
				} else if (field.equals("comEcfVendaProdutoLiquido")) {
					if (liquido < bruto) {
						desc = 100 - (liquido / bruto * 100);
						String strDesc = NumberFormat.getFormat("0.##").format(desc);
						desc = Double.valueOf(strDesc.replace(",", "."));
						acres = 0;
					} else if (liquido > bruto) {
						acres = (liquido / bruto * 100) - 100;
						String strAcres = NumberFormat.getFormat("0.##").format(acres);
						acres = Double.valueOf(strAcres.replace(",", "."));
						desc = 0;
					}
				}

				if (desc > 100) {
					desc = 99.99;
					liquido = 0.01;
				}

				double totLiquido = qtd * liquido;
				record.set("comEcfVendaProdutoQuantidade", qtd);
				record.set("comEcfVendaProdutoDesconto", desc);
				record.set("comEcfVendaProdutoAcrescimo", acres);
				record.set("comEcfVendaProdutoLiquido", liquido);
				record.set("comEcfVendaProdutoTotal", totLiquido);
			}
		});

		filtroPadrao = new FiltroNumero("comEcfVendaProdutoId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtProduto(), "icon-produtovenda");
		setHeight(Ext.getBody().getHeight() - 320);
		super.inicializar();

		getStore().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				for (int i = 0; i < records.length; i++) {
					if (records[i].getAsInteger("prodProdutoId") == 0) {
						getView().getRow(i).getStyle().setColor("blue");
					}
				}
			}
		});
	}

	public boolean validar(List<ComEcfVendaProduto> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();
		int ordem = 1;

		for (Record rec : recs) {
			try {
				int prodId = rec.getAsInteger("prodProdutoId");
				double quantidade = rec.getAsDouble("comEcfVendaProdutoQuantidade");
				int embalagemId = rec.getAsInteger("prodEmbalagem.prodEmbalagemId");
				double bruto = rec.getAsDouble("comEcfVendaProdutoBruto");
				double desconto = rec.getAsDouble("comEcfVendaProdutoDesconto");
				double acrescimo = rec.getAsDouble("comEcfVendaProdutoAcrescimo");
				double liquido = rec.getAsDouble("comEcfVendaProdutoLiquido");
				double totLiquido = rec.getAsDouble("comEcfVendaProdutoTotal");

				if (prodId == 0 || quantidade < 1 || desconto >= 100.00 || liquido < 0.01) {
					throw new Exception();
				}

				ComEcfVendaProduto venEcfProduto = new ComEcfVendaProduto();
				venEcfProduto.setProdProduto(new ProdProduto(prodId));
				venEcfProduto.setComEcfVendaProdutoQuantidade(quantidade);
				venEcfProduto.setProdEmbalagem(new ProdEmbalagem(embalagemId));
				venEcfProduto.setComEcfVendaProdutoBruto(bruto);
				venEcfProduto.setComEcfVendaProdutoDesconto(desconto);
				venEcfProduto.setComEcfVendaProdutoAcrescimo(acrescimo);
				venEcfProduto.setComEcfVendaProdutoLiquido(liquido);
				venEcfProduto.setComEcfVendaProdutoTotal(totLiquido);
				venEcfProduto.setComEcfVendaProdutoOrdem(ordem);
				lista.add(venEcfProduto);
				ordem++;
			} catch (Exception ex) {
				valida = false;
				int row = getStore().indexOf(rec);
				String cor = rec.getAsInteger("prodProdutoId") == 0 ? "blue" : "red";
				getView().getRow(row).getStyle().setColor(cor);
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

	public NumberField getTxtAcrescimo() {
		return txtAcrescimo;
	}

	public void setTxtAcrescimo(NumberField txtAcrescimo) {
		this.txtAcrescimo = txtAcrescimo;
	}

	public NumberField getTxtLiquido() {
		return txtLiquido;
	}

	public void setTxtLiquido(NumberField txtLiquido) {
		this.txtLiquido = txtLiquido;
	}

}
