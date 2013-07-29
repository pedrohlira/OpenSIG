package br.com.opensig.comercial.client.visao.form;

import br.com.opensig.comercial.shared.modelo.ComConsumo;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class FormularioConsumo extends AFormulario<ComConsumo> {

	private Hidden hdnCod;
	private Hidden hdnFornecedor;
	private Hidden hdnEmpresa;
	private ComboBox cmbFornecedor;
	private ComboBox cmbTipo;
	private NumberField txtDocumento;
	private DateField dtData;
	private NumberField txtValor;
	private NumberField txtCfop;
	private NumberField txtBase;
	private NumberField txtAliquota;
	private NumberField txtIcms;
	private TextArea txtObservacao;

	public FormularioConsumo(SisFuncao funcao) {
		super(new ComConsumo(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comConsumoId", "0");
		add(hdnCod);
		hdnFornecedor = new Hidden("empFornecedor.empFornecedorId", "0");
		add(hdnFornecedor);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtDocumento = new NumberField(OpenSigCore.i18n.txtDocumento(), "comConsumoDocumento", 100);
		txtDocumento.setAllowBlank(false);
		txtDocumento.setAllowDecimals(false);
		txtDocumento.setAllowNegative(false);
		txtDocumento.setMinLength(1);
		txtDocumento.setMaxLength(9);
		
		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getFornecedor(), 330);
		linha1.addToRow(getTipo(), 130);
		linha1.addToRow(txtDocumento, 120);
		add(linha1);

		dtData = new DateField(OpenSigCore.i18n.txtData(), "comConsumoData", 100);
		dtData.setAllowBlank(false);

		txtCfop = new NumberField(OpenSigCore.i18n.txtCfop(), "comConsumoCfop", 50);
		txtCfop.setAllowBlank(false);
		txtCfop.setAllowDecimals(false);
		txtCfop.setAllowNegative(false);
		txtCfop.setMinLength(4);
		txtCfop.setMaxLength(4);
		txtCfop.setMaxValue(4000);

		txtBase = new NumberField(OpenSigCore.i18n.txtIcmsBase(), "comConsumoBase", 70);
		txtBase.setAllowBlank(false);
		txtBase.setAllowNegative(false);
		txtBase.setMaxLength(11);
		txtBase.setDecimalPrecision(2);

		txtAliquota = new NumberField(OpenSigCore.i18n.txtAliquota() + " %", "comConsumoAliquota", 70);
		txtAliquota.setAllowBlank(false);
		txtAliquota.setAllowNegative(false);
		txtAliquota.setDecimalPrecision(2);
		txtAliquota.setMaxLength(5);

		txtIcms = new NumberField(OpenSigCore.i18n.txtIcms(), "comConsumoIcms", 70);
		txtIcms.setAllowBlank(false);
		txtIcms.setAllowNegative(false);
		txtIcms.setMaxLength(11);
		txtIcms.setDecimalPrecision(2);

		txtValor = new NumberField(OpenSigCore.i18n.txtValor(), "comConsumoValor", 120);
		txtValor.setAllowBlank(false);
		txtValor.setAllowNegative(false);
		txtValor.setMaxLength(11);
		txtValor.setDecimalPrecision(2);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(dtData, 125);
		linha2.addToRow(txtCfop, 70);
		linha2.addToRow(txtValor, 150);
		linha2.addToRow(txtBase, 90);
		linha2.addToRow(txtAliquota, 90);
		linha2.addToRow(txtIcms, 90);
		add(linha2);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), "comConsumoObservacao");
		txtObservacao.setMaxLength(255);
		txtObservacao.setWidth("95%");
		add(txtObservacao);
	}

	public boolean setDados() {
		boolean retorno = true;

		classe.setComConsumoId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setComConsumoTipo(cmbTipo.getValue());
		classe.setComConsumoData(dtData.getValue());
		if (txtDocumento.getValue() != null) {
			classe.setComConsumoDocumento(txtDocumento.getValue().intValue());
		}
		if (txtValor.getValue() != null) {
			classe.setComConsumoValor(txtValor.getValue().doubleValue());
		}
		if (txtCfop.getValue() != null) {
			classe.setComConsumoCfop(txtCfop.getValue().intValue());
		}
		if (txtBase.getValue() != null) {
			classe.setComConsumoBase(txtBase.getValue().doubleValue());
		}
		if (txtAliquota.getValue() != null) {
			classe.setComConsumoAliquota(txtAliquota.getValue().doubleValue());
		}
		if (txtIcms.getValue() != null) {
			classe.setComConsumoIcms(txtIcms.getValue().doubleValue());
		}
		classe.setComConsumoObservacao(txtObservacao.getValueAsString());

		if (!hdnFornecedor.getValueAsString().equals("0")) {
			EmpFornecedor fornecedor = new EmpFornecedor(Integer.valueOf(hdnFornecedor.getValueAsString()));
			classe.setEmpFornecedor(fornecedor);
		}

		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
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
		cmbTipo.focus();

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			classe.setComConsumoFechada(false);
			classe.setComConsumoPaga(false);
			duplicar = false;
		}
	}

	private ComboBox getFornecedor() {
		cmbFornecedor = UtilClient.getComboEntidade(new ComboEntidade(new EmpFornecedor()));
		cmbFornecedor.setName("empFornecedor.empEntidade.empEntidadeNome1");
		cmbFornecedor.setLabel(OpenSigCore.i18n.txtFornecedor());
		cmbFornecedor.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnFornecedor.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbFornecedor.getRawValue().equals("")) {
					hdnFornecedor.setValue("0");
				}
			}
		});
		return cmbFornecedor;
	}
	
	private ComboBox getTipo() {
		Store store = new SimpleStore(new String[] { "id", "valor" }, new String[][] { 
				new String[] { "21 - REDE", "21 - REDE" },
				new String[] { "22 - FONE", "22 - FONE" },
				new String[] { "06 - LUZ", "06 - LUZ" }, 
				new String[] { "28 - GÁS", "28 - GÁS" },
				new String[] { "29 - ÁGUA", "29 - ÁGUA" }
				});
		store.load();

		cmbTipo = new ComboBox(OpenSigCore.i18n.txtTipo(), "comConsumoTipo", 100);
		cmbTipo.setAllowBlank(false);
		cmbTipo.setStore(store);
		cmbTipo.setTriggerAction(ComboBox.ALL);
		cmbTipo.setMode(ComboBox.LOCAL);
		cmbTipo.setDisplayField("id");
		cmbTipo.setValueField("valor");
		cmbTipo.setTpl("<div class=\"x-combo-list-item\"><b>{id}</b> - <i>[{valor}]</i></div>");
		cmbTipo.setForceSelection(true);
		cmbTipo.setListWidth(200);
		cmbTipo.setEditable(false);

		return cmbTipo;
	}

	public void gerarListas() {
	}

	public ComboBox getCmbFornecedor() {
		return cmbFornecedor;
	}

	public void setCmbFornecedor(ComboBox cmbFornecedor) {
		this.cmbFornecedor = cmbFornecedor;
	}

	public DateField getDtData() {
		return dtData;
	}

	public void setDtData(DateField dtData) {
		this.dtData = dtData;
	}

	public NumberField getTxtCfop() {
		return txtCfop;
	}

	public void setTxtCfop(NumberField txtCfop) {
		this.txtCfop = txtCfop;
	}

	public NumberField getTxtBase() {
		return txtBase;
	}

	public void setTxtBase(NumberField txtBase) {
		this.txtBase = txtBase;
	}

	public NumberField getTxtAliquota() {
		return txtAliquota;
	}

	public void setTxtAliquota(NumberField txtAliquota) {
		this.txtAliquota = txtAliquota;
	}

	public NumberField getTxtIcms() {
		return txtIcms;
	}

	public void setTxtIcms(NumberField txtIcms) {
		this.txtIcms = txtIcms;
	}

	public NumberField getTxtValor() {
		return txtValor;
	}

	public void setTxtValor(NumberField txtValor) {
		this.txtValor = txtValor;
	}

	public TextArea getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(TextArea txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public Hidden getHdnFornecedor() {
		return hdnFornecedor;
	}

	public void setHdnFornecedor(Hidden hdnFornecedor) {
		this.hdnFornecedor = hdnFornecedor;
	}

	public Hidden getHdnEmpresa() {
		return hdnEmpresa;
	}

	public void setHdnEmpresa(Hidden hdnEmpresa) {
		this.hdnEmpresa = hdnEmpresa;
	}
}
