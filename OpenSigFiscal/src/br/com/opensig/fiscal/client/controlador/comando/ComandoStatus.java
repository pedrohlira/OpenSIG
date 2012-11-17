package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoStatus extends ComandoFuncao {

	private Window wndStatus;
	private FormPanel frmStatus;
	private ComboBox cmbAmbiente;
	private ComboBox cmbEstado;
	private Button btnOk;

	@Override
	public void execute(Map contexto) {
		frmStatus = new FormPanel(Position.CENTER);
		frmStatus.setLabelAlign(Position.TOP);
		frmStatus.setBorder(false);
		frmStatus.setPaddings(5);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getAmbiente(), 140);
		linha1.addToRow(getEstado(), 70);
		frmStatus.add(linha1);

		btnOk = new Button(OpenSigCore.i18n.txtOk());
		btnOk.setIconCls("icon-salvar");
		btnOk.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				status();
			}
		});

		wndStatus = new Window(OpenSigCore.i18n.txtStatus(), 240, 130, true, false);
		wndStatus.setButtonAlign(Position.CENTER);
		wndStatus.setLayout(new FitLayout());
		wndStatus.setIconCls("icon-sefaz");
		wndStatus.addButton(btnOk);
		wndStatus.add(frmStatus);
		wndStatus.doLayout();
		wndStatus.show();
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
		if (frmStatus.getForm().isValid()) {
			int amb = Integer.valueOf(cmbAmbiente.getValue());
			int uf = Integer.valueOf(cmbEstado.getValue());
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtStatus());

			FiscalProxy proxy = new FiscalProxy();
			proxy.status(amb, uf, Integer.valueOf(Ponte.getLogin().getEmpresa()[0]), new AsyncCallback<String>() {

				public void onSuccess(String result) {
					MessageBox.hide();
					Document doc = XMLParser.parse(result);
					String motivo = doc.getElementsByTagName("xMotivo").item(0).getFirstChild().getNodeValue();
					new ToastWindow(OpenSigCore.i18n.txtStatus(), motivo).show();
				}

				public void onFailure(Throwable caught) {
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtStatus(), caught.getMessage());
				}
			});
		}
	}
}
