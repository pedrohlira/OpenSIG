package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEnderecoTipo;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.TextField;

public class FormularioEnderecoTipo extends AFormulario<EmpEnderecoTipo> {

	private Hidden hdnCod;
	private TextField txtTipo;

	public FormularioEnderecoTipo(SisFuncao funcao) {
		super(new EmpEnderecoTipo(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("empEnderecoTipoId", "0");
		add(hdnCod);

		txtTipo = new TextField(OpenSigCore.i18n.txtDescricao(), "empEnderecoTipoDescricao", 250);
		txtTipo.setAllowBlank(false);
		txtTipo.setMaxLength(100);
		add(txtTipo);
	}

	public boolean setDados() {
		classe.setEmpEnderecoTipoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setEmpEnderecoTipoDescricao(txtTipo.getValueAsString());
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
		txtTipo.focus(true);

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

	public TextField getTxtTipo() {
		return txtTipo;
	}

	public void setTxtTipo(TextField txtTipo) {
		this.txtTipo = txtTipo;
	}
}
