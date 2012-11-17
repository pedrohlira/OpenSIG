package br.com.opensig.fiscal.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.fiscal.shared.modelo.FisIncentivoEstado;

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
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;

public class FormularioIncentivo extends AFormulario<FisIncentivoEstado> {

	private Hidden hdnCod;
	private Hidden hdnEmpresa;
	private ComboBox cmbEstado;
	private NumberField txtIcms1;
	private NumberField txtIcms2;

	public FormularioIncentivo(SisFuncao funcao) {
		super(new FisIncentivoEstado(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("fisIncentivoEstadoId", "0");
		add(hdnCod);
		hdnEmpresa = new Hidden("empEmpresa.empEmpresaId", "0");
		add(hdnEmpresa);

		txtIcms1 = new NumberField("c/ " + OpenSigCore.i18n.txtIncentivo(), "fisIncentivoEstadoIcms1", 100);
		txtIcms1.setAllowBlank(false);
		txtIcms1.setAllowNegative(false);
		txtIcms1.setMaxLength(5);

		txtIcms2 = new NumberField("s/ " + OpenSigCore.i18n.txtIncentivo(), "fisIncentivoEstadoIcms2", 100);
		txtIcms2.setAllowBlank(false);
		txtIcms2.setAllowNegative(false);
		txtIcms2.setMaxLength(5);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getEstado(), 220);
		linha1.addToRow(txtIcms1, 120);
		linha1.addToRow(txtIcms2, 120);
		add(linha1);
	}

	public boolean setDados() {
		classe.setFisIncentivoEstadoId(Integer.valueOf(hdnCod.getValueAsString()));
		if (cmbEstado.getValue() != null) {
			EmpEstado estado = new EmpEstado(Integer.valueOf(cmbEstado.getValue()));
			classe.setEmpEstado(estado);
		}
		if (txtIcms1.getValue() != null) {
			classe.setFisIncentivoEstadoIcms1(txtIcms1.getValue().doubleValue());
		}
		if (txtIcms2.getValue() != null) {
			classe.setFisIncentivoEstadoIcms2(txtIcms2.getValue().doubleValue());
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
		if(cmbEstado.getStore().getRecords().length == 0){
			cmbEstado.getStore().load();
		}else{
			mostrar();
		}
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		}
		cmbEstado.focus(true);
		
		if (duplicar) {
			hdnCod.setValue("0");
			hdnEmpresa.setValue("0");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	private ComboBox getEstado() {
		FieldDef[] fdEstado = new FieldDef[] { new IntegerFieldDef("empEstadoId"), new IntegerFieldDef("empEstadoIbge"), new StringFieldDef("empEstadoDescricao") };
		CoreProxy<EmpEstado> proxy = new CoreProxy<EmpEstado>(new EmpEstado());
		final Store storeEstado = new Store(proxy, new ArrayReader(new RecordDef(fdEstado)), false);
		storeEstado.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				mostrar();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtEstado(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeEstado.load();
						}
					}
				});
			}
		});

		cmbEstado = new ComboBox(OpenSigCore.i18n.txtEstado(), "empEstado.empEstadoId", 200);
		cmbEstado.setAllowBlank(false);
		cmbEstado.setStore(storeEstado);
		cmbEstado.setTriggerAction(ComboBox.ALL);
		cmbEstado.setMode(ComboBox.LOCAL);
		cmbEstado.setDisplayField("empEstadoDescricao");
		cmbEstado.setValueField("empEstadoId");
		cmbEstado.setForceSelection(true);
		cmbEstado.setEditable(false);
		cmbEstado.setListWidth(200);

		return cmbEstado;
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

	public ComboBox getCmbEstado() {
		return cmbEstado;
	}

	public void setCmbEstado(ComboBox cmbEstado) {
		this.cmbEstado = cmbEstado;
	}

	public NumberField getTxtIcms1() {
		return txtIcms1;
	}

	public void setTxtIcms1(NumberField txtIcms1) {
		this.txtIcms1 = txtIcms1;
	}

	public NumberField getTxtIcms2() {
		return txtIcms2;
	}

	public void setTxtIcms2(NumberField txtIcms2) {
		this.txtIcms2 = txtIcms2;
	}
}
