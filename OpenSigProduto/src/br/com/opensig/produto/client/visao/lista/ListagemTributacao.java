package br.com.opensig.produto.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemTributacao extends AListagem<ProdTributacao> {

	public ListagemTributacao(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodTributacaoId"), new StringFieldDef("prodTributacaoNome"), new StringFieldDef("prodTributacaoCst"),
				new StringFieldDef("prodTributacaoCson"), new IntegerFieldDef("prodTributacaoCfop"), new StringFieldDef("prodTributacaoEcf"), new FloatFieldDef("prodTributacaoDentro"),
				new FloatFieldDef("prodTributacaoFora"), new StringFieldDef("prodTributacaoDecreto"), };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodTributacaoId", 50, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "prodTributacaoNome", 300, true);
		ColumnConfig ccCst = new ColumnConfig(OpenSigCore.i18n.txtCst(), "prodTributacaoCst", 50, true);
		ColumnConfig ccCson = new ColumnConfig(OpenSigCore.i18n.txtCson(), "prodTributacaoCson", 50, true);
		ColumnConfig ccCfop = new ColumnConfig(OpenSigCore.i18n.txtCfop(), "prodTributacaoCfop", 50, true);
		ColumnConfig ccEcf = new ColumnConfig(OpenSigCore.i18n.txtEcf(), "prodTributacaoEcf", 50, true);
		ColumnConfig ccDentro = new ColumnConfig(OpenSigCore.i18n.txtDentro(), "prodTributacaoDentro", 50, true, PORCENTAGEM);
		ColumnConfig ccFora = new ColumnConfig(OpenSigCore.i18n.txtFora(), "prodTributacaoFora", 50, true, PORCENTAGEM);
		ColumnConfig ccDecreto = new ColumnConfig(OpenSigCore.i18n.txtDecreto(), "prodTributacaoDecreto", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNome, ccCst, ccCson, ccCfop, ccEcf, ccDentro, ccFora, ccDecreto };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
