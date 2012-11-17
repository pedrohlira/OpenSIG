package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.empresa.shared.modelo.EmpPais;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

public class FormularioEstado extends AFormulario<EmpEstado> {

	private Hidden hdnCod;
	private NumberField txtIbge;
	private TextField txtEstado;
	private TextField txtSigla;
	private ComboBox cmbPais;

	public FormularioEstado(SisFuncao funcao) {
		super(new EmpEstado(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("empEstadoId", "0");
		add(hdnCod);

		txtIbge = new NumberField(OpenSigCore.i18n.txtIbge(), "empEstadoIbge", 40);
		txtIbge.setAllowBlank(false);
		txtIbge.setAllowDecimals(false);
		txtIbge.setAllowNegative(false);
		txtIbge.setMinLength(2);
		txtIbge.setMaxLength(2);

		txtEstado = new TextField(OpenSigCore.i18n.txtNome(), "empEstadoDescricao", 250);
		txtEstado.setAllowBlank(false);
		txtEstado.setMaxLength(100);

		txtSigla = new TextField(OpenSigCore.i18n.txtSigla(), "empEstadoSigla", 40);
		txtSigla.setAllowBlank(false);
		txtSigla.setMinLength(2);
		txtSigla.setMaxLength(2);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtIbge, 60);
		linha1.addToRow(txtEstado, 270);
		linha1.addToRow(txtSigla, 60);
		linha1.addToRow(getPais(), new ColumnLayoutData(1));
		add(linha1);
	}

	public boolean setDados() {
		classe.setEmpEstadoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setEmpEstadoDescricao(txtEstado.getValueAsString());
		if (txtIbge.getValue() != null) {
			classe.setEmpEstadoIbge(txtIbge.getValue().intValue());
		}
		if (txtSigla.getValueAsString() != null) {
			classe.setEmpEstadoSigla(txtSigla.getValueAsString().toUpperCase());
		}
		if (cmbPais.getValue() != null) {
			EmpPais pais = new EmpPais(Integer.valueOf(cmbPais.getValue()));
			classe.setEmpPais(pais);
		}

		return true;
	}

	public void limparDados() {
		getForm().reset();
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		}
		txtIbge.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	private ComboBox getPais() {
		FieldDef[] fdPais = new FieldDef[] { new IntegerFieldDef("empPaisId"), new IntegerFieldDef("empPaisIbge"), new StringFieldDef("empPaisDescricao") };
		CoreProxy<EmpPais> proxy = new CoreProxy<EmpPais>(new EmpPais());
		Store pais = new Store(proxy, new ArrayReader(new RecordDef(fdPais)), false);
		pais.load();

		cmbPais = new ComboBox(OpenSigCore.i18n.txtPais(), "empPais.empPaisId", 200);
		cmbPais.setAllowBlank(false);
		cmbPais.setStore(pais);
		cmbPais.setTriggerAction(ComboBox.ALL);
		cmbPais.setMode(ComboBox.LOCAL);
		cmbPais.setDisplayField("empPaisDescricao");
		cmbPais.setValueField("empPaisId");
		cmbPais.setForceSelection(true);
		cmbPais.setListWidth(200);
		cmbPais.setEditable(false);

		return cmbPais;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public NumberField getTxtIbge() {
		return txtIbge;
	}

	public void setTxtIbge(NumberField txtIbge) {
		this.txtIbge = txtIbge;
	}

	public TextField getTxtEstado() {
		return txtEstado;
	}

	public void setTxtEstado(TextField txtEstado) {
		this.txtEstado = txtEstado;
	}

	public TextField getTxtSigla() {
		return txtSigla;
	}

	public void setTxtSigla(TextField txtSigla) {
		this.txtSigla = txtSigla;
	}

	public ComboBox getCmbPais() {
		return cmbPais;
	}

	public void setCmbPais(ComboBox cmbPais) {
		this.cmbPais = cmbPais;
	}

}
