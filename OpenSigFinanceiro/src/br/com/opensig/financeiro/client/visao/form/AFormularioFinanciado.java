package br.com.opensig.financeiro.client.visao.form;

import java.util.HashMap;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.shared.modelo.FinBanco;
import br.com.opensig.financeiro.shared.modelo.FinForma;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

public abstract class AFormularioFinanciado<E extends Dados> extends AFormulario<E> {

	protected E financeiro;
	protected Hidden hdnCod;
	protected Hidden hdnFinanceiro;
	protected ComboBox cmbForma;
	protected Store stBanco;
	protected TextField txtDocumento;
	protected TextField txtParcela;
	protected NumberField txtValor;
	protected DateField dtVencimento;
	protected TextArea txtObservacao;
	protected Label lblBanco;
	protected Label lblAgencia;
	protected Label lblConta;
	protected Label lblNumero;
	protected FieldSet fsCheque;
	protected Map<String, String> nomes;

	public AFormularioFinanciado(E classe, SisFuncao funcao) {
		super(classe, funcao);
		nomes = new HashMap();
	}

	public void inicializar() {
		hdnCod = new Hidden(nomes.get("id"), "0");
		add(hdnCod);
		hdnFinanceiro = new Hidden(nomes.get("financeiroId"), "0");
		add(hdnFinanceiro);

		txtDocumento = new TextField(OpenSigCore.i18n.txtDocumento(), nomes.get("documento"), 250);
		txtDocumento.setAllowBlank(true);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getForma(), 150);
		linha1.addToRow(txtDocumento, new ColumnLayoutData(1));
		add(linha1);

		txtValor = new NumberField(OpenSigCore.i18n.txtValor(), nomes.get("valor"), 100);
		txtValor.setAllowBlank(false);
		txtValor.setAllowNegative(false);
		txtValor.setMaxLength(13);

		txtParcela = new TextField(OpenSigCore.i18n.txtParcela(), nomes.get("parcela"), 100);
		txtParcela.setRegex("\\d{2}/\\d{2}");
		txtParcela.setMaxLength(5);
		txtParcela.setAllowBlank(false);

		dtVencimento = new DateField(OpenSigCore.i18n.txtVencimento(), nomes.get("vencimento"), 100);
		dtVencimento.setAllowBlank(false);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtValor, 120);
		linha2.addToRow(txtParcela, 120);
		linha2.addToRow(dtVencimento, 120);
		add(linha2);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), nomes.get("observacao"));
		txtObservacao.setMaxLength(255);
		txtObservacao.setWidth("95%");
		add(txtObservacao);

		getBanco();
		Label banco = new Label(OpenSigCore.i18n.txtBanco() + ":");
		lblBanco = new Label();
		lblBanco.setCls("destaque");
		Label agencia = new Label(OpenSigCore.i18n.txtAgencia() + ":");
		lblAgencia = new Label();
		lblAgencia.setCls("destaque");
		Label conta = new Label(OpenSigCore.i18n.txtConta() + ":");
		lblConta = new Label();
		lblConta.setCls("destaque");
		Label numero = new Label(OpenSigCore.i18n.txtNumero() + ":");
		lblNumero = new Label();
		lblNumero.setCls("destaque");

		MultiFieldPanel linha3 = new MultiFieldPanel();
		linha3.addToRow(banco, 50);
		linha3.addToRow(lblBanco, 300);
		linha3.addToRow(agencia, 60);
		linha3.addToRow(lblAgencia, 75);
		linha3.addToRow(conta, 50);
		linha3.addToRow(lblConta, 75);
		linha3.addToRow(numero, 60);
		linha3.addToRow(lblNumero, 75);
		linha3.setBorder(false);

		fsCheque = new FieldSet(OpenSigCore.i18n.txtCheque());
		fsCheque.setWidth("95%");
		fsCheque.hide();
		fsCheque.add(linha3);
		add(fsCheque);

		super.inicializar();
	}

	public void limparDados() {
		getForm().reset();
	}

	protected ComboBox getForma() {
		FieldDef[] fdForma = new FieldDef[] { new IntegerFieldDef("finFormaId"), new StringFieldDef("finFormaDescricao") };
		CoreProxy<FinForma> proxy = new CoreProxy<FinForma>(new FinForma());
		final Store storeForma = new Store(proxy, new ArrayReader(new RecordDef(fdForma)), false);
		storeForma.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				stBanco.load();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtTipo(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeForma.load();
						}
					}
				});
			}
		});

		cmbForma = new ComboBox(OpenSigCore.i18n.txtTipo(), "finForma.finFormaId", 120);
		cmbForma.setListWidth(120);
		cmbForma.setAllowBlank(false);
		cmbForma.setStore(storeForma);
		cmbForma.setTriggerAction(ComboBox.ALL);
		cmbForma.setMode(ComboBox.LOCAL);
		cmbForma.setDisplayField("finFormaDescricao");
		cmbForma.setValueField("finFormaId");
		cmbForma.setForceSelection(true);
		cmbForma.setEditable(false);
		cmbForma.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				mostrarCheque();
				if (record.getAsString("finFormaDescricao").equalsIgnoreCase(OpenSigCore.i18n.txtCheque())) {
					txtDocumento.setMinLength(30);
					txtDocumento.setMaxLength(32);
				} else {
					txtDocumento.setMinLength(1);
					txtDocumento.setMaxLength(50);
				}
			}
		});

		return cmbForma;
	}

	protected Store getBanco() {
		FieldDef[] fdBanco = new FieldDef[] { new IntegerFieldDef("finBancoId"), new StringFieldDef("finBancoNumero"), new StringFieldDef("finBancoDescricao") };
		CoreProxy<FinBanco> proxy = new CoreProxy<FinBanco>(new FinBanco());
		stBanco = new Store(proxy, new ArrayReader(new RecordDef(fdBanco)), false);
		stBanco.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				mostrar();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtBanco(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							stBanco.load();
						}
					}
				});
			}
		});

		return stBanco;
	}

	public void mostrarDados() {
		if (cmbForma.getStore().getRecords().length == 0) {
			cmbForma.getStore().load();
		} else {
			mostrar();
		}
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();

		if (rec != null) {
			getForm().loadRecord(rec);
		}
		cmbForma.focus(true);
		mostrarCheque();
	}

	protected void mostrarCheque() {
		fsCheque.hide();
		try {
			if (cmbForma.getRawValue().equalsIgnoreCase(OpenSigCore.i18n.txtCheque())) {
				String cmc7 = txtDocumento.getValueAsString().replaceAll("\\D", "");

				if (cmc7.length() == 30) {
					String banco = cmc7.substring(0, 3);
					stBanco.filter("finBancoNumero", banco);
					if (stBanco.getAt(0) != null) {
						lblBanco.setText(stBanco.getAt(0).getAsString("finBancoDescricao"));
					} else {
						lblBanco.setText(OpenSigCore.i18n.msgRegistro());
					}

					String agencia = cmc7.substring(3, 7);
					lblAgencia.setText(agencia);

					String conta = cmc7.substring(23, 29);
					lblConta.setText(conta);

					String numero = cmc7.substring(11, 17);
					lblNumero.setText(numero);
					fsCheque.show();
				}
			}
		} catch (Exception e) {
			// nada
		}
	}

	public void gerarListas() {
	}

	public E getFinanceiro() {
		return financeiro;
	}

	public void setFinanceiro(E financeiro) {
		this.financeiro = financeiro;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnFinanceiro() {
		return hdnFinanceiro;
	}

	public void setHdnFinanceiro(Hidden hdnFinanceiro) {
		this.hdnFinanceiro = hdnFinanceiro;
	}

	public ComboBox getCmbForma() {
		return cmbForma;
	}

	public void setCmbForma(ComboBox cmbForma) {
		this.cmbForma = cmbForma;
	}

	public TextField getTxtDocumento() {
		return txtDocumento;
	}

	public void setTxtDocumento(TextField txtDocumento) {
		this.txtDocumento = txtDocumento;
	}

	public TextField getTxtParcela() {
		return txtParcela;
	}

	public void setTxtParcela(TextField txtParcela) {
		this.txtParcela = txtParcela;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public DateField getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(DateField dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public TextArea getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(TextArea txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public Map<String, String> getNomes() {
		return nomes;
	}

	public void setNomes(Map<String, String> nomes) {
		this.nomes = nomes;
	}

	public Label getLblBanco() {
		return lblBanco;
	}

	public void setLblBanco(Label lblBanco) {
		this.lblBanco = lblBanco;
	}

	public Label getLblAgencia() {
		return lblAgencia;
	}

	public void setLblAgencia(Label lblAgencia) {
		this.lblAgencia = lblAgencia;
	}

	public Label getLblConta() {
		return lblConta;
	}

	public void setLblConta(Label lblConta) {
		this.lblConta = lblConta;
	}

	public Label getLblNumero() {
		return lblNumero;
	}

	public void setLblNumero(Label lblNumero) {
		this.lblNumero = lblNumero;
	}

	public FieldSet getFsCheque() {
		return fsCheque;
	}

	public void setFsCheque(FieldSet fsCheque) {
		this.fsCheque = fsCheque;
	}

}
