package br.com.opensig.comercial.client.visao.lista;

import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoTroca;
import br.com.opensig.comercial.shared.modelo.ComTrocaProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.produto.client.controlador.comando.ComandoProduto;
import br.com.opensig.produto.shared.modelo.ProdEmbalagem;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemTrocaProduto extends AListagem<ComTrocaProduto> {

	public ListagemTrocaProduto(IFormulario<ComTrocaProduto> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comTrocaProdutoId"), new IntegerFieldDef("comTroca.comTrocaId"), new IntegerFieldDef("comTroca.empEmpresa.empEmpresaId"),
				new StringFieldDef("comTroca.empEmpresa.empEntidade.empEntidadeNome1"), new StringFieldDef("prodProduto.empFornecedor.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("prodProduto.prodProdutoId"), new StringFieldDef("comTrocaProdutoBarra"), new StringFieldDef("prodProduto.prodProdutoDescricao"),
				new StringFieldDef("prodProduto.prodProdutoReferencia"), new DateFieldDef("comTroca.comTrocaData"), new FloatFieldDef("comTrocaProdutoQuantidade"),
				new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"), new FloatFieldDef("comTrocaProdutoValor"),
				new FloatFieldDef("comTrocaProdutoTotal"), new IntegerFieldDef("comTrocaProdutoOrdem") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comTrocaProdutoId", 75, true);
		ColumnConfig ccTrocaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtTroca(), "comTroca.comTrocaId", 100, true);
		ccTrocaId.setHidden(true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "comTroca.empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "comTroca.empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccFornecedor = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "prodProduto.empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccProdId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoId", 100, true);
		ccProdId.setHidden(true);
		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "comTrocaProdutoBarra", 100, true);
		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);
		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 100, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comTroca.comTrocaData", 75, true, DATA);
		ColumnConfig ccEmbalagemId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 100, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);
		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);
		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comTrocaProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// somarotios
		SummaryColumnConfig ccQuantidade = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comTrocaProdutoQuantidade", 50, true, NUMERO), NUMERO);
		SummaryColumnConfig ccValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), "comTrocaProdutoValor", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccTotal = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comTrocaProdutoTotal", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccTrocaId, ccEmpresaId, ccEmpresa, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccData, ccQuantidade, ccEmbalagemId,
				ccEmbalagem, ccValor, ccTotal, ccOrdem };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("comTroca.empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comVenda.empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("comVenda.empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("comVenda.empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);
			} else if (entry.getKey().equals("prodEmbalagem.prodEmbalagemNome")) {
				// tipo
				FieldDef[] fdEmbalagem = new FieldDef[] { new IntegerFieldDef("prodEmbalagemId"), new StringFieldDef("prodEmbalagemNome") };
				CoreProxy<ProdEmbalagem> proxy = new CoreProxy<ProdEmbalagem>(new ProdEmbalagem());
				Store storeEmbalagem = new Store(proxy, new ArrayReader(new RecordDef(fdEmbalagem)), true);

				GridListFilter fEmbalagem = new GridListFilter("prodEmbalagem.prodEmbalagemNome", storeEmbalagem);
				fEmbalagem.setLabelField("prodEmbalagemNome");
				fEmbalagem.setLabelValue("prodEmbalagemNome");
				fEmbalagem.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmbalagem);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comTroca.empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// troca
		SisFuncao troca = UtilClient.getFuncaoPermitida(ComandoTroca.class);
		MenuItem itemTroca = gerarFuncao(troca, "comTrocaId", "comTroca.comTrocaId");
		if (itemTroca != null) {
			mnuContexto.addItem(itemTroca);
		}

		// produto
		SisFuncao produto = UtilClient.getFuncaoPermitida(ComandoProduto.class);
		MenuItem itemProduto = gerarFuncao(produto, "prodProdutoId", "prodProduto.prodProdutoId");
		if (itemProduto != null) {
			mnuContexto.addItem(itemProduto);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}