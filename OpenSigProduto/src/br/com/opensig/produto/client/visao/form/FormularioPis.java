package br.com.opensig.produto.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.shared.modelo.ProdPis;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;

public class FormularioPis extends AFormulario<ProdPis> {

	private Hidden hdnCod;
	private TextField txtNome;
	private TextField txtCstEntrada;
	private TextField txtCstSaida;
	private NumberField txtAliquota;
	private TextArea txtDecreto;

	public FormularioPis(SisFuncao funcao) {
		super(new ProdPis(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("prodPisId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "prodPisNome", 250);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(100);
		txtNome.focus();

		txtCstEntrada = new TextField(OpenSigCore.i18n.txtCst() + " " + OpenSigCore.i18n.txtEntrada(), "prodPisCstEntrada", 50);
		txtCstEntrada.setAllowBlank(false);
		txtCstEntrada.setRegex("\\d{2}");
		txtCstEntrada.setMinLength(2);
		txtCstEntrada.setMaxLength(2);

		txtCstSaida = new TextField(OpenSigCore.i18n.txtCst() + " " + OpenSigCore.i18n.txtSaida(), "prodPisCstSaida", 50);
		txtCstSaida.setAllowBlank(false);
		txtCstSaida.setRegex("\\d{2}");
		txtCstSaida.setMinLength(2);
		txtCstSaida.setMaxLength(2);

		txtAliquota = new NumberField(OpenSigCore.i18n.txtAliquota() + " %", "prodPisAliquota", 50);
		txtAliquota.setAllowBlank(false);
		txtAliquota.setAllowNegative(false);
		txtAliquota.setDecimalPrecision(2);
		txtAliquota.setMaxLength(5);
		
		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 270);
		linha1.addToRow(txtCstEntrada, 100);
		linha1.addToRow(txtCstSaida, 100);
		linha1.addToRow(txtAliquota, 100);
		add(linha1);

		txtDecreto = new TextArea(OpenSigCore.i18n.txtDecreto(), "prodPisDecreto");
		txtDecreto.setMaxLength(1000);
		txtDecreto.setWidth("95%");
		add(txtDecreto);
	}

	public boolean setDados() {
		classe.setProdPisId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setProdPisNome(txtNome.getValueAsString());
		classe.setProdPisCstEntrada(txtCstEntrada.getValueAsString());
		classe.setProdPisCstSaida(txtCstSaida.getValueAsString());
		if (txtAliquota.getValue() != null) {
			classe.setProdPisAliquota(txtAliquota.getValue().doubleValue());
		}
		classe.setProdPisDecreto(txtDecreto.getValueAsString() == null ? "" : txtDecreto.getValueAsString());
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

	public TextField getTxtCstEntrada() {
		return txtCstEntrada;
	}

	public void setTxtCstEntrada(TextField txtCstEntrada) {
		this.txtCstEntrada = txtCstEntrada;
	}

	public TextField getTxtCstSaida() {
		return txtCstSaida;
	}

	public void setTxtCstSaida(TextField txtCstSaida) {
		this.txtCstSaida = txtCstSaida;
	}

	public NumberField getTxtAliquota() {
		return txtAliquota;
	}

	public void setTxtAliquota(NumberField txtAliquota) {
		this.txtAliquota = txtAliquota;
	}

	public TextArea getTxtDecreto() {
		return txtDecreto;
	}

	public void setTxtDecreto(TextArea txtDecreto) {
		this.txtDecreto = txtDecreto;
	}

}
