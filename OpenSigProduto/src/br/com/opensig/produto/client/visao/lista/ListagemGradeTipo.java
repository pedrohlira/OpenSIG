package br.com.opensig.produto.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.produto.shared.modelo.ProdGradeTipo;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;

public class ListagemGradeTipo extends AListagem<ProdGradeTipo> {

	public ListagemGradeTipo(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("prodGradeTipoId"), new StringFieldDef("prodGradeTipoNome"), new StringFieldDef("prodGradeTipoOpcao") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "prodGradeTipoId", 50, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "prodGradeTipoNome", 150, true);
		ColumnConfig ccOpcao = new ColumnConfig(OpenSigCore.i18n.txtOpcao(), "prodGradeTipoOpcao", 50, true);
		
		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNome, ccOpcao };
		modelos = new ColumnModel(bcc);
		super.inicializar();
	}
}
