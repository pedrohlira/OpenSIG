package br.com.opensig.comercial.client.visao.lista;

import java.util.Date;
import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoEcfNotaProduto;
import br.com.opensig.comercial.shared.modelo.ComEcfNota;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.controlador.comando.ComandoCliente;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.financeiro.client.controlador.comando.ComandoReceber;

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

public class ListagemEcfNota extends AListagem<ComEcfNota> {

	public ListagemEcfNota(IFormulario<ComEcfNota> formulario) {
		super(formulario);
		inicializar();
		addPlugin(new GridSummaryPlugin());
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comEcfNotaId"), new IntegerFieldDef("empCliente.empClienteId"), new StringFieldDef("empCliente.empEntidade.empEntidadeNome1"),
				new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"), new IntegerFieldDef("comEcfNotaSerie"),
				new IntegerFieldDef("comEcfNotaSubserie"), new IntegerFieldDef("comEcfNotaNumero"), new DateFieldDef("comEcfNotaData"), new FloatFieldDef("comEcfNotaBruto"),
				new FloatFieldDef("comEcfNotaDesconto"), new FloatFieldDef("comEcfNotaLiquido"), new FloatFieldDef("comEcfNotaPis"), new FloatFieldDef("comEcfNotaCofins"),
				new IntegerFieldDef("finReceber.finReceberId"), new BooleanFieldDef("comEcfNotaCancelada") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comEcfNotaId", 75, true);
		ColumnConfig ccClienteId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtCliente(), "empCliente.empClienteId", 100, true);
		ccClienteId.setHidden(true);
		ColumnConfig ccCliente = new ColumnConfig(OpenSigCore.i18n.txtCliente(), "empCliente.empEntidade.empEntidadeNome1", 200, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, false);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 200, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccSerie = new ColumnConfig(OpenSigCore.i18n.txtSerie(), "comEcfNotaSerie", 75, true);
		ColumnConfig ccSubSerie = new ColumnConfig(OpenSigCore.i18n.txtSubSerie(), "comEcfNotaSubserie", 75, true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "comEcfNotaNumero", 75, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "comEcfNotaData", 75, true, DATA);
		ColumnConfig ccDesconto = new ColumnConfig(OpenSigCore.i18n.txtDesconto(), "comEcfNotaDesconto", 75, true, PORCENTAGEM);
		ColumnConfig ccReceberId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtReceber(), "finReceber.finReceberId", 100, true);
		ccReceberId.setHidden(true);
		ColumnConfig ccCancelada = new ColumnConfig(OpenSigCore.i18n.txtCancelada(), "comEcfNotaCancelada", 75, true, BOLEANO);

		// sumarios
		SummaryColumnConfig sumBruto = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comEcfNotaBruto", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumLiquido = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtLiquido(), "comEcfNotaLiquido", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumPis = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtPis(), "comEcfNotaPis", 75, true, DINHEIRO), DINHEIRO);
		SummaryColumnConfig sumCofins = new SummaryColumnConfig(SummaryColumnConfig.SUM, new ColumnConfig(OpenSigCore.i18n.txtCofins(), "comEcfNotaCofins", 75, true, DINHEIRO), DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccClienteId, ccCliente, ccEmpresaId, ccEmpresa, ccSerie, ccSubSerie, ccNumero, ccData, sumBruto, ccDesconto, sumLiquido, sumPis,
				sumCofins, ccReceberId, ccCancelada };
		modelos = new ColumnModel(bcc);

		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
			gf.add(fo, EJuncao.E);
		}

		filtroPadrao = gf.size() > 0 ? gf : null;
		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comEcfNotaData")) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
			} else if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresa.empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comEcfNotaData").setActive(false, true);
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// cliente
		SisFuncao cliente = UtilClient.getFuncaoPermitida(ComandoCliente.class);
		MenuItem itemCliente = gerarFuncao(cliente, "empClienteId", "empCliente.empClienteId");
		if (itemCliente != null) {
			mnuContexto.addItem(itemCliente);
		}
		
		// produtos nota
		SisFuncao produto = UtilClient.getFuncaoPermitida(ComandoEcfNotaProduto.class);
		MenuItem itemProduto = gerarFuncao(produto, "comEcfNota.comEcfNotaId", "comEcfNotaId");
		if (itemProduto != null) {
			mnuContexto.addItem(itemProduto);
		}
		
		// receber
		SisFuncao receber = UtilClient.getFuncaoPermitida(ComandoReceber.class);
		MenuItem itemReceber = gerarFuncao(receber, "finReceberId", "finReceber.finReceberId");
		if (itemReceber != null) {
			mnuContexto.addItem(itemReceber);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
