package br.com.opensig.fiscal.client.visao.form;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.form.ComandoSalvar;
import br.com.opensig.core.client.visao.Ponte;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.Dados;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.empresa.shared.modelo.EmpEmpresa;
import br.com.opensig.fiscal.client.servico.FiscalProxy;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaEntrada;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;
import br.com.opensig.fiscal.shared.modelo.FisNotaStatus;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.FormPanelListenerAdapter;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtextux.client.widgets.window.ToastWindow;

public class FormularioErro<E extends Dados> extends AFormulario<E> {

	private TextArea txtErro;
	private TextArea txtXml;
	private ToolbarButton btnSituacao;
	private Window janela;

	public FormularioErro(E classe, SisFuncao funcao, Window janela) {
		super(classe, funcao);
		this.janela = janela;
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		btnSalvar.setMenu(new Menu());
		enable();
		setHeader(false);

		txtErro = new TextArea(OpenSigCore.i18n.txtErro(), "fisNotaSaidaErro");
		txtErro.setReadOnly(true);
		txtErro.setWidth("98%");
		txtErro.setHeight(230);
		add(txtErro);

		txtXml = new TextArea(OpenSigCore.i18n.txtNfe(), "fisNotaSaidaXml");
		txtXml.setAllowBlank(false);
		txtXml.setWidth("98%");
		txtXml.setHeight(230);
		add(txtXml);

		btnSituacao = new ToolbarButton(OpenSigCore.i18n.txtAnalisar());
		btnSituacao.setIconCls("icon-analisar");
		btnSituacao.setTooltip(OpenSigCore.i18n.msgPedidoSituacao());
		btnSituacao.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				situacao();
			}
		});

		addListener(new FormPanelListenerAdapter() {
			public void onRender(Component component) {
				getTopToolbar().addSeparator();
				getTopToolbar().addButton(btnSituacao);
			}
		});
	}

	@Override
	public void DepoisDaAcao(IComando comando) {
		janela.close();
	}

	public boolean setDados() {
		if (classe instanceof FisNotaSaida) {
			FisNotaSaida nota = (FisNotaSaida) classe;
			if (nota.getFisNotaSaidaProtocolo().equals("")) {
				nota.setFisNotaSaidaXml(txtXml.getValueAsString());
				if (nota.getFisNotaSaidaChave().length() == 44) {
					nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.AUTORIZANDO));
				} else {
					nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.INUTILIZANDO));
				}
			} else {
				nota.setFisNotaSaidaXmlCancelado(txtXml.getValueAsString());
				nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.CANCELANDO));
			}
			nota.setFisNotaSaidaErro("");
			nota.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		} else {
			FisNotaEntrada nota = (FisNotaEntrada) classe;
			if (nota.getFisNotaEntradaProtocolo().equals("")) {
				nota.setFisNotaEntradaXml(txtXml.getValueAsString());
				if (nota.getFisNotaEntradaChave().length() == 44) {
					nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.AUTORIZANDO));
				} else {
					nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.INUTILIZANDO));
				}
			} else {
				nota.setFisNotaEntradaXmlCancelado(txtXml.getValueAsString());
				nota.setFisNotaStatus(new FisNotaStatus(ENotaStatus.CANCELANDO));
			}
			nota.setFisNotaEntradaErro("");
			nota.setEmpEmpresa(new EmpEmpresa(Ponte.getLogin().getEmpresaId()));
		}

		return true;
	}

	public void mostrarDados() {
		if (classe instanceof FisNotaSaida) {
			FisNotaSaida nota = (FisNotaSaida) classe;
			txtErro.setValue(nota.getFisNotaSaidaErro());
			if (nota.getFisNotaSaidaProtocolo().equals("")) {
				txtXml.setValue(nota.getFisNotaSaidaXml());
			} else {
				txtXml.setValue(nota.getFisNotaSaidaXmlCancelado());
			}
		} else {
			FisNotaEntrada nota = (FisNotaEntrada) classe;
			txtErro.setValue(nota.getFisNotaEntradaErro());
			if (nota.getFisNotaEntradaProtocolo().equals("")) {
				txtXml.setValue(nota.getFisNotaEntradaXml());
			} else {
				txtXml.setValue(nota.getFisNotaEntradaXmlCancelado());
			}
		}
	}

	public void limparDados() {
	}

	public void gerarListas() {
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		if (comando instanceof ComandoSalvar) {
			return new AComando() {
				public void execute(Map contexto) {
					salvar();
				}
			};
		} else {
			return comando;
		}
	}

	private void situacao() {
		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSituacao());
		setDados();

		AsyncCallback<Map<String, String>> async = new AsyncCallback<Map<String, String>>() {

			public void onSuccess(Map<String, String> result) {
				MessageBox.hide();
				janela.close();
				getLista().getPanel().getStore().reload();
				new ToastWindow(OpenSigCore.i18n.txtSituacao(), OpenSigCore.i18n.msgSalvarOK()).show();
			}

			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtSituacao(), caught.getMessage());
			}
		};

		FiscalProxy<E> proxy = new FiscalProxy<E>();
		if (classe instanceof FisNotaSaida) {
			proxy.analisarNFeSaida((FisNotaSaida) classe, async);
		} else {
			proxy.analisarNFeEntrada((FisNotaEntrada) classe, async);
		}
	}

	private void salvar() {
		MessageBox.wait(OpenSigCore.i18n.txtAguarde(), OpenSigCore.i18n.txtSalvar());
		setDados();

		AsyncCallback<Map<String, String>> asinc = new AsyncCallback<Map<String, String>>() {
			public void onSuccess(Map<String, String> result) {
				Timer tempo = new Timer() {
					public void run() {
						MessageBox.hide();
						getLista().getPanel().getStore().reload();
						janela.close();
					}
				};
				int espera = Integer.valueOf(UtilClient.CONF.get("nfe.tempo_retorno"));
				tempo.schedule(1000 * espera);
			}

			public void onFailure(Throwable caught) {
				MessageBox.hide();
				MessageBox.alert(OpenSigCore.i18n.txtSalvar(), caught.getMessage());
			}
		};

		FiscalProxy<E> proxy = new FiscalProxy<E>();
		if (classe instanceof FisNotaSaida) {
			proxy.salvarSaida(txtXml.getValueAsString(), ((FisNotaSaida) classe).getFisNotaStatus(), ((FisNotaSaida) classe).getEmpEmpresa(), asinc);
		} else {
			proxy.salvarEntrada(txtXml.getValueAsString(), ((FisNotaEntrada) classe).getFisNotaStatus(), ((FisNotaEntrada) classe).getEmpEmpresa(), asinc);
		}
	}
}
