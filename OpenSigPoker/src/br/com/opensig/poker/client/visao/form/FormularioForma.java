package br.com.opensig.poker.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.shared.modelo.PokerForma;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TextField;

public class FormularioForma extends AFormulario<PokerForma> {

	private Hidden hdnCod;
	private TextField txtNome;
	private Checkbox chkRealizado;
	private Checkbox chkJackpot;

	public FormularioForma(SisFuncao funcao) {
		super(new PokerForma(), funcao);
		inicializar();
	}

	public void inicializar() {
		hdnCod = new Hidden("pokerFormaId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "pokerFormaNome", 200);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(50);

		chkRealizado = new Checkbox(OpenSigCore.i18n.txtRealizado(), "pokerFormaRealizado");

		chkJackpot = new Checkbox(OpenSigCore.i18n.txtJackpot(), "pokerFormaJackpot");

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 220);
		linha1.addToRow(chkRealizado, 120);
		linha1.addToRow(chkJackpot, 120);
		add(linha1);

		super.inicializar();
	}

	public boolean setDados() {
		classe.setPokerFormaId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setPokerFormaNome(txtNome.getValueAsString());
		classe.setPokerFormaRealizado(chkRealizado.getValue());
		classe.setPokerFormaJackpot(chkJackpot.getValue());
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

	public Checkbox getChkRealizado() {
		return chkRealizado;
	}

	public void setChkRealizado(Checkbox chkRealizado) {
		this.chkRealizado = chkRealizado;
	}

	public Checkbox getChkJackpot() {
		return chkJackpot;
	}

	public void setChkJackpot(Checkbox chkJackpot) {
		this.chkJackpot = chkJackpot;
	}

}
