package br.com.opensig.produto.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.produto.shared.modelo.ProdGradeTipo;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;

public class FormularioGradeTipo extends AFormulario<ProdGradeTipo> {

	private Hidden hdnCod;
	private TextField txtNome;
	private ComboBox cmbOpcao;

	public FormularioGradeTipo(SisFuncao funcao) {
		super(new ProdGradeTipo(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("prodGradeTipoId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "prodGradeTipoNome", 200);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(50);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 220);
		linha1.addToRow(getOpcao(), 120);
		add(linha1);
	}

	public boolean setDados() {
		classe.setProdGradeTipoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setProdGradeTipoNome(txtNome.getValueAsString());
		classe.setProdGradeTipoOpcao(cmbOpcao.getValue());
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

	private ComboBox getOpcao() {
		Store store = new SimpleStore(new String[] { "id", "valor" }, new String[][] { new String[] { "T", OpenSigCore.i18n.txtTamanho().toUpperCase() },
				new String[] { "C", OpenSigCore.i18n.txtCor().toUpperCase() }, new String[] { "O", OpenSigCore.i18n.txtOpcao().toUpperCase() } });
		store.load();

		cmbOpcao = new ComboBox(OpenSigCore.i18n.txtOpcao(), "prodGradeTipoOpcao", 100);
		cmbOpcao.setForceSelection(true);
		cmbOpcao.setEditable(false);
		cmbOpcao.setStore(store);
		cmbOpcao.setDisplayField("valor");
		cmbOpcao.setValueField("id");
		cmbOpcao.setMode(ComboBox.LOCAL);
		cmbOpcao.setTriggerAction(ComboBox.ALL);
		cmbOpcao.setAllowBlank(false);

		return cmbOpcao;
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

	public ComboBox getCmbOpcao() {
		return cmbOpcao;
	}

	public void setCmbOpcao(ComboBox cmbOpcao) {
		this.cmbOpcao = cmbOpcao;
	}

}
