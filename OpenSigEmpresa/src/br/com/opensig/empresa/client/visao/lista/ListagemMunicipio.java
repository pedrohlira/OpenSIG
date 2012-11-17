package br.com.opensig.empresa.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemMunicipio extends AListagem<EmpMunicipio> {

	public ListagemMunicipio(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("empMunicipioId"), new IntegerFieldDef("empMunicipioIbge"), new StringFieldDef("empMunicipioDescricao"),
				new IntegerFieldDef("empEstado.empEstadoId"), new StringFieldDef("empEstado.empEstadoDescricao"), new IntegerFieldDef("empEstado.empPais.empPaisId"),
				new StringFieldDef("empEstado.empPais.empPaisDescricao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empMunicipioId", 50, true);
		ColumnConfig ccEstado = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtEstado(), "empEstado.empEstadoId", 100, true);
		ccEstado.setHidden(true);
		ColumnConfig ccPais = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtPais(), "empEstado.empPais.empPaisId", 100, true);
		ccPais.setHidden(true);
		ColumnConfig ccIbge = new ColumnConfig(OpenSigCore.i18n.txtIbge(), "empMunicipioIbge", 100, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtMunicipio(), "empMunicipioDescricao", 200, true);
		ColumnConfig ccEstadoDescricao = new ColumnConfig(OpenSigCore.i18n.txtEstado(), "empEstado.empEstadoDescricao", 200, true);
		ColumnConfig ccPaisDescricao = new ColumnConfig(OpenSigCore.i18n.txtPais(), "empEstado.empPais.empPaisDescricao", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccIbge, ccDescricao, ccEstado, ccEstadoDescricao, ccPais, ccPaisDescricao };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
