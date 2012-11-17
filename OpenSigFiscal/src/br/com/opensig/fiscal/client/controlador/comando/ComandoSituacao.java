package br.com.opensig.fiscal.client.controlador.comando;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.ComandoFuncao;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.fiscal.client.servico.FiscalProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoSituacao extends ComandoFuncao {

	private Window wndSituacao;
	private FormPanel frmSituacao;
	private ComboBox cmbAmbiente;
	private TextField txtChave;
	private Button btnOk;

	@Override
	public void execute(Map contexto) {
		frmSituacao = new FormPanel(Position.CENTER);
		frmSituacao.setLabelAlign(Position.TOP);
		frmSituacao.setBorder(false);
		frmSituacao.setPaddings(5);

		txtChave = new TextField(OpenSigCore.i18n.txtChave(), "txtChave", 300);
		txtChave.setAllowBlank(false);
		txtChave.setRegex("\\d{44}");
		txtChave.setInvalidText(OpenSigCore.i18n.msgCampoInvalido());
		txtChave.setMaxLength(44);
		txtChave.setMinLength(44);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getAmbiente(), 140);
		linha1.addToRow(txtChave, 320);
		frmSituacao.add(linha1);

		btnOk = new Button(OpenSigCore.i18n.txtOk());
		btnOk.setIconCls("icon-salvar");
		btnOk.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				situacao();
			}
		});

		wndSituacao = new Window(OpenSigCore.i18n.txtSituacao(), 500, 130, true, false);
		wndSituacao.setButtonAlign(Position.CENTER);
		wndSituacao.setLayout(new FitLayout());
		wndSituacao.setIconCls("icon-sefaz");
		wndSituacao.addButton(btnOk);
		wndSituacao.add(frmSituacao);
		wndSituacao.doLayout();
		wndSituacao.show();
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

	private void situacao() {
		if (frmSituacao.getForm().isValid()) {
			int amb = Integer.valueOf(cmbAmbiente.getValue());
			String chave = txtChave.getValueAsString();
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSituacao());

			FiscalProxy proxy = new FiscalProxy();
			proxy.situacao(amb, chave, Integer.valueOf(Ponte.getLogin().getEmpresa()[0]), new AsyncCallback<String>() {

				public void onSuccess(String result) {
					MessageBox.hide();
					Document doc = XMLParser.parse(result);
					String motivo = UtilClient.getValorTag(doc.getDocumentElement(), "xMotivo", false);
					new ToastWindow(OpenSigCore.i18n.txtSituacao(), motivo).show();
				}

				public void onFailure(Throwable caught) {
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtSituacao(), caught.getMessage());
				}
			});
		}
	}
}
