package br.com.opensig.permissao.client.visao;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.permissao.client.servico.PermissaoProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class EsquecerSenha {

	private Window wndEsquecer;
	private FormPanel frmEsquecer;
	private Label lblMensagem;
	private TextField txtEmail;

	public EsquecerSenha() {
		inicializar();
	}

	private void inicializar() {
		wndEsquecer = new Window(OpenSigCore.i18n.txtEsqueceu(), 400, 170, true, false);
		wndEsquecer.setButtonAlign(Position.CENTER);
		wndEsquecer.setLayout(new FitLayout());
		wndEsquecer.setIconCls("icon-esqueceu");

		frmEsquecer = new FormPanel(Position.CENTER);
		frmEsquecer.setLabelWidth(50);
		frmEsquecer.setPaddings(5);

		lblMensagem = new Label(OpenSigCore.i18n.msgEsqueceuSenha());
		lblMensagem.setHeight(75);
		frmEsquecer.add(lblMensagem);

		frmEsquecer.add(new HTML("<br/><hr/>"));

		txtEmail = new TextField(OpenSigCore.i18n.txtEmail(), "txtEmail", 300);
		txtEmail.setLabelSeparator(":");
		txtEmail.setAllowBlank(false);
		txtEmail.setMaxLength(100);
		txtEmail.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});
		frmEsquecer.add(txtEmail);

		Button btnEntrar = new Button(OpenSigCore.i18n.txtOk());
		btnEntrar.setIconCls("icon-selecionar");
		btnEntrar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				entrar();
			}
		});

		wndEsquecer.addButton(btnEntrar);
		wndEsquecer.add(frmEsquecer);
		wndEsquecer.doLayout();
		wndEsquecer.addListener(new FormPanelListenerAdapter() {
			public void onShow(Component component) {
				txtEmail.focus(true, 10);
			}
		});
		wndEsquecer.show();
	}

	private void entrar() {
		if (frmEsquecer.getForm().isValid()) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtEmail());
			PermissaoProxy proxy = new PermissaoProxy();
			proxy.recuperarSenha(txtEmail.getValueAsString(), new AsyncCallback() {
				public void onSuccess(Object result) {
					MessageBox.hide();
					new ToastWindow(OpenSigCore.i18n.txtEmail(), OpenSigCore.i18n.msgSalvarOK()).show();
					wndEsquecer.close();
				}

				public void onFailure(Throwable caught) {
					MessageBox.hide();
					new ToastWindow(OpenSigCore.i18n.txtEmail(), caught.getMessage()).show();
					wndEsquecer.close();
				}
			});
		}
	}
}
