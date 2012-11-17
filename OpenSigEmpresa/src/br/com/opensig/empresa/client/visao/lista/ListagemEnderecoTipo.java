package br.com.opensig.empresa.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.empresa.shared.modelo.EmpEnderecoTipo;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemEnderecoTipo extends AListagem<EmpEnderecoTipo> {

	public ListagemEnderecoTipo(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("empEnderecoTipoId"), new StringFieldDef("empEnderecoTipoDescricao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empEnderecoTipoId", 50, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "empEnderecoTipoDescricao", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccDescricao };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
