package br.com.opensig.empresa.client.visao.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EDirecao;
import br.com.opensig.core.shared.modelo.ExpListagem;
import br.com.opensig.core.shared.modelo.ExpMeta;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.client.js.OpenSigEmpresaJS;
import br.com.opensig.empresa.client.servico.EmpresaProxy;
import br.com.opensig.empresa.client.visao.lista.ListagemContato;
import br.com.opensig.empresa.client.visao.lista.ListagemEndereco;
import br.com.opensig.empresa.shared.modelo.EmpContato;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEntidade;

import com.gwtext.client.core.Ext;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FormLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public abstract class FormularioEntidade<E extends Dados> extends AFormulario<E> {

	protected EmpEntidade entidade;
	protected Hidden hdnCod;
	protected Hidden hdnId;
	protected TextField txtNome1;
	protected TextField txtNome2;
	protected TextField txtCNPJ;
	protected TextField txtCPF;
	protected TextField txtDoc2;
	protected TextArea txtObservacao;
	protected ComboBox cmbPessoa;
	protected Checkbox chkAtivo;
	protected TabPanel tabDados;
	protected ListagemContato gridContatos;
	protected ListagemEndereco gridEnderecos;
	protected String fisica;
	protected String juridica;
	protected String prefixo;

	public FormularioEntidade(E classe, SisFuncao funcao, String prefixo) {
		super(classe, funcao);
		this.entidade = new EmpEntidade();
		this.prefixo = prefixo;
	}

	protected void configurar() {
		fisica = OpenSigCore.i18n.txtFisica();
		juridica = OpenSigCore.i18n.txtJuridica();

		Panel coluna1 = new Panel();
		coluna1.setBorder(false);
		coluna1.setLayout(new FormLayout());

		hdnCod = new Hidden(prefixo + ".empEntidadeId", "0");
		coluna1.add(hdnCod);

		txtNome1 = new TextField(OpenSigCore.i18n.txtEntidadeNome1(), prefixo + ".empEntidadeNome1", 250);
		txtNome1.setAllowBlank(false);
		txtNome1.setMaxLength(100);
		coluna1.add(txtNome1);

		txtCNPJ = new TextField(OpenSigCore.i18n.txtEntidadeDoc1(), "docCNPJ", 150);
		txtCNPJ.setAllowBlank(false);
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

		txtCPF = new TextField(OpenSigCore.i18n.txtEntidadeDoc1(), "docCPF", 150);
		txtCPF.setAllowBlank(false);
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

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getPessoa(), 100);
		linha1.addToRow(txtCNPJ, new ColumnLayoutData(1));
		linha1.addToRow(txtCPF, new ColumnLayoutData(1));
		coluna1.add(linha1);

		Panel coluna2 = new Panel();
		coluna2.setBorder(false);
		coluna2.setLayout(new FormLayout());

		txtNome2 = new TextField(OpenSigCore.i18n.txtEntidadeNome2(), prefixo + ".empEntidadeNome2", 250);
		txtNome2.setMaxLength(15);
		txtNome2.setAllowBlank(false);
		coluna2.add(txtNome2);

		txtDoc2 = new TextField(OpenSigCore.i18n.txtEntidadeDoc2(), prefixo + ".empEntidadeDocumento2", 150);
		txtDoc2.setMaxLength(20);
		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), prefixo + ".empEntidadeAtivo");

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtDoc2, 170);
		linha2.addToRow(chkAtivo, new ColumnLayoutData(1));
		coluna2.add(linha2);

		Panel formColuna = new Panel();
		formColuna.setBorder(false);
		formColuna.setLayout(new ColumnLayout());
		formColuna.add(coluna1, new ColumnLayoutData(.5));
		formColuna.add(coluna2, new ColumnLayoutData(.5));
		add(formColuna);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), prefixo + ".empEntidadeObservacao");
		txtObservacao.setMaxLength(255);
		txtObservacao.setWidth("95%");
		add(txtObservacao);

		tabDados = new TabPanel();
		tabDados.setPlain(true);
		tabDados.setHeight(Ext.getBody().getHeight() - 340);
		tabDados.setActiveTab(0);

		gridContatos = new ListagemContato(true);
		gridEnderecos = new ListagemEndereco(true);
		tabDados.add(gridContatos);
		tabDados.add(gridEnderecos);
		add(tabDados);
	}

	public void gerarListas() {
		// contatos
		List<ExpMeta> metaCont = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridContatos.getModelos().getColumnCount(); i++) {
			if (gridContatos.getModelos().isHidden(i)) {
				metaCont.add(null);
			} else {
				metaCont.add(new ExpMeta(gridContatos.getModelos().getColumnHeader(i), gridContatos.getModelos().getColumnWidth(i), null));
			}
		}

		// trocando campos visiveis
		metaCont.set(2, metaCont.get(1));
		metaCont.set(1, null);

		SortState ordem = gridContatos.getStore().getSortState();
		EmpContato cont = new EmpContato();
		cont.setCampoOrdem(ordem.getField());
		cont.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		// filtro
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		int id = rec.getAsInteger("empEntidade.empEntidadeId");
		FiltroObjeto filtro = new FiltroObjeto("empEntidade", ECompara.IGUAL, new EmpEntidade(id));
		
		ExpListagem<EmpContato> contatos = new ExpListagem<EmpContato>();
		contatos.setClasse(cont);
		contatos.setMetadados(metaCont);
		contatos.setNome(gridContatos.getTitle());
		contatos.setFiltro(filtro);

		// enderecos
		List<ExpMeta> metaEnde = new ArrayList<ExpMeta>();
		for (int i = 0; i < gridEnderecos.getModelos().getColumnCount(); i++) {
			if (gridEnderecos.getModelos().isHidden(i)) {
				metaEnde.add(null);
			} else {
				metaEnde.add(new ExpMeta(gridEnderecos.getModelos().getColumnHeader(i), gridEnderecos.getModelos().getColumnWidth(i), null));
			}
		}

		// trocando campos visiveis
		metaEnde.set(2, metaEnde.get(1));
		metaEnde.set(6, metaEnde.get(5));
		metaEnde.set(1, null);
		metaEnde.set(5, null);

		ordem = gridEnderecos.getStore().getSortState();
		EmpEndereco ende = new EmpEndereco();
		ende.setCampoOrdem(ordem.getField());
		ende.setOrdemDirecao(EDirecao.valueOf(ordem.getDirection().getDirection()));
		
		ExpListagem<EmpEndereco> enderecos = new ExpListagem<EmpEndereco>();
		enderecos.setClasse(ende);
		enderecos.setMetadados(metaEnde);
		enderecos.setNome(gridEnderecos.getTitle());
		enderecos.setFiltro(filtro);

		// sub listagens
		expLista = new ArrayList<ExpListagem>();
		expLista.add(contatos);
		expLista.add(enderecos);
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		// salvando
		if (comando instanceof ComandoSalvar) {
			comando = new AComando(new ComandoSalvarFinal()) {
				public void execute(Map contexto) {
					super.execute(contexto);
					EmpresaProxy<E> proxy = new EmpresaProxy<E>();
					proxy.salvar(classe, ASYNC);
				}
			};
		}

		return comando;
	}

	public void limparDados() {
		getForm().reset();
		FiltroObjeto fo = new FiltroObjeto("empEntidade", ECompara.IGUAL, null);
		gridContatos.getProxy().setFiltroPadrao(fo);
		gridContatos.getStore().removeAll();
		gridEnderecos.getProxy().setFiltroPadrao(fo);
		gridEnderecos.getStore().removeAll();
	}

	public boolean setDados() {
		boolean retorno = true;
		List<EmpContato> contatos = new ArrayList<EmpContato>();
		List<EmpEndereco> enderecos = new ArrayList<EmpEndereco>();

		if (!gridContatos.validar(contatos)) {
			retorno = false;
			tabDados.setActiveItem(0);
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		if (!gridEnderecos.validar(enderecos)) {
			retorno = false;
			tabDados.setActiveItem(1);
			new ToastWindow(OpenSigCore.i18n.txtListagem(), OpenSigCore.i18n.errLista()).show();
		}

		entidade.setEmpEnderecos(enderecos);
		entidade.setEmpContatos(contatos);
		entidade.setEmpEntidadeId(Integer.valueOf(hdnCod.getValueAsString()));
		entidade.setEmpEntidadeNome1(txtNome1.getValueAsString());
		entidade.setEmpEntidadeNome2(txtNome2.getValueAsString());
		entidade.setEmpEntidadePessoa(cmbPessoa.getValue());
		entidade.setEmpEntidadeDocumento2(txtDoc2.getValueAsString() == null ? "" : txtDoc2.getValueAsString());
		entidade.setEmpEntidadeAtivo(chkAtivo.getValue());
		entidade.setEmpEntidadeObservacao(txtObservacao.getValueAsString());

		if (cmbPessoa.getValue().equalsIgnoreCase(fisica)) {
			entidade.setEmpEntidadeDocumento1(txtCPF.getValueAsString());
		} else {
			entidade.setEmpEntidadeDocumento1(txtCNPJ.getValueAsString());
		}
		return retorno;
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();

		if (rec != null) {
			getForm().loadRecord(rec);
			txtCNPJ.setValue(rec.getAsString(prefixo + ".empEntidadeDocumento1"));
			txtCPF.setValue(rec.getAsString(prefixo + ".empEntidadeDocumento1"));

			entidade.setEmpEntidadeId(Integer.valueOf(hdnCod.getValueAsString()));
			FiltroObjeto fo = new FiltroObjeto("empEntidade", ECompara.IGUAL, entidade);
			gridContatos.getProxy().setFiltroPadrao(fo);
			gridContatos.getStore().reload();

			gridEnderecos.getProxy().setFiltroPadrao(fo);
			gridEnderecos.getStore().reload();
		}

		if (duplicar) {
			hdnCod.setValue("0");
			hdnId.setValue("0");
			duplicar = false;
		}

		txtNome1.focus(true);
		tabDados.setActiveTab(0);
		mudarPessoa(cmbPessoa.getValue());
	}

	private ComboBox getPessoa() {
		Store store = new SimpleStore(new String[] { "id", "valor" }, new String[][] { new String[] { fisica, fisica }, new String[] { juridica, juridica } });
		store.load();

		cmbPessoa = new ComboBox(OpenSigCore.i18n.txtPessoa(), prefixo + ".empEntidadePessoa", 75);
		cmbPessoa.setForceSelection(true);
		cmbPessoa.setEditable(false);
		cmbPessoa.setMinChars(1);
		cmbPessoa.setStore(store);
		cmbPessoa.setDisplayField("valor");
		cmbPessoa.setValueField("id");
		cmbPessoa.setMode(ComboBox.LOCAL);
		cmbPessoa.setTriggerAction(ComboBox.ALL);
		cmbPessoa.setAllowBlank(false);
		cmbPessoa.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				mudarPessoa(record.getAsString("id"));
			}
		});

		return cmbPessoa;
	}

	private void mudarPessoa(String id) {
		if (id != null && id.toUpperCase().equalsIgnoreCase(fisica)) {
			txtCNPJ.hide();
			txtCNPJ.setValue("00000000000000");
			txtCPF.show();
		} else {
			txtCPF.hide();
			txtCPF.setValue("00000000000");
			txtCNPJ.show();
		}
	}

	public EmpEntidade getEntidade() {
		return entidade;
	}

	public void setEntidade(EmpEntidade entidade) {
		this.entidade = entidade;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnId() {
		return hdnId;
	}

	public void setHdnId(Hidden hdnId) {
		this.hdnId = hdnId;
	}

	public TextField getTxtNome1() {
		return txtNome1;
	}

	public void setTxtNome1(TextField txtNome1) {
		this.txtNome1 = txtNome1;
	}

	public TextField getTxtNome2() {
		return txtNome2;
	}

	public void setTxtNome2(TextField txtNome2) {
		this.txtNome2 = txtNome2;
	}

	public TextField getTxtCNPJ() {
		return txtCNPJ;
	}

	public void setTxtCNPJ(TextField txtCNPJ) {
		this.txtCNPJ = txtCNPJ;
	}

	public TextField getTxtCPF() {
		return txtCPF;
	}

	public void setTxtCPF(TextField txtCPF) {
		this.txtCPF = txtCPF;
	}

	public TextField getTxtDoc2() {
		return txtDoc2;
	}

	public void setTxtDoc2(TextField txtDoc2) {
		this.txtDoc2 = txtDoc2;
	}

	public TextArea getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(TextArea txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public ComboBox getCmbPessoa() {
		return cmbPessoa;
	}

	public void setCmbPessoa(ComboBox cmbPessoa) {
		this.cmbPessoa = cmbPessoa;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

	public TabPanel getTabDados() {
		return tabDados;
	}

	public void setTabDados(TabPanel tabDados) {
		this.tabDados = tabDados;
	}

	public ListagemContato getGridContatos() {
		return gridContatos;
	}

	public void setGridContatos(ListagemContato gridContatos) {
		this.gridContatos = gridContatos;
	}

	public ListagemEndereco getGridEnderecos() {
		return gridEnderecos;
	}

	public void setGridEnderecos(ListagemEndereco gridEnderecos) {
		this.gridEnderecos = gridEnderecos;
	}

	public String getFisica() {
		return fisica;
	}

	public void setFisica(String fisica) {
		this.fisica = fisica;
	}

	public String getJuridica() {
		return juridica;
	}

	public void setJuridica(String juridica) {
		this.juridica = juridica;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}
}
