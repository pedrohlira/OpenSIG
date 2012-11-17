package br.com.opensig.comercial.client.visao.lista;

import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoEcf;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfVenda;
import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.comercial.shared.modelo.ComEcfZ;
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
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;

public class ListagemEcfZ extends AListagem<ComEcfZ> {

	public ListagemEcfZ(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comEcfZId"), new IntegerFieldDef("comEcf.comEcfId"), new StringFieldDef("comEcf.comEcfSerie"),
				new IntegerFieldDef("comEcf.empEmpresa.empEmpresaId"), new StringFieldDef("comEcf.empEmpresa.empEntidade.empEntidadeNome1"), new IntegerFieldDef("comEcfZUsuario"),
				new IntegerFieldDef("comEcfZCooIni"), new IntegerFieldDef("comEcfZCooFin"), new IntegerFieldDef("comEcfZCro"), new IntegerFieldDef("comEcfZCrz"), new DateFieldDef("comEcfZMovimento"),
				new DateFieldDef("comEcfZEmissao"), new FloatFieldDef("comEcfZBruto"), new FloatFieldDef("comEcfZGt"), new BooleanFieldDef("comEcfZIssqn") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comEcfZId", 50, true);
		ColumnConfig ccEcfId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEcf(), "comEcf.comEcfId", 100, false);
		ccEcfId.setHidden(true);
		ColumnConfig ccEcf = new ColumnConfig(OpenSigCore.i18n.txtEcf(), "comEcf.comEcfSerie", 200, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "comEcf.empEmpresa.empEmpresaId", 100, false);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "comEcf.empEmpresa.empEntidade.empEntidadeNome1", 200, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccUsuario = new ColumnConfig("", "comEcfZUsuario", 10, true);
		ccUsuario.setHidden(true);
		ccUsuario.setFixed(true);
		ColumnConfig ccCooIni = new ColumnConfig(OpenSigCore.i18n.txtCoo() + " " + OpenSigCore.i18n.txtInicio(), "comEcfZCooIni", 75, true);
		ColumnConfig ccCooFin = new ColumnConfig(OpenSigCore.i18n.txtCoo() + " " + OpenSigCore.i18n.txtFim(), "comEcfZCooFin", 75, true);
		ColumnConfig ccCro = new ColumnConfig(OpenSigCore.i18n.txtCro(), "comEcfZCro", 75, true);
		ColumnConfig ccCrz = new ColumnConfig(OpenSigCore.i18n.txtCrz(), "comEcfZCrz", 75, true);
		ColumnConfig ccMovimento = new ColumnConfig(OpenSigCore.i18n.txtData(), "comEcfZMovimento", 80, true, DATA);
		ColumnConfig ccEmissao = new ColumnConfig(OpenSigCore.i18n.txtEmissao(), "comEcfZEmissao", 120, true, DATAHORA);
		ColumnConfig ccBruto = new ColumnConfig(OpenSigCore.i18n.txtBruto(), "comEcfZBruto", 100, true, DINHEIRO);
		ColumnConfig ccGt = new ColumnConfig(OpenSigCore.i18n.txtTotal(), "comEcfZGt", 100, true, DINHEIRO);
		ColumnConfig ccIssqn = new ColumnConfig(OpenSigCore.i18n.txtIssqn(), "comEcfZIssqn", 50, true, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEcfId, ccEcf, ccEmpresaId, ccEmpresa, ccUsuario, ccCooIni, ccCooFin, ccCro, ccCrz, ccMovimento, ccEmissao, ccBruto, ccGt, ccIssqn };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("comEcf.empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("comEcf.empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("comEcf.empEmpresa.empEntidade.empEntidadeNome1")) {
				// empresa
				FiltroNumero fn = null;
				if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
					fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
				}

				FieldDef[] fdEmpresa = new FieldDef[] { new IntegerFieldDef("empEmpresaId"), new IntegerFieldDef("empEntidade.empEntidadeId"), new StringFieldDef("empEntidade.empEntidadeNome1") };
				CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa(), fn);
				Store storeEmpresa = new Store(proxy, new ArrayReader(new RecordDef(fdEmpresa)), true);

				GridListFilter fEmpresa = new GridListFilter("comEcf.empEmpresa.empEntidade.empEntidadeNome1", storeEmpresa);
				fEmpresa.setLabelField("empEntidade.empEntidadeNome1");
				fEmpresa.setLabelValue("empEntidade.empEntidadeNome1");
				fEmpresa.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEmpresa);
			} else if (entry.getKey().equals("comEcf.comEcfSerie")) {
				// ecf
				FieldDef[] fdEcf = new FieldDef[] { new IntegerFieldDef("comEcfId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
						new StringFieldDef("comEcfCodigo"), new StringFieldDef("comEcfMfAdicional"), new StringFieldDef("comEcfIdentificacao"), new StringFieldDef("comEcfTipo"),
						new StringFieldDef("comEcfMarca"), new StringFieldDef("comEcfModelo"), new StringFieldDef("comEcfSerie"), new IntegerFieldDef("comEcfCaixa") };
				CoreProxy<ComEcf> proxy = new CoreProxy<ComEcf>(new ComEcf());
				Store storeEcf = new Store(proxy, new ArrayReader(new RecordDef(fdEcf)), true);

				GridListFilter fEcf = new GridListFilter("comEcf.comEcfSerie", storeEcf);
				fEcf.setLabelField("comEcfSerie");
				fEcf.setLabelValue("comEcfSerie");
				fEcf.setLoadingText(OpenSigCore.i18n.txtAguarde());
				entry.setValue(fEcf);
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("comEcf.empEmpresa.empEmpresaId").setActive(false, true);
		super.setFavorito(favorito);
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// Ecf
		SisFuncao ecf = UtilClient.getFuncaoPermitida(ComandoEcf.class);
		MenuItem itemEcf = gerarFuncao(ecf, "comEcfId", "comEcf.comEcfId");
		if (itemEcf != null) {
			mnuContexto.addItem(itemEcf);
		}
		
		// vendas
		SisFuncao venda = UtilClient.getFuncaoPermitida(ComandoEcfVenda.class);
		MenuItem itemVenda = gerarFuncao(venda, "comEcfZ.comEcfZId", "comEcfZId");
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
