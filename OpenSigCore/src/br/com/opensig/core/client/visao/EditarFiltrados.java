package br.com.opensig.core.client.visao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.controlador.comando.ComandoExecutar;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarFinal;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvarInicio;
import br.com.opensig.core.client.controlador.parametro.IParametro;
import br.com.opensig.core.client.controlador.parametro.ParametroBinario;
import br.com.opensig.core.client.controlador.parametro.ParametroData;
import br.com.opensig.core.client.controlador.parametro.ParametroFormula;
import br.com.opensig.core.client.controlador.parametro.ParametroNumero;
import br.com.opensig.core.client.controlador.parametro.ParametroObjeto;
import br.com.opensig.core.client.controlador.parametro.ParametroTexto;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.EComando;
import br.com.opensig.core.shared.modelo.Sql;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.grid.plugins.SummaryColumnConfig;

/**
 * Classe que motra visualmente as opcoes de editar em lote.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class EditarFiltrados {

	private IListagem lista;
	private Window wndEditar;
	private FormPanel frmEditar;
	private ComboBox cmbOperacao;
	private ComboBox cmbCampo;
	private ComboBox cmbCampo2;
	private TextField txtValor;
	private TextField txtFormula;
	private NumberField nmbValor;
	private DateField dtValor;
	private Checkbox chkValor;
	private HTML lblFormula;
	private Button btnCancelar;
	private Button btnEditar;

	/**
	 * Construtor que recebe a listagem atual a ser aplicada a edicao em lote.
	 * 
	 * @param lista
	 *            lista atual que usará o filtro ativo.
	 */
	public EditarFiltrados(IListagem lista) {
		this.lista = lista;
		inicializar();
	}

	private void inicializar() {
		txtFormula = new TextField(OpenSigCore.i18n.txtFormula(), "txtFormula", 100);
		txtFormula.setSelectOnFocus(true);
		txtFormula.setRegex("^([\\+|\\-|\\*|\\/]\\d+(\\.\\d+)?)?$");
		txtFormula.setRegexText(OpenSigCore.i18n.errInvalido());

		txtValor = new TextField(OpenSigCore.i18n.txtValor(), "txtValor", 100);
		nmbValor = new NumberField(OpenSigCore.i18n.txtValor(), "nmbValor", 100);
		dtValor = new DateField(OpenSigCore.i18n.txtValor(), "dtValor", 100);
		chkValor = new Checkbox(OpenSigCore.i18n.txtSim(), "chkValor");

		cmbOperacao = getOperacao();
		cmbCampo = getCampos();
		setValoresCombo(true, cmbCampo);
		cmbCampo.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				exibirValor(comboBox.getValue());
			}
		});

		cmbCampo2 = getCampos();
		setValoresCombo(false, cmbCampo2);
		cmbCampo2.setLabel(OpenSigCore.i18n.txtCampo() + " - " + OpenSigCore.i18n.txtRelativo());

		frmEditar = new FormPanel(Position.CENTER);
		frmEditar.setLabelAlign(Position.TOP);
		frmEditar.setLabelWidth(100);
		frmEditar.setPaddings(5);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(cmbCampo, 130);
		linha1.addToRow(cmbCampo2, 130);
		linha1.addToRow(txtValor, 130);
		linha1.addToRow(nmbValor, 130);
		linha1.addToRow(dtValor, 130);
		linha1.addToRow(chkValor, 130);
		frmEditar.add(linha1);

		frmEditar.add(new HTML("<hr>"));

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(cmbOperacao, 130);
		linha2.addToRow(txtFormula, 130);
		frmEditar.add(linha2);

		lblFormula = new HTML("<b>" + OpenSigCore.i18n.txtFormula() + " ex: +10 | ex: -20 | ex: *1.10 | ex: /2 </b>");
		frmEditar.add(lblFormula);

		btnCancelar = new Button(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				wndEditar.close();
			}
		});

		btnEditar = new Button(OpenSigCore.i18n.txtFinalizar());
		btnEditar.setIconCls("icon-editar");
		btnEditar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				editar();
			}
		});

		Panel panel = Ponte.getCentro().getActiveTab();
		wndEditar = new Window(panel.getTitle(), 300, 220, true, false);
		wndEditar.setClosable(false);
		wndEditar.setButtonAlign(Position.CENTER);
		wndEditar.setLayout(new FitLayout());
		wndEditar.setIconCls(panel.getIconCls());
		wndEditar.addButton(btnCancelar);
		wndEditar.addButton(btnEditar);
		wndEditar.add(frmEditar);
		wndEditar.addListener(new WindowListenerAdapter() {
			public void onShow(Component component) {
				exibirValor(cmbCampo.getValue());
			}
		});

		wndEditar.doLayout();
		wndEditar.show();
	}

	private void editar() {
		if (frmEditar.getForm().isValid()) {
			MessageBox.confirm(wndEditar.getTitle(), OpenSigCore.i18n.msgEditarFiltrados(), new MessageBox.ConfirmCallback() {

				public void execute(String btnID) {
					if (btnID.equalsIgnoreCase("yes")) {
						MessageBox.wait(OpenSigCore.i18n.txtAguarde(), wndEditar.getTitle());

						try {
							IParametro param = null;
							MatchResult mat = RegExp.compile("^t\\d+\\.").exec(cmbCampo.getValue());

							if (txtFormula.isVisible()) {
								if (txtFormula.getValueAsString() == null) {
									txtFormula.setValue("+0");
								}
								param = new ParametroFormula(cmbCampo.getValue(), cmbCampo2.getValue(), txtFormula.getValueAsString());
							} else if (txtValor.isVisible()) {
								param = new ParametroTexto(cmbCampo.getValue(), txtValor.getValueAsString());
							} else if (nmbValor.isVisible()) {
								if (mat == null && cmbCampo.getValue().contains(".")) {
									String[] objetos = cmbCampo.getValue().split("\\.");
									Dados valor = lista.getClasse().getObjeto(objetos[0]);
									valor.setId(nmbValor.getValue());
									param = new ParametroObjeto(objetos[0], valor);
								} else {
									param = new ParametroNumero(cmbCampo.getValue(), nmbValor.getValue());
								}
							} else if (chkValor.isVisible()) {
								param = new ParametroBinario(cmbCampo.getValue(), chkValor.getValue() ? 1 : 0);
							} else if (dtValor.isVisible()) {
								param = new ParametroData(cmbCampo.getValue(), dtValor.getValue());
							}

							if (mat != null) {
								param.setCampoPrefixo("");
							}

							ComandoSalvarFinal cmdFim = new ComandoSalvarFinal() {
								public void execute(Map contexto) {
									super.execute(contexto);
								}
							};

							Sql sql = new Sql(lista.getClasse(), EComando.ATUALIZAR, lista.getProxy().getFiltroTotal(), param);
							ComandoExecutar<Dados> cmdAtualizar = new ComandoExecutar<Dados>(cmdFim, new Sql[] { sql });
							ComandoSalvarInicio cmdInicio = new ComandoSalvarInicio(cmdAtualizar);
							cmdInicio.execute(lista.getContexto());
						} catch (Exception e) {
							MessageBox.hide();
							MessageBox.alert(OpenSigCore.i18n.txtEditarFiltrados(), OpenSigCore.i18n.errInvalido());
						}
					}
				}
			});
		}
	}

	private void exibirValor(String valor) {
		txtValor.setVisible(false);
		nmbValor.setVisible(false);
		dtValor.setVisible(false);
		chkValor.setVisible(false);
		cmbOperacao.setVisible(false);
		cmbOperacao.setValue("0");
		cmbCampo2.setVisible(false);
		txtFormula.setVisible(false);
		lblFormula.setVisible(false);

		if (lista.validarCampoTexto(valor)) {
			txtValor.setVisible(true);
		} else if (lista.validarCampoNumero(valor)) {
			nmbValor.setVisible(true);
			cmbOperacao.setVisible(true);
		} else if (lista.validarCampoData(valor)) {
			dtValor.setVisible(true);
		} else if (lista.validarCampoBinario(valor)) {
			chkValor.setVisible(true);
		}
	}

	private ComboBox getCampos() {
		ComboBox cmbCampos = new ComboBox(OpenSigCore.i18n.txtCampo());
		cmbCampos.setEditable(false);
		cmbCampos.setForceSelection(true);
		cmbCampos.setDisplayField("nome");
		cmbCampos.setValueField("valor");
		cmbCampos.setMode(ComboBox.LOCAL);
		cmbCampos.setTriggerAction(ComboBox.ALL);
		cmbCampos.setSelectOnFocus(true);
		cmbCampos.setWidth(120);
		cmbCampos.setListWidth(150);
		return cmbCampos;
	}

	private ComboBox getOperacao() {
		String[][] strValor = new String[][] { new String[] { OpenSigCore.i18n.txtValor(), "0" }, new String[] { OpenSigCore.i18n.txtRelativo(), "1" } };
		Store store = new SimpleStore(new String[] { "nome", "valor" }, strValor);

		final ComboBox cmbOperacao = new ComboBox(OpenSigCore.i18n.txtOperacao());
		cmbOperacao.setEditable(false);
		cmbOperacao.setForceSelection(true);
		cmbOperacao.setDisplayField("nome");
		cmbOperacao.setValueField("valor");
		cmbOperacao.setMode(ComboBox.LOCAL);
		cmbOperacao.setTriggerAction(ComboBox.ALL);
		cmbOperacao.setSelectOnFocus(true);
		cmbOperacao.setWidth(120);
		cmbOperacao.setListWidth(120);
		cmbOperacao.setStore(store);
		cmbOperacao.addListener(new ComboBoxListenerAdapter() {
			public void onSelect(ComboBox comboBox, Record record, int index) {
				if (comboBox.getValue().equals("0")) {
					nmbValor.setVisible(true);
					cmbCampo2.setVisible(false);
					txtFormula.setVisible(false);
					lblFormula.setVisible(false);
				} else {
					nmbValor.setVisible(false);
					cmbCampo2.setVisible(true);
					txtFormula.setVisible(true);
					lblFormula.setVisible(true);
				}
			}
		});

		store.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				cmbOperacao.setValue(records[0].getAsString("valor"));
			}
		});
		store.load();

		return cmbOperacao;
	}

	private void setValoresCombo(boolean todos, final ComboBox combo) {
		List<String[]> valores = new ArrayList<String[]>();

		for (int i = 1; i < lista.getModelos().getColumnCount(); i++) {
			ColumnConfig cc = null;
			try {
				cc = (ColumnConfig) lista.getModelos().getColumnConfigs()[i];
			} catch (Exception ex) {
				try {
					SummaryColumnConfig scc = (SummaryColumnConfig) lista.getModelos().getColumnConfigs()[i];
					cc = (ColumnConfig) scc.getColumn();
				} catch (Exception e) {
					cc = null;
				}
			}

			if (cc != null) {
				String[] valor = new String[] { lista.getModelos().getColumnHeader(i), lista.getModelos().getDataIndex(i) };
				if (!cc.getFixed() && (todos || lista.validarCampoNumero(valor[1])) && valor[1].split("\\.").length < 3) {
					valores.add(valor);
				}
			}
		}

		Store store = new SimpleStore(new String[] { "nome", "valor" }, valores.toArray(new String[][] {}));
		store.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				if (records != null && records.length > 0) {
					combo.setValue(records[0].getAsString("valor"));
				}
			}
		});
		store.load();
		combo.setStore(store);
	}
}
