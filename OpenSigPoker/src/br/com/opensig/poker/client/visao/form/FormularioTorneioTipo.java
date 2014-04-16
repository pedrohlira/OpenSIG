package br.com.opensig.poker.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.shared.modelo.PokerTorneioTipo;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;

public class FormularioTorneioTipo extends AFormulario<PokerTorneioTipo> {

	private Hidden hdnCod;
	private TextField txtNome;

	public FormularioTorneioTipo(SisFuncao funcao) {
		super(new PokerTorneioTipo(), funcao);
		inicializar();
	}

	public void inicializar() {
		hdnCod = new Hidden("pokerTorneioTipoId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "pokerTorneioTipoNome", 200);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(50);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 220);
		add(linha1);
		
		super.inicializar();
	}

	public boolean setDados() {
		classe.setPokerTorneioTipoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setPokerTorneioTipoNome(txtNome.getValueAsString());
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
}
