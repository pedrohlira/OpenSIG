package br.com.opensig.produto.client.visao.lista;

import java.util.Map.Entry;

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
import br.com.opensig.produto.shared.modelo.ProdEstoqueGrade;

import com.gwtext.client.data.ArrayReader;
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

public class ListagemGrade extends AListagem<ProdEstoqueGrade> {

	public ListagemGrade(IFormulario<ProdEstoqueGrade> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodEstoqueGradeId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("prodGrade.prodProduto.empFornecedor.empEntidade.empEntidadeNome1"), new IntegerFieldDef("prodGrade.prodProduto.prodProdutoId"),
				new StringFieldDef("prodGrade.prodGradeBarra"), new StringFieldDef("prodGrade.prodProduto.prodProdutoDescricao"), new StringFieldDef("prodGrade.prodProduto.prodProdutoReferencia"),
				new StringFieldDef("prodGrade.prodGradeTamanho"), new StringFieldDef("prodGrade.prodGradeCor"), new StringFieldDef("prodGrade.prodGradeOpcao"),
				new FloatFieldDef("prodEstoqueGradeQuantidade") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodEstoqueGradeId", 75, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, true);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 100, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccFornecedor = new ColumnConfig(OpenSigCore.i18n.txtFornecedor(), "prodGrade.prodProduto.empFornecedor.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccProdId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtProduto(), "prodGrade.prodProduto.prodProdutoId", 100, true);
		ccProdId.setHidden(true);
		ColumnConfig ccBarra = new ColumnConfig(OpenSigCore.i18n.txtBarra(), "prodGrade.prodGradeBarra", 100, true);
		ColumnConfig ccProduto = new ColumnConfig(OpenSigCore.i18n.txtProduto(), "prodGrade.prodProduto.prodProdutoDescricao", 250, true);
		ColumnConfig ccReferencia = new ColumnConfig(OpenSigCore.i18n.txtRef(), "prodGrade.prodProduto.prodProdutoReferencia", 100, true);
		ColumnConfig ccTamanho = new ColumnConfig(OpenSigCore.i18n.txtTamanho(), "prodGrade.prodGradeTamanho", 100, true);
		ColumnConfig ccCor = new ColumnConfig(OpenSigCore.i18n.txtCor(), "prodGrade.prodGradeCor", 100, true);
		ColumnConfig ccOpcao = new ColumnConfig(OpenSigCore.i18n.txtOpcao(), "prodGrade.prodGradeOpcao", 100, true);
		
		// somarotios
		SummaryColumnConfig ccEstoque = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtEstoque(), "prodEstoqueGradeQuantidade", 100, true, NUMERO), NUMERO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresaId, ccEmpresa, ccFornecedor, ccProdId, ccBarra, ccProduto, ccReferencia, ccTamanho, ccCor, ccOpcao, ccEstoque };
		modelos = new ColumnModel(bcc);

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
			gf.add(fo, EJuncao.E);
		}
		FiltroBinario fb = new FiltroBinario("prodGrade.prodProduto.prodProdutoAtivo", ECompara.IGUAL, 1);
		gf.add(fb);
		
		filtroPadrao = gf;
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("empEmpresa.empEntidade.empEntidadeNome1")) {
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

			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// produto
		SisFuncao produto = UtilClient.getFuncaoPermitida(ComandoProduto.class);
		MenuItem itemProduto = gerarFuncao(produto, "prodProdutoId", "prodGrade.prodProduto.prodProdutoId");
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