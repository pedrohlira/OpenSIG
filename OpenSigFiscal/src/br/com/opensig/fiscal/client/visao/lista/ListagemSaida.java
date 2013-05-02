package br.com.opensig.fiscal.client.visao.lista;

import java.util.Map;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.UtilClient;
import br.com.opensig.core.client.controlador.comando.AComando;
import br.com.opensig.core.client.controlador.comando.FabricaComando;
import br.com.opensig.core.client.controlador.comando.IComando;
import br.com.opensig.core.client.controlador.comando.lista.ComandoExcluir;
import br.com.opensig.core.client.visao.abstrato.IFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.fiscal.client.controlador.comando.acao.ComandoCartaSaida;
import br.com.opensig.fiscal.client.servico.FiscalProxy;
import br.com.opensig.fiscal.client.visao.form.FormularioErro;
import br.com.opensig.fiscal.shared.modelo.ENotaStatus;
import br.com.opensig.fiscal.shared.modelo.FisNotaSaida;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtextux.client.widgets.window.ToastWindow;

public class ListagemSaida extends AListagemNota<FisNotaSaida> {

	private IComando cmdCancelar;
	private IComando cmdCarta;
	private String motivo;

	public ListagemSaida(IFormulario<FisNotaSaida> formulario) {
		super(formulario);
		nomes.put("id", "fisNotaSaidaId");
		nomes.put("numero", "fisNotaSaidaNumero");
		nomes.put("cadastro", "fisNotaSaidaCadastro");
		nomes.put("data", "fisNotaSaidaData");
		nomes.put("valor", "fisNotaSaidaValor");
		nomes.put("chave", "fisNotaSaidaChave");
		nomes.put("icms", "fisNotaSaidaIcms");
		nomes.put("ipi", "fisNotaSaidaIpi");
		nomes.put("pis", "fisNotaSaidaPis");
		nomes.put("cofins", "fisNotaSaidaCofins");
		nomes.put("evento", "fisNotaSaidaEvento");
		nomes.put("protocolo", "fisNotaSaidaProtocolo");
		nomes.put("xml", "fisNotaSaidaXml");
		nomes.put("protocoloCancelado", "fisNotaSaidaProtocoloCancelado");
		nomes.put("xmlCancelado", "fisNotaSaidaXmlCancelado");
		nomes.put("protocoloCarta", "fisNotaSaidaProtocoloCarta");
		nomes.put("xmlCarta", "fisNotaSaidaXmlCarta");
		nomes.put("recibo", "fisNotaSaidaRecibo");
		nomes.put("erro", "fisNotaSaidaErro");
		inicializar();

		// cancelando
		cmdCancelar = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				FiscalProxy<FisNotaSaida> proxy = new FiscalProxy<FisNotaSaida>();
				proxy.cancelarSaida(classe, motivo, new AsyncCallback<Map<String, String>>() {
					public void onFailure(Throwable caught) {
						getPanel().getEl().unmask();
						MessageBox.alert(OpenSigCore.i18n.txtCancelar(), caught.getMessage());
					};

					public void onSuccess(Map<String, String> result) {
						getPanel().getEl().unmask();
						getStore().reload();
					};
				});
			}
		};

		// carta
		cmdCarta = new AComando() {
			public void execute(Map contexto) {
				super.execute(contexto);
				int id = UtilClient.getSelecionado(getPanel());
				classe.setId(id);

				FiscalProxy<FisNotaSaida> proxy = new FiscalProxy<FisNotaSaida>();
				proxy.cartaSaida(classe, motivo, new AsyncCallback<Map<String, String>>() {
					public void onFailure(Throwable caught) {
						getPanel().getEl().unmask();
						MessageBox.alert(OpenSigCore.i18n.txtCarta(), caught.getMessage());
					};

					public void onSuccess(Map<String, String> result) {
						getPanel().getEl().unmask();
						getStore().reload();
					};
				});
			}
		};
	}

	protected String getXml(FisNotaSaida result) {
		return result.getFisNotaSaidaXml();
	}

	protected String getXmlCancelado(FisNotaSaida result) {
		return result.getFisNotaSaidaXmlCancelado();
	};

	protected String getXmlCarta(FisNotaSaida result) {
		return result.getFisNotaSaidaXmlCarta();
	}

	protected String getChave(FisNotaSaida result) {
		return result.getFisNotaSaidaChave();
	}

	protected String getErro(FisNotaSaida result) {
		return result.getFisNotaSaidaErro();
	}

	protected void mostrarErro(FisNotaSaida result) {
		Window wnd = new Window(OpenSigCore.i18n.txtErro(), 800, 600, true, false);
		FormularioErro<FisNotaSaida> frmErro = new FormularioErro<FisNotaSaida>(result, getFuncao(), wnd);
		frmErro.setLista(this);
		frmErro.mostrarDados();
		wnd.add(frmErro);
		wnd.show();
	}

	@Override
	public IComando AntesDaAcao(IComando comando) {
		Record rec = getSelectionModel().getSelected();

		// valida se pode excluir ou cancelar
		if (comando instanceof ComandoExcluir) {
			if (rec != null && rec.getAsInteger("fisNotaStatus.fisNotaStatusId") == ENotaStatus.AUTORIZADO.getId()) {
				comando = null;
				MessageBox.prompt(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.msgConfirma(), new PromptCallback() {
					public void execute(String btnID, String text) {
						if (btnID.equalsIgnoreCase("ok")) {
							if (text == null || text.trim().length() < 1) {
								new ToastWindow(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.errInvalido()).show();
							} else {
								getPanel().getEl().mask(OpenSigCore.i18n.txtAguarde());
								motivo = text.trim();
								cmdCancelar.execute(contexto);
							}
						}
					}
				}, true);
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtCancelar(), OpenSigCore.i18n.errSelecionar());
			}
		} else if (comando instanceof ComandoCartaSaida) {
			if (rec != null && rec.getAsInteger("fisNotaStatus.fisNotaStatusId") == ENotaStatus.AUTORIZADO.getId()) {
				MessageBox.prompt(OpenSigCore.i18n.txtCarta(), "Texto de correcao completo. (min=15 max=1000)", new PromptCallback() {
					public void execute(String btnID, String text) {
						if (btnID.equalsIgnoreCase("ok")) {
							if (text == null || text.trim().length() < 15 || text.trim().length() > 1000) {
								new ToastWindow(OpenSigCore.i18n.txtCarta(), OpenSigCore.i18n.errInvalido()).show();
							} else {
								getPanel().getEl().mask(OpenSigCore.i18n.txtAguarde());
								motivo = text.trim();
								cmdCarta.execute(contexto);
							}
						}
					}
				}, true);
			} else {
				MessageBox.alert(OpenSigCore.i18n.txtCarta(), OpenSigCore.i18n.errSelecionar());
			}
		}

		return comando;
	}

	@Override
	public void irPara() {
		Menu mnuContexto = new Menu();

		// venda
		String strVenda = FabricaComando.getInstancia().getComandoCompleto("ComandoVenda");
		SisFuncao venda = UtilClient.getFuncaoPermitida(strVenda);
		MenuItem itemVenda = gerarFuncao(venda, "fisNotaSaida.fisNotaSaidaId", "fisNotaSaidaId");
		if (itemVenda != null) {
			mnuContexto.addItem(itemVenda);
		}

		if (mnuContexto.getItems().length > 0) {
			MenuItem mnuItem = getIrPara();
			mnuItem.setMenu(mnuContexto);

			getMenu().addSeparator();
			getMenu().addItem(mnuItem);
		}
	}
}
