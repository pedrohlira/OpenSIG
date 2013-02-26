package br.com.opensig.financeiro.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.financeiro.shared.modelo.FinConta;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemConta extends AListagem<FinConta> {

	public ListagemConta(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("finContaId"), new IntegerFieldDef("finBanco.finBancoId"), new StringFieldDef("finBanco.finBancoDescricao"),
				new StringFieldDef("finContaNome"), new StringFieldDef("finContaNumero"), new StringFieldDef("finContaAgencia"), new StringFieldDef("finContaCarteira"),
				new StringFieldDef("finContaConvenio"), new FloatFieldDef("finContaSaldo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "finContaId", 50, true);
		ColumnConfig ccBancoId = new ColumnConfig(OpenSigCore.i18n.txtCod() + " - " + OpenSigCore.i18n.txtBanco(), "finBanco.finBancoId", 100, true);
		ccBancoId.setHidden(true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtBanco(), "finBanco.finBancoDescricao", 200, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "finContaNome", 100, true);
		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "finContaNumero", 75, true);
		ColumnConfig ccAgencia = new ColumnConfig(OpenSigCore.i18n.txtAgencia(), "finContaAgencia", 75, true);
		ColumnConfig ccCarteira = new ColumnConfig(OpenSigCore.i18n.txtCarteira(), "finContaCarteira", 75, true);
		ColumnConfig ccConvencio = new ColumnConfig(OpenSigCore.i18n.txtConvenio(), "finContaConvenio", 75, true);
		ColumnConfig ccSaldo = new ColumnConfig(OpenSigCore.i18n.txtSaldo(), "finContaSaldo", 100, true, DINHEIRO);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccBancoId, ccDescricao, ccNome, ccNumero, ccAgencia, ccCarteira, ccConvencio, ccSaldo };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}

}
