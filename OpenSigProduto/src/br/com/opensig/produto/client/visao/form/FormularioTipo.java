package br.com.opensig.produto.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.shared.modelo.ProdTipo;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;

public class FormularioTipo extends AFormulario<ProdTipo> {

	private Hidden hdnCod;
	private TextField txtValor;
	private TextField txtDescricao;

	public FormularioTipo(SisFuncao funcao) {
		super(new ProdTipo(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("prodTipoId", "0");
		add(hdnCod);

		txtValor = new TextField(OpenSigCore.i18n.txtValor(), "prodTipoValor", 100);
		txtValor.setAllowBlank(false);
		txtValor.setMinLength(2);
		txtValor.setMaxLength(2);
		txtValor.setRegex("^\\d{2}$");

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "prodTipoDescricao", 400);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(100);
		
		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtValor, 120);
		linha1.addToRow(txtDescricao, 420);
		add(linha1);
	}

	public boolean setDados() {
		classe.setProdTipoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setProdTipoValor(txtValor.getValueAsString());
		classe.setProdTipoDescricao(txtDescricao.getValueAsString());
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

	public TextField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(TextField txtValor) {
		this.txtValor = txtValor;
	}

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

}
