package br.com.opensig.financeiro.client.visao.lista;

import java.util.Date;
import java.util.Map.Entry;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.IFavorito;
import br.com.opensig.financeiro.shared.modelo.FinCartaoAuditoria;

import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtextux.client.widgets.grid.plugins.GridDateFilter;
import com.gwtextux.client.widgets.grid.plugins.GridFilter;

public class ListagemCartaoAuditoria extends AListagem<FinCartaoAuditoria> {

	public ListagemCartaoAuditoria(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("finCartaoAuditoriaId"), new StringFieldDef("finCartaoPresente.finCartaoPresenteNumero"),
				new StringFieldDef("sisUsuario.sisUsuarioLogin"), new DateFieldDef("finCartaoAuditoriaData"), new StringFieldDef("finCartaoAuditoriaAcao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "finCartaoAuditoriaId", 50, true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "finCartaoPresente.finCartaoPresenteNumero", 100, true);
		ColumnConfig ccUsuario = new ColumnConfig(OpenSigCore.i18n.txtUsuario(), "sisUsuario.sisUsuarioLogin", 100, true);
		ColumnConfig ccData = new ColumnConfig(OpenSigCore.i18n.txtData(), "finCartaoAuditoriaData", 120, true, DATAHORA);
		ColumnConfig ccAcao = new ColumnConfig(OpenSigCore.i18n.txtAcao(), "finCartaoAuditoriaAcao", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNumero, ccUsuario, ccData, ccAcao };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
	
	public void setGridFiltro() {
		super.setGridFiltro();
		for (Entry<String, GridFilter> entry : filtros.entrySet()) {
			if (entry.getKey().equals("finCartaoAuditoriaData")) {
				((GridDateFilter) entry.getValue()).setValueOn(new Date());
				break;
			}
		}
	}

	public void setFavorito(IFavorito favorito) {
		filtros.get("finCartaoAuditoriaData").setActive(false, true);
		super.setFavorito(favorito);
	}
}
