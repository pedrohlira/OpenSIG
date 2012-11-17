package br.com.opensig.comercial.client.controlador.comando.acao;

import java.util.Map;

import br.com.opensig.comercial.client.servico.ComercialProxy;
import br.com.opensig.comercial.shared.modelo.ComCompra;
import br.com.opensig.comercial.shared.modelo.ComFrete;
import br.com.opensig.comercial.shared.modelo.ComNatureza;
import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.ComandoAcao;
import br.com.opensig.core.client.visao.ComboEntidade;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.empresa.shared.modelo.EmpFornecedor;
import br.com.opensig.empresa.shared.modelo.EmpTransportadora;
import br.com.opensig.financeiro.shared.modelo.FinPagar;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ComandoGerarNfeEntrada extends ComandoAcao {

	private ComCompra compra;
	private ComFrete frete;

	private Radio rdEmitente;
	private Radio rdDestinatario;
	private Radio rdTerceiro;
	private Radio rdSemFrete;
	private NumberField txtVolume;
	private NumberField txtLiquido;
	private NumberField txtBruto;
	private TextField txtEspecie;
	private ComboBox cmbTransportadora;
	private TextArea txtObservacao;
	private Window wndNFe;

	/**
	 * @see ComandoAcao#execute(Map)
	 */
	public void execute(final Map contexto) {
		super.execute(contexto, new AsyncCallback() {
			public void onSuccess(Object result) {
				Record rec = LISTA.getPanel().getSelectionModel().getSelected();

				if (rec != null && rec.getAsBoolean("comCompraFechada") && !rec.getAsBoolean("comCompraNfe") && !rec.getAsBoolean("comCompraCancelada")) {
					compra = new ComCompra();
					compra.setEmpEmpresa(new EmpEmpresa(rec.getAsInteger("empEmpresa.empEmpresaId")));
					compra.setEmpFornecedor(new EmpFornecedor(rec.getAsInteger("empFornecedor.empFornecedor")));
					compra.setFinPagar(new FinPagar(rec.getAsInteger("finPagar.finPagarId")));
					compra.setComNatureza(new ComNatureza(rec.getAsInteger("comNatureza.comNaturezaId")));
					compra.setComCompraId(rec.getAsInteger("comCompraId"));
					compra.setComCompraEmissao(rec.getAsDate("comCompraEmissao"));
					compra.setComCompraValorNota(rec.getAsDouble("comCompraValorNota"));
					compra.setComCompraObservacao(rec.getAsString("comCompraObservacao") == null ? "" : rec.getAsString("comCompraObservacao"));
					abrirFrete();
				} else {
					MessageBox.alert(OpenSigCore.i18n.txtAcesso(), OpenSigCore.i18n.txtAcessoNegado());
				}
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	private AsyncCallback<FisNotaEntrada> salvar = new AsyncCallback<FisNotaEntrada>() {
		public void onFailure(Throwable caught) {
			MessageBox.hide();
			MessageBox.alert(OpenSigCore.i18n.txtNfe(), OpenSigCore.i18n.errSalvar());
			new ToastWindow(OpenSigCore.i18n.txtNfe(), caught.getMessage()).show();
		}

		public void onSuccess(FisNotaEntrada result) {
			Record rec = LISTA.getPanel().getSelectionModel().getSelected();
			rec.set("comCompraNfe", true);

			MessageBox.hide();
			wndNFe.close();
			new ToastWindow(OpenSigCore.i18n.txtNfe(), OpenSigCore.i18n.msgSalvarOK()).show();
		}
	};

	private void abrirFrete() {
		// janela
		wndNFe = new Window(OpenSigCore.i18n.txtNfe() + " -> " + OpenSigCore.i18n.txtFrete(), 350, 300, true, false);
		wndNFe.setLayout(new FitLayout());
		wndNFe.setIconCls("icon-nfe");
		wndNFe.setClosable(false);
		wndNFe.addListener(new FormPanelListenerAdapter() {
			public void onShow(Component component) {
				cmbTransportadora.focus(true, 10);
			}
		});

		// formulario
		FormPanel frm = new FormPanel();
		frm.setLabelAlign(Position.TOP);
		frm.setPaddings(5);
		frm.setMargins(1);

		// botoes
		setBotoes(frm);

		// campos
		setCampos(frm);

		wndNFe.add(frm);
		wndNFe.show();
	}

	private void setBotoes(final FormPanel frm) {
		Button btnCancelar = new Button();
		btnCancelar.setText(OpenSigCore.i18n.txtCancelar());
		btnCancelar.setIconCls("icon-cancelar");
		btnCancelar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				wndNFe.close();
			}
		});

		Button btnSalvar = new Button();
		btnSalvar.setText(OpenSigCore.i18n.txtSalvar());
		btnSalvar.setIconCls("icon-salvar");
		btnSalvar.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (rdSemFrete.getValue() || frm.getForm().isValid()) {
					frete = new ComFrete();
					// tipo
					if (rdEmitente.getValue()) {
						frete.setComFreteId(0);
					} else if (rdDestinatario.getValue()) {
						frete.setComFreteId(1);
					} else if (rdTerceiro.getValue()) {
						frete.setComFreteId(2);
					} else {
						frete.setComFreteId(9);
					}

					if (!rdSemFrete.getValue()) {
						// transportadora
						frete.setEmpTransportadora(new EmpTransportadora(Integer.valueOf(cmbTransportadora.getValue())));
						// volume
						frete.setComFreteVolume(txtVolume.getValue().intValue());
						// especie
						frete.setComFreteEspecie(txtEspecie.getValueAsString());
						// peso bruto
						if (txtBruto.getValue() != null) {
							frete.setComFretePeso(txtBruto.getValue().doubleValue());
						}
						// peso liquido
						if (txtLiquido.getValue() != null) {
							frete.setComFreteCubagem(txtLiquido.getValue().doubleValue());
						}
						// seta info
						compra.setComCompraObservacao(txtObservacao.getValueAsString());
					}

					MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtNfe());
					ComercialProxy proxy = new ComercialProxy();
					proxy.gerarNfe(compra, frete, salvar);
				}
			}
		});

		frm.setTopToolbar(new Button[] { btnSalvar, btnCancelar });
	}

	private void setCampos(FormPanel frm) {
		rdEmitente = new Radio(OpenSigCore.i18n.txtOrigem(), "tipo");
		rdEmitente.setChecked(true);

		rdDestinatario = new Radio(OpenSigCore.i18n.txtDestino(), "tipo");
		rdTerceiro = new Radio(OpenSigCore.i18n.txtTerceiro(), "tipo");
		rdSemFrete = new Radio(OpenSigCore.i18n.txtSemFrete(), "tipo");

		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(rdEmitente, 80);
		linha1.addToRow(rdDestinatario, 80);
		linha1.addToRow(rdTerceiro, 80);
		linha1.addToRow(rdSemFrete, 80);
		frm.add(linha1);

		MultiFieldPanel linha2 = new MultiFieldPanel();
		linha2.setBorder(false);
		linha2.addToRow(getTransportadora(), 320);

		frm.add(linha2);

		txtVolume = new NumberField(OpenSigCore.i18n.txtVolume(), "comFreteVolume", 60);
		txtVolume.setAllowBlank(false);
		txtVolume.setAllowDecimals(false);
		txtVolume.setAllowNegative(false);
		txtVolume.setMaxLength(6);

		txtEspecie = new TextField(OpenSigCore.i18n.txtEspecie(), "comFreteEspecie", 60);
		txtEspecie.setMaxLength(10);
		txtEspecie.setAllowBlank(false);

		txtBruto = new NumberField(OpenSigCore.i18n.txtBruto(), "comFreteBruto", 60);
		txtBruto.setAllowNegative(false);
		txtBruto.setMaxLength(13);

		txtLiquido = new NumberField(OpenSigCore.i18n.txtLiquido(), "comFreteLiquido", 60);
		txtLiquido.setAllowNegative(false);
		txtLiquido.setMaxLength(13);

		MultiFieldPanel linha3 = new MultiFieldPanel();
		linha3.setBorder(false);
		linha3.addToRow(txtVolume, 80);
		linha3.addToRow(txtEspecie, 80);
		linha3.addToRow(txtBruto, 80);
		linha3.addToRow(txtLiquido, 80);
		frm.add(linha3);

		txtObservacao = new TextArea(OpenSigCore.i18n.txtObservacao(), "comCompraObservacao");
		txtObservacao.setMaxLength(255);
		txtObservacao.setWidth("95%");
		txtObservacao.setValue(compra.getComCompraObservacao());
		frm.add(txtObservacao);
	}

	private ComboBox getTransportadora() {
		cmbTransportadora = UtilClient.getComboEntidade(new ComboEntidade(new EmpTransportadora()));
		cmbTransportadora.setName("empTransportadora.empEntidade.empEntidadeNome1");
		cmbTransportadora.setLabel(OpenSigCore.i18n.txtTransportadora());
		cmbTransportadora.setTriggerAction(ComboBox.ALL);

		return cmbTransportadora;
	}
}
