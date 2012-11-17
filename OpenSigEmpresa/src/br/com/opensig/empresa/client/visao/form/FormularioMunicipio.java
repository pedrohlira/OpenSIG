package br.com.opensig.empresa.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroObjeto;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEstado;
import br.com.opensig.empresa.shared.modelo.EmpMunicipio;
import br.com.opensig.empresa.shared.modelo.EmpPais;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class FormularioMunicipio extends AFormulario<EmpMunicipio> {

	private Hidden hdnCod;
	private NumberField txtIbge;
	private TextField txtMunicipio;
	private ComboBox cmbPais;
	private ComboBox cmbEstado;
	private CoreProxy proxyEstado;
	private Store storeEstado;

	public FormularioMunicipio(SisFuncao funcao) {
		super(new EmpMunicipio(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("empMunicipioId", "0");
		add(hdnCod);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(getPais(), 220);
		linha1.addToRow(getEstado(), 220);
		add(linha1);

		txtIbge = new NumberField(OpenSigCore.i18n.txtIbge(), "empMunicipioIbge", 100);
		txtIbge.setAllowBlank(false);
		txtIbge.setAllowDecimals(false);
		txtIbge.setAllowNegative(false);
		txtIbge.setMinLength(7);
		txtIbge.setMaxLength(7);

		txtMunicipio = new TextField(OpenSigCore.i18n.txtMunicipio(), "empMunicipioDescricao", 250);
		txtMunicipio.setAllowBlank(false);
		txtMunicipio.setMaxLength(100);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(txtIbge, 120);
		linha2.addToRow(txtMunicipio, 270);
		add(linha2);
	}

	public boolean setDados() {
		classe.setEmpMunicipioId(Integer.valueOf(hdnCod.getValueAsString()));
		if (txtIbge.getValue() != null) {
			classe.setEmpMunicipioIbge(txtIbge.getValue().intValue());
		}
		classe.setEmpMunicipioDescricao(txtMunicipio.getValueAsString());
		if (cmbEstado.getValue() != null) {
			EmpEstado estado = new EmpEstado(Integer.valueOf(cmbEstado.getValue()));
			classe.setEmpEstado(estado);
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
			carregaEstado();
		} else {
			cmbEstado.disable();
		}
		cmbPais.focus(true);
		
		if (duplicar) {
			hdnCod.setValue("0");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	private void carregaEstado() {
		cmbEstado.enable();
		EmpPais pais = new EmpPais(Integer.valueOf(cmbPais.getValue()));
		IFiltro filtro = new FiltroObjeto("empPais", ECompara.IGUAL, pais);
		proxyEstado.setFiltroPadrao(filtro);
		storeEstado.load();
	}

	private ComboBox getPais() {
		FieldDef[] fdPais = new FieldDef[] { new IntegerFieldDef("empPaisId"), new IntegerFieldDef("empPaisIbge"), new StringFieldDef("empPaisDescricao") };
		CoreProxy<EmpPais> proxy = new CoreProxy<EmpPais>(new EmpPais());
		Store pais = new Store(proxy, new ArrayReader(new RecordDef(fdPais)), false);
		pais.load();

		cmbPais = new ComboBox(OpenSigCore.i18n.txtPais(), "empEstado.empPais.empPaisId", 200);
		cmbPais.setAllowBlank(false);
		cmbPais.setStore(pais);
		cmbPais.setTriggerAction(ComboBox.ALL);
		cmbPais.setMode(ComboBox.LOCAL);
		cmbPais.setDisplayField("empPaisDescricao");
		cmbPais.setValueField("empPaisId");
		cmbPais.setForceSelection(true);
		cmbPais.setListWidth(200);
		cmbPais.setEditable(false);
		cmbPais.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				cmbEstado.setValue("");
				carregaEstado();
			}
		});

		return cmbPais;
	}

	private ComboBox getEstado() {
		FieldDef[] fdEstado = new FieldDef[] { new IntegerFieldDef("empEstadoId"), new IntegerFieldDef("empEstadoIbge"), new StringFieldDef("empEstadoDescricao") };

		proxyEstado = new CoreProxy<EmpEstado>(new EmpEstado());
		storeEstado = new Store(proxyEstado, new ArrayReader(new RecordDef(fdEstado)), false);
		storeEstado.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (cmbEstado.getValue() != null && !cmbEstado.getValue().equals("")) {
					for (Record record : records) {
						if (record.getAsString("empEstadoId").equals(cmbEstado.getValue())) {
							cmbEstado.setRawValue(record.getAsString("empEstadoDescricao"));
							break;
						}
					}
				}
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
		cmbEstado.setLinked(true);
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

	public NumberField getTxtIbge() {
		return txtIbge;
	}

	public void setTxtIbge(NumberField txtIbge) {
		this.txtIbge = txtIbge;
	}

	public TextField getTxtMunicipio() {
		return txtMunicipio;
	}

	public void setTxtMunicipio(TextField txtMunicipio) {
		this.txtMunicipio = txtMunicipio;
	}

	public ComboBox getCmbPais() {
		return cmbPais;
	}

	public void setCmbPais(ComboBox cmbPais) {
		this.cmbPais = cmbPais;
	}

	public ComboBox getCmbEstado() {
		return cmbEstado;
	}

	public void setCmbEstado(ComboBox cmbEstado) {
		this.cmbEstado = cmbEstado;
	}

	public CoreProxy getProxyEstado() {
		return proxyEstado;
	}

	public void setProxyEstado(CoreProxy proxyEstado) {
		this.proxyEstado = proxyEstado;
	}

	public Store getStoreEstado() {
		return storeEstado;
	}

	public void setStoreEstado(Store storeEstado) {
		this.storeEstado = storeEstado;
	}

}
