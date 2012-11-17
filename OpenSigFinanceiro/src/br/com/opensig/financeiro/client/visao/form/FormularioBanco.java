package br.com.opensig.financeiro.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.shared.modelo.FinBanco;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;

public class FormularioBanco extends AFormulario<FinBanco> {

	private Hidden hdnCod;
	private TextField txtNumero;
	private TextField txtDescricao;

	public FormularioBanco(SisFuncao funcao) {
		super(new FinBanco(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("finBancoId", "0");
		add(hdnCod);

		txtNumero = new TextField(OpenSigCore.i18n.txtNumero(), "finBancoNumero", 50);
		txtNumero.setAllowBlank(false);
		txtNumero.setMinLength(3);
		txtNumero.setMaxLength(5);

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "finBancoDescricao", 300);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(100);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNumero, 70);
		linha1.addToRow(txtDescricao, 330);
		add(linha1);
	}

	public boolean setDados() {
		classe.setFinBancoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setFinBancoNumero(txtNumero.getValueAsString());
		classe.setFinBancoDescricao(txtDescricao.getValueAsString());
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

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}
}
