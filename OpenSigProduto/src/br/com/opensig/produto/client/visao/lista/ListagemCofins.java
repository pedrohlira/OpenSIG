package br.com.opensig.produto.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.produto.shared.modelo.ProdCofins;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemCofins extends AListagem<ProdCofins> {

	public ListagemCofins(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodCofinsId"), new StringFieldDef("prodCofinsNome"), new StringFieldDef("prodCofinsCstEntrada"), new StringFieldDef("prodCofinsCstSaida"),
				new FloatFieldDef("prodCofinsAliquota"), new StringFieldDef("prodCofinsDecreto") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodCofinsId", 50, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "prodCofinsNome", 300, true);
		ColumnConfig ccCstEntrada = new ColumnConfig(OpenSigCore.i18n.txtCst() + " " + OpenSigCore.i18n.txtEntrada(), "prodCofinsCstEntrada", 75, true);
		ColumnConfig ccCstSaida = new ColumnConfig(OpenSigCore.i18n.txtCst() + " " + OpenSigCore.i18n.txtSaida(), "prodCofinsCstSaida", 75, true);
		ColumnConfig ccAliquota = new ColumnConfig(OpenSigCore.i18n.txtAliquota(), "prodCofinsAliquota", 75, true, PORCENTAGEM);
		ColumnConfig ccDecreto = new ColumnConfig(OpenSigCore.i18n.txtDecreto(), "prodCofinsDecreto", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNome, ccCstEntrada, ccCstSaida, ccAliquota, ccDecreto };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
