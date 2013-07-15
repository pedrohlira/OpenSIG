package br.com.opensig.comercial.client.visao.lista;

import java.util.List;

import br.com.opensig.comercial.shared.modelo.ComTrocaProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;
import br.com.opensig.produto.shared.modelo.ProdProduto;

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

public class ListagemTrocaProdutos extends AListagemEditor<ComTrocaProduto> {

	private NumberField txtQuantidade;
	private NumberField txtValor;

	public ListagemTrocaProdutos(boolean barraTarefa) {
		super(new ComTrocaProduto(), barraTarefa);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comTrocaProdutoId"), new IntegerFieldDef("comTroca.comTrocaId"), new IntegerFieldDef("comTroca.empEmpresa.empEmpresaId"),
				new StringFieldDef("comTroca.empEmpresa.empEntidade.empEntidadeNome1"), new StringFieldDef("prodProduto.empFornecedor.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("prodProdutoId"), new StringFieldDef("comTrocaProdutoBarra"), new StringFieldDef("prodProduto.prodProdutoDescricao"),
				new StringFieldDef("prodProduto.prodProdutoReferencia"), new DateFieldDef("comTroca.comTrocaData"), new FloatFieldDef("comTrocaProdutoQuantidade"),
				new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"), new FloatFieldDef("comTrocaProdutoValor"),
				new FloatFieldDef("comTrocaProdutoTotal"), new IntegerFieldDef("comTrocaProdutoOrdem") };
		campos = new RecordDef(fd);

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
		txtValor.setMaxLength(13);

		// colunas
		ColumnConfig ccId = new ColumnConfig("", "comTrocaProdutoId", 10, true);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccTrocaId = new ColumnConfig("", "comTrocaId", 10, true);
		ccTrocaId.setHidden(true);
		ccTrocaId.setFixed(true);

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

		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "comTrocaProdutoBarra", 100, true);

		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);

		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 75, true);

		ColumnConfig ccData = new ColumnConfig("", "comTroca.comTrocaData", 10, true);
		ccData.setHidden(true);
		ccData.setFixed(true);

		ColumnConfig ccQuantidade = new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comTrocaProdutoQuantidade", 50, true, IListagem.NUMERO);
		ccQuantidade.setEditor(new GridEditor(txtQuantidade));

		ColumnConfig ccEmbalagemId = new ColumnConfig("", "prodEmbalagem.prodEmbalagemId", 10, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);

		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);

		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "comTrocaProdutoValor", 75, true, IListagem.DINHEIRO);
		ccValor.setEditor(new GridEditor(txtValor));

		ColumnConfig ccTotal = new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comTrocaProdutoTotal", 75, true, IListagem.DINHEIRO);

		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comTrocaProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccTrocaId, ccEmpresaId, ccEmpresa, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccData, ccQuantidade, ccEmbalagemId,
				ccEmbalagem, ccValor, ccTotal, ccOrdem };
		modelos = new ColumnModel(bcc);

		// configurações padrão e carrega dados
		addEditorGridListener(new EditorGridListenerAdapter() {
			public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
				double qtd = record.getAsDouble("comTrocaProdutoQuantidade");
				double valor = record.getAsDouble("comTrocaProdutoValor");
				record.set("comTrocaProdutoTotal", qtd * valor);
			}
		});

		filtroPadrao = new FiltroNumero("comTrocaProdutoId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtProduto(), "icon-produtotroca");
		setHeight(Ext.getBody().getHeight() - 320);
		super.inicializar();
	}

	public boolean validar(List<ComTrocaProduto> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();
		int ordem = 1;

		for (Record rec : recs) {
			try {
				int prodId = rec.getAsInteger("prodProdutoId");
				String barra = rec.getAsString("comTrocaProdutoBarra");
				double quantidade = rec.getAsDouble("comTrocaProdutoQuantidade");
				int embalagemId = rec.getAsInteger("prodEmbalagem.prodEmbalagemId");
				double valor = rec.getAsDouble("comTrocaProdutoValor");
				double total = rec.getAsDouble("comTrocaProdutoTotal");

				if (quantidade < 1 || valor < 0.01) {
					throw new Exception();
				}

				ComTrocaProduto tp = new ComTrocaProduto();
				tp.setProdProduto(new ProdProduto(prodId));
				tp.setComTrocaProdutoBarra(barra);
				tp.setComTrocaProdutoQuantidade(quantidade);
				tp.setProdEmbalagem(new ProdEmbalagem(embalagemId));
				tp.setComTrocaProdutoValor(valor);
				tp.setComTrocaProdutoTotal(total);
				tp.setComTrocaProdutoOrdem(ordem);
				lista.add(tp);
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

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

}
