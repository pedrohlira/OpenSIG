package br.com.opensig.permissao.client.visao;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.visao.PermitirSistema;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.core.shared.modelo.ILogin;
import br.com.opensig.permissao.client.servico.PermissaoProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class PermitirSistemaImpl implements PermitirSistema {

	private Window wndPermissao;
	private FormPanel frmPermissao;
	private TextField txtUsuario;
	private TextField txtSenha;
	private Label lblInfo;
	private Button btnEntrar;
	private Button btnCancelar;
	private AsyncCallback<ILogin> async;

	public PermitirSistemaImpl() {
		inicializar();
	}

	private void inicializar() {
		frmPermissao = new FormPanel(Position.CENTER);
		frmPermissao.setLabelAlign(Position.TOP);
		frmPermissao.setBorder(false);
		frmPermissao.setPaddings(5);

		txtUsuario = new TextField(OpenSigCore.i18n.txtUsuario());
		txtUsuario.setAllowBlank(false);
		txtUsuario.setMaxLength(40);
		txtUsuario.setWidth(100);
		txtUsuario.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});

		txtSenha = new TextField(OpenSigCore.i18n.txtSenha());
		txtSenha.setAllowBlank(false);
		txtSenha.setWidth(100);
		txtSenha.setMinLength(6);
		txtSenha.setMaxLength(40);
		txtSenha.setInputType("password");
		txtSenha.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtUsuario, 120);
		linha1.addToRow(txtSenha, 120);
		frmPermissao.add(linha1);

		lblInfo = new Label(OpenSigCore.i18n.txtAcessoNegado());
		lblInfo.setCls("style-erro");
		frmPermissao.add(lblInfo);

		btnEntrar = new Button(OpenSigCore.i18n.txtEntrar());
		btnEntrar.setIconCls("icon-salvar");
		btnEntrar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				entrar();
			}
		});

		btnCancelar = new Button(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				wndPermissao.close();
				async.onSuccess(null);
			}
		});

		wndPermissao = new Window(OpenSigCore.i18n.txtPermissao(), 270, 150, true, false);
		wndPermissao.setButtonAlign(Position.CENTER);
		wndPermissao.setClosable(false);
		wndPermissao.setLayout(new FitLayout());
		wndPermissao.setIconCls("icon-acesso");
		wndPermissao.addButton(btnEntrar);
		wndPermissao.addButton(btnCancelar);
		wndPermissao.add(frmPermissao);
		wndPermissao.addListener(new FormPanelListenerAdapter() {
			public void onShow(Component component) {
				txtUsuario.focus(true, 10);
			}
		});
		wndPermissao.doLayout();
	}

	public void executar(AsyncCallback<ILogin> async) {
		this.async = async;
		wndPermissao.show();
	}

	private void entrar() {
		if (frmPermissao.getForm().isValid()) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtAcessandoServidor());
			int empresaId = Integer.valueOf(Ponte.getLogin().getEmpresa()[0]);

			final PermissaoProxy login = new PermissaoProxy();
			login.entrar(txtUsuario.getText(), OpenSigCoreJS.sha1(txtSenha.getValueAsString()), "", empresaId, true, new AsyncCallback<Autenticacao>() {

				public void onFailure(Throwable caught) {
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtAtencao(), OpenSigCore.i18n.errLoginErro());
					txtUsuario.setValue("");
					txtSenha.setValue("");
				}

				public void onSuccess(Autenticacao result) {
					MessageBox.hide();
					async.onSuccess(login);
					wndPermissao.close();
				}
			});
		}
	}

	public Window getWndPermissao() {
		return wndPermissao;
	}

	public void setWndPermissao(Window wndPermissao) {
		this.wndPermissao = wndPermissao;
	}

	public FormPanel getFrmPermissao() {
		return frmPermissao;
	}

	public void setFrmPermissao(FormPanel frmPermissao) {
		this.frmPermissao = frmPermissao;
	}

	public TextField getTxtUsuario() {
		return txtUsuario;
	}

	public void setTxtUsuario(TextField txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public TextField getTxtSenha() {
		return txtSenha;
	}

	public void setTxtSenha(TextField txtSenha) {
		this.txtSenha = txtSenha;
	}

	public Label getLblInfo() {
		return lblInfo;
	}

	public void setLblInfo(Label lblInfo) {
		this.lblInfo = lblInfo;
	}

	public Button getBtnEntrar() {
		return btnEntrar;
	}

	public void setBtnEntrar(Button btnEntrar) {
		this.btnEntrar = btnEntrar;
	}

	public Button getBtnCancelar() {
		return btnCancelar;
	}

	public void setBtnCancelar(Button btnCancelar) {
		this.btnCancelar = btnCancelar;
	}

	public AsyncCallback<ILogin> getAsync() {
		return async;
	}

	public void setAsync(AsyncCallback<ILogin> async) {
		this.async = async;
	}

	public void setInfo(String info) {
		lblInfo.setText(info);
	}

}
