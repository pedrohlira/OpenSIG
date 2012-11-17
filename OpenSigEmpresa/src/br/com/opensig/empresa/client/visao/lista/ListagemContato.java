package br.com.opensig.empresa.client.visao.lista;

import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpContatoTipo;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;

public class ListagemContato extends AListagemEditor<EmpContato> {

	private ComboBox cmbTipo;
	private TextField txtDescricao;
	private TextField txtPessoa;
	private TextField txtTipo;

	public ListagemContato(boolean barraTarefa) {
		super(new EmpContato(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("empContatoId"), new IntegerFieldDef("empContatoTipoId"), new StringFieldDef("empContatoTipoDescricao"),
				new StringFieldDef("empContatoDescricao"), new StringFieldDef("empContatoPessoa") };
		campos = new RecordDef(fd);

		// editores
		FieldDef[] fdTipo = new FieldDef[] { new IntegerFieldDef("empContatoTipoId"), new StringFieldDef("empContatoTipoDescricao") };
		CoreProxy<EmpContatoTipo> proxy = new CoreProxy<EmpContatoTipo>(new EmpContatoTipo());
		final Store storeTipo = new Store(proxy, new ArrayReader(new RecordDef(fdTipo)), false);
		storeTipo.load();

		cmbTipo = new ComboBox();
		cmbTipo.setWidth(100);
		cmbTipo.setAllowBlank(false);
		cmbTipo.setStore(storeTipo);
		cmbTipo.setTriggerAction(ComboBox.ALL);
		cmbTipo.setMode(ComboBox.LOCAL);
		cmbTipo.setDisplayField("empContatoTipoDescricao");
		cmbTipo.setValueField("empContatoTipoId");
		cmbTipo.setMinChars(1);
		cmbTipo.setForceSelection(true);
		cmbTipo.setSelectOnFocus(true);
		cmbTipo.setEditable(false);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empContatoId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "empContatoTipoId", 100, false, new Renderer() {

			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (value != null) {
					Record reg = UtilClient.getRegistro(storeTipo, "empContatoTipoId", value.toString());
					return reg.getAsString("empContatoTipoDescricao");
				} else {
					return "";
				}
			}
		});
		ccTipo.setEditor(new GridEditor(cmbTipo));

		ColumnConfig ccTipoDescricao = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "empContatoTipoDescricao", 100, false);
		ccTipoDescricao.setHidden(true);
		ccTipoDescricao.setFixed(true);

		txtDescricao = new TextField();
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(100);
		txtDescricao.setSelectOnFocus(true);
		ColumnConfig ccDescricao = new ColumnConfig(OpenSigCore.i18n.txtDescricao(), "empContatoDescricao", 300, false);
		ccDescricao.setEditor(new GridEditor(txtDescricao));

		txtPessoa = new TextField();
		txtPessoa.setMaxLength(100);
		txtPessoa.setSelectOnFocus(true);
		ColumnConfig ccPessoa = new ColumnConfig(OpenSigCore.i18n.txtPessoa(), "empContatoPessoa", 150, false);
		ccPessoa.setEditor(new GridEditor(txtPessoa));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccTipo, ccTipoDescricao, ccDescricao, ccPessoa };
		modelos = new ColumnModel(bcc);

		filtroPadrao = new FiltroNumero("empContatoId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtContato(), "icon-contato");
		super.inicializar();
	}

	public boolean validar(List<EmpContato> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				int contTipo = rec.getAsInteger("empContatoTipoId");
				String descricao = rec.getAsString("empContatoDescricao") == null ? "" : rec.getAsString("empContatoDescricao");
				String pessoa = rec.getAsString("empContatoPessoa") == null ? "" : rec.getAsString("empContatoPessoa");
				
				if (cmbTipo.getText() == null || descricao.equals("")) {
					throw new Exception();
				}

				EmpContatoTipo tipo = new EmpContatoTipo(contTipo);
				tipo.setEmpContatoTipoDescricao(cmbTipo.getText());

				EmpContato cont = new EmpContato();
				cont.setEmpContatoTipo(tipo);
				cont.setEmpContatoDescricao(descricao);
				cont.setEmpContatoPessoa(pessoa);
				lista.add(cont);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida && recs.length > 0;
	}

	public ComboBox getCmbTipo() {
		return cmbTipo;
	}

	public void setCmbTipo(ComboBox cmbTipo) {
		this.cmbTipo = cmbTipo;
	}

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public TextField getTxtPessoa() {
		return txtPessoa;
	}

	public void setTxtPessoa(TextField txtPessoa) {
		this.txtPessoa = txtPessoa;
	}

	public TextField getTxtTipo() {
		return txtTipo;
	}

	public void setTxtTipo(TextField txtTipo) {
		this.txtTipo = txtTipo;
	}

}
