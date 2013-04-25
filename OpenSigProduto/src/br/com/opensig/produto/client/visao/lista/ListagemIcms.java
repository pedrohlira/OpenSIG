package br.com.opensig.produto.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.produto.shared.modelo.ProdIcms;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemIcms extends AListagem<ProdIcms> {

	public ListagemIcms(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodIcmsId"), new StringFieldDef("prodIcmsNome"), new StringFieldDef("prodIcmsCst"), new StringFieldDef("prodIcmsCson"),
				new IntegerFieldDef("prodIcmsCfop"), new StringFieldDef("prodIcmsEcf"), new FloatFieldDef("prodIcmsDentro"), new FloatFieldDef("prodIcmsFora"), new StringFieldDef("prodIcmsDecreto"), };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodIcmsId", 50, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "prodIcmsNome", 300, true);
		ColumnConfig ccCst = new ColumnConfig(OpenSigCore.i18n.txtCst(), "prodIcmsCst", 50, true);
		ColumnConfig ccCson = new ColumnConfig(OpenSigCore.i18n.txtCson(), "prodIcmsCson", 50, true);
		ColumnConfig ccCfop = new ColumnConfig(OpenSigCore.i18n.txtCfop(), "prodIcmsCfop", 50, true);
		ColumnConfig ccEcf = new ColumnConfig(OpenSigCore.i18n.txtEcf(), "prodIcmsEcf", 50, true);
		ColumnConfig ccDentro = new ColumnConfig(OpenSigCore.i18n.txtDentro(), "prodIcmsDentro", 50, true, PORCENTAGEM);
		ColumnConfig ccFora = new ColumnConfig(OpenSigCore.i18n.txtFora(), "prodIcmsFora", 50, true, PORCENTAGEM);
		ColumnConfig ccDecreto = new ColumnConfig(OpenSigCore.i18n.txtDecreto(), "prodIcmsDecreto", 200, true);

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNome, ccCst, ccCson, ccCfop, ccEcf, ccDentro, ccFora, ccDecreto };
		modelos = new ColumnModel(bcc);

		super.inicializar();
	}
}
