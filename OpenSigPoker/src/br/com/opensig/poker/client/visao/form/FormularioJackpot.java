package br.com.opensig.poker.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.shared.modelo.PokerForma;
import br.com.opensig.poker.shared.modelo.PokerJackpot;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class FormularioJackpot extends AFormulario<PokerJackpot> {

	private Hidden hdnCod;
	private Hidden hdnForma;
	private ComboBox cmbForma;
	private NumberField txtTotal;

	public FormularioJackpot(SisFuncao funcao) {
		super(new PokerJackpot(), funcao);
		inicializar();
	}

	public void inicializar() {
		hdnCod = new Hidden("pokerJackpotId", "0");
		add(hdnCod);

		hdnForma = new Hidden("pokerForma.pokerFormaId", "0");
		add(hdnForma);

		txtTotal = new NumberField(OpenSigCore.i18n.txtTotal(), "pokerJackpotTotal", 100, 0);
		txtTotal.setAllowBlank(false);
		txtTotal.setAllowNegative(false);
		txtTotal.setDecimalPrecision(2);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getForma(), 150);
		linha1.addToRow(txtTotal, 120);
		add(linha1);

		super.inicializar();
	}

	public boolean setDados() {
		boolean retorno = true;
		classe.setPokerJackpotId(Integer.valueOf(hdnCod.getValueAsString()));
		if (!hdnForma.getValueAsString().equals("0")) {
			PokerForma forma = new PokerForma(Integer.valueOf(hdnForma.getValueAsString()));
			classe.setPokerForma(forma);
		}
		if (txtTotal.getValue() != null) {
			classe.setPokerJackpotTotal(txtTotal.getValue().intValue());
		}
		return retorno;
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

	private ComboBox getForma() {
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("pokerFormaId"), new StringFieldDef("pokerFormaNome") };
		FiltroBinario fb = new FiltroBinario("pokerFormaJackpot", ECompara.IGUAL, 1);
		CoreProxy<PokerForma> proxy = new CoreProxy<PokerForma>(new PokerForma(), fb);
		Store store = new Store(proxy, new ArrayReader(new RecordDef(campos)), false);

		cmbForma = new ComboBox(OpenSigCore.i18n.txtTipo(), "pokerForma.pokerFormaNome", 130);
		cmbForma.setAllowBlank(false);
		cmbForma.setStore(store);
		cmbForma.setTriggerAction(ComboBox.ALL);
		cmbForma.setMode(ComboBox.REMOTE);
		cmbForma.setMinChars(1);
		cmbForma.setDisplayField("pokerFormaNome");
		cmbForma.setValueField("pokerFormaId");
		cmbForma.setForceSelection(true);
		cmbForma.setEditable(false);
		cmbForma.setListWidth(130);
		cmbForma.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnForma.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbForma.getRawValue().equals("")) {
					hdnForma.setValue("0");
				}
			}
		});

		return cmbForma;
	}

}
