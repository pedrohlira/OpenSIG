package br.com.opensig.financeiro.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.financeiro.shared.modelo.FinForma;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemForma extends AListagem<FinForma> {

	public ListagemForma(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("finFormaId"), new StringFieldDef("finFormaDescricao"), new StringFieldDef("finFormaCodigo"), new BooleanFieldDef("finFormaTef"),
				new BooleanFieldDef("finFormaVinculado"), new BooleanFieldDef("finFormaDebito"), new StringFieldDef("finFormaRede"), new BooleanFieldDef("finFormaPagar"),
				new BooleanFieldDef("finFormaReceber") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "finFormaId", 50, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "finFormaDescricao", 200, true);
		ColumnConfig ccCodigo = new ColumnConfig(OpenSigCore.i18n.txtCodigo(), "finFormaCodigo", 75, true);
		ColumnConfig ccTef = new ColumnConfig(OpenSigCore.i18n.txtTef(), "finFormaTef", 75, true, BOLEANO);
		ColumnConfig ccVinculado = new ColumnConfig(OpenSigCore.i18n.txtVinculado(), "finFormaVinculado", 75, true, BOLEANO);
		ColumnConfig ccDebito = new ColumnConfig(OpenSigCore.i18n.txtDebito(), "finFormaDebito", 75, true, BOLEANO);
		ColumnConfig ccRede = new ColumnConfig(OpenSigCore.i18n.txtRede(), "finFormaRede", 75, true);
		ColumnConfig ccPagar = new ColumnConfig(OpenSigCore.i18n.txtPagar(), "finFormaPagar", 75, true, BOLEANO);
		ColumnConfig ccReceber = new ColumnConfig(OpenSigCore.i18n.txtReceber(), "finFormaReceber", 75, true, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccDescricao, ccCodigo, ccTef, ccVinculado, ccDebito, ccRede, ccPagar, ccReceber };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}
}
