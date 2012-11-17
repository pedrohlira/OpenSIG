package br.com.opensig.financeiro.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.shared.modelo.FinForma;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;

public class FormularioForma extends AFormulario<FinForma> {

	private Hidden hdnCod;
	private TextField txtDescricao;
	private TextField txtCodigo;
	private Checkbox chkTef;
	private Checkbox chkVinculado;
	private Checkbox chkDebito;
	private TextField txtRede;
	private Checkbox chkPagar;
	private Checkbox chkReceber;

	public FormularioForma(SisFuncao funcao) {
		super(new FinForma(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("finFormaId", "0");
		add(hdnCod);

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "finFormaDescricao", 200);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(20);

		txtCodigo = new TextField(OpenSigCore.i18n.txtCodigo(), "finFormaCodigo", 50);
		txtCodigo.setAllowBlank(false);
		txtCodigo.setMaxLength(2);

		chkTef = new Checkbox(OpenSigCore.i18n.txtTef(), "finFormaTef");
		chkVinculado = new Checkbox(OpenSigCore.i18n.txtVinculado(), "finFormaVinculado");
		chkDebito = new Checkbox(OpenSigCore.i18n.txtDebito(), "finFormaDebito");

		txtRede = new TextField(OpenSigCore.i18n.txtRede(), "finFormaRede", 100);
		txtRede.setAllowBlank(false);
		txtRede.setMaxLength(20);

		chkPagar = new Checkbox(OpenSigCore.i18n.txtPagar(), "finFormaPagar");
		chkReceber = new Checkbox(OpenSigCore.i18n.txtReceber(), "finFormaReceber");

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtDescricao, 220);
		linha1.addToRow(txtCodigo, 70);
		linha1.addToRow(chkTef, 70);
		linha1.addToRow(chkVinculado, 100);
		linha1.addToRow(chkDebito, 70);
		linha1.addToRow(txtRede, 120);
		linha1.addToRow(chkPagar, 70);
		linha1.addToRow(chkReceber, 70);
		add(linha1);
	}

	public boolean setDados() {
		classe.setFinFormaId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setFinFormaDescricao(txtDescricao.getValueAsString());
		classe.setFinFormaCodigo(txtCodigo.getValueAsString());
		classe.setFinFormaTef(chkTef.getValue());
		classe.setFinFormaVinculado(chkVinculado.getValue());
		classe.setFinFormaDebito(chkDebito.getValue());
		classe.setFinFormaRede(txtRede.getValueAsString());
		classe.setFinFormaPagar(chkPagar.getValue());
		classe.setFinFormaReceber(chkReceber.getValue());

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
		txtCodigo.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
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

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public TextField getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(TextField txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

	public Checkbox getChkTef() {
		return chkTef;
	}

	public void setChkTef(Checkbox chkTef) {
		this.chkTef = chkTef;
	}

	public Checkbox getChkVinculado() {
		return chkVinculado;
	}

	public void setChkVinculado(Checkbox chkVinculado) {
		this.chkVinculado = chkVinculado;
	}

	public Checkbox getChkDebito() {
		return chkDebito;
	}

	public void setChkDebito(Checkbox chkDebito) {
		this.chkDebito = chkDebito;
	}

	public TextField getTxtRede() {
		return txtRede;
	}

	public void setTxtRede(TextField txtRede) {
		this.txtRede = txtRede;
	}

	public Checkbox getChkPagar() {
		return chkPagar;
	}

	public void setChkPagar(Checkbox chkPagar) {
		this.chkPagar = chkPagar;
	}

	public Checkbox getChkReceber() {
		return chkReceber;
	}

	public void setChkReceber(Checkbox chkReceber) {
		this.chkReceber = chkReceber;
	}

}
