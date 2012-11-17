package br.com.opensig.permissao.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.permissao.shared.modelo.SisConfiguracao;

import com.google.gwt.dom.client.Style.FontWeight;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;

public class FormularioConfiguracao extends AFormulario<SisConfiguracao> {

	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private TextField txtChave;
	private TextField txtValor;
	private TextArea txtDescricao;
	private Checkbox chkAtivo;
	private Checkbox chkSistema;
	private Label lblMensagem;

	public FormularioConfiguracao(SisFuncao funcao) {
		super(new SisConfiguracao(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("sisConfiguracaoId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtChave = new TextField(OpenSigCore.i18n.txtChave(), "sisConfiguracaoChave", 300);
		txtChave.setAllowBlank(false);
		txtChave.setMaxLength(255);

		txtValor = new TextField(OpenSigCore.i18n.txtValor(), "sisConfiguracaoValor", 300);
		txtValor.setAllowBlank(false);
		txtValor.setMaxLength(255);
		
		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "sisConfiguracaoAtivo");
		chkAtivo.setValue(true);

		chkSistema = new Checkbox(OpenSigCore.i18n.txtSistema(), "sisConfiguracaoSistema");
		chkSistema.setValue(false);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtChave, 320);
		linha1.addToRow(txtValor, 320);
		linha1.addToRow(chkAtivo, 70);
		if (Ponte.getLogin().getId() == 1) {
			linha1.addToRow(chkSistema, 70);
		}
		add(linha1);

		txtDescricao = new TextArea(OpenSigCore.i18n.txtDescricao(), "sisConfiguracaoDescricao");
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(255);
		txtDescricao.setWidth("95%");
		add(txtDescricao);

		lblMensagem = new Label(OpenSigCore.i18n.msgConfigMensagem());
		lblMensagem.getElement().getStyle().setColor("red");
		lblMensagem.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		add(lblMensagem);
	}

	public boolean setDados() {
		classe.setSisConfiguracaoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setSisConfiguracaoChave(txtChave.getValueAsString());
		classe.setSisConfiguracaoValor(txtValor.getValueAsString());
		classe.setSisConfiguracaoAtivo(chkAtivo.getValue());
		classe.setSisConfiguracaoSistema(chkSistema.getValue());
		classe.setSisConfiguracaoDescricao(txtDescricao.getValueAsString());
		
		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}

		return true;
	}

	public void limparDados() {
		getForm().reset();
	}

	public void mostrarDados() {
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		}
		txtChave.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public TextField getTxtChave() {
		return txtChave;
	}

	public void setTxtChave(TextField txtChave) {
		this.txtChave = txtChave;
	}

	public TextField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(TextField txtValor) {
		this.txtValor = txtValor;
	}

	public TextArea getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextArea txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public Label getLblMensagem() {
		return lblMensagem;
	}

	public void setLblMensagem(Label lblMensagem) {
		this.lblMensagem = lblMensagem;
	}

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
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

}
