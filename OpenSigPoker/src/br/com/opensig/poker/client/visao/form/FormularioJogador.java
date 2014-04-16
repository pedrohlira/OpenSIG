package br.com.opensig.poker.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.EJuncao;
import br.com.opensig.core.client.controlador.filtro.FiltroBinario;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.GrupoFiltro;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.shared.modelo.PokerCash;
import br.com.opensig.poker.shared.modelo.PokerCliente;
import br.com.opensig.poker.shared.modelo.PokerJogador;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class FormularioJogador extends AFormulario<PokerJogador> {

	private Hidden hdnCod;
	private Hidden hdnCliente;
	private Hidden hdnCash;
	private ComboBox cmbCliente;
	private ComboBox cmbCash;
	private Checkbox chkAtivo;

	public FormularioJogador(SisFuncao funcao) {
		super(new PokerJogador(), funcao);
		inicializar();
	}

	public void inicializar() {
		hdnCod = new Hidden("pokerJogadorId", "0");
		add(hdnCod);

		hdnCliente = new Hidden("pokerCliente.pokerClienteId", "0");
		add(hdnCliente);

		hdnCash = new Hidden("pokerCash.pokerCashId", "0");
		add(hdnCash);

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "pokerJogadorAtivo");
		chkAtivo.setValue(true);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getCash(), 150);
		linha1.addToRow(getCliente(), 200);
		linha1.addToRow(chkAtivo, 100);
		add(linha1);

		super.inicializar();
	}

	public boolean setDados() {
		classe.setPokerJogadorId(Integer.valueOf(hdnCod.getValueAsString()));
		if (!hdnCliente.getValueAsString().equals("0")) {
			PokerCliente cliente = new PokerCliente(Integer.valueOf(hdnCliente.getValueAsString()));
			classe.setPokerCliente(cliente);
		}
		if (!hdnCash.getValueAsString().equals("0")) {
			PokerCash cash = new PokerCash(Integer.valueOf(hdnCash.getValueAsString()));
			classe.setPokerCash(cash);
		}
		classe.setPokerJogadorAtivo(chkAtivo.getValue());

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
		cmbCash.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnCash.setValue("0");
			cmbCash.setRawValue("");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	private ComboBox getCash() {
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("pokerCashId"), new StringFieldDef("pokerCashCodigo") };
		FiltroBinario fb = new FiltroBinario("pokerCashFechado", ECompara.IGUAL, 0);
		CoreProxy<PokerCash> proxy = new CoreProxy<PokerCash>(new PokerCash(), fb);
		Store store = new Store(proxy, new ArrayReader(new RecordDef(campos)), false);

		cmbCash = new ComboBox(OpenSigCore.i18n.txtCash(), "pokerCash.pokerCashCodigo", 130);
		cmbCash.setAllowBlank(false);
		cmbCash.setStore(store);
		cmbCash.setTriggerAction(ComboBox.ALL);
		cmbCash.setMode(ComboBox.REMOTE);
		cmbCash.setDisplayField("pokerCashCodigo");
		cmbCash.setValueField("pokerCashId");
		cmbCash.setForceSelection(true);
		cmbCash.setEditable(false);
		cmbCash.setListWidth(200);
		cmbCash.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnCash.setValue(comboBox.getValue());
				PokerCash cash = new PokerCash(Integer.valueOf(hdnCash.getValueAsString()));
				// filtra os clientes
				FiltroObjeto fo1 = new FiltroObjeto("pokerCash", ECompara.NULO, null);
				fo1.setCampoPrefixo("t2.");
				FiltroNumero fn1 = new FiltroNumero("pokerCash.pokerCashId", ECompara.DIFERENTE, cash.getId());
				fn1.setCampoPrefixo("t2.");
				GrupoFiltro gf1 = new GrupoFiltro(EJuncao.OU, new IFiltro[] { fo1, fn1 });
				FiltroBinario fb1 = new FiltroBinario("pokerClienteAtivo", ECompara.IGUAL, 1);
				GrupoFiltro gf2 = new GrupoFiltro(EJuncao.E, new IFiltro[] { gf1, fb1 });
				CoreProxy<PokerCliente> proxy1 = new CoreProxy<PokerCliente>(new PokerCliente(), gf2);
				cmbCliente.getStore().setDataProxy(proxy1);
				cmbCliente.getStore().reload();
			}

			public void onBlur(Field field) {
				if (cmbCash.getRawValue().equals("")) {
					hdnCash.setValue("0");
				}
			}
		});

		return cmbCash;
	}

	private ComboBox getCliente() {
		FieldDef[] campos = new FieldDef[] { new IntegerFieldDef("pokerClienteId"), new IntegerFieldDef("pokerClienteCodigo"), new StringFieldDef("pokerClienteNome") };
		Store store = new Store(new ArrayReader(new RecordDef(campos)));

		cmbCliente = new ComboBox(OpenSigCore.i18n.txtCliente(), "pokerCliente.pokerClienteNome", 180);
		cmbCliente.setAllowBlank(false);
		cmbCliente.setStore(store);
		cmbCliente.setTriggerAction(ComboBox.ALL);
		cmbCliente.setMode(ComboBox.REMOTE);
		cmbCliente.setDisplayField("pokerClienteNome");
		cmbCliente.setValueField("pokerClienteId");
		cmbCliente.setForceSelection(true);
		cmbCliente.setEditable(true);
		cmbCliente.setMinChars(1);
		cmbCliente.setTypeAhead(true);
		cmbCliente.setListWidth(200);
		cmbCliente.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnCliente.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbCliente.getRawValue().equals("")) {
					hdnCliente.setValue("0");
				}
			}
		});

		return cmbCliente;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnCliente() {
		return hdnCliente;
	}

	public void setHdnCliente(Hidden hdnCliente) {
		this.hdnCliente = hdnCliente;
	}

	public Hidden getHdnCash() {
		return hdnCash;
	}

	public void setHdnCash(Hidden hdnCash) {
		this.hdnCash = hdnCash;
	}

	public ComboBox getCmbCliente() {
		return cmbCliente;
	}

	public void setCmbCliente(ComboBox cmbCliente) {
		this.cmbCliente = cmbCliente;
	}

	public ComboBox getCmbCash() {
		return cmbCash;
	}

	public void setCmbCash(ComboBox cmbCash) {
		this.cmbCash = cmbCash;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

}
