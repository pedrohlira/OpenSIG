package br.com.opensig.fiscal.client.visao.form;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.visao.abstrato.IListagem;
import br.com.opensig.fiscal.client.servico.FiscalProxy;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioInutilizar {

	private Window wndInut;
	private FormPanel frmInut;
	private NumberField txtIni;
	private NumberField txtFim;
	private TextArea taMotivo;
	private Button btnOk;
	private IListagem lista;

	public FormularioInutilizar(IListagem lista) {
		this.lista = lista;
		inicializar();
	}

	private void inicializar() {
		frmInut = new FormPanel(Position.CENTER);
		frmInut.setLabelAlign(Position.TOP);
		frmInut.setBorder(false);
		frmInut.setPaddings(5);

		txtIni = new NumberField(OpenSigCore.i18n.txtInicio());
		txtIni.setAllowBlank(false);
		txtIni.setAllowDecimals(false);
		txtIni.setAllowNegative(false);
		txtIni.setWidth(100);
		txtIni.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				inutilizar();
			}
		});

		txtFim = new NumberField(OpenSigCore.i18n.txtFim());
		txtFim.setAllowBlank(false);
		txtFim.setAllowDecimals(false);
		txtFim.setAllowNegative(false);
		txtFim.setWidth(100);
		txtFim.setValidator(new Validator() {
			public boolean validate(String value) throws ValidationException {
				try {
					return txtFim.getValue().intValue() >= txtIni.getValue().intValue();
				} catch (Exception e) {
					return false;
				}
			}
		});
		txtFim.addKeyListener(EventObject.ENTER, new KeyListener() {
			public void onKey(int key, EventObject e) {
				inutilizar();
			}
		});

		taMotivo = new TextArea(OpenSigCore.i18n.txtObservacao());
		taMotivo.setAllowBlank(false);
		taMotivo.setMinLength(15);
		taMotivo.setMaxLength(255);
		taMotivo.setWidth(220);
		taMotivo.setHeight(50);

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtIni, 120);
		linha1.addToRow(txtFim, 120);
		frmInut.add(linha1);
		frmInut.add(taMotivo);

		btnOk = new Button(OpenSigCore.i18n.txtOk());
		btnOk.setIconCls("icon-salvar");
		btnOk.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				inutilizar();
			}
		});

		wndInut = new Window(OpenSigCore.i18n.txtInutilizar(), 270, 200, true, false);
		wndInut.setButtonAlign(Position.CENTER);
		wndInut.setLayout(new FitLayout());
		wndInut.setIconCls("icon-inutilizar");
		wndInut.addButton(btnOk);
		wndInut.add(frmInut);
		wndInut.addListener(new FormPanelListenerAdapter() {
			public void onShow(Component component) {
				txtIni.focus(true, 10);
			}
		});
		wndInut.doLayout();
		wndInut.show();
	}

	private void inutilizar() {
		taMotivo.setValue(taMotivo.getValueAsString().trim());
		if (frmInut.getForm().isValid()) {
			MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtInutilizar());

			AsyncCallback<Map<String, String>> async = new AsyncCallback<Map<String, String>>() {
				public void onSuccess(Map<String, String> result) {
					MessageBox.hide();
					wndInut.close();
					lista.getPanel().getStore().reload();

					ENotaStatus status = ENotaStatus.valueOf(result.get("status"));
					String msg = status == ENotaStatus.ERRO ? OpenSigCore.i18n.errExcluir() : OpenSigCore.i18n.msgExcluirOK();
					new ToastWindow(OpenSigCore.i18n.txtInutilizar(), msg).show();
				}

				public void onFailure(Throwable caught) {
					MessageBox.hide();
					MessageBox.alert(OpenSigCore.i18n.txtInutilizar(), caught.getMessage());
				}
			};

			if (lista.getClasse() instanceof FisNotaSaida) {
				FiscalProxy<FisNotaSaida> proxy = new FiscalProxy<FisNotaSaida>();
				proxy.inutilizarSaida(null, taMotivo.getValueAsString(), txtIni.getValue().intValue(), txtFim.getValue().intValue(), async);
			} else {
				FiscalProxy<FisNotaEntrada> proxy = new FiscalProxy<FisNotaEntrada>();
				proxy.inutilizarEntrada(null, taMotivo.getValueAsString(), txtIni.getValue().intValue(), txtFim.getValue().intValue(), async);
			}
		}
	}
}
