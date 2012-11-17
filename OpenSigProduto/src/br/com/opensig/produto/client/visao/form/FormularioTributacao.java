package br.com.opensig.produto.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.shared.modelo.ProdTributacao;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;

public class FormularioTributacao extends AFormulario<ProdTributacao> {

	private Hidden hdnCod;
	private TextField txtNome;
	private TextField txtCst;
	private TextField txtCson;
	private NumberField txtCfop;
	private TextField txtEcf;
	private NumberField txtDentro;
	private NumberField txtFora;
	private TextArea txtDecreto;

	public FormularioTributacao(SisFuncao funcao) {
		super(new ProdTributacao(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("prodTributacaoId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "prodTributacaoNome", 250);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(100);
		txtNome.focus();

		txtCst = new TextField(OpenSigCore.i18n.txtCst(), "prodTributacaoCst", 50);
		txtCst.setAllowBlank(false);
		txtCst.setRegex("\\d{2}");
		txtCst.setMinLength(2);
		txtCst.setMaxLength(2);

		txtCson = new TextField(OpenSigCore.i18n.txtCson(), "prodTributacaoCson", 50);
		txtCson.setAllowBlank(false);
		txtCson.setRegex("\\d{3}");
		txtCson.setMinLength(3);
		txtCson.setMaxLength(3);

		txtCfop = new NumberField(OpenSigCore.i18n.txtCfop(), "prodTributacaoCfop", 50);
		txtCfop.setAllowBlank(false);
		txtCfop.setAllowDecimals(false);
		txtCfop.setAllowNegative(false);
		txtCfop.setMinLength(4);
		txtCfop.setMaxLength(4);

		txtEcf = new TextField(OpenSigCore.i18n.txtEcf(), "prodTributacaoEcf", 50);
		txtEcf.setMinLength(2);
		txtEcf.setMaxLength(7);

		txtDentro = new NumberField(OpenSigCore.i18n.txtDentro() + " %", "prodTributacaoDentro", 50);
		txtDentro.setAllowBlank(false);
		txtDentro.setAllowNegative(false);
		txtDentro.setDecimalPrecision(2);
		txtDentro.setMaxLength(5);

		txtFora = new NumberField(OpenSigCore.i18n.txtFora() + " %", "prodTributacaoFora", 50);
		txtFora.setAllowBlank(false);
		txtFora.setAllowNegative(false);
		txtFora.setDecimalPrecision(2);
		txtFora.setMaxLength(5);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 270);
		linha1.addToRow(txtCst, 70);
		linha1.addToRow(txtCson, 70);
		linha1.addToRow(txtCfop, 70);
		linha1.addToRow(txtEcf, 70);
		linha1.addToRow(txtDentro, 70);
		linha1.addToRow(txtFora, 70);
		add(linha1);

		txtDecreto = new TextArea(OpenSigCore.i18n.txtDecreto(), "prodTributacaoDecreto");
		txtDecreto.setMaxLength(1000);
		txtDecreto.setWidth("95%");
		add(txtDecreto);
	}

	public boolean setDados() {
		classe.setProdTributacaoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setProdTributacaoNome(txtNome.getValueAsString());
		classe.setProdTributacaoCst(txtCst.getValueAsString());
		classe.setProdTributacaoCson(txtCson.getValueAsString());
		classe.setProdTributacaoEcf(txtEcf.getValueAsString());
		if (txtCfop.getValue() != null) {
			classe.setProdTributacaoCfop(txtCfop.getValue().intValue());
		}
		if (txtDentro.getValue() != null) {
			classe.setProdTributacaoDentro(txtDentro.getValue().doubleValue());
		}
		if (txtFora.getValue() != null) {
			classe.setProdTributacaoFora(txtFora.getValue().doubleValue());
		}
		classe.setProdTributacaoDecreto(txtDecreto.getValueAsString() == null ? "" : txtDecreto.getValueAsString());
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
		txtNome.focus(true);

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

	public TextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(TextField txtNome) {
		this.txtNome = txtNome;
	}

	public TextField getTxtCst() {
		return txtCst;
	}

	public void setTxtCst(TextField txtCst) {
		this.txtCst = txtCst;
	}

	public TextField getTxtCson() {
		return txtCson;
	}

	public void setTxtCson(TextField txtCson) {
		this.txtCson = txtCson;
	}

	public NumberField getTxtCfop() {
		return txtCfop;
	}

	public void setTxtCfop(NumberField txtCfop) {
		this.txtCfop = txtCfop;
	}

	public TextField getTxtEcf() {
		return txtEcf;
	}

	public void setTxtEcf(TextField txtEcf) {
		this.txtEcf = txtEcf;
	}

	public NumberField getTxtDentro() {
		return txtDentro;
	}

	public void setTxtDentro(NumberField txtDentro) {
		this.txtDentro = txtDentro;
	}

	public NumberField getTxtFora() {
		return txtFora;
	}

	public void setTxtFora(NumberField txtFora) {
		this.txtFora = txtFora;
	}

	public TextArea getTxtDecreto() {
		return txtDecreto;
	}

	public void setTxtDecreto(TextArea txtDecreto) {
		this.txtDecreto = txtDecreto;
	}
}
