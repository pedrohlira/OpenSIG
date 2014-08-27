package br.com.opensig.financeiro.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.shared.modelo.FinCartaoPresente;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FormularioCartaoPresente extends AFormulario<FinCartaoPresente> {

	private Hidden hdnCod;
	private TextField txtNumero;
	private NumberField txtValor;
	private Checkbox chkAtivo;

	public FormularioCartaoPresente(SisFuncao funcao) {
		super(new FinCartaoPresente(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("finCartaoPresenteId", "0");
		add(hdnCod);

		txtNumero = new TextField(OpenSigCore.i18n.txtNumero(), "finCartaoPresenteNumero", 120);
		txtNumero.setMaxLength(14);

		txtValor = new NumberField(OpenSigCore.i18n.txtValor(), "finCartaoPresenteValor", 80);
		txtValor.setAllowBlank(false);
		txtValor.setMaxLength(11);
		txtValor.setDecimalPrecision(2);

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "finCartaoPresenteAtivo");

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNumero, 140);
		linha1.addToRow(txtValor, 100);
		linha1.addToRow(chkAtivo, 80);
		add(linha1);
	}

	public boolean setDados() {
		classe.setFinCartaoPresenteId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setFinCartaoPresenteNumero(txtNumero.getValueAsString());
		if (txtValor.getValue() != null) {
			classe.setFinCartaoPresenteValor(txtValor.getValue().doubleValue());
		}
		classe.setFinCartaoPresenteAtivo(chkAtivo.getValue());
		return true;
	}

	public void limparDados() {
		getForm().reset();
	}

	public void mostrarDados() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		}
		txtNumero.focus(true);

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

	public TextField getTxtNumero() {
		return txtNumero;
	}

	public void setTxtNumero(TextField txtNumero) {
		this.txtNumero = txtNumero;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

}
