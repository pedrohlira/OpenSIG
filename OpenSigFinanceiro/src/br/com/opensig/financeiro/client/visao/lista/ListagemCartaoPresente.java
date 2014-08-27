package br.com.opensig.financeiro.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.financeiro.shared.modelo.FinCartaoPresente;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemCartaoPresente extends AListagem<FinCartaoPresente> {

	public ListagemCartaoPresente(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("finCartaoPresenteId"), new StringFieldDef("finCartaoPresenteNumero"), new FloatFieldDef("finCartaoPresenteValor"),
				new BooleanFieldDef("finCartaoPresenteAtivo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "finCartaoPresenteId", 50, true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "finCartaoPresenteNumero", 100, true);
		ColumnConfig ccValor = new ColumnConfig(OpenSigCore.i18n.txtValor(), "finCartaoPresenteValor", 100, true, DINHEIRO);
		ColumnConfig ccAtivo = new ColumnConfig(OpenSigCore.i18n.txtAtivo(), "finCartaoPresenteAtivo", 50, true, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNumero, ccValor, ccAtivo };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
