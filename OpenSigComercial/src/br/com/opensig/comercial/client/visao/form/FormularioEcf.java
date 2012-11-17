package br.com.opensig.comercial.client.visao.form;

import br.com.opensig.comercial.shared.modelo.ComEcf;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FormularioEcf extends AFormulario<ComEcf> {

	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private TextField txtCodigo;
	private TextField txtMfAdicional;
	private TextField txtIdentificacao;
	private TextField txtTipo;
	private TextField txtMarca;
	private TextField txtModelo;
	private TextField txtSerie;
	private NumberField txtCaixa;
	private Checkbox chkAtivo;

	public FormularioEcf(SisFuncao funcao) {
		super(new ComEcf(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comEcfId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtCodigo = new TextField(OpenSigCore.i18n.txtCodigo(), "comEcfCodigo", 50);
		txtCodigo.setAllowBlank(false);
		txtCodigo.setMaxLength(2);
		txtCodigo.setMinLength(2);
		
		txtMfAdicional = new TextField(OpenSigCore.i18n.txtMfAdicional(), "comEcfMfAdicional", 50);
		txtMfAdicional.setMaxLength(1);
		txtMfAdicional.setMinLength(1);
		
		txtIdentificacao = new TextField(OpenSigCore.i18n.txtIdentificacao(), "comEcfIdentificacao", 80);
		txtIdentificacao.setAllowBlank(false);
		txtIdentificacao.setMaxLength(6);
		txtIdentificacao.setMinLength(6);

		txtTipo = new TextField(OpenSigCore.i18n.txtTipo(), "comEcfTipo", 100);
		txtTipo.setAllowBlank(false);
		txtTipo.setMaxLength(7);
		
		txtMarca = new TextField(OpenSigCore.i18n.txtMarca(), "comEcfMarca", 100);
		txtMarca.setAllowBlank(false);
		txtMarca.setMaxLength(20);
		
		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtCodigo, 70);
		linha1.addToRow(txtMfAdicional, 70);
		linha1.addToRow(txtIdentificacao, 100);
		linha1.addToRow(txtTipo, 120);
		linha1.addToRow(txtMarca, 120);
		add(linha1);
		
		txtModelo = new TextField(OpenSigCore.i18n.txtModelo(), "comEcfModelo", 150);
		txtModelo.setAllowBlank(false);
		txtModelo.setMaxLength(20);

		txtSerie = new TextField(OpenSigCore.i18n.txtSerie(), "comEcfSerie", 150);
		txtSerie.setAllowBlank(false);
		txtSerie.setMinLength(20);
		txtSerie.setMaxLength(20);

		txtCaixa = new NumberField(OpenSigCore.i18n.txtCaixa(), "comEcfCaixa", 50);
		txtCaixa.setAllowBlank(false);
		txtCaixa.setAllowDecimals(false);
		txtCaixa.setAllowNegative(false);
		txtCaixa.setMaxLength(3);

		chkAtivo = new Checkbox(OpenSigCore.i18n.txtAtivo(), "comEcfAtivo");
		chkAtivo.setValue(true);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtModelo, 170);
		linha2.addToRow(txtSerie, 170);
		linha2.addToRow(txtCaixa, 70);
		linha2.addToRow(chkAtivo, 70);
		add(linha2);
	}

	public boolean setDados() {
		classe.setComEcfId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setComEcfCodigo(txtCodigo.getValueAsString());
		classe.setComEcfMfAdicional(txtMfAdicional.getValueAsString());
		classe.setComEcfIdentificacao(txtIdentificacao.getValueAsString());
		classe.setComEcfTipo(txtTipo.getValueAsString());
		classe.setComEcfMarca(txtMarca.getValueAsString());
		classe.setComEcfModelo(txtModelo.getValueAsString());
		classe.setComEcfSerie(txtSerie.getValueAsString());
		classe.setComEcfAtivo(chkAtivo.getValue());
		if (txtCaixa.getValue() != null) {
			classe.setComEcfCaixa(txtCaixa.getValue().intValue());
		}
		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}

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

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
	}

	public TextField getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(TextField txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

	public TextField getTxtMfAdicional() {
		return txtMfAdicional;
	}

	public void setTxtMfAdicional(TextField txtMfAdicional) {
		this.txtMfAdicional = txtMfAdicional;
	}

	public TextField getTxtIdentificacao() {
		return txtIdentificacao;
	}

	public void setTxtIdentificacao(TextField txtIdentificacao) {
		this.txtIdentificacao = txtIdentificacao;
	}

	public TextField getTxtTipo() {
		return txtTipo;
	}

	public void setTxtTipo(TextField txtTipo) {
		this.txtTipo = txtTipo;
	}

	public TextField getTxtMarca() {
		return txtMarca;
	}

	public void setTxtMarca(TextField txtMarca) {
		this.txtMarca = txtMarca;
	}

	public TextField getTxtModelo() {
		return txtModelo;
	}

	public void setTxtModelo(TextField txtModelo) {
		this.txtModelo = txtModelo;
	}

	public TextField getTxtSerie() {
		return txtSerie;
	}

	public void setTxtSerie(TextField txtSerie) {
		this.txtSerie = txtSerie;
	}

	public NumberField getTxtCaixa() {
		return txtCaixa;
	}

	public void setTxtCaixa(NumberField txtCaixa) {
		this.txtCaixa = txtCaixa;
	}

	public Checkbox getChkAtivo() {
		return chkAtivo;
	}

	public void setChkAtivo(Checkbox chkAtivo) {
		this.chkAtivo = chkAtivo;
	}

}
