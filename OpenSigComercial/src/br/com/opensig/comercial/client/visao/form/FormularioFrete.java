package br.com.opensig.comercial.client.visao.form;

import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpTransportadora;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class FormularioFrete extends AFormulario<ComFrete> {

	private Hidden hdnCod;
	private Hidden hdnFornecedor;
	private Hidden hdnEmpresa;
	private Hidden hdnTransportadora;
	private ComboBox cmbFornecedor;
	private ComboBox cmbTransportadora;
	private NumberField txtCtrc;
	private DateField dtEmissao;
	private DateField dtRecebimento;
	private NumberField txtSerie;
	private NumberField txtCfop;
	private NumberField txtVolume;
	private TextField txtEspecie;
	private NumberField txtPeso;
	private NumberField txtCubagem;
	private NumberField txtValorProduto;
	private NumberField txtBase;
	private NumberField txtAliquota;
	private NumberField txtIcms;
	private NumberField txtValor;
	private NumberField txtNota;
	private TextArea txtObservacao;

	public FormularioFrete(SisFuncao funcao) {
		super(new ComFrete(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("comFreteId", "0");
		add(hdnCod);
		hdnFornecedor = new Hidden("empFornecedor.empFornecedorId", "0");
		add(hdnFornecedor);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);
		hdnTransportadora = new Hidden("empTransportadora.empTransportadoraId", "0");
		add(hdnTransportadora);

		txtCtrc = new NumberField(OpenSigCore.i18n.txtCtrc(), "comFreteCtrc", 70);
		txtCtrc.setAllowBlank(false);
		txtCtrc.setAllowDecimals(false);
		txtCtrc.setAllowNegative(false);
		txtCtrc.setMaxLength(6);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getFornecedor(), 330);
		linha1.addToRow(getTransportadora(), 330);
		linha1.addToRow(txtCtrc, 90);
		add(linha1);

		dtEmissao = new DateField(OpenSigCore.i18n.txtEmissao(), "comFreteEmissao", 100);
		dtEmissao.setAllowBlank(false);

		dtRecebimento = new DateField(OpenSigCore.i18n.txtRecebimento(), "comFreteRecebimento", 100);
		dtRecebimento.setAllowBlank(false);

		txtSerie = new NumberField(OpenSigCore.i18n.txtSerie(), "comFreteSerie", 70);
		txtSerie.setAllowBlank(false);
		txtSerie.setAllowDecimals(false);
		txtSerie.setAllowNegative(false);
		txtSerie.setMaxLength(6);

		txtCfop = new NumberField(OpenSigCore.i18n.txtCfop(), "comFreteCfop", 50);
		txtCfop.setAllowBlank(false);
		txtCfop.setAllowDecimals(false);
		txtCfop.setAllowNegative(false);
		txtCfop.setMinLength(4);
		txtCfop.setMaxLength(4);
		txtCfop.setMaxValue(4000);

		txtVolume = new NumberField(OpenSigCore.i18n.txtVolume(), "comFreteVolume", 70);
		txtVolume.setAllowBlank(false);
		txtVolume.setAllowDecimals(false);
		txtVolume.setAllowNegative(false);
		txtVolume.setMaxLength(6);

		txtEspecie = new TextField(OpenSigCore.i18n.txtEspecie(), "comFreteEspecie", 100);
		txtEspecie.setAllowBlank(false);
		txtEspecie.setMaxLength(10);

		txtPeso = new NumberField(OpenSigCore.i18n.txtPeso() + " kg", "comFretePeso", 70);
		txtPeso.setAllowBlank(false);
		txtPeso.setAllowNegative(false);
		txtPeso.setMaxLength(13);

		txtCubagem = new NumberField(OpenSigCore.i18n.txtCubagem(), "comFreteCubagem", 70);
		txtCubagem.setAllowBlank(false);
		txtCubagem.setAllowNegative(false);
		txtCubagem.setMaxLength(13);

		MultiFieldPanel linha3 = new MultiFieldPanel();
		linha3.setBorder(false);
		linha3.addToRow(dtEmissao, 125);
		linha3.addToRow(dtRecebimento, 125);
		linha3.addToRow(txtSerie, 90);
		linha3.addToRow(txtCfop, 70);
		linha3.addToRow(txtVolume, 90);
		linha3.addToRow(txtEspecie, 120);
		linha3.addToRow(txtPeso, 90);
		linha3.addToRow(txtCubagem, 90);
		add(linha3);

		txtValorProduto = new NumberField(OpenSigCore.i18n.txtValorProduto(), "comFreteValorProduto", 120);
		txtValorProduto.setAllowBlank(false);
		txtValorProduto.setAllowNegative(false);
		txtValorProduto.setMaxLength(13);

		txtNota = new NumberField(OpenSigCore.i18n.txtNota(), "comFreteNota", 100);
		txtNota.setAllowBlank(false);
		txtNota.setAllowNegative(false);
		txtNota.setAllowDecimals(false);
		txtNota.setMaxLength(10);

		txtBase = new NumberField(OpenSigCore.i18n.txtIcmsBase(), "comFreteBase", 70);
		txtBase.setAllowBlank(false);
		txtBase.setAllowNegative(false);
		txtBase.setMaxLength(13);

		txtAliquota = new NumberField(OpenSigCore.i18n.txtAliquota() + " %", "comFreteAliquota", 70);
		txtAliquota.setAllowBlank(false);
		txtAliquota.setAllowNegative(false);
		txtAliquota.setDecimalPrecision(2);
		txtAliquota.setMaxLength(5);

		txtIcms = new NumberField(OpenSigCore.i18n.txtIcms(), "comFreteIcms", 70);
		txtIcms.setAllowBlank(false);
		txtIcms.setAllowNegative(false);
		txtIcms.setMaxLength(13);

		txtValor = new NumberField(OpenSigCore.i18n.txtValor(), "comFreteValor", 120);
		txtValor.setAllowBlank(false);
		txtValor.setAllowNegative(false);
		txtValor.setMaxLength(13);

		MultiFieldPanel linha4 = new MultiFieldPanel();
		linha4.setBorder(false);
		linha4.addToRow(txtNota, 120);
		linha4.addToRow(txtValorProduto, 140);
		linha4.addToRow(txtBase, 90);
		linha4.addToRow(txtAliquota, 90);
		linha4.addToRow(txtIcms, 90);
		linha4.addToRow(txtValor, 150);
		add(linha4);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), "comFreteObservacao");
		txtObservacao.setMaxLength(255);
		txtObservacao.setWidth("95%");
		add(txtObservacao);
	}

	public boolean setDados() {
		boolean retorno = true;

		classe.setComFreteId(Integer.valueOf(hdnCod.getValueAsString()));
		if (txtCtrc.getValue() != null) {
			classe.setComFreteCtrc(txtCtrc.getValue().intValue());
		}
		classe.setComFreteEspecie(txtEspecie.getValueAsString());
		classe.setComFreteEmissao(dtEmissao.getValue());
		classe.setComFreteRecebimento(dtRecebimento.getValue());
		if (txtSerie.getValue() != null) {
			classe.setComFreteSerie(txtSerie.getValue().intValue());
		}
		if (txtCfop.getValue() != null) {
			classe.setComFreteCfop(txtCfop.getValue().intValue());
		}
		if (txtVolume.getValue() != null) {
			classe.setComFreteVolume(txtVolume.getValue().intValue());
		}
		if (txtPeso.getValue() != null) {
			classe.setComFretePeso(txtPeso.getValue().doubleValue());
		}
		if (txtCubagem.getValue() != null) {
			classe.setComFreteCubagem(txtCubagem.getValue().doubleValue());
		}
		if (txtValorProduto.getValue() != null) {
			classe.setComFreteValorProduto(txtValorProduto.getValue().doubleValue());
		}
		if (txtNota.getValue() != null) {
			classe.setComFreteNota(txtNota.getValue().intValue());
		}
		if (txtBase.getValue() != null) {
			classe.setComFreteBase(txtBase.getValue().doubleValue());
		}
		if (txtAliquota.getValue() != null) {
			classe.setComFreteAliquota(txtAliquota.getValue().doubleValue());
		}
		if (txtIcms.getValue() != null) {
			classe.setComFreteIcms(txtIcms.getValue().doubleValue());
		}
		if (txtValor.getValue() != null) {
			classe.setComFreteValor(txtValor.getValue().doubleValue());
		}
		classe.setComFreteObservacao(txtObservacao.getValueAsString());

		if (!hdnFornecedor.getValueAsString().equals("0")) {
			EmpFornecedor fornecedor = new EmpFornecedor(Integer.valueOf(hdnFornecedor.getValueAsString()));
			classe.setEmpFornecedor(fornecedor);
		}

		if (hdnEmpresa.getValueAsString().equals("0")) {
			classe.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			classe.setEmpEmpresa(new EmpEmpresa(Integer.valueOf(hdnEmpresa.getValueAsString())));
		}

		if (!hdnTransportadora.getValueAsString().equals("0")) {
			EmpTransportadora transportadora = new EmpTransportadora(Integer.valueOf(hdnTransportadora.getValueAsString()));
			classe.setEmpTransportadora(transportadora);
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
		txtCtrc.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			classe.setComFreteFechada(false);
			classe.setComFretePaga(false);
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

	private ComboBox getTransportadora() {
		cmbTransportadora = UtilClient.getComboEntidade(new ComboEntidade(new EmpTransportadora()));
		cmbTransportadora.setName("empTransportadora.empEntidade.empEntidadeNome1");
		cmbTransportadora.setLabel(OpenSigCore.i18n.txtTransportadora());
		;
		cmbTransportadora.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				hdnTransportadora.setValue(comboBox.getValue());
			}

			public void onBlur(Field field) {
				if (cmbTransportadora.getRawValue().equals("")) {
					hdnTransportadora.setValue("0");
				}
			}
		});
		return cmbTransportadora;
	}

	public void gerarListas() {
	}

	public ComboBox getCmbFornecedor() {
		return cmbFornecedor;
	}

	public void setCmbFornecedor(ComboBox cmbFornecedor) {
		this.cmbFornecedor = cmbFornecedor;
	}

	public ComboBox getCmbTransportadora() {
		return cmbTransportadora;
	}

	public void setCmbTransportadora(ComboBox cmbTransportadora) {
		this.cmbTransportadora = cmbTransportadora;
	}

	public NumberField getTxtCtrc() {
		return txtCtrc;
	}

	public void setTxtCtrc(NumberField txtCtrc) {
		this.txtCtrc = txtCtrc;
	}

	public DateField getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(DateField dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public DateField getDtRecebimento() {
		return dtRecebimento;
	}

	public void setDtRecebimento(DateField dtRecebimento) {
		this.dtRecebimento = dtRecebimento;
	}

	public NumberField getTxtSerie() {
		return txtSerie;
	}

	public void setTxtSerie(NumberField txtSerie) {
		this.txtSerie = txtSerie;
	}

	public NumberField getTxtCfop() {
		return txtCfop;
	}

	public void setTxtCfop(NumberField txtCfop) {
		this.txtCfop = txtCfop;
	}

	public NumberField getTxtVolume() {
		return txtVolume;
	}

	public void setTxtVolume(NumberField txtVolume) {
		this.txtVolume = txtVolume;
	}

	public TextField getTxtEspecie() {
		return txtEspecie;
	}

	public void setTxtEspecie(TextField txtEspecie) {
		this.txtEspecie = txtEspecie;
	}

	public NumberField getTxtPeso() {
		return txtPeso;
	}

	public void setTxtPeso(NumberField txtPeso) {
		this.txtPeso = txtPeso;
	}

	public NumberField getTxtCubagem() {
		return txtCubagem;
	}

	public void setTxtCubagem(NumberField txtCubagem) {
		this.txtCubagem = txtCubagem;
	}

	public NumberField getTxtValorProduto() {
		return txtValorProduto;
	}

	public void setTxtValorProduto(NumberField txtValorProduto) {
		this.txtValorProduto = txtValorProduto;
	}

	public NumberField getTxtNota() {
		return txtNota;
	}

	public void setTxtNota(NumberField txtNota) {
		this.txtNota = txtNota;
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

	public Hidden getHdnTransportadora() {
		return hdnTransportadora;
	}

	public void setHdnTransportadora(Hidden hdnTransportadora) {
		this.hdnTransportadora = hdnTransportadora;
	}
}
