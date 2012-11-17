package br.com.opensig.permissao.client.visao;

import java.util.HashMap;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.ComandoExecutar;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class AlterarSenha {

	private Window wndAlterar;
	private FormPanel frmAlterar;
	private TextField txtSenha;
	private TextField txtNova;
	private TextField txtConfirma;

	public AlterarSenha() {
		inicializar();
	}

	private void inicializar() {
		wndAlterar = new Window(Ponte.getLogin().getUsuario(), 280, 160, true, false);
		wndAlterar.setButtonAlign(Position.CENTER);
		wndAlterar.setLayout(new FitLayout());
		wndAlterar.setIconCls("icon-acesso");

		frmAlterar = new FormPanel(Position.CENTER);
		frmAlterar.setLabelWidth(100);
		frmAlterar.setPaddings(5);

		txtSenha = new TextField(OpenSigCore.i18n.txtSenha(), "sigpermissao_txtSenha", 125);
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
		frmAlterar.add(txtSenha);

		txtNova = new TextField(OpenSigCore.i18n.txtNova(), "sigpermissao_txtNova", 125);
		txtNova.setLabelSeparator(":");
		txtNova.setAllowBlank(false);
		txtNova.setMinLength(6);
		txtNova.setMaxLength(40);
		txtNova.setInputType("password");
		txtNova.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});
		frmAlterar.add(txtNova);

		txtConfirma = new TextField(OpenSigCore.i18n.txtConfirma(), "sigpermissao_txtConfirma", 125);
		txtConfirma.setLabelSeparator(":");
		txtConfirma.setAllowBlank(false);
		txtConfirma.setMinLength(6);
		txtConfirma.setMaxLength(40);
		txtConfirma.setInputType("password");
		txtConfirma.setInvalidText(OpenSigCore.i18n.msgComparaSenha());
		txtConfirma.setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return value.toUpperCase().equals(txtNova.getValueAsString().toUpperCase());
			}
		});
		txtConfirma.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});
		frmAlterar.add(txtConfirma);

		Button btnEntrar = new Button(OpenSigCore.i18n.txtEntrar());
		btnEntrar.setIconCls("icon-entrar");
		btnEntrar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				entrar();
			}
		});
		wndAlterar.addButton(btnEntrar);
		wndAlterar.add(frmAlterar);
		wndAlterar.doLayout();
		wndAlterar.addListener(new FormPanelListenerAdapter() {
			public void onShow(Component component) {
				txtSenha.focus(true, 10);
			}
		});
		wndAlterar.show();
	}

	private void entrar() {
		if (frmAlterar.getForm().isValid()) {
			// contexto
			SisUsuario usuario = new SisUsuario();
			Map contexto = new HashMap();
			contexto.put("dados", usuario);

			// finalizando
			AComando cmdFim = new AComando() {
				public void execute(Map contexto) {
					wndAlterar.close();
					MessageBox.hide();
					new ToastWindow(OpenSigCore.i18n.txtSalvar(), OpenSigCore.i18n.msgSalvarOK()).show();
				}
			};
			// atualizando
			FiltroTexto ft1 = new FiltroTexto("sisUsuarioLogin", ECompara.IGUAL, Ponte.getLogin().getUsuario());
			FiltroTexto ft2 = new FiltroTexto("sisUsuarioSenha", ECompara.IGUAL, Ponte.getLogin().getSenha());
			GrupoFiltro gf = new GrupoFiltro(EJuncao.E, new IFiltro[] { ft1, ft2 });
			ParametroTexto pt = new ParametroTexto("sisUsuarioSenha", OpenSigCoreJS.sha1(txtNova.getValueAsString()));
			Sql sql = new Sql(usuario, EComando.ATUALIZAR, gf, pt);
			ComandoExecutar<SisUsuario> cmdExecutar = new ComandoExecutar<SisUsuario>(cmdFim, new Sql[] { sql });
			
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
			cmdExecutar.execute(contexto);
		}
	}
}
