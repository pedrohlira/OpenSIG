package br.com.opensig.produto.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.produto.shared.modelo.ProdTipo;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemTipo extends AListagem<ProdTipo> {

	public ListagemTipo(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodTipoId"), new StringFieldDef("prodTipoValor"), new StringFieldDef("prodTipoDescricao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodTipoId", 50, true);
		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "prodTipoValor", 100, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "prodTipoDescricao", 400, true);
		
		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccValor, ccDescricao };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
