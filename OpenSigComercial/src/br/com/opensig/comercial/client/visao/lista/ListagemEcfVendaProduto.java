package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.comercial.shared.modelo.ComEcfVendaProduto;
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

public class ListagemEcfVendaProduto extends AListagem<ComEcfVendaProduto> {

	public ListagemEcfVendaProduto(IFormulario<ComEcfVendaProduto> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comEcfVendaProdutoId"), new IntegerFieldDef("comEcfVenda.comEcfVendaId"), new IntegerFieldDef("comEcfVenda.comEcf.comEcfId"),
				new StringFieldDef("comEcfVenda.comEcf.comEcfSerie"), new IntegerFieldDef("comEcfVenda.comEcf.empEmpresa.empEmpresaId"),
				new StringFieldDef("comEcfVenda.comEcf.empEmpresa.empEntidade.empEntidadeNome1"), new StringFieldDef("prodProduto.empFornecedor.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("prodProduto.prodProdutoId"), new StringFieldDef("prodProduto.prodProdutoBarra"), new StringFieldDef("prodProduto.prodProdutoDescricao"),
				new StringFieldDef("prodProduto.prodProdutoReferencia"), new DateFieldDef("comEcfVenda.comEcfVendaData"), new FloatFieldDef("comEcfVendaProdutoQuantidade"),
				new IntegerFieldDef("prodEmbalagem.prodEmbalagemId"), new StringFieldDef("prodEmbalagem.prodEmbalagemNome"), new FloatFieldDef("comEcfVendaProdutoBruto"),
				new FloatFieldDef("comEcfVendaProdutoDesconto"), new FloatFieldDef("comEcfVendaProdutoAcrescimo"), new FloatFieldDef("comEcfVendaProdutoLiquido"),
				new FloatFieldDef("comEcfVendaProdutoTotal"), new BooleanFieldDef("comEcfVendaProdutoCancelado"), new IntegerFieldDef("comEcfVendaProdutoOrdem") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comEcfVendaProdutoId", 75, true);
		ColumnConfig ccVendaEcfId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtVenda(), "comEcfVenda.comEcfVendaId", 100, true);
		ccVendaEcfId.setHidden(true);
		ColumnConfig ccProdId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoId", 100, true);
		ccProdId.setHidden(true);
		ColumnConfig ccEcfId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEcf(), "comEcfVenda.comEcf.comEcfId", 100, false);
		ccEcfId.setHidden(true);
		ColumnConfig ccEcf = new ColumnConfig(OpenSigCore.i18n.txtEcf(), "comEcfVenda.comEcf.comEcfSerie", 200, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "comEcfVenda.comEcf.empEmpresa.empEmpresaId", 100, false);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "comEcfVenda.comEcf.empEmpresa.empEntidade.empEntidadeNome1", 200, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccFornecedor = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "prodProduto.empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodProduto.prodProdutoBarra", 100, true);
		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodProduto.prodProdutoDescricao", 250, true);
		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodProduto.prodProdutoReferencia", 100, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comEcfVenda.comEcfVendaData", 100, true, DATAHORA);
		ColumnConfig ccEmbalagemId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemId", 100, true);
		ccEmbalagemId.setHidden(true);
		ccEmbalagemId.setFixed(true);
		ColumnConfig ccEmbalagem = new ColumnConfig(OpenSigCore.i18n.txtEmbalagem(), "prodEmbalagem.prodEmbalagemNome", 75, true);
		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto(), "comEcfVendaProdutoDesconto", 50, true, PORCENTAGEM);
		ColumnConfig ccAcrescimo = new ColumnConfig(OpenSigCore.i18n.txtAcrescimo(), "comEcfVendaProdutoAcrescimo", 50, true, PORCENTAGEM);
		ColumnConfig ccCancelado = new ColumnConfig(OpenSigCore.i18n.txtCancelada(), "comEcfVendaProdutoCancelado", 75, true, BOLEANO);
		ColumnConfig ccOrdem = new ColumnConfig(OpenSigCore.i18n.txtOrdem(), "comVendaProdutoOrdem", 100, true);
		ccOrdem.setHidden(true);

		// somatorios
		SummaryColumnConfig ccQuantidade = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtQtd(), "comEcfVendaProdutoQuantidade", 50, true, NUMERO), NUMERO);
		SummaryColumnConfig ccBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comEcfVendaProdutoBruto", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtLiquido(), "comEcfVendaProdutoLiquido", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig ccTotal = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comEcfVendaProdutoTotal", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccVendaEcfId, ccEcfId, ccEcf, ccEmpresaId, ccEmpresa, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccData,
				ccQuantidade, ccEmbalagemId, ccEmbalagem, ccBruto, ccDesconto, ccAcrescimo, ccLiquido, ccTotal, ccCancelado, ccOrdem };
		modelos = new ColumnModel(bcc);

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("comEcfVenda.comEcf.empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
			gf.add(fo, EJuncao.E);
		}

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("comEcfVenda.sisUsuario", ECompara.IGUAL, new SisUsuario(Ponte.getLogin().getId()));
			gf.add(fo, EJuncao.E);
		}
		FiltroBinario fb1 = new FiltroBinario("comEcfVenda.comEcfVendaCancelada", ECompara.IGUAL, 0);
		gf.add(fb1, EJuncao.E);
		FiltroBinario fb2 = new FiltroBinario("comEcfVenda.comEcfVendaFechada", ECompara.IGUAL, 1);
		gf.add(fb2);

		filtroPadrao = gf;
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comEcfVenda.comEcfVendaData")) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("comEcfVenda.comEcf.empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("comEcfVenda.comEcf.empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("comEcfVenda.comEcf.empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
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
			} else if (entry.getKey().equals("comEcfVenda.comEcf.comEcfSerie")) {
				// ecf
				FieldDef[] fdEcf = new FieldDef[] { new IntegerFieldDef("comEcfId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
						new StringFieldDef("comEcfCodigo"), new StringFieldDef("comEcfMfAdicional"), new StringFieldDef("comEcfIdentificacao"), new StringFieldDef("comEcfTipo"),
						new StringFieldDef("comEcfMarca"), new StringFieldDef("comEcfModelo"), new StringFieldDef("comEcfSerie"), new IntegerFieldDef("comEcfCaixa") };
				CoreProxy<ComEcf> proxy = new CoreProxy<ComEcf>(new ComEcf());
				Store storeEcf = new Store(proxy, new ArrayReader(new RecordDef(fdEcf)), true);

				GridListFilter fEcf = new GridListFilter("comEcfVenda.comEcf.comEcfSerie", storeEcf);
				fEcf.setLabelField("comEcfSerie");
				fEcf.setLabelValue("comEcfSerie");
				fEcf.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEcf);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comEcfVenda.comEcfVendaData").setActive(false, true);
		filtros.get("comEcfVenda.comEcf.empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// venda
		SisFuncao venda = UtilClient.getFuncaoPermitida(ComandoEcfVenda.class);
		MenuItem itemVenda = gerarFuncao(venda, "comEcfVendaId", "comEcfVenda.comEcfVendaId");
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