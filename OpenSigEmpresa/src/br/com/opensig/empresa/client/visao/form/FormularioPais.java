package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpPais;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FormularioPais extends AFormulario<EmpPais> {

	private Hidden hdnCod;
	private NumberField txtIbge;
	private TextField txtPais;
	private TextField txtSigla;

	public FormularioPais(SisFuncao funcao) {
		super(new EmpPais(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("empPaisId", "0");
		add(hdnCod);

		txtIbge = new NumberField(OpenSigCore.i18n.txtIbge(), "empPaisIbge", 40);
		txtIbge.setAllowBlank(false);
		txtIbge.setAllowDecimals(false);
		txtIbge.setAllowNegative(false);
		txtIbge.setMinLength(4);
		txtIbge.setMaxLength(4);

		txtPais = new TextField(OpenSigCore.i18n.txtNome(), "empPaisDescricao",
				250);
		txtPais.setAllowBlank(false);
		txtPais.setMaxLength(100);

		txtSigla = new TextField(OpenSigCore.i18n.txtSigla(), "empPaisSigla", 40);
		txtSigla.setAllowBlank(false);
		txtSigla.setMinLength(2);
		txtSigla.setMaxLength(2);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtIbge, 60);
		linha1.addToRow(txtPais, 270);
		linha1.addToRow(txtSigla, 60);
		add(linha1);
	}

	public boolean setDados() {
		classe.setEmpPaisId(Integer.valueOf(hdnCod.getValueAsString()));
		if (txtIbge.getValue() != null) {
			classe.setEmpPaisIbge(txtIbge.getValue().intValue());
		}
		classe.setEmpPaisDescricao(txtPais.getValueAsString());
		classe.setEmpPaisSigla(txtSigla.getValueAsString().toUpperCase());
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
		txtIbge.focus(true);
		
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

	public NumberField getTxtIbge() {
		return txtIbge;
	}

	public void setTxtIbge(NumberField txtIbge) {
		this.txtIbge = txtIbge;
	}

	public TextField getTxtPais() {
		return txtPais;
	}

	public void setTxtPais(TextField txtPais) {
		this.txtPais = txtPais;
	}

	public TextField getTxtSigla() {
		return txtSigla;
	}

	public void setTxtSigla(TextField txtSigla) {
		this.txtSigla = txtSigla;
	}

}
