package br.com.opensig.comercial.client.visao.lista;

import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoCompra;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfVenda;
import br.com.opensig.comercial.client.controlador.comando.ComandoTrocaProduto;
import br.com.opensig.comercial.shared.modelo.ComTroca;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoEditar;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridBooleanFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;
import com.gwtextux.client.widgets.grid.plugins.GridSummaryPlugin;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

public class ListagemTroca extends AListagem<ComTroca> {

	public ListagemTroca(IFormulario<ComTroca> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comTrocaId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("comCompra.comCompraId"), new DateFieldDef("comTrocaData"), new StringFieldDef("comTrocaCliente"), new FloatFieldDef("comTrocaValor"),
				new IntegerFieldDef("comTrocaEcf"), new IntegerFieldDef("comTrocaCoo"), new BooleanFieldDef("comTrocaAtivo") };
		campos = new RecordDef(fd);

		// selected
		CheckboxSelectionModel model = new CheckboxSelectionModel();
		CheckboxColumnConfig check = new CheckboxColumnConfig(model);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comTrocaId", 75, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, false);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 200, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccCompraId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtCompra(), "comCompra.comCompraId", 100, true);
		ccCompraId.setHidden(true);
		ccCompraId.setFixed(true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comTrocaData", 120, true, DATAHORA);
		ColumnConfig ccCliente = new ColumnConfig(OpenSigCore.i18n.txtEntidadeDoc1(), "comTrocaCliente", 150, true);
		ColumnConfig ccEcf = new ColumnConfig(OpenSigCore.i18n.txtEcf(), "comTrocaEcf", 75, true, NUMERO);
		ColumnConfig ccCoo = new ColumnConfig(OpenSigCore.i18n.txtCoo(), "comTrocaCoo", 75, true, NUMERO);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "comTrocaAtivo", 75, true, BOLEANO);

		// sumarios
		SummaryColumnConfig sumValor = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtValor(), "comTrocaValor", 100, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { check, ccId, ccEmpresaId, ccEmpresa, ccCompraId, ccData, ccCliente, sumValor, ccEcf, ccCoo, ccAtivo };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
		setSelectionModel(model);
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		final Record rec = getSelectionModel().getSelected();
		// valida se pode editar ou excluir
		if (comando instanceof ComandoEditar || comando instanceof ComandoExcluir) {
			if (rec != null && !rec.getAsBoolean("comTrocaAtivo")) {
				MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				comando = null;
			}
		}
		return comando;
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

				GridListFilter fEmpresa = new GridListFilter("empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);
			} else if (entry.getKey().equals("comTrocaAtivo")) {
				((GridBooleanFilter) entry.getValue()).setValue(true);
				entry.getValue().setActive(true, true);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comTrocaAtivo").setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// produtos troca
		SisFuncao produto = UtilClient.getFuncaoPermitida(ComandoTrocaProduto.class);
		MenuItem itemProduto = gerarFuncao(produto, "comTroca.comTrocaId", "comTrocaId");
		if (itemProduto != null) {
			mnuContexto.addItem(itemProduto);
		}

		// compra
		SisFuncao compra = UtilClient.getFuncaoPermitida(ComandoCompra.class);
		MenuItem itemCompra = gerarFuncao(compra, "comCompraId", "comCompra.comCompraId");
		if (itemCompra != null) {
			mnuContexto.addItem(itemCompra);
		}

		// ecf venda
		SisFuncao venda = UtilClient.getFuncaoPermitida(ComandoEcfVenda.class);
		MenuItem itemVenda = gerarFuncao(venda, "comTroca.comTrocaId", "comTrocaId");
		if (itemVenda != null) {
			mnuContexto.addItem(itemVenda);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
