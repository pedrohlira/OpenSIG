package br.com.opensig.produto.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.produto.shared.modelo.ProdIpi;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemIpi extends AListagem<ProdIpi> {

	public ListagemIpi(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodIpiId"), new StringFieldDef("prodIpiNome"), new StringFieldDef("prodIpiCstEntrada"), new StringFieldDef("prodIpiCstSaida"),
				new FloatFieldDef("prodIpiAliquota"), new StringFieldDef("prodIpiEnq"), new StringFieldDef("prodIpiDecreto") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodIpiId", 50, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "prodIpiNome", 300, true);
		ColumnConfig ccCstEntrada = new ColumnConfig(OpenSigCore.i18n.txtCst() + " " + OpenSigCore.i18n.txtEntrada(), "prodIpiCstEntrada", 75, true);
		ColumnConfig ccCstSaida = new ColumnConfig(OpenSigCore.i18n.txtCst() + " " + OpenSigCore.i18n.txtSaida(), "prodIpiCstSaida", 75, true);
		ColumnConfig ccAliquota = new ColumnConfig(OpenSigCore.i18n.txtAliquota(), "prodIpiAliquota", 75, true, PORCENTAGEM);
		ColumnConfig ccEnq = new ColumnConfig(OpenSigCore.i18n.txtEnq(), "prodIpiEnq", 100, true);
		ColumnConfig ccDecreto = new ColumnConfig(OpenSigCore.i18n.txtDecreto(), "prodIpiDecreto", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNome, ccCstEntrada, ccCstSaida, ccAliquota, ccEnq, ccDecreto };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
