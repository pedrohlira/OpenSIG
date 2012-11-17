package br.com.opensig.permissao.client.visao.form;

import java.util.ArrayList;
import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteEmpresa;
import br.com.opensig.core.client.controlador.comando.lista.ComandoPermiteUsuario;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Arvore;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.Lista;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;
import br.com.opensig.permissao.shared.modelo.SisGrupo;
import br.com.opensig.permissao.shared.modelo.SisPermissao;
import br.com.opensig.permissao.shared.modelo.SisUsuario;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

public class FormularioUsuario extends AFormulario<SisUsuario> {

	private Hidden hdnCod;
	private Hidden hdnSenha;
	private TextField txtLogin;
	private TextField txtEmail;
	private Checkbox chkAtivo;
	private Checkbox chkSistema;
	private TextField txtSenha;
	private TextField txtConfirma;
	private NumberField txtDesconto;
	private HTML htmlTexto;
	private Arvore<Dados> treeEmpresasGrupos;
	private int carregados;

	public FormularioUsuario(SisFuncao funcao) {
		super(new SisUsuario(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("sisUsuarioId", "0");
		add(hdnCod);

		hdnSenha = new Hidden("sisUsuarioSenha", "");
		add(hdnSenha);

		txtLogin = new TextField(OpenSigCore.i18n.txtNome(), "sisUsuarioLogin", 250);
		txtLogin.setAllowBlank(false);
		txtLogin.setMinLength(4);
		txtLogin.setMaxLength(40);

		txtDesconto = new NumberField(OpenSigCore.i18n.txtDesconto() + "_%", "sisUsuarioDesconto", 50);
		txtDesconto.setAllowBlank(false);
		txtDesconto.setAllowDecimals(false);
		txtDesconto.setAllowNegative(false);
		txtDesconto.setMinValue(0);
		txtDesconto.setMaxValue(100);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtLogin, 270);
		linha1.addToRow(txtDesconto, 70);

		txtEmail = new TextField(OpenSigCore.i18n.txtEmail(), "sisUsuarioEmail", 250);
		txtEmail.setAllowBlank(false);
		txtEmail.setMaxLength(100);

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "sisUsuarioAtivo");
		chkAtivo.setValue(true);

		chkSistema = new Checkbox(OpenSigCore.i18n.txtSistema(), "sisUsuarioSistema");
		chkSistema.setValue(false);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtEmail, 270);
		linha2.addToRow(chkAtivo, 60);
		linha2.addToRow(chkSistema, 70);

		txtSenha = new TextField(OpenSigCore.i18n.txtSenha(), "sisUsuarioSenha1", 110);
		txtSenha.setAllowBlank(false);
		txtSenha.setMinLength(6);
		txtSenha.setMaxLength(40);
		txtSenha.setPassword(true);
		txtSenha.addListener(new TextFieldListenerAdapter() {
			public void onRender(Component component) {
				txtSenha.getEl().addListener("keyup", new EventCallback() {
					public void execute(EventObject e) {
						seguranca();
					}
				});
			}
		});

		txtConfirma = new TextField(OpenSigCore.i18n.txtConfirma(), "sisUsuarioSenha2", 110);
		txtConfirma.setAllowBlank(false);
		txtConfirma.setMinLength(6);
		txtConfirma.setMaxLength(40);
		txtConfirma.setPassword(true);
		txtConfirma.setInvalidText(OpenSigCore.i18n.msgComparaSenha());
		txtConfirma.setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				return value.toUpperCase().equals(txtSenha.getValueAsString().toUpperCase());
			}
		});

		htmlTexto = new HTML("<span></span>");
		htmlTexto.setStyleName("");

		MultiFieldPanel linha3 = new MultiFieldPanel();
		linha3.setBorder(false);
		linha3.addToRow(txtSenha, 130);
		linha3.addToRow(txtConfirma, 130);
		linha3.addToRow(htmlTexto, 130);

		Panel coluna1 = new Panel();
		coluna1.setBorder(false);
		coluna1.setLayout(new FormLayout());
		coluna1.add(linha1);
		coluna1.add(linha2);
		coluna1.add(linha3);

		treeEmpresasGrupos = new Arvore(new EmpEmpresa(), "");
		treeEmpresasGrupos.setTitle(OpenSigCore.i18n.txtEmpresa() + " - " + OpenSigCore.i18n.txtGrupo());
		treeEmpresasGrupos.setIconeNode("icon-empresa");
		treeEmpresasGrupos.setExpandir(true);
		treeEmpresasGrupos.setFiltrar(true);
		treeEmpresasGrupos.setWidth(200);
		treeEmpresasGrupos.setHeight(300);
		treeEmpresasGrupos.setBodyBorder(true);
		treeEmpresasGrupos.inicializar();
		treeEmpresasGrupos.addListener(new TreePanelListenerAdapter() {
			public void onCheckChange(TreeNode node, boolean checked) {
				if (checked) {
					do {
						node = (TreeNode) node.getParentNode();
						node.getUI().toggleCheck(true);
					} while (node.getDepth() > 0);
				} else {
					for (Node filho : node.getChildNodes()) {
						((TreeNode) filho).getUI().toggleCheck(false);
					}
				}
			}
		});

		Panel formColuna = new Panel();
		formColuna.setBodyBorder(false);
		formColuna.setLayout(new ColumnLayout());
		formColuna.add(coluna1, new ColumnLayoutData(.60));
		formColuna.add(treeEmpresasGrupos, new ColumnLayoutData(.40));
		add(formColuna);
	}

	private void carregarEmpresa() {
		treeEmpresasGrupos.getEl().mask(OpenSigCore.i18n.txtAguarde());
		GrupoFiltro gf = new GrupoFiltro();
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteEmpresa.class) == null) {
			FiltroNumero fn = new FiltroNumero("empEmpresaId", ECompara.IGUAL, Ponte.getLogin().getEmpresaId());
			gf.add(fn, EJuncao.E);
		}
		FiltroBinario fb = new FiltroBinario("empEntidade.empEntidadeAtivo", ECompara.IGUAL, 1);
		gf.add(fb);

		CoreProxy<EmpEmpresa> proxy = new CoreProxy<EmpEmpresa>(new EmpEmpresa());
		proxy.selecionar(gf, new AsyncCallback<Lista<EmpEmpresa>>() {

			public void onFailure(Throwable caught) {
				treeEmpresasGrupos.getEl().unmask();
				MessageBox.confirm(OpenSigCore.i18n.txtEmpresa(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							carregarEmpresa();
						}
					}
				});
			}

			public void onSuccess(Lista<EmpEmpresa> result) {
				for (String[] emp : result.getDados()) {
					EmpEntidade entidade = new EmpEntidade();
					entidade.setEmpEntidadeNome1(emp[2]);

					EmpEmpresa empresa = new EmpEmpresa();
					empresa.setEmpEmpresaId(Integer.valueOf(emp[0]));
					empresa.setEmpEntidade(entidade);

					TreeNode no = treeEmpresasGrupos.novoItem(empresa);
					carregarGrupos(no, empresa);
					treeEmpresasGrupos.getRoot().appendChild(no);
					carregados++;
				}
			}
		});
	}

	private void carregarGrupos(final TreeNode no, final EmpEmpresa emp) {
		GrupoFiltro gf = new GrupoFiltro();
		FiltroObjeto fo = new FiltroObjeto("empEmpresa", ECompara.IGUAL, emp);
		gf.add(fo, EJuncao.E);

		FiltroBinario fb = new FiltroBinario("sisGrupoAtivo", ECompara.IGUAL, 1);
		gf.add(fb);

		CoreProxy<SisGrupo> proxy = new CoreProxy<SisGrupo>(new SisGrupo());
		proxy.selecionar(gf, new AsyncCallback<Lista<SisGrupo>>() {

			public void onFailure(Throwable caught) {
				treeEmpresasGrupos.getEl().unmask();
				MessageBox.confirm(OpenSigCore.i18n.txtGrupo(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							carregarGrupos(no, emp);
						}
					}
				});
			}

			public void onSuccess(Lista<SisGrupo> result) {
				for (String[] grp : result.getDados()) {
					SisGrupo grupo = SisGrupo.getGrupo(grp);
					TreeNode node = treeEmpresasGrupos.novoItem(grupo);
					node.setIconCls("icon-grupo");
					no.appendChild(node);
				}

				carregados--;
				if (carregados == 0) {
					treeEmpresasGrupos.expandAll();
					treeEmpresasGrupos.getEl().unmask();
					mostrar();
				}
			}
		});
	}

	public boolean setDados() {
		boolean retorno = true;

		classe.setSisUsuarioId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setSisUsuarioLogin(txtLogin.getValueAsString());
		classe.setSisUsuarioEmail(txtEmail.getValueAsString());
		if (txtDesconto.getValue() != null) {
			classe.setSisUsuarioDesconto(txtDesconto.getValue().intValue());
		}
		classe.setSisUsuarioAtivo(chkAtivo.getValue());
		classe.setSisUsuarioSistema(chkSistema.getValue());
		if (hdnCod.getValueAsString().equals("0") || !txtSenha.getValueAsString().equals("")) {
			classe.setSisUsuarioSenha(OpenSigCoreJS.sha1(txtSenha.getValueAsString()));
		} else {
			classe.setSisUsuarioSenha(hdnSenha.getValueAsString());
		}

		List<Dados> objetos = new ArrayList<Dados>();
		List<EmpEmpresa> empresas = new ArrayList<EmpEmpresa>();
		List<SisGrupo> grupos = new ArrayList<SisGrupo>();

		treeEmpresasGrupos.validar(objetos);
		for (Dados dado : objetos) {
			if (dado instanceof EmpEmpresa) {
				empresas.add((EmpEmpresa) dado);
			} else {
				grupos.add((SisGrupo) dado);
			}
		}
		classe.setEmpEmpresas(empresas);
		classe.setSisGrupos(grupos);

		if (empresas.isEmpty() || grupos.isEmpty()) {
			retorno = false;
			treeEmpresasGrupos.getLblValidacao().setVisible(true);
		}

		return retorno;
	}

	public void limparDados() {
		getForm().reset();
		treeEmpresasGrupos.selecionar(null);
	}

	public void mostrarDados() {
		if (treeEmpresasGrupos.getRoot().getChildNodes().length == 0) {
			carregarEmpresa();
		} else {
			mostrar();
		}
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();

		if (rec != null) {
			getForm().loadRecord(rec);
			txtSenha.setAllowBlank(true);
			txtConfirma.setAllowBlank(true);

			List<String> nomes = new ArrayList<String>();
			// adicionando as empresas
			for (String empresa : rec.getAsString("empEmpresas").split("::")) {
				nomes.add(empresa);
			}
			// adicionando os grupos
			String[] ids = rec.getAsString("grupoId").split("::");
			String[] grupos = rec.getAsString("sisGrupos").split("::");
			for (int i = 0; i < ids.length; i++) {
				nomes.add(ids[i] + "-" + grupos[i]);
			}
			treeEmpresasGrupos.selecionar(nomes.toArray(new String[] {}));

			// permissoes
			String per = rec.getAsString("sisPermissao");
			classe.setSisPermissoes(SisPermissao.getPermissoes(per));
		} else {
			txtSenha.setAllowBlank(false);
			txtConfirma.setAllowBlank(false);
		}
		txtLogin.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			txtSenha.setAllowBlank(false);
			txtConfirma.setAllowBlank(false);
			duplicar = false;
		}
		
		if (UtilClient.getAcaoPermitida(funcao, ComandoPermiteUsuario.class) == null) {
			chkSistema.disable();
		}
	}

	private void seguranca() {
		int nivel = OpenSigCoreJS.policy(txtSenha.getValueAsString());
		String texto = "";
		String estilo = "";

		if (nivel < 33) {
			texto = OpenSigCore.i18n.txtFraca();
			estilo = "badPass";
		} else if (nivel < 66) {
			texto = OpenSigCore.i18n.txtBoa();
			estilo = "goodPass";
		} else {
			texto = OpenSigCore.i18n.txtForte();
			estilo = "strongPass";
		}

		htmlTexto.setHTML("<span class='testresult'>" + texto + "</span>");
		htmlTexto.setStyleName(estilo);
	}

	public void gerarListas() {
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnSenha() {
		return hdnSenha;
	}

	public void setHdnSenha(Hidden hdnSenha) {
		this.hdnSenha = hdnSenha;
	}

	public TextField getTxtLogin() {
		return txtLogin;
	}

	public void setTxtLogin(TextField txtLogin) {
		this.txtLogin = txtLogin;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

	public Checkbox getChkSistema() {
		return chkSistema;
	}

	public void setChkSistema(Checkbox chkSistema) {
		this.chkSistema = chkSistema;
	}

	public HTML getHtmlTexto() {
		return htmlTexto;
	}

	public void setHtmlTexto(HTML htmlTexto) {
		this.htmlTexto = htmlTexto;
	}

	public TextField getTxtSenha() {
		return txtSenha;
	}

	public void setTxtSenha(TextField txtSenha) {
		this.txtSenha = txtSenha;
	}

	public TextField getTxtConfirma() {
		return txtConfirma;
	}

	public void setTxtConfirma(TextField txtConfirma) {
		this.txtConfirma = txtConfirma;
	}

	public TextField getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(TextField txtEmail) {
		this.txtEmail = txtEmail;
	}

	public NumberField getTxtDesconto() {
		return txtDesconto;
	}

	public void setTxtDesconto(NumberField txtDesconto) {
		this.txtDesconto = txtDesconto;
	}

}
