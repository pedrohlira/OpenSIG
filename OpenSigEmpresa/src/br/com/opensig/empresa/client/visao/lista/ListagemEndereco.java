package br.com.opensig.empresa.client.visao.lista;

import java.util.List;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.filtro.ECompara;
import br.com.opensig.core.client.controlador.filtro.FiltroNumero;
import br.com.opensig.core.client.controlador.filtro.FiltroTexto;
import br.com.opensig.core.client.controlador.filtro.IFiltro;
import br.com.opensig.core.client.js.OpenSigCoreJS;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AListagemEditor;
import br.com.opensig.empresa.shared.modelo.EmpEndereco;
import br.com.opensig.empresa.shared.modelo.EmpEnderecoTipo;
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
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;

public class ListagemEndereco extends AListagemEditor<EmpEndereco> {

	private ComboBox cmbTipo;
	private ComboBox cmbPais;
	private ComboBox cmbEstado;
	private ComboBox cmbMunicipio;
	private NumberField txtNumero;
	private TextField txtLogradouro;
	private TextField txtComplemento;
	private TextField txtBairro;
	private TextField txtCep;
	private boolean mudouPais;
	private boolean mudouEstado;
	private boolean mudouMunicipio;

	public ListagemEndereco(boolean barraTarefa) {
		super(new EmpEndereco(), barraTarefa);
		inicializar();
	}

	public void inicializar() {
		// campos
		FieldDef[] fd = new FieldDef[] { new IntegerFieldDef("empEnderecoId"), new IntegerFieldDef("empEnderecoTipoId"), new StringFieldDef("empEnderecoTipoDescricao"),
				new StringFieldDef("empEnderecoPais"), new StringFieldDef("empEnderecoEstado"), new IntegerFieldDef("empMunicipioId"), new StringFieldDef("empEnderecoMunicipio"),
				new StringFieldDef("empEnderecoLogradouro"), new IntegerFieldDef("empEnderecoNumero"), new StringFieldDef("empEnderecoComplemento"), new StringFieldDef("empEnderecoBairro"),
				new StringFieldDef("empEnderecoCep") };
		campos = new RecordDef(fd);

		// tipo endereço
		FieldDef[] fdTipo = new FieldDef[] { new IntegerFieldDef("empEnderecoTipoId"), new StringFieldDef("empEnderecoTipoDescricao") };
		CoreProxy<EmpEnderecoTipo> proxy = new CoreProxy<EmpEnderecoTipo>(new EmpEnderecoTipo());
		final Store storeTipo = new Store(proxy, new ArrayReader(new RecordDef(fdTipo)), false);
		storeTipo.load();

		cmbTipo = new ComboBox();
		cmbTipo.setWidth(100);
		cmbTipo.setAllowBlank(false);
		cmbTipo.setStore(storeTipo);
		cmbTipo.setTriggerAction(ComboBox.ALL);
		cmbTipo.setMode(ComboBox.LOCAL);
		cmbTipo.setDisplayField("empEnderecoTipoDescricao");
		cmbTipo.setValueField("empEnderecoTipoId");
		cmbTipo.setMinChars(1);
		cmbTipo.setSelectOnFocus(true);
		cmbTipo.setEditable(false);

		// pais
		FieldDef[] fdPais = new FieldDef[] { new IntegerFieldDef("empPaisId"), new IntegerFieldDef("empPaisIbge"), new StringFieldDef("empPaisDescricao") };
		CoreProxy<EmpPais> proxy1 = new CoreProxy<EmpPais>(new EmpPais());
		Store storePais = new Store(proxy1, new ArrayReader(new RecordDef(fdPais)), false);
		storePais.load();

		cmbPais = new ComboBox();
		cmbPais.setWidth(100);
		cmbPais.setAllowBlank(false);
		cmbPais.setStore(storePais);
		cmbPais.setTriggerAction(ComboBox.ALL);
		cmbPais.setMode(ComboBox.LOCAL);
		cmbPais.setDisplayField("empPaisDescricao");
		cmbPais.setValueField("empPaisDescricao");
		cmbPais.setEditable(false);
		cmbPais.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				mudouPais = true;
			}
		});

		// estado
		FieldDef[] fdEstado = new FieldDef[] { new IntegerFieldDef("empEstadoId"), new IntegerFieldDef("empEstadoIbge"), new StringFieldDef("empEstadoDescricao") };
		final CoreProxy<EmpEstado> proxyEstado = new CoreProxy<EmpEstado>(new EmpEstado());
		final Store storeEstado = new Store(proxyEstado, new ArrayReader(new RecordDef(fdEstado)), false);

		cmbEstado = new ComboBox();
		cmbEstado.setWidth(100);
		cmbEstado.setAllowBlank(false);
		cmbEstado.setStore(storeEstado);
		cmbEstado.setTriggerAction(ComboBox.ALL);
		cmbEstado.setMode(ComboBox.LOCAL);
		cmbEstado.setDisplayField("empEstadoDescricao");
		cmbEstado.setValueField("empEstadoDescricao");
		cmbEstado.setEditable(false);
		cmbEstado.setForceSelection(true);
		cmbEstado.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				mudouEstado = true;
			}
		});

		// municipio
		FieldDef[] fdMunicipio = new FieldDef[] { new IntegerFieldDef("empMunicipioId"), new IntegerFieldDef("empMunicipioIbge"), new StringFieldDef("empMunicipioDescricao") };
		final CoreProxy<EmpMunicipio> proxyMunicipio = new CoreProxy<EmpMunicipio>(new EmpMunicipio());
		Store storeMunicipio = new Store(proxyMunicipio, new ArrayReader(new RecordDef(fdMunicipio)), false);

		cmbMunicipio = new ComboBox();
		cmbMunicipio.setMinChars(1);
		cmbMunicipio.setWidth(250);
		cmbMunicipio.setAllowBlank(false);
		cmbMunicipio.setStore(storeMunicipio);
		cmbMunicipio.setTriggerAction(ComboBox.ALL);
		cmbMunicipio.setMode(ComboBox.REMOTE);
		cmbMunicipio.setDisplayField("empMunicipioDescricao");
		cmbMunicipio.setValueField("empMunicipioId");
		cmbMunicipio.setLoadingText(OpenSigCore.i18n.txtAguarde());
		cmbMunicipio.setForceSelection(true);
		cmbMunicipio.setHideTrigger(true);
		cmbMunicipio.setPageSize(20);
		cmbMunicipio.setSelectOnFocus(true);
		cmbMunicipio.addListener(new ComboBoxListenerAdapter() {
			public void onChange(Field field, Object newVal, Object oldVal) {
				mudouMunicipio = true;
			}
		});

		txtLogradouro = new TextField();
		txtLogradouro.setAllowBlank(false);
		txtLogradouro.setMaxLength(100);
		txtLogradouro.setSelectOnFocus(true);

		txtNumero = new NumberField();
		txtNumero.setAllowBlank(false);
		txtNumero.setAllowDecimals(false);
		txtNumero.setAllowNegative(false);
		txtNumero.setSelectOnFocus(true);

		txtComplemento = new TextField();
		txtComplemento.setMaxLength(100);
		txtComplemento.setSelectOnFocus(true);

		txtBairro = new TextField();
		txtBairro.setMaxLength(100);
		txtBairro.setSelectOnFocus(true);

		txtCep = new TextField();
		txtCep.setMaxLength(9);
		txtCep.setSelectOnFocus(true);
		txtCep.addListener(new TextFieldListenerAdapter() {

			public void onRender(Component component) {
				super.onRender(component);
				OpenSigCoreJS.mascarar(component.getId(), "99999-999", null);
			}
		});
		txtCep.setSelectOnFocus(true);

		// colunas
		ColumnConfig ccId = new ColumnConfig(OpenSigCore.i18n.txtCod(), "empEnderecoId", 10, false);
		ccId.setHidden(true);
		ccId.setFixed(true);

		ColumnConfig ccTipo = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "empEnderecoTipoId", 100, false, new Renderer() {

			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				try {
					Record reg = UtilClient.getRegistro(storeTipo, "empEnderecoTipoId", value.toString());
					return reg.getAsString("empEnderecoTipoDescricao");
				} catch (Exception e) {
					return "";
				}
			}
		});
		ccTipo.setEditor(new GridEditor(cmbTipo));

		ColumnConfig ccTipoDescricao = new ColumnConfig(OpenSigCore.i18n.txtTipo(), "empEnderecoTipoDescricao", 100, false);
		ccTipoDescricao.setHidden(true);
		ccTipoDescricao.setFixed(true);

		ColumnConfig ccPais = new ColumnConfig(OpenSigCore.i18n.txtPais(), "empEnderecoPais", 100, false, new Renderer() {

			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				IFiltro filtro = new FiltroTexto("empPais.empPaisDescricao", ECompara.IGUAL, record.getAsString("empEnderecoPais"));
				proxyEstado.setFiltroPadrao(filtro);
				storeEstado.load();

				if (mudouPais) {
					record.set("empEnderecoEstado", "");
					mudouPais = false;
					mudouEstado = true;
				}

				return value == null ? "" : value.toString();
			}
		});
		ccPais.setEditor(new GridEditor(cmbPais));

		ColumnConfig ccEstado = new ColumnConfig(OpenSigCore.i18n.txtEstado(), "empEnderecoEstado", 150, false, new Renderer() {

			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				IFiltro filtro = new FiltroTexto("empEstado.empEstadoDescricao", ECompara.IGUAL, record.getAsString("empEnderecoEstado"));
				proxyMunicipio.setFiltroPadrao(filtro);

				if (mudouEstado) {
					record.set("empMunicipioId", 0);
					record.set("empEnderecoMunicipio", "");
					mudouEstado = false;
				}

				return value == null ? "" : value.toString();
			}
		});
		ccEstado.setEditor(new GridEditor(cmbEstado));

		ColumnConfig ccMun = new ColumnConfig(OpenSigCore.i18n.txtMunicipio(), "empMunicipioId", 200, false, new Renderer() {

			public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
				if (mudouMunicipio) {
					record.set("empMunicipioId", cmbMunicipio.getValue());
					record.set("empEnderecoMunicipio", cmbMunicipio.getRawValue());
					mudouMunicipio = false;
				}

				return value == null ? "" : record.getAsString("empEnderecoMunicipio");
			}
		});
		ccMun.setEditor(new GridEditor(cmbMunicipio));

		ColumnConfig ccMunicipioDescricao = new ColumnConfig(OpenSigCore.i18n.txtMunicipio(), "empEnderecoMunicipio", 100, false);
		ccMunicipioDescricao.setHidden(true);
		ccMunicipioDescricao.setFixed(true);

		ColumnConfig ccLogradouro = new ColumnConfig(OpenSigCore.i18n.txtLogradouro(), "empEnderecoLogradouro", 300, false);
		ccLogradouro.setEditor(new GridEditor(txtLogradouro));

		ColumnConfig ccNumero = new ColumnConfig(OpenSigCore.i18n.txtNumero(), "empEnderecoNumero", 60, false);
		ccNumero.setEditor(new GridEditor(txtNumero));

		ColumnConfig ccComplemento = new ColumnConfig(OpenSigCore.i18n.txtComplemento(), "empEnderecoComplemento", 200, false);
		ccComplemento.setEditor(new GridEditor(txtComplemento));

		ColumnConfig ccBairro = new ColumnConfig(OpenSigCore.i18n.txtBairro(), "empEnderecoBairro", 200, false);
		ccBairro.setEditor(new GridEditor(txtBairro));

		ColumnConfig ccCep = new ColumnConfig(OpenSigCore.i18n.txtCep(), "empEnderecoCep", 100, false);
		ccCep.setEditor(new GridEditor(txtCep));

		BaseColumnConfig[] bcc = new BaseColumnConfig[] { ccId, ccTipo, ccTipoDescricao, ccPais, ccEstado, ccMun, ccMunicipioDescricao, ccLogradouro, ccNumero, ccComplemento, ccBairro, ccCep };
		modelos = new ColumnModel(bcc);

		filtroPadrao = new FiltroNumero("empEnderecoId", ECompara.IGUAL, 0);
		setTitle(OpenSigCore.i18n.txtEndereco(), "icon-endereco");
		super.inicializar();
	}

	public boolean validar(List<EmpEndereco> lista) {
		boolean valida = true;
		Record[] recs = getStore().getRecords();

		for (Record rec : recs) {
			try {
				int endTipo = rec.getAsInteger("empEnderecoTipoId");
				int municipio = rec.getAsInteger("empMunicipioId");
				String logra = rec.getAsString("empEnderecoLogradouro") == null ? "" : rec.getAsString("empEnderecoLogradouro");
				int numero = rec.getAsInteger("empEnderecoNumero");
				String comp = rec.getAsString("empEnderecoComplemento") == null ? "" : rec.getAsString("empEnderecoComplemento");
				String bairro = rec.getAsString("empEnderecoBairro") == null ? "" : rec.getAsString("empEnderecoBairro");
				String cep = rec.getAsString("empEnderecoCep") == null ? "" : rec.getAsString("empEnderecoCep");

				if (endTipo == 0 || municipio == 0 || logra.equals("") || numero < 0 || bairro.equals("") || cep.equals("")) {
					throw new Exception();
				}

				EmpEndereco ende = new EmpEndereco();
				ende.setEmpEnderecoTipo(new EmpEnderecoTipo(endTipo));
				ende.setEmpMunicipio(new EmpMunicipio(municipio));
				ende.setEmpEnderecoLogradouro(logra);
				ende.setEmpEnderecoNumero(numero);
				ende.setEmpEnderecoComplemento(comp);
				ende.setEmpEnderecoBairro(bairro);
				ende.setEmpEnderecoCep(cep);
				lista.add(ende);
			} catch (Exception ex) {
				valida = false;
				break;
			}
		}

		return valida && recs.length > 0;
	}

	public ComboBox getCmbTipo() {
		return cmbTipo;
	}

	public void setCmbTipo(ComboBox cmbTipo) {
		this.cmbTipo = cmbTipo;
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

	public ComboBox getCmbMunicipio() {
		return cmbMunicipio;
	}

	public void setCmbMunicipio(ComboBox cmbMunicipio) {
		this.cmbMunicipio = cmbMunicipio;
	}

	public NumberField getTxtNumero() {
		return txtNumero;
	}

	public void setTxtNumero(NumberField txtNumero) {
		this.txtNumero = txtNumero;
	}

	public TextField getTxtLogradouro() {
		return txtLogradouro;
	}

	public void setTxtLogradouro(TextField txtLogradouro) {
		this.txtLogradouro = txtLogradouro;
	}

	public TextField getTxtComplemento() {
		return txtComplemento;
	}

	public void setTxtComplemento(TextField txtComplemento) {
		this.txtComplemento = txtComplemento;
	}

	public TextField getTxtBairro() {
		return txtBairro;
	}

	public void setTxtBairro(TextField txtBairro) {
		this.txtBairro = txtBairro;
	}

	public TextField getTxtCep() {
		return txtCep;
	}

	public void setTxtCep(TextField txtCep) {
		this.txtCep = txtCep;
	}

	public boolean isMudouPais() {
		return mudouPais;
	}

	public void setMudouPais(boolean mudouPais) {
		this.mudouPais = mudouPais;
	}

	public boolean isMudouEstado() {
		return mudouEstado;
	}

	public void setMudouEstado(boolean mudouEstado) {
		this.mudouEstado = mudouEstado;
	}

	public boolean isMudouMunicipio() {
		return mudouMunicipio;
	}

	public void setMudouMunicipio(boolean mudouMunicipio) {
		this.mudouMunicipio = mudouMunicipio;
	}
}
