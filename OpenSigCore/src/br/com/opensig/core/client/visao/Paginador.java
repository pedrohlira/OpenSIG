package br.com.opensig.core.client.visao;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;

import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.ComponentListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

/**
 * Classe que representa a paginacao da listagem.
 * 
 * @author Pedro H. Lira
 * @version 1.0
 */
public class Paginador extends PagingToolbar {

	private NumberField txtPag;
	private ToolbarButton btnAuto;
	private int tempo;
	private Timer relogio;

	/**
	 * Construtor padrao que recebe o objeto de dados da listagem e um booleano
	 * que indica se terá atualizacao de tela automatica.
	 * 
	 * @param store
	 *            o objeto de dados da listagem.
	 * @param auto
	 *            verdadeiro se tiver atualizar automatico, false em caso
	 *            contrario.
	 */
	public Paginador(final Store store, boolean auto) {
		// o tamanho de paginacao
		int tamanho;
		try {
			tamanho = Integer.valueOf(UtilClient.CONF.get("listagem.paginacao"));
		} catch (Exception e) {
			tamanho = 50;
		}

		// dados basicos
		setHeight(25);
		setStore(store);
		setPageSize(tamanho);
		setDisplayInfo(true);

		// seta o tempo de atualizacao
		try {
			tempo = Integer.valueOf(UtilClient.CONF.get("listagem.tempo"));
		} catch (Exception e) {
			tempo = 30;
		}

		// cria o relogio
		relogio = new Timer() {
			private int aux = tempo;

			public void run() {
				if (aux == 0) {
					store.reload();
					aux = tempo;
				}
				btnAuto.setText(OpenSigCore.i18n.txtAuto() + " - " + aux);
				aux--;
			}

			public void cancel() {
				super.cancel();
				aux = tempo;
			}
		};

		txtPag = new NumberField();
		txtPag.setAllowDecimals(false);
		txtPag.setAllowNegative(false);
		txtPag.setMinValue(1);
		txtPag.setWidth(30);
		txtPag.setValue(tamanho);
		txtPag.setSelectOnFocus(true);
		txtPag.addListener(new TextFieldListenerAdapter() {

			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					int tamanho = Integer.parseInt(field.getValueAsString());
					setPageSize(tamanho);
					store.load(0, tamanho);
				}
			}
		});

		addField(txtPag);
		addSpacer();

		if (auto) {
			btnAuto = new ToolbarButton(OpenSigCore.i18n.txtAuto());
			btnAuto.setEnableToggle(true);
			btnAuto.setIconCls("icon-auto");
			btnAuto.setTooltip(OpenSigCore.i18n.msgAuto(tempo + ""));
			btnAuto.addListener(new ButtonListenerAdapter() {

				public void onToggle(final Button button, boolean pressed) {
					if (pressed) {
						relogio.scheduleRepeating(1000);
					} else {
						relogio.cancel();
						button.setText(OpenSigCore.i18n.txtAuto());
					}
				}
			});
			addButton(btnAuto);

			addListener(new ComponentListenerAdapter() {
				public boolean doBeforeDestroy(Component component) {
					relogio.cancel();
					return true;
				}
			});
		}
	}

	// Gets e Seteres
	
	public NumberField getTxtPag() {
		return txtPag;
	}

	public void setTxtPag(NumberField txtPag) {
		this.txtPag = txtPag;
	}

	public ToolbarButton getBtnAuto() {
		return btnAuto;
	}

	public void setBtnAuto(ToolbarButton btnAuto) {
		this.btnAuto = btnAuto;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public Timer getRelogio() {
		return relogio;
	}

	public void setRelogio(Timer relogio) {
		this.relogio = relogio;
	}

}
