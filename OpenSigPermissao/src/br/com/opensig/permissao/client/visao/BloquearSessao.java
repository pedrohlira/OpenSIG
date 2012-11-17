package br.com.opensig.permissao.client.visao;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.permissao.client.servico.PermissaoProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class BloquearSessao {

	protected static Window wndBloquear;
	protected FormPanel frmBloquear;
	protected Label lblMensagem;
	protected TextField txtSenha;
	protected int ping;

	public BloquearSessao() {
		if (wndBloquear == null) {
			inicializar();
		}
	}

	protected void inicializar() {
		wndBloquear = new Window(Ponte.getLogin().getUsuario(), 280, 140, true, false);
		wndBloquear.setButtonAlign(Position.CENTER);
		wndBloquear.setLayout(new FitLayout());
		wndBloquear.setClosable(false);
		wndBloquear.setIconCls("icon-acesso");

		frmBloquear = new FormPanel(Position.CENTER);
		frmBloquear.setLabelWidth(75);
		frmBloquear.setPaddings(5);

		lblMensagem = new Label(OpenSigCore.i18n.msgBloquearTexto());
		lblMensagem.setHeight(50);
		frmBloquear.add(lblMensagem);

		txtSenha = new TextField(OpenSigCore.i18n.txtSenha(), "sigpermissao_txtSenha", 150);
		txtSenha.setLabelSeparator(":");
		txtSenha.setAllowBlank(false);
		txtSenha.setMinLength(6);
		txtSenha.setMaxLength(40);
		txtSenha.setInputType("password");
		txtSenha.setInvalidText(OpenSigCore.i18n.msgCampoInvalido());
		txtSenha.setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				String criptado = OpenSigCoreJS.sha1(value);
				return criptado.equalsIgnoreCase(Ponte.getLogin().getSenha());
			}
		});
		txtSenha.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});
		frmBloquear.add(txtSenha);

		Button btnEntrar = new Button(OpenSigCore.i18n.txtEntrar());
		btnEntrar.setIconCls("icon-entrar");
		btnEntrar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				entrar();
			}
		});

		wndBloquear.addButton(btnEntrar);
		wndBloquear.add(frmBloquear);
		wndBloquear.doLayout();
		wndBloquear.addListener(new FormPanelListenerAdapter() {
			public void onShow(Component component) {
				txtSenha.focus(true, 10);
			}
		});
		wndBloquear.show();
	}

	protected void entrar() {
		if (frmBloquear.getForm().isValid()) {
			PermissaoProxy login = new PermissaoProxy();
			login.bloquear(false, new AsyncCallback() {
				public void onSuccess(Object result) {
					wndBloquear.close();
					wndBloquear = null;
				}

				public void onFailure(Throwable caught) {
					com.google.gwt.user.client.Window.Location.reload();
				}
			});
		}
	}
}
