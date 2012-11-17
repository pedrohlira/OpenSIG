package br.com.opensig.comercial.client.visao.lista;

import java.util.Map.Entry;

import br.com.opensig.comercial.client.controlador.comando.ComandoEcfDocumento;
import br.com.opensig.comercial.client.controlador.comando.ComandoEcfZ;
import br.com.opensig.comercial.shared.modelo.ComEcf;
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
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.grid.plugins.GridBooleanFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;
import com.gwtextux.client.widgets.grid.plugins.GridListFilter;
import com.gwtextux.client.widgets.grid.plugins.GridLongFilter;

public class ListagemEcf extends AListagem<ComEcf> {

	public ListagemEcf(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("comEcfId"), new IntegerFieldDef("empEmpresa.empEmpresaId"), new StringFieldDef("empEmpresa.empEntidade.empEntidadeNome1"),
				new StringFieldDef("comEcfCodigo"), new StringFieldDef("comEcfMfAdicional"), new StringFieldDef("comEcfIdentificacao"), new StringFieldDef("comEcfTipo"),
				new StringFieldDef("comEcfMarca"), new StringFieldDef("comEcfModelo"), new StringFieldDef("comEcfSerie"), new IntegerFieldDef("comEcfCaixa"), new BooleanFieldDef("comEcfAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "comEcfId", 50, true);
		ColumnConfig ccEmpresaId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEmpresaId", 100, false);
		ccEmpresaId.setHidden(true);
		ColumnConfig ccEmpresa = new ColumnConfig(OpenSigCore.i18n.txtEmpresa(), "empEmpresa.empEntidade.empEntidadeNome1", 200, true);
		ccEmpresa.setHidden(true);
		ColumnConfig ccCodigo = new ColumnConfig(OpenSigCore.i18n.txtCodigo(), "comEcfCodigo", 75, true);
		ColumnConfig ccMF = new ColumnConfig(OpenSigCore.i18n.txtMfAdicional(), "comEcfMfAdicional", 100, true);
		ColumnConfig ccIdent = new ColumnConfig(OpenSigCore.i18n.txtIdentificacao(), "comEcfIdentificacao", 100, true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "comEcfTipo", 100, true);
		ColumnConfig ccMarca = new ColumnConfig(OpenSigCore.i18n.txtMarca(), "comEcfMarca", 100, true);
		ColumnConfig ccModelo = new ColumnConfig(OpenSigCore.i18n.txtModelo(), "comEcfModelo", 150, true);
		ColumnConfig ccSerie = new ColumnConfig(OpenSigCore.i18n.txtSerie(), "comEcfSerie", 150, true);
		ColumnConfig ccCaixa = new ColumnConfig(OpenSigCore.i18n.txtCaixa(), "comEcfCaixa", 50, true);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "comEcfAtivo", 50, true, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccEmpresaId, ccEmpresa, ccCodigo, ccMF, ccIdent, ccTipo, ccMarca, ccModelo, ccSerie, ccCaixa, ccAtivo };
		modelos = new ColumnModel(bcc);

		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			filtroPadrao = new FiltroObjeto("empEmpresa", ECompara.IGUAL, new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		super.inicializar();
	}

	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("empEmpresa.empEmpresaId")) {
				((GridLongFilter) entry.getValue()).setValueEquals(Ponte.getLogin().getEmpresaId());
			} else if (entry.getKey().equals("comEcfAtivo")) {
				((GridBooleanFilter) entry.getValue()).setValue(true);
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
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("empEmpresa.empEmpresaId").setActive(false, true);
		filtros.get("comEcfAtivo").setActive(false, true);
		super.setFavorito(favorito);
	}
	
	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// z
		SisFuncao ecfZ = UtilClient.getFuncaoPermitida(ComandoEcfZ.class);
		MenuItem itemEcfZ = gerarFuncao(ecfZ, "comEcf.comEcfId", "comEcfId");
		if (itemEcfZ != null) {
			mnuContexto.addItem(itemEcfZ);
		}

		// docs
		SisFuncao doc = UtilClient.getFuncaoPermitida(ComandoEcfDocumento.class);
		MenuItem itemDoc = gerarFuncao(doc, "comEcf.comEcfId", "comEcfId");
		if (itemDoc != null) {
			mnuContexto.addItem(itemDoc);
		}
		
		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
