package br.com.opensig.poker.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.poker.shared.modelo.PokerForma;

import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemForma extends AListagem<PokerForma> {

	public ListagemForma(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("pokerFormaId"), new StringFieldDef("pokerFormaNome"), new BooleanFieldDef("pokerFormaRealizado"),
				new BooleanFieldDef("pokerFormaJackpot") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "pokerFormaId", 50, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "pokerFormaNome", 200, true);
		ColumnConfig ccRealizado = new ColumnConfig(OpenSigCore.i18n.txtRealizado(), "pokerFormaRealizado", 75, true, BOLEANO);
		ColumnConfig ccJackpot = new ColumnConfig(OpenSigCore.i18n.txtJackpot(), "pokerFormaJackpot", 75, true, BOLEANO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNome, ccRealizado, ccJackpot };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}

}
