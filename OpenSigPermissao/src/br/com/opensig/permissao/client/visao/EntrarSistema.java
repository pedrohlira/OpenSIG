package br.com.opensig.permissao.client.visao;

import java.util.Date;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.shared.modelo.Autenticacao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.permissao.client.servico.PermissaoProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.image.Image;
import com.gwtextux.client.widgets.image.ImageListenerAdapter;

public class EntrarSistema {

	protected Window wndAcesso;
	protected FormPanel frmAcesso;
	protected TextField txtUsuario;
	protected TextField txtSenha;
	protected TextField txtCaptcha;
	protected Image imgCaptcha;
	protected ComboBox cmbEmpresa;
	protected Button btnEntrar;
	protected Button btnEsqueceu;
	protected Button btnSobre;
	protected PermissaoProxy permissao;

	public EntrarSistema() {
		inicializar();
	}

	public void inicializar() {
		int alturaJanela = 200;

		frmAcesso = new FormPanel(Position.CENTER);
		frmAcesso.setLabelAlign(Position.TOP);
		frmAcesso.setBorder(false);
		frmAcesso.setPaddings(5);

		txtUsuario = new TextField(OpenSigCore.i18n.txtUsuario(), "txtUsuario", 100);
		txtUsuario.setAllowBlank(false);
		txtUsuario.setMaxLength(40);
		txtUsuario.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});

		txtSenha = new TextField(OpenSigCore.i18n.txtSenha(), "txtSenha", 100);
		txtSenha.setAllowBlank(false);
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
		linha1.addToRow(txtUsuario, new ColumnLayoutData(0.5));
		linha1.addToRow(txtSenha, new ColumnLayoutData(0.5));
		frmAcesso.add(linha1);

		String captcha = RootPanel.get("captcha").getElement().getInnerText();
		imgCaptcha = new Image("imgCaptcha", GWT.getHostPageBaseURL() + "PermissaoService?data=" + new Date().getTime());
		imgCaptcha.setTooltip(OpenSigCore.i18n.msgRecarregar());
		imgCaptcha.addListener(new ImageListenerAdapter(){
			public void onClick(Image image, EventObject e) {
				image.setSrc(GWT.getHostPageBaseURL() + "PermissaoService?data=" + new Date().getTime());
			}
		});

		txtCaptcha = new TextField(OpenSigCore.i18n.txtImagem(), "captcha", 50);
		txtCaptcha.setAllowBlank(false);
		txtCaptcha.setMinLength(5);
		txtCaptcha.setMaxLength(5);
		txtCaptcha.setRegex("\\w");
		txtCaptcha.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});
		

		if (captcha.equalsIgnoreCase("true")) {
			frmAcesso.add(getCaptcha());
			alturaJanela = 250;
		}
		frmAcesso.add(new HTML("<hr/>"));
		frmAcesso.add(getEmpresa());

		btnEntrar = new Button(OpenSigCore.i18n.txtEntrar());
		btnEntrar.setIconCls("icon-entrar");
		btnEntrar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				entrar();
			}
		});

		String esqueceu = RootPanel.get("esqueceu").getElement().getInnerText();
		btnEsqueceu = new Button(OpenSigCore.i18n.txtEsqueceu());
		btnEsqueceu.setIconCls("icon-esqueceu");
		btnEsqueceu.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				esqueceu();
			}
		});
		
		btnSobre = new Button(OpenSigCore.i18n.txtSobre());
		btnSobre.setIconCls("icon-sobre");

		Tool ajuda = new Tool(Tool.HELP, getAjuda() , "Video Ajuda!");
		
		wndAcesso = new Window(OpenSigCore.i18n.txtAutenticao(), 260, alturaJanela, true, false);
		wndAcesso.setButtonAlign(Position.CENTER);
		wndAcesso.setClosable(false);
		wndAcesso.setLayout(new FitLayout());
		wndAcesso.setIconCls("icon-acesso");
		wndAcesso.setTools(new Tool[]{ajuda});
		wndAcesso.addButton(btnEntrar);
		if (esqueceu.equalsIgnoreCase("true")) {
			wndAcesso.addButton(btnEsqueceu);
		}
		wndAcesso.addButton(btnSobre);
		wndAcesso.add(frmAcesso);
		wndAcesso.addListener(new FormPanelListenerAdapter() {
			public void onShow(Component component) {
				txtUsuario.focus(true, 10);
			}
		});
		wndAcesso.show();
		wndAcesso.doLayout();

	}

	public Component getEmpresa() {
		cmbEmpresa = UtilClient.getComboEntidade(new ComboEntidade(new EmpEmpresa()));
		cmbEmpresa.setName("empEmpresa.empEntidade.empEntidadeNome1");
		cmbEmpresa.setLabel(OpenSigCore.i18n.txtEmpresa());
		cmbEmpresa.setWidth(210);
		cmbEmpresa.setTriggerAction(ComboBox.ALL);
		cmbEmpresa.setMode(ComboBox.LOCAL);
		cmbEmpresa.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				entrar();
			}
		});
		cmbEmpresa.getStore().addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (records.length > 0) {
					cmbEmpresa.setValue(records[0].getAsString("empEmpresaId"));
				}
			}
		});
		cmbEmpresa.getStore().load();

		return cmbEmpresa;
	}

	public void entrar() {
		if (frmAcesso.getForm().isValid()) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtAcessandoServidor());

			permissao = new PermissaoProxy();
			permissao.entrar(txtUsuario.getValueAsString(), OpenSigCoreJS.sha1(txtSenha.getValueAsString()), txtCaptcha.getValueAsString(), Integer.valueOf(cmbEmpresa.getValue()), false,
					new AsyncCallback<Autenticacao>() {

						public void onFailure(final Throwable caught) {
							MessageBox.hide();
							MessageBox.alert(OpenSigCore.i18n.txtAutenticao(), caught.getMessage());
							txtCaptcha.setValue("");
							imgCaptcha.setSrc(GWT.getHostPageBaseURL() + "PermissaoService?data=" + new Date().getTime());
						}

						public void onSuccess(Autenticacao result) {
							MessageBox.hide();
							wndAcesso.close();
							Ponte.setLogin(permissao);
						}
					});
		}
	}

	public Function getAjuda(){
		return new Function() {
			public void execute() {
				
			}
		};
	}
	
	public void esqueceu() {
		new EsquecerSenha();
	}

	public MultiFieldPanel getCaptcha() {
		MultiFieldPanel linha = new MultiFieldPanel();
		linha.setBorder(false);
		linha.addToRow(imgCaptcha, new ColumnLayoutData(0.7));
		linha.addToRow(txtCaptcha, new ColumnLayoutData(0.3));
		return linha;
	}

	public Window getWndAcesso() {
		return wndAcesso;
	}

	public void setWndAcesso(Window wndAcesso) {
		this.wndAcesso = wndAcesso;
	}

	public FormPanel getFrmAcesso() {
		return frmAcesso;
	}

	public void setFrmAcesso(FormPanel frmAcesso) {
		this.frmAcesso = frmAcesso;
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

	public TextField getTxtCaptcha() {
		return txtCaptcha;
	}

	public void setTxtCaptcha(TextField txtCaptcha) {
		this.txtCaptcha = txtCaptcha;
	}

	public Image getImgCaptcha() {
		return imgCaptcha;
	}

	public void setImgCaptcha(Image imgCaptcha) {
		this.imgCaptcha = imgCaptcha;
	}

	public ComboBox getCmbEmpresa() {
		return cmbEmpresa;
	}

	public void setCmbEmpresa(ComboBox cmbEmpresa) {
		this.cmbEmpresa = cmbEmpresa;
	}

	public Button getBtnEntrar() {
		return btnEntrar;
	}

	public void setBtnEntrar(Button btnEntrar) {
		this.btnEntrar = btnEntrar;
	}

	public Button getBtnEsqueceu() {
		return btnEsqueceu;
	}

	public void setBtnEsqueceu(Button btnEsqueceu) {
		this.btnEsqueceu = btnEsqueceu;
	}

	public Button getBtnSobre() {
		return btnSobre;
	}

	public void setBtnSobre(Button btnSobre) {
		this.btnSobre = btnSobre;
	}

	public PermissaoProxy getPermissao() {
		return permissao;
	}

	public void setPermissao(PermissaoProxy permissao) {
		this.permissao = permissao;
	}

}
