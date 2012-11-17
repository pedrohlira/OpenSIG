package br.com.opensig.empresa.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.empresa.shared.modelo.EmpEstado;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemEstado extends AListagem<EmpEstado> {

	public ListagemEstado(IFormulario<EmpEstado> formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("empEstadoId"), new IntegerFieldDef("empEstadoIbge"), new StringFieldDef("empEstadoDescricao"), new StringFieldDef("empEstadoSigla"),
				new IntegerFieldDef("empPais.empPaisId"), new StringFieldDef("empPais.empPaisDescricao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empEstadoId", 50, true);
		ColumnConfig ccPais = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtPais(), "empPais.empPaisId", 100, true);
		ccPais.setHidden(true);
		ColumnConfig ccIbge = new ColumnConfig(OpenSigCore.i18n.txtIbge(), "empEstadoIbge", 50, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtNome(), "empEstadoDescricao", 200, true);
		ColumnConfig ccSigla = new ColumnConfig(OpenSigCore.i18n.txtSigla(), "empEstadoSigla", 50, true);
		ColumnConfig ccPaisDescricao = new ColumnConfig(OpenSigCore.i18n.txtPais(), "empPais.empPaisDescricao", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccIbge, ccDescricao, ccSigla, ccPais, ccPaisDescricao };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
