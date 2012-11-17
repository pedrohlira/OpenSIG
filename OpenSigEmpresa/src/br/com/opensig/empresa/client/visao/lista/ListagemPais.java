package br.com.opensig.empresa.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.empresa.shared.modelo.EmpPais;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemPais extends AListagem<EmpPais> {

	public ListagemPais(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("empPaisId"), new IntegerFieldDef("empPaisIbge"), new StringFieldDef("empPaisDescricao"), new StringFieldDef("empPaisSigla") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empPaisId", 50, true);
		ColumnConfig ccIbge = new ColumnConfig(OpenSigCore.i18n.txtIbge(), "empPaisIbge", 50, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtNome(), "empPaisDescricao", 200, true);
		ColumnConfig ccSigla = new ColumnConfig(OpenSigCore.i18n.txtSigla(), "empPaisSigla", 50, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccIbge, ccDescricao, ccSigla };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
