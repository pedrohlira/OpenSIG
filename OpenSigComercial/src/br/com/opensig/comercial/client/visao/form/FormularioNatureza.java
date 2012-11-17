package br.com.opensig.comercial.client.visao.form;

import br.com.opensig.comercial.shared.modelo.ComNatureza;
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

public class FormularioNatureza extends AFormulario<ComNatureza> {

	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private TextField txtNome;
	private TextField txtDescricao;
	private NumberField txtTrib;
	private NumberField txtSub;
	private Checkbox chkIcms;
	private Checkbox chkIpi;
	private NumberField txtPis;
	private NumberField txtCofins;

	public FormularioNatureza(SisFuncao funcao) {
		super(new ComNatureza(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comNaturezaId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "comNaturezaNome", 200);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(20);

		txtDescricao = new TextField(OpenSigCore.i18n.txtDescricao(), "comNaturezaDescricao", 400);
		txtDescricao.setAllowBlank(false);
		txtDescricao.setMaxLength(60);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 220);
		linha1.addToRow(txtDescricao, 420);
		add(linha1);

		txtTrib = new NumberField(OpenSigCore.i18n.txtTributacao(), "comNaturezaCfopTrib", 50);
		txtTrib.setAllowBlank(false);
		txtTrib.setAllowDecimals(false);
		txtTrib.setAllowNegative(false);
		txtTrib.setMaxLength(4);
		txtTrib.setMinLength(4);

		txtSub = new NumberField(OpenSigCore.i18n.txtSubstituicao(), "comNaturezaCfopSub", 50);
		txtSub.setAllowBlank(false);
		txtSub.setAllowDecimals(false);
		txtSub.setAllowNegative(false);
		txtSub.setMaxLength(4);
		txtSub.setMinLength(4);

		chkIcms = new Checkbox(OpenSigCore.i18n.txtIcms(), "comNaturezaIcms");

		chkIpi = new Checkbox(OpenSigCore.i18n.txtIpi(), "comNaturezaIpi");

		txtPis = new NumberField(OpenSigCore.i18n.txtPis() + " %", "comNaturezaPis", 50);
		txtPis.setAllowBlank(false);
		txtPis.setAllowNegative(false);
		txtPis.setDecimalPrecision(2);
		txtPis.setMaxLength(5);

		txtCofins = new NumberField(OpenSigCore.i18n.txtCofins() + " %", "comNaturezaCofins", 50);
		txtCofins.setAllowBlank(false);
		txtCofins.setAllowNegative(false);
		txtCofins.setDecimalPrecision(2);
		txtCofins.setMaxLength(5);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtTrib, 100);
		linha2.addToRow(txtSub, 100);
		linha2.addToRow(chkIcms, 70);
		linha2.addToRow(chkIpi, 70);
		linha2.addToRow(txtPis, 100);
		linha2.addToRow(txtCofins, 100);
		add(linha2);
	}

	public boolean setDados() {
		classe.setComNaturezaId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setComNaturezaNome(txtNome.getValueAsString());
		classe.setComNaturezaDescricao(txtDescricao.getValueAsString());
		if (txtTrib.getValue() != null) {
			classe.setComNaturezaCfopTrib(txtTrib.getValue().intValue());
		}
		if (txtSub.getValue() != null) {
			classe.setComNaturezaCfopSub(txtSub.getValue().intValue());
		}
		classe.setComNaturezaIcms(chkIcms.getValue());
		classe.setComNaturezaIpi(chkIpi.getValue());
		if (txtPis.getValue() != null) {
			classe.setComNaturezaPis(txtPis.getValue().doubleValue());
		}
		if (txtCofins.getValue() != null) {
			classe.setComNaturezaCofins(txtCofins.getValue().doubleValue());
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
		txtNome.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
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

	public TextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(TextField txtNome) {
		this.txtNome = txtNome;
	}

	public TextField getTxtDescricao() {
		return txtDescricao;
	}

	public void setTxtDescricao(TextField txtDescricao) {
		this.txtDescricao = txtDescricao;
	}

	public NumberField getTxtTrib() {
		return txtTrib;
	}

	public void setTxtTrib(NumberField txtTrib) {
		this.txtTrib = txtTrib;
	}

	public NumberField getTxtSub() {
		return txtSub;
	}

	public void setTxtSub(NumberField txtSub) {
		this.txtSub = txtSub;
	}

	public Checkbox getChkIcms() {
		return chkIcms;
	}

	public void setChkIcms(Checkbox chkIcms) {
		this.chkIcms = chkIcms;
	}

	public Checkbox getChkIpi() {
		return chkIpi;
	}

	public void setChkIpi(Checkbox chkIpi) {
		this.chkIpi = chkIpi;
	}

	public NumberField getTxtPis() {
		return txtPis;
	}

	public void setTxtPis(NumberField txtPis) {
		this.txtPis = txtPis;
	}

	public NumberField getTxtCofins() {
		return txtCofins;
	}

	public void setTxtCofins(NumberField txtCofins) {
		this.txtCofins = txtCofins;
	}
	
}
