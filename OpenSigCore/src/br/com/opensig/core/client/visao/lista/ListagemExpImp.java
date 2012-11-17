package br.com.opensig.core.client.visao.lista;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AListagem;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisExpImp;

import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.grid.plugins.ColumnWithCellActionsConfig;
import com.gwtextux.client.widgets.grid.plugins.GridCellAction;
import com.gwtextux.client.widgets.grid.plugins.GridCellActionListener;
import com.gwtextux.client.widgets.grid.plugins.GridCellActionsPlugin;

public class ListagemExpImp extends AListagem<SisExpImp> {

	public ListagemExpImp(IFormulario formulario) {
		super(formulario);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("sisExpImpId"), new StringFieldDef("sisExpImpNome"), new StringFieldDef("sisExpImpExtensoes"), new StringFieldDef("sisExpImpDescricao"),
				new StringFieldDef("sisExpImpImagem"), new StringFieldDef("sisExpImpClasse"), new StringFieldDef("sisExpImpFuncao"), new StringFieldDef("sisExpImpTipo"),
				new StringFieldDef("sisExpImpModelo") };
		campos = new RecordDef(fd);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "sisExpImpId", 50, true);
		ColumnConfig ccNome = new ColumnConfig(OpenSigCore.i18n.txtNome(), "sisExpImpNome", 100, true);
		ColumnConfig ccExtensao = new ColumnConfig(OpenSigCore.i18n.txtExtensao(), "sisExpImpExtensoes", 100, true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "sisExpImpDescricao", 300, true);
		ColumnConfig ccImagem = new ColumnConfig(OpenSigCore.i18n.txtImagem(), "sisExpImpImagem", 75, true);
		ColumnConfig ccClasse = new ColumnConfig(OpenSigCore.i18n.txtClasse(), "sisExpImpClasse", 300, true);
		ColumnConfig ccFuncao = new ColumnConfig(OpenSigCore.i18n.txtFuncao(), "sisExpImpFuncao", 300, true);
		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "sisExpImpTipo", 50, true);

		GridCellAction cellModelo = new GridCellAction("icon-padrao", OpenSigCore.i18n.txtModelo(), new GridCellActionListener() {
			public boolean execute(GridPanel grid, Record record, String action, Object value, String dataIndex, int rowIndex, int colIndex) {
				TextArea ta = new TextArea();
				ta.setReadOnly(true);
				ta.setValue(record.getAsString("sisExpImpModelo"));

				Window wnd = new Window(OpenSigCore.i18n.txtModelo(), 600, 400, true, true);
				wnd.setLayout(new FitLayout());
				wnd.setIconCls("icon-padrao");
				wnd.add(ta);
				wnd.doLayout();
				wnd.show();
				return true;
			}
		});
		ColumnWithCellActionsConfig ccModelo = new ColumnWithCellActionsConfig(OpenSigCore.i18n.txtModelo(), "sisExpImpTipo", 75);
		ccModelo.setMenuDisabled(true);
		ccModelo.setCellActions(new GridCellAction[] { cellModelo });

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccNome, ccExtensao, ccDescricao, ccImagem, ccClasse, ccFuncao, ccTipo, ccModelo };
		modelos = new ColumnModel(bcc);

		addPlugin(new GridCellActionsPlugin("left", null));
		super.inicializar();
	}
}
