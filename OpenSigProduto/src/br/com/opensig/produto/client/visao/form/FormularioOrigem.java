package br.com.opensig.produto.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.shared.modelo.ProdOrigem;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FormularioOrigem extends AFormulario<ProdOrigem> {

	private Hidden hdnCod;
	private NumberField txtValor;
	private TextField txtDescricao;

	public FormularioOrigem(SisFuncao funcao) {
		super(new ProdOrigem(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("prodOrigemId", "0");
		add(hdnCod);

		txtValor = new NumberField(OpenSigCore.i18n.txtValor(), "prodOrigemValor", 50);
		txtValor.setAllowBlank(false);
		txtValor.setAllowNegative(false);
		txtValor.setAllowDecimals(false);

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "prodOrigemDescricao", 400);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(255);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtValor, 70);
		linha1.addToRow(txtDescricao, 420);
		add(linha1);
	}

	public boolean setDados() {
		classe.setProdOrigemId(Integer.valueOf(hdnCod.getValueAsString()));
		if (txtValor.getValue() != null) {
			classe.setProdOrigemValor(txtValor.getValue().intValue());
		}
		classe.setProdOrigemDescricao(txtDescricao.getValueAsString());
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

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

}
