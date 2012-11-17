package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.empresa.client.js.OpenSigEmpresaJS;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.fiscal.client.servico.FiscalProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoCadastro extends ComandoFuncao {

	private Window wndCad;
	private FormPanel frmCad;
	private ComboBox cmbAmbiente;
	private ComboBox cmbEstado;
	private Radio chkCNPJ;
	private Radio chkCPF;
	private Radio chkIE;
	private TextField txtCNPJ;
	private TextField txtCPF;
	private TextField txtIE;
	private Button btnOk;

	@Override
	public void execute(Map contexto) {
		frmCad = new FormPanel(Position.CENTER);
		frmCad.setLabelAlign(Position.TOP);
		frmCad.setBorder(false);
		frmCad.setPaddings(5);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getAmbiente(), 140);
		linha1.addToRow(getEstado(), 70);
		frmCad.add(linha1);

		chkCNPJ = new Radio("CNPJ", "tipo");
		chkCNPJ.addListener(new CheckboxListenerAdapter() {
			public void onCheck(Checkbox field, boolean checked) {
				mudarDoc();
			}
		});
		chkCPF = new Radio("CPF", "tipo");
		chkCPF.addListener(new CheckboxListenerAdapter() {
			public void onCheck(Checkbox field, boolean checked) {
				mudarDoc();
			}
		});
		chkIE = new Radio("IE", "tipo");
		chkIE.addListener(new CheckboxListenerAdapter() {
			public void onCheck(Checkbox field, boolean checked) {
				mudarDoc();
			}
		});
		chkCNPJ.setChecked(true);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(chkCNPJ, 70);
		linha2.addToRow(chkCPF, 70);
		linha2.addToRow(chkIE, 70);
		frmCad.add(linha2);

		txtCNPJ = new TextField("", "docCNPJ", 200);
		txtCNPJ.setAllowBlank(false);
		txtCNPJ.setHideLabel(true);
		txtCNPJ.setMaxLength(18);
		txtCNPJ.setSelectOnFocus(true);
		txtCNPJ.setInvalidText(OpenSigCore.i18n.msgCampoInvalido());
		txtCNPJ.setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return OpenSigEmpresaJS.validarCNPJ(txtCNPJ.getId(), value);
			}
		});
		txtCNPJ.addListener(new TextFieldListenerAdapter() {
			public void onRender(Component component) {
				super.onRender(component);
				OpenSigCoreJS.mascarar(component.getId(), "99.999.999/9999-99", null);
			}
		});

		txtCPF = new TextField("", "docCPF", 200, "00000000000");
		txtCPF.hide();
		txtCPF.setAllowBlank(false);
		txtCPF.setHideLabel(true);
		txtCPF.setMaxLength(14);
		txtCPF.setSelectOnFocus(true);
		txtCPF.setInvalidText(OpenSigCore.i18n.msgCampoInvalido());
		txtCPF.setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return OpenSigEmpresaJS.validarCPF(txtCPF.getId(), value);
			}
		});
		txtCPF.addListener(new TextFieldListenerAdapter() {
			public void onRender(Component component) {
				super.onRender(component);
				OpenSigCoreJS.mascarar(component.getId(), "999.999.999-99", null);
			}
		});
		txtIE = new TextField("", "docIE", 200, "00000000000");
		txtIE.hide();
		txtIE.setAllowBlank(false);
		txtIE.setHideLabel(true);
		txtIE.setMaxLength(14);
		txtIE.setSelectOnFocus(true);
		txtIE.setInvalidText(OpenSigCore.i18n.msgCampoInvalido());

		MultiFieldPanel linha3 = new MultiFieldPanel();
		linha3.setBorder(false);
		linha3.addToRow(txtCNPJ, new ColumnLayoutData(1));
		linha3.addToRow(txtCPF, new ColumnLayoutData(1));
		linha3.addToRow(txtIE, new ColumnLayoutData(1));
		frmCad.add(linha3);

		btnOk = new Button(OpenSigCore.i18n.txtOk());
		btnOk.setIconCls("icon-salvar");
		btnOk.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				status();
			}
		});

		wndCad = new Window(OpenSigCore.i18n.txtCadastro(), 240, 200, true, false);
		wndCad.setButtonAlign(Position.CENTER);
		wndCad.setLayout(new FitLayout());
		wndCad.setIconCls("icon-sefaz");
		wndCad.addButton(btnOk);
		wndCad.add(frmCad);
		wndCad.doLayout();
		wndCad.show();
	}

	private ComboBox getAmbiente() {
		Store store = new SimpleStore(new String[] { "id", "valor" }, new String[][] { new String[] { "1", "PRODUÇÃO" }, new String[] { "2", "HOMOLOGAÇÃO" } });
		store.load();

		cmbAmbiente = new ComboBox(OpenSigCore.i18n.txtTipo(), "tipoAmbiente", 120);
		cmbAmbiente.setForceSelection(true);
		cmbAmbiente.setEditable(false);
		cmbAmbiente.setMinChars(1);
		cmbAmbiente.setStore(store);
		cmbAmbiente.setDisplayField("valor");
		cmbAmbiente.setValueField("id");
		cmbAmbiente.setMode(ComboBox.LOCAL);
		cmbAmbiente.setTriggerAction(ComboBox.ALL);
		cmbAmbiente.setAllowBlank(false);

		return cmbAmbiente;
	}

	private ComboBox getEstado() {
		FieldDef[] fdEstado = new FieldDef[] { new IntegerFieldDef("empEstadoId"), new IntegerFieldDef("empEstadoIbge"), new StringFieldDef("empEstadoDescricao"), new StringFieldDef("empEstadoSigla") };
		CoreProxy proxyEstado = new CoreProxy<EmpEstado>(new EmpEstado());
		Store storeEstado = new Store(proxyEstado, new ArrayReader(new RecordDef(fdEstado)), false);
		storeEstado.load();

		cmbEstado = new ComboBox(OpenSigCore.i18n.txtEstado(), "empEstado", 50);
		cmbEstado.setAllowBlank(false);
		cmbEstado.setStore(storeEstado);
		cmbEstado.setTriggerAction(ComboBox.ALL);
		cmbEstado.setMode(ComboBox.LOCAL);
		cmbEstado.setDisplayField("empEstadoSigla");
		cmbEstado.setValueField("empEstadoIbge");
		cmbEstado.setForceSelection(true);
		cmbEstado.setEditable(false);

		return cmbEstado;
	}

	private void status() {
		if (frmCad.getForm().isValid()) {
			int amb = Integer.valueOf(cmbAmbiente.getValue());
			int ibge = Integer.valueOf(cmbEstado.getValue());
			String uf = cmbEstado.getRawValue();
			String doc = "";
			String tipo = "";
			if (chkCNPJ.getValue()) {
				doc = txtCNPJ.getValueAsString();
				tipo = "CNPJ";
			} else if (chkCPF.getValue()) {
				doc = txtCPF.getValueAsString();
				tipo = "CPF";
			} else {
				doc = txtIE.getValueAsString();
				tipo = "IE";
			}
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtCadastro());

			FiscalProxy proxy = new FiscalProxy();
			proxy.cadastro(amb, ibge, uf, tipo, doc, Integer.valueOf(Ponte.getLogin().getEmpresa()[0]), new AsyncCallback<String>() {

				public void onSuccess(String result) {
					MessageBox.hide();
					Document doc = XMLParser.parse(result);
					String motivo = doc.getElementsByTagName("xMotivo").item(0).getFirstChild().getNodeValue();
					new ToastWindow(OpenSigCore.i18n.txtCadastro(), motivo).show();
				}

				public void onFailure(Throwable caught) {
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtCadastro(), caught.getMessage());
				}
			});
		}
	}

	private void mudarDoc() {
		if (chkCNPJ.getValue()) {
			txtCPF.hide();
			txtCPF.setValue("00000000000");
			txtIE.hide();
			txtIE.setValue("00000000000");
			txtCNPJ.show();
			txtCNPJ.focus();
		} else if (chkCPF.getValue()) {
			txtCNPJ.hide();
			txtCNPJ.setValue("00000000000000");
			txtIE.hide();
			txtIE.setValue("00000000000");
			txtCPF.show();
			txtCPF.focus();
		} else {
			txtCPF.hide();
			txtCPF.setValue("00000000000");
			txtCNPJ.hide();
			txtCNPJ.setValue("00000000000000");
			txtIE.show();
			txtIE.focus();
		}
	}
}
