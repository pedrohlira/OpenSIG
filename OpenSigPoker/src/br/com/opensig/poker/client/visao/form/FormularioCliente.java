package br.com.opensig.poker.client.visao.form;

import java.util.Date;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.poker.shared.modelo.PokerCliente;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FormularioCliente extends AFormulario<PokerCliente> {

	private Hidden hdnCod;
	private NumberField txtCodigo;
	private TextField txtNome;
	private NumberField txtAuxiliar;
	private Checkbox chkAtivo;
	private Checkbox chkAssociado;
	private TextField txtDocumento;
	private TextField txtContato;
	private TextField txtEmail;
	private DateField dtData;

	public FormularioCliente(SisFuncao funcao) {
		super(new PokerCliente(), funcao);
		inicializar();
	}

	public void inicializar() {
		hdnCod = new Hidden("pokerClienteId", "0");
		add(hdnCod);

		txtCodigo = new NumberField(OpenSigCore.i18n.txtNumero(), "pokerClienteCodigo", 100);
		txtCodigo.setAllowBlank(false);
		txtCodigo.setAllowNegative(false);
		txtCodigo.setAllowDecimals(false);
		txtCodigo.setMaxLength(10);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "pokerClienteNome", 200);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(100);

		txtAuxiliar = new NumberField(OpenSigCore.i18n.txtAuxiliar(), "pokerClienteAuxiliar", 100);
		txtAuxiliar.setAllowBlank(false);
		txtAuxiliar.setAllowNegative(false);
		txtAuxiliar.setAllowDecimals(false);
		txtAuxiliar.setMaxLength(10);
		txtAuxiliar.setValue(0);

		chkAssociado = new Checkbox(OpenSigCore.i18n.txtAssociado(), "pokerClienteAssociado");

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "pokerClienteAtivo");
		chkAtivo.setValue(true);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtCodigo, 120);
		linha1.addToRow(txtNome, 220);
		linha1.addToRow(txtAuxiliar, 120);
		linha1.addToRow(chkAssociado, 100);
		linha1.addToRow(chkAtivo, 100);
		add(linha1);

		txtDocumento = new TextField(OpenSigCore.i18n.txtDocumento(), "pokerClienteDocumento", 100);
		txtDocumento.setMaxLength(20);

		txtContato = new TextField(OpenSigCore.i18n.txtContato(), "pokerClienteContato", 200);
		txtContato.setMaxLength(50);

		txtEmail = new TextField(OpenSigCore.i18n.txtEmail(), "pokerClienteEmail", 200);
		txtEmail.setMaxLength(100);

		dtData = new DateField(OpenSigCore.i18n.txtData(), "pokerClienteData", 100);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.addToRow(txtDocumento, 120);
		linha2.addToRow(txtContato, 220);
		linha2.addToRow(txtEmail, 220);
		linha2.addToRow(dtData, 120);
		linha2.setBorder(false);
		add(linha2);
		
		super.inicializar();
	}

	public boolean setDados() {
		classe.setPokerClienteId(Integer.valueOf(hdnCod.getValueAsString()));
		if (txtCodigo.getValue() != null) {
			classe.setPokerClienteCodigo(txtCodigo.getValue().intValue());
		}
		classe.setPokerClienteNome(txtNome.getValueAsString());
		if (txtAuxiliar.getValue() != null) {
			classe.setPokerClienteAuxiliar(txtAuxiliar.getValue().intValue());
		}
		classe.setPokerClienteAssociado(chkAssociado.getValue());
		classe.setPokerClienteAtivo(chkAtivo.getValue());
		classe.setPokerClienteDocumento(txtDocumento.getValueAsString());
		classe.setPokerClienteContato(txtContato.getValueAsString());
		classe.setPokerClienteEmail(txtEmail.getValueAsString());
		classe.setPokerClienteData(dtData.getValue() == null ? new Date() : dtData.getValue());

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
		txtCodigo.focus(true);
		dtData.setDisabled(true);
		
		if (duplicar) {
			hdnCod.setValue("0");
			txtCodigo.setValue("");
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

	public NumberField getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(NumberField txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

	public NumberField getTxtAuxiliar() {
		return txtAuxiliar;
	}

	public void setTxtAuxiliar(NumberField txtAuxiliar) {
		this.txtAuxiliar = txtAuxiliar;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

	public Checkbox getChkAssociado() {
		return chkAssociado;
	}

	public void setChkAssociado(Checkbox chkAssociado) {
		this.chkAssociado = chkAssociado;
	}

	public TextField getTxtDocumento() {
		return txtDocumento;
	}

	public void setTxtDocumento(TextField txtDocumento) {
		this.txtDocumento = txtDocumento;
	}

	public TextField getTxtContato() {
		return txtContato;
	}

	public void setTxtContato(TextField txtContato) {
		this.txtContato = txtContato;
	}

	public TextField getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(TextField txtEmail) {
		this.txtEmail = txtEmail;
	}

	public DateField getDtData() {
		return dtData;
	}

	public void setDtData(DateField dtData) {
		this.dtData = dtData;
	}
}
