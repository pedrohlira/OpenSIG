package br.com.opensig.produto.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.produto.shared.modelo.ProdOrigem;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemOrigem extends AListagem<ProdOrigem> {

	public ListagemOrigem(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodOrigemId"), new IntegerFieldDef("prodOrigemValor"), new StringFieldDef("prodOrigemDescricao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodOrigemId", 50, true);
		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "prodOrigemValor", 60, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "prodOrigemDescricao", 500, true);
		
		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccValor, ccDescricao };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
