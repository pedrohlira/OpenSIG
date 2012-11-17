package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoCompra;
import br.com.opensig.comercial.shared.modelo.ComCompraProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
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
import com.gwtext.client.data.BooleanFieldDef;
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
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemCompraProduto extends AListagem<ComCompraProduto> {

	public ListagemCompraProduto(IFormulario<ComCompraProduto> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comCompraProdutoId"), new IntegerFieldDef("comCompra.comCompraId"), new IntegerFieldDef("comCompra.empEmpresa.empEmpresaId"),
				new StringFieldDef("comCompra.empEmpresa.empEntidade.empEntidadeNome1"), new StringFieldDef("prodProduto.empFornecedor.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("prodProduto.prodProdutoId"), new StringFieldDef("prodProduto.prodProdutoBarra"), new StringFieldDef("prodProduto.prodProdutoDescricao"),
				new StringFieldDef("prodProduto.prodProdutoReferencia"), new IntegerFieldDef("prodProduto.prodTributacao.prodTributacaoDentro"),
				new StringFieldDef("prodProduto.prodTributacao.prodTributacaoCst"), new DateFieldDef("comCompra.comCompraRecebimento"), new FloatFieldDef("comCompraProdutoQuantidade"),
				new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"), new FloatFieldDef("comCompraProdutoValor"),
				new FloatFieldDef("comCompraProdutoTotal"), new IntegerFieldDef("comCompraProdutoCfop"), new FloatFieldDef("comCompraProdutoIcms"), new FloatFieldDef("comCompraProdutoIpi"),
				new FloatFieldDef("comCompraProdutoPreco"), new BooleanFieldDef("prodProduto.prodProdutoIncentivo"), new IntegerFieldDef("comCompraProdutoOrdem") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comCompraProdutoId", 75, true);
		ColumnConfig ccCompraId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtCompra(), "comCompra.comCompraId", 100, true);
		ccCompraId.setHidden(true);
		ColumnConfig ccProdId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoId", 100, true);
		ccProdId.setHidden(true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "comCompra.empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "comCompra.empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccFornecedor = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "prodProduto.empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodProduto.prodProdutoBarra", 100, true);
		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);
		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 100, true);
		ColumnConfig ccTributacao = new ColumnConfig(OpenSigCore.i18n.txtDentro(), "prodProduto.prodTributacao.prodTributacaoDentro", 50, true, PORCENTAGEM);
		ColumnConfig ccCst = new ColumnConfig(OpenSigCore.i18n.txtCst(), "prodProduto.prodTributacao.prodTributacaoCst", 50, true);
		ColumnConfig ccRecebimento = new ColumnConfig(OpenSigCore.i18n.txtData(), "comCompra.comCompraRecebimento", 75, true, DATA);
		ColumnConfig ccIncentivo = new ColumnConfig(OpenSigCore.i18n.txtIncentivo(), "prodProduto.prodProdutoIncentivo", 75, true, BOLEANO);
		ColumnConfig ccEmbalagemId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 100, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);
		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);
		ColumnConfig ccCfop = new ColumnConfig(OpenSigCore.i18n.txtCfop(), "comCompraProdutoCfop", 50, true, NUMERO);
		ColumnConfig ccIcms = new ColumnConfig(OpenSigCore.i18n.txtIcms(), "comCompraProdutoIcms", 50, true, PORCENTAGEM);
		ColumnConfig ccIpi = new ColumnConfig(OpenSigCore.i18n.txtIpi(), "comCompraProdutoIpi", 50, true, PORCENTAGEM);
		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comCompraProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// somatorios
		SummaryColumnConfig ccQuantidade = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comCompraProdutoQuantidade", 75, true, NUMERO), NUMERO);
		SummaryColumnConfig ccValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), "comCompraProdutoValor", 75, true, VALOR), VALOR);
		SummaryColumnConfig ccTotal = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comCompraProdutoTotal", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccPreco = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtPreco(), "comCompraProdutoPreco", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccCompraId, ccEmpresaId, ccEmpresa, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccTributacao, ccCst, ccRecebimento, ccQuantidade, ccEmbalagemId, ccEmbalagem,
				ccValor, ccTotal, ccCfop, ccIcms, ccIpi, ccPreco, ccIncentivo };
		modelos = new ColumnModel(bcc);

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("comCompra.empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
			gf.add(fo, EJuncao.E);
		}
		FiltroBinario fb = new FiltroBinario("comCompra.comCompraFechada", ECompara.IGUAL, 1);
		gf.add(fb);
		
		filtroPadrao = gf;
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comCompra.comCompraRecebimento")) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("comCompra.empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("comCompra.empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("comCompra.empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
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
		filtros.get("comCompra.comCompraRecebimento").setActive(false, true);
		filtros.get("comCompra.empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();
		
		// compra
		SisFuncao compra = UtilClient.getFuncaoPermitida(ComandoCompra.class);
		MenuItem itemCompra = gerarFuncao(compra, "comCompraId", "comCompra.comCompraId");
		if (itemCompra != null) {
			mnuContexto.addItem(itemCompra);
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
