package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoVenda;
import br.com.opensig.comercial.shared.modelo.ComVendaProduto;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
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
import br.com.opensig.permissao.shared.modelo.SisUsuario;
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
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemVendaProduto extends AListagem<ComVendaProduto> {

	public ListagemVendaProduto(IFormulario<ComVendaProduto> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comVendaProdutoId"), new IntegerFieldDef("comVenda.comVendaId"), new IntegerFieldDef("comVenda.empEmpresa.empEmpresaId"),
				new StringFieldDef("comVenda.empEmpresa.empEntidade.empEntidadeNome1"), new StringFieldDef("comVenda.empCliente.empEntidade.empEntidadeNome1"),
				new StringFieldDef("prodProduto.empFornecedor.empEntidade.empEntidadeNome1"), new IntegerFieldDef("prodProduto.prodProdutoId"), new StringFieldDef("prodProduto.prodProdutoBarra"),
				new StringFieldDef("prodProduto.prodProdutoDescricao"), new StringFieldDef("prodProduto.prodProdutoReferencia"), new DateFieldDef("comVenda.comVendaData"),
				new FloatFieldDef("comVendaProdutoQuantidade"), new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"),
				new FloatFieldDef("comVendaProdutoBruto"), new FloatFieldDef("comVendaProdutoDesconto"), new FloatFieldDef("comVendaProdutoLiquido"), new FloatFieldDef("comVendaProdutoTotalBruto"),
				new FloatFieldDef("comVendaProdutoTotalLiquido"), new IntegerFieldDef("comVendaProdutoEstoque"), new IntegerFieldDef("comVendaProdutoOrigem"),
				new FloatFieldDef("comVendaProdutoIcms"), new FloatFieldDef("comVendaProdutoIpi"), new IntegerFieldDef("comVendaProdutoOrdem") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comVendaProdutoId", 75, true);
		ColumnConfig ccVendaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtVenda(), "comVenda.comVendaId", 100, true);
		ccVendaId.setHidden(true);
		ColumnConfig ccProdId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoId", 100, true);
		ccProdId.setHidden(true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "comVenda.empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "comVenda.empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccCliente = new ColumnConfig(OpenSigCore.i18n.txtCliente(), "comVenda.empCliente.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccFornecedor = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "prodProduto.empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodProduto.prodProdutoBarra", 100, true);
		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);
		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 100, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comVenda.comVendaData", 100, true, DATAHORA);
		ColumnConfig ccEmbalagemId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 100, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);
		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);
		ColumnConfig ccEstoque = new ColumnConfig("", "comVendaProdutoEstoque", 0, false);
		ccEstoque.setHidden(true);
		ccEstoque.setFixed(true);
		ColumnConfig ccOrigem = new ColumnConfig("", "comVendaProdutoOrigem", 0, false);
		ccOrigem.setHidden(true);
		ccOrigem.setFixed(true);
		ColumnConfig ccIcms = new ColumnConfig(OpenSigCore.i18n.txtIcms(), "comVendaProdutoIcms", 75, true, PORCENTAGEM);
		ccIcms.setHidden(true);
		ColumnConfig ccIpi = new ColumnConfig(OpenSigCore.i18n.txtIpi(), "comVendaProdutoIpi", 75, true, PORCENTAGEM);
		ccIpi.setHidden(true);
		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto(), "comVendaProdutoDesconto", 50, true, PORCENTAGEM);
		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comVendaProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// somarotios
		SummaryColumnConfig ccQuantidade = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comVendaProdutoQuantidade", 50, true, NUMERO), NUMERO);
		SummaryColumnConfig ccBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comVendaProdutoBruto", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtLiquido(), "comVendaProdutoLiquido", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccTotalBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtTotal() + " " + OpenSigCore.i18n.txtBruto(),
				"comVendaProdutoTotalBruto", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccTatalLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtTotal() + " " + OpenSigCore.i18n.txtLiquido(),
				"comVendaProdutoTotalLiquido", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccVendaId, ccEmpresaId, ccEmpresa, ccCliente, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccData, ccQuantidade, ccEmbalagemId, ccEmbalagem, ccBruto,
				ccDesconto, ccLiquido, ccTotalBruto, ccTatalLiquido, ccEstoque, ccOrigem, ccIcms, ccIpi, ccOrdem };
		modelos = new ColumnModel(bcc);

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("comVenda.empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
			gf.add(fo, EJuncao.E);
		}

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("comVenda.sisUsuario", ECompara.IGUAL, new SisUsuario(Ponte.getLogin().getId()));
			gf.add(fo, EJuncao.E);
		}
		FiltroBinario fb1 = new FiltroBinario("comVenda.comVendaCancelada", ECompara.IGUAL, 0);
		gf.add(fb1, EJuncao.E);
		FiltroBinario fb2 = new FiltroBinario("comVenda.comVendaFechada", ECompara.IGUAL, 1);
		gf.add(fb2);

		filtroPadrao = gf;
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comVenda.comVendaData")) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("comVenda.empEmpresa.empEmpresaId")) {
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
		filtros.get("comVenda.comVendaData").setActive(false, true);
		filtros.get("comVenda.empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();
		
		// venda
		SisFuncao venda = UtilClient.getFuncaoPermitida(ComandoVenda.class);
		MenuItem itemVenda = gerarFuncao(venda, "comVendaId", "comVenda.comVendaId");
		if (itemVenda != null) {
			mnuContexto.addItem(itemVenda);
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